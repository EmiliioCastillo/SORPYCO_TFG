package com.emilio.sorpyco_tfg.servicio.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.emilio.sorpyco_tfg.dto.MatrizCostosDTO;
import com.emilio.sorpyco_tfg.entidades.Alternativa;
import com.emilio.sorpyco_tfg.entidades.Pedido;
import com.emilio.sorpyco_tfg.entidades.Planificacion;
import com.emilio.sorpyco_tfg.entidades.Vehiculo;
import com.emilio.sorpyco_tfg.servicio.OptimizacionService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.emilio.sorpyco_tfg.dto.MatrizCostosDTO;
import com.emilio.sorpyco_tfg.entidades.Alternativa;
import com.emilio.sorpyco_tfg.entidades.Nodo;
import com.emilio.sorpyco_tfg.entidades.Parada;
import com.emilio.sorpyco_tfg.entidades.Pedido;
import com.emilio.sorpyco_tfg.entidades.Planificacion;
import com.emilio.sorpyco_tfg.entidades.Ruta;
import com.emilio.sorpyco_tfg.entidades.Vehiculo;
import com.emilio.sorpyco_tfg.enums.Algoritmo;
import com.emilio.sorpyco_tfg.enums.Prioridad;
import com.emilio.sorpyco_tfg.enums.TipoNodo;
import com.emilio.sorpyco_tfg.servicio.OptimizacionService;

@Service
public class OptimizacionServiceImpl implements OptimizacionService {

    @Override
    public List<Alternativa> generarAlternativas(
            Planificacion planificacion,
            List<Pedido> pedidos,
            List<Vehiculo> vehiculos,
            MatrizCostosDTO matrizCostos) {
    	
    	//se calcula el tiempo de respuesta con la mayor precision posible
        long inicio = System.nanoTime();
        
        //Validamos con el metodo que los datos sean los adecuados para empezar el motor
        validarDatos(planificacion, pedidos, vehiculos, matrizCostos);

        
        List<Nodo> nodos = matrizCostos.nodos();
        double[][] distancias = matrizCostos.distancias();
        double[][] duraciones = matrizCostos.duraciones();

        // Relacionamos cada nodo con su posición en las matrices.
        Map<Long, Integer> indicesNodos = new HashMap<>();

        for (int i = 0; i < nodos.size(); i++) {
        	System.out.println(i);
            indicesNodos.put(nodos.get(i).getId(), i);
        }
        
        
        // Agrupamos pedidos que pertenecen a una sucursal en concreto
        
        /*La idea es la siguiente:
         * Un cliente (sucursal) puede tener varios pedidos para el mismo día.
         *  Cuando encontramos el primer pedido de ese cliente, necesitamos crear un "grupo" para él.
         *   Pero cuando encontramos el segundo, tercer o cuarto pedido del mismo cliente,
         *    no debemos crear un grupo nuevo; 
         * tenemos que usar el mismo grupo que ya habiamos creado para meter el cuarto.
         * Ahora surge la duda: 
         * Cómo sabemos que no estamos metiendo el mismo
         *  pedido dos veces o que los pedidos no se pisan?
         * La clave de todo es el Map, proporcionando la 
         * key y value. la key es el idNodo y el value es objeto GrupoEntrega que tendra los datos
         * del pedido
         * En concreto debemos hacer:
         * 1.buscar la key
         * 2.si ya existe la recuperamos y la devuelve
         * 3.si no existe creamos un objeto grupoEntrega, con su posicion en la matriz y su nodo
         * 4.lo guardamos en el map  asociado a la key
         * 5.lo devolvemos
         * 
         * */
        Map<Long, GrupoEntrega> grupos = new LinkedHashMap<>();

        for (Pedido pedido : pedidos) {

            Long idNodo = pedido.getSucursal().getNodo().getId();

            if (!indicesNodos.containsKey(idNodo)) {
                throw new IllegalStateException(
                        "No se encontró el nodo del pedido " + pedido.getId()
                );
            }
            //1.buscar la key
            GrupoEntrega grupo = grupos.get(idNodo);
            //evaluamos
            if (grupo == null) {
            	//, si no existe creamos
            	grupo = new GrupoEntrega(
                        indicesNodos.get(idNodo),
                        pedido.getSucursal().getNodo());
            	//agrupamos
            	grupos.put(idNodo, grupo);
            }
           //si existe agrupamos
            grupo.agregarPedido(pedido);
        }

        List<GrupoEntrega> entregas = new ArrayList<>(grupos.values());

        // 3. Calculamos la distancia inicial:
        // depósito -> cada entrega -> depósito.
        double distanciaInicial = 0;

        for (GrupoEntrega entrega : entregas) {
            int indice = entrega.indiceMatriz;

            distanciaInicial +=
                    distancias[0][indice] +
                    distancias[indice][0];
        }

        // 4. Calculamos los ahorros de Clarke & Wright.
        List<Ahorro> ahorros = new ArrayList<>();

        for (int i = 0; i < entregas.size(); i++) {

            for (int j = i + 1; j < entregas.size(); j++) {

                int nodoI = entregas.get(i).indiceMatriz;
                int nodoJ = entregas.get(j).indiceMatriz;

                double ahorro =
                        distancias[0][nodoI]
                        + distancias[0][nodoJ]
                        - distancias[nodoI][nodoJ];

                int prioridad =
                        valorPrioridad(entregas.get(i).prioridad)
                        + valorPrioridad(entregas.get(j).prioridad);

                ahorros.add(new Ahorro(i, j, ahorro, prioridad));
            }
        }

        // Primero los mayores ahorros.
        // En caso de empate, priorizamos las ubicaciones con pedidos urgentes.
        ahorros.sort(
                Comparator.comparingDouble(Ahorro::valor).reversed()
                        .thenComparing(
                                Comparator.comparingInt(
                                        Ahorro::prioridad
                                ).reversed()
                        )
        );

        // 5. Inicialmente cada ubicación tiene su propio recorrido.
        List<RutaTrabajo> rutasTrabajo = new ArrayList<>();

        for (int i = 0; i < entregas.size(); i++) {
            rutasTrabajo.add(new RutaTrabajo(i, entregas.get(i).peso));
        }

        // 6. Combinamos recorridos respetando las capacidades.
        double capacidadMaxima = vehiculos.stream()
                .map(Vehiculo::getCapacidad)
                .mapToDouble(BigDecimal::doubleValue)
                .max()
                .orElse(0);

        for (Ahorro ahorro : ahorros) {

            RutaTrabajo rutaI = buscarRuta(
                    rutasTrabajo, ahorro.indiceI
            );

            RutaTrabajo rutaJ = buscarRuta(
                    rutasTrabajo, ahorro.indiceJ
            );

            if (rutaI == null || rutaJ == null || rutaI == rutaJ) {
                continue;
            }

            double pesoCombinado = rutaI.peso + rutaJ.peso;

            if (pesoCombinado > capacidadMaxima) {
                continue;
            }

            List<Integer> combinacion = combinar(
                    rutaI.nodos,
                    rutaJ.nodos,
                    ahorro.indiceI,
                    ahorro.indiceJ
            );

            if (combinacion == null) {
                continue;
            }

            rutaI.nodos = combinacion;
            rutaI.peso = pesoCombinado;

            rutasTrabajo.remove(rutaJ);
        }

        // 7. Asignamos vehículos a los recorridos.
        // Los recorridos más pesados se asignan primero.
        rutasTrabajo.sort(
                Comparator.comparingDouble(
                        (RutaTrabajo r) -> r.peso
                ).reversed()
        );

        List<Vehiculo> vehiculosOrdenados = new ArrayList<>(vehiculos);

        vehiculosOrdenados.sort(
                Comparator.comparing(Vehiculo::getCapacidad)
        );

        List<Vehiculo> vehiculosUtilizados = new ArrayList<>();

        Alternativa alternativa = new Alternativa();
        alternativa.setPlanificacion(planificacion);
        alternativa.setAlgoritmo(Algoritmo.CLARKE_WRIGHT);
        alternativa.setDistanciaInicial(
                decimal(distanciaInicial / 1000)
        );

        double distanciaTotal = 0;
        double tiempoTotal = 0;
        double pesoAsignado = 0;
        double capacidadUtilizada = 0;
        int pedidosNoAsignados = 0;

        // 8. Creamos las rutas y sus paradas.
        for (RutaTrabajo recorrido : rutasTrabajo) {

            Vehiculo vehiculo = vehiculosOrdenados.stream()
                    .filter(v -> !vehiculosUtilizados.contains(v))
                    .filter(v -> v.getCapacidad().doubleValue()
                            >= recorrido.peso)
                    .findFirst()
                    .orElse(null);

            // Si ningún vehículo puede realizar el recorrido,
            // dejamos sus pedidos sin asignar.
            if (vehiculo == null) {
                pedidosNoAsignados += contarPedidos(
                        recorrido, entregas
                );
                continue;
            }

            vehiculosUtilizados.add(vehiculo);

            Ruta ruta = new Ruta();
            ruta.setVehiculo(vehiculo);

            double distanciaRuta = 0;
            double tiempoRuta = 0;

            int posicion = 1;
            int nodoAnterior = 0; // El depósito ocupa la posición 0.

            for (Integer indiceGrupo : recorrido.nodos) {

                GrupoEntrega entrega = entregas.get(indiceGrupo);
                int nodoActual = entrega.indiceMatriz;

                distanciaRuta += distancias[nodoAnterior][nodoActual];
                tiempoRuta += duraciones[nodoAnterior][nodoActual];

                for (Pedido pedido : entrega.pedidos) {

                    Parada parada = new Parada();
                    parada.setPedido(pedido);
                    parada.setPosicion(posicion++);

                    ruta.addParada(parada);
                }

                nodoAnterior = nodoActual;
            }

            // Regreso al depósito.
            distanciaRuta += distancias[nodoAnterior][0];
            tiempoRuta += duraciones[nodoAnterior][0];

            ruta.setDistancia(decimal(distanciaRuta / 1000));
            ruta.setTiempoEstimado((int) Math.round(tiempoRuta));

            alternativa.addRuta(ruta);

            distanciaTotal += distanciaRuta;
            tiempoTotal += tiempoRuta;
            pesoAsignado += recorrido.peso;
            capacidadUtilizada += vehiculo.getCapacidad().doubleValue();
        }

        // 9. Calculamos las métricas de la alternativa.
        alternativa.setDistanciaTotal(
                decimal(distanciaTotal / 1000)
        );

        alternativa.setTiempoTotal(
                (int) Math.round(tiempoTotal)
        );

        alternativa.setCantidadVehiculos(
                vehiculosUtilizados.size()
        );

        alternativa.setPedidosNoAsignados(pedidosNoAsignados);

        alternativa.setVentanasIncumplidas(0);

        double utilizacion = capacidadUtilizada > 0
                ? (pesoAsignado / capacidadUtilizada) * 100
                : 0;

        alternativa.setUtilizacionCapacidad(
                decimal(utilizacion)
        );

        alternativa.setTiempoCalculo(
                (int) ((System.nanoTime() - inicio) / 1_000_000)
        );

        // Mantenemos la relación entre planificación y alternativa.
        planificacion.addAlternativa(alternativa);

        return List.of(alternativa);
    }

    private void validarDatos(
            Planificacion planificacion,
            List<Pedido> pedidos,
            List<Vehiculo> vehiculos,
            MatrizCostosDTO matriz) {

        if (planificacion == null || pedidos == null || pedidos.isEmpty()) {
            throw new IllegalArgumentException(
                    "La planificación y los pedidos son obligatorios."
            );
        }

        if (vehiculos == null || vehiculos.isEmpty()) {
            throw new IllegalStateException(
                    "No existen vehículos disponibles."
            );
        }

        if (matriz == null || matriz.nodos() == null
                || matriz.nodos().size() < 2) {
            throw new IllegalArgumentException(
                    "La matriz de costos no contiene suficientes nodos."
            );
        }

        int cantidad = matriz.nodos().size();

        if (matriz.distancias() == null
                || matriz.duraciones() == null
                || matriz.distancias().length != cantidad
                || matriz.duraciones().length != cantidad) {
            throw new IllegalArgumentException(
                    "Las dimensiones de la matriz son incorrectas."
            );
        }

        for (int i = 0; i < cantidad; i++) {

            if (matriz.distancias()[i] == null
                    || matriz.duraciones()[i] == null
                    || matriz.distancias()[i].length != cantidad
                    || matriz.duraciones()[i].length != cantidad) {
                throw new IllegalArgumentException(
                        "La matriz de costos tiene filas incorrectas."
                );
            }
        }

        if (matriz.nodos().get(0).getTipoNodo() != TipoNodo.DEPOSITO) {
            throw new IllegalArgumentException(
                    "El primer nodo de la matriz debe ser el depósito."
            );
        }
    }

    private int valorPrioridad(Prioridad prioridad) {

        if (prioridad == null) {
            return 0;
        }

        return switch (prioridad) {
            case ALTA -> 3;
            case MEDIA -> 2;
            case BAJA -> 1;
        };
    }

    private RutaTrabajo buscarRuta(
            List<RutaTrabajo> rutas,
            int indiceNodo) {

        return rutas.stream()
                .filter(r -> r.nodos.contains(indiceNodo))
                .findFirst()
                .orElse(null);
    }

    private List<Integer> combinar(
            List<Integer> rutaA,
            List<Integer> rutaB,
            int nodoI,
            int nodoJ) {

        List<Integer> izquierda = new ArrayList<>(rutaA);
        List<Integer> derecha = new ArrayList<>(rutaB);

        // El nodo I debe quedar al final de la primera ruta.
        if (izquierda.get(0) == nodoI) {
            java.util.Collections.reverse(izquierda);
        }

        if (izquierda.get(izquierda.size() - 1) != nodoI) {
            return null;
        }

        // El nodo J debe quedar al comienzo de la segunda ruta.
        if (derecha.get(derecha.size() - 1) == nodoJ) {
            java.util.Collections.reverse(derecha);
        }

        if (derecha.get(0) != nodoJ) {
            return null;
        }

        izquierda.addAll(derecha);

        return izquierda;
    }

    private int contarPedidos(
            RutaTrabajo recorrido,
            List<GrupoEntrega> entregas) {

        return recorrido.nodos.stream()
                .mapToInt(i -> entregas.get(i).pedidos.size())
                .sum();
    }

    private BigDecimal decimal(double valor) {
        return BigDecimal.valueOf(valor)
                .setScale(2, RoundingMode.HALF_UP);
    }

    // Representa los pedidos que deben entregarse en una ubicación.
    private static class GrupoEntrega {

        private final int indiceMatriz;
        private final Nodo nodo;
        private final List<Pedido> pedidos = new ArrayList<>();

        private double peso;
        private Prioridad prioridad = Prioridad.BAJA;

        public GrupoEntrega(int indiceMatriz, Nodo nodo) {
            this.indiceMatriz = indiceMatriz;
            this.nodo = nodo;
        }

        public void agregarPedido(Pedido pedido) {

            pedidos.add(pedido);
            peso += pedido.getPeso().doubleValue();

            if (valorPrioridadEstatica(pedido.getPrioridad())
                    > valorPrioridadEstatica(prioridad)) {
                prioridad = pedido.getPrioridad();
            }
        }

        private static int valorPrioridadEstatica(Prioridad prioridad) {
            if (prioridad == null) return 0;

            return switch (prioridad) {
                case ALTA -> 3;
                case MEDIA -> 2;
                case BAJA -> 1;
            };
        }
    }

    // Representa una ruta mientras el algoritmo está trabajando.
    private static class RutaTrabajo {

        private List<Integer> nodos = new ArrayList<>();
        private double peso;

        public RutaTrabajo(int indiceNodo, double peso) {
            this.nodos.add(indiceNodo);
            this.peso = peso;
        }
    }

    private record Ahorro(
            int indiceI,
            int indiceJ,
            double valor,
            int prioridad) {
    }
}

