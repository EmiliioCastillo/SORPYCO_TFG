package com.emilio.sorpyco_tfg.servicio.impl;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/*
 * Metodo encargado de traducir y preparar los datos para pasarle al algoritmo de optimizacion
 * C&W y barrido
 *  La idea es que si tenemos los pedidos estamos amarrados a un punto geografico (sucursal y nodo)
 * si tenemos los pedidos, necesitamos su estado, y si sabemos su estado sabemos de donde vienen
 * porque si estan pendientes significa que hay que ir a entregar a una sucursal
 * si juntamos los nodos que pertenezcan al tipo de deposito junto con los otros que no son deposito
 * tenemos los datos suficientes para armar la lista y mandarle a OSRM, OSRM nos devuelve la distancia
 * y duracion con esos datos ya podemos armar el objeto que nos va a servir para el algoritmo
 * PedidoRepository → NodoRepository → RutaService → MatrizCostosDTO
 * */
import com.emilio.sorpyco_tfg.dto.MatrizCostosDTO;
import com.emilio.sorpyco_tfg.dto.RuteoDTO;
import com.emilio.sorpyco_tfg.entidades.Nodo;
import com.emilio.sorpyco_tfg.entidades.Pedido;
import com.emilio.sorpyco_tfg.enums.EstadoPedido;
import com.emilio.sorpyco_tfg.enums.TipoNodo;
import com.emilio.sorpyco_tfg.repositorio.NodoRepository;
import com.emilio.sorpyco_tfg.repositorio.PedidoRepository;
import com.emilio.sorpyco_tfg.servicio.MatrizCostoService;
import com.emilio.sorpyco_tfg.servicio.RutaService;

@Service
public class MatrizCostoServiceImpl implements MatrizCostoService {
	  private  PedidoRepository pedidoRepository;
	    private  NodoRepository nodoRepository;
	    private  RutaService rutaService;
	    
	    
	    public MatrizCostoServiceImpl(
	            PedidoRepository pedidoRepository,
	            NodoRepository nodoRepository,
	            RutaService rutaService) {

	        this.pedidoRepository = pedidoRepository;
	        this.nodoRepository = nodoRepository;
	        this.rutaService = rutaService;
	    }

	    /*Este metodo nos sirve para generar la matriz de costos que representa
	     * la distancia y duracion.
	     * Recibe como parametro la fecha de entrega del pedido, si esta en estado pendiente
	     * recuperamos el deposito de donde sale, la sucursal a donde tiene que ir,
	     * la distancia que hay y la duracion aproximada hacia esa sucursal, la sucursal estaria
	     * asociado al cliente, por lo tanto si sabemos la sucursal sabemos el cliente al que va
	     * por ende podriamos crear una relacion mas fuerte a posteriori, agregando variables nuevas.
	     * 
	     * 
	     * */
	@Override
	public MatrizCostosDTO generarMatriz(LocalDate fechaEntrega) {
		// 1. Validar la fecha del pedido
        if (fechaEntrega == null) {
            throw new IllegalArgumentException(
                    "La fecha de entrega es obligatoria.");
        }

        // 2. Recuperar pedidos pendientes
        //Como vamos a tener cargado pedido, tambien tendremos info sobre a quien le corresponde
        List<Pedido> pedidos =
                pedidoRepository.findByFechaEntregaAndEstado(
                        fechaEntrega,
                        EstadoPedido.PENDIENTE
                );

        if (pedidos.isEmpty()) {
            throw new IllegalStateException(
                    "No existen pedidos pendientes para esa fecha.");
        }

        // 3. buscamos el nodo cuyo tipo sea el deposito, obvio que es 1 solo porque
        //es 1 deposito central de donde sale todo
        List<Nodo> depositos =
                nodoRepository.findByTipoNodo(TipoNodo.DEPOSITO);

        if (depositos.size() != 1) {
            throw new IllegalStateException(
                    "El sistema debe tener exactamente un depósito registrado.");
        }
        //Guardamos el 1er resultado
        Nodo deposito = depositos.get(0);

        // 4. nos aseguramos que no haya duplicados
        Map<Long, Nodo> nodosUnicos = new LinkedHashMap<>();
        //lo agregamos
        nodosUnicos.put(deposito.getId(), deposito);

        
        for (Pedido pedido : pedidos) {

            if (pedido.getSucursal() == null
                    || pedido.getSucursal().getNodo() == null) {

                throw new IllegalStateException(
                        "Un pedido no tiene una sucursal o nodo asociado.");
            }

            Nodo nodoEntrega = pedido.getSucursal().getNodo();

            nodosUnicos.put(nodoEntrega.getId(), nodoEntrega);
        }

        // 5. Creamos la lista ordenada de nodos
        List<Nodo> nodos = List.copyOf(nodosUnicos.values());

        // 6. se consulta a OSRM llamando a la interfaz de rutaService para armar la matriz
        RuteoDTO ruteo = rutaService.calcularMatriz(nodos);

        if (ruteo == null
                || ruteo.distancias() == null
                || ruteo.duraciones() == null) {

            throw new IllegalStateException(
                    "No se pudo obtener la matriz de ruteo.");
        }

        // 7. Validar dimensiones de las matrices
        validarDimensiones(ruteo.distancias(), nodos.size());
        validarDimensiones(ruteo.duraciones(), nodos.size());

        MatrizCostosDTO resultadoCompleto = new MatrizCostosDTO(nodos,ruteo.distancias(),ruteo.duraciones());
        // 8. Devolver los resultados
        
        System.out.println(resultadoCompleto);
        
        return resultadoCompleto;
    }

    private void validarDimensiones(
            double[][] matriz,
            int cantidadNodos) {

        if (matriz.length != cantidadNodos) {
            throw new IllegalStateException(
                    "La matriz recibida no coincide con la cantidad de nodos.");
        }

        for (double[] fila : matriz) {

            if (fila == null || fila.length != cantidadNodos) {
                throw new IllegalStateException(
                        "La matriz de ruteo tiene dimensiones incorrectas.");
            }
        }
    
	}

}
