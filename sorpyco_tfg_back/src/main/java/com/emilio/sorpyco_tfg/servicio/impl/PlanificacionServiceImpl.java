package com.emilio.sorpyco_tfg.servicio.impl;


import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emilio.sorpyco_tfg.dto.MatrizCostosDTO;
import com.emilio.sorpyco_tfg.entidades.Alternativa;
import com.emilio.sorpyco_tfg.entidades.Pedido;
import com.emilio.sorpyco_tfg.entidades.Planificacion;
import com.emilio.sorpyco_tfg.entidades.Vehiculo;
import com.emilio.sorpyco_tfg.enums.EstadoPedido;
import com.emilio.sorpyco_tfg.enums.EstadoPlanificacion;
import com.emilio.sorpyco_tfg.repositorio.PedidoRepository;
import com.emilio.sorpyco_tfg.repositorio.PlanificacionRepository;
import com.emilio.sorpyco_tfg.repositorio.VehiculoRepository;
import com.emilio.sorpyco_tfg.servicio.MatrizCostoService;
import com.emilio.sorpyco_tfg.servicio.OptimizacionService;
import com.emilio.sorpyco_tfg.servicio.PlanificacionService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emilio.sorpyco_tfg.dto.PlanificacionRespuestaDTO;
import com.emilio.sorpyco_tfg.dto.RutaDTO;
import com.emilio.sorpyco_tfg.entidades.*;
import com.emilio.sorpyco_tfg.enums.EstadoPedido;
import com.emilio.sorpyco_tfg.enums.EstadoPlanificacion;
import com.emilio.sorpyco_tfg.repositorio.PlanificacionRepository;
import com.emilio.sorpyco_tfg.repositorio.PedidoRepository;
import com.emilio.sorpyco_tfg.repositorio.VehiculoRepository;
import com.emilio.sorpyco_tfg.servicio.MatrizCostoService;
import com.emilio.sorpyco_tfg.servicio.OptimizacionService;
import com.emilio.sorpyco_tfg.servicio.PlanificacionService;

@Service
@Transactional
public class PlanificacionServiceImpl implements PlanificacionService {

    private final PlanificacionRepository planificacionRepository;
    private final PedidoRepository pedidoRepository;
    private final VehiculoRepository vehiculoRepository;
    private final MatrizCostoService matrizCostoService;
    private final OptimizacionService optimizacionService;

    public PlanificacionServiceImpl(
            PlanificacionRepository planificacionRepository,
            PedidoRepository pedidoRepository,
            VehiculoRepository vehiculoRepository,
            MatrizCostoService matrizCostoService,
            OptimizacionService optimizacionService) {

        this.planificacionRepository = planificacionRepository;
        this.pedidoRepository = pedidoRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.matrizCostoService = matrizCostoService;
        this.optimizacionService = optimizacionService;
    }

    @Override
    public PlanificacionRespuestaDTO generarPlanificacion(LocalDate fecha) {

        if (fecha == null) {
            throw new IllegalArgumentException(
                "La fecha de planificación es obligatoria.");
        }

        List<Pedido> pedidos =
            pedidoRepository.findByFechaEntregaAndEstado(
                fecha, EstadoPedido.PENDIENTE);

        if (pedidos.isEmpty()) {
            throw new IllegalStateException(
                "No existen pedidos pendientes para la fecha indicada.");
        }

        List<Vehiculo> vehiculos =
            vehiculoRepository.findByDisponibilidadTrue();

        if (vehiculos.isEmpty()) {
            throw new IllegalStateException(
                "No hay vehículos disponibles para generar la planificación.");
        }

        Planificacion planificacion = new Planificacion();
        planificacion.setFecha(fecha);
        planificacion.setEstado(EstadoPlanificacion.GENERADA);

        MatrizCostosDTO matrizCostos =
            matrizCostoService.generarMatriz(fecha);

        optimizacionService.generarAlternativas(
            planificacion,
            pedidos,
            vehiculos,
            matrizCostos
        );

        Planificacion guardada = planificacionRepository.save(planificacion);

        return convertirADTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public PlanificacionRespuestaDTO buscarPorId(Long id) {

        Planificacion planificacion = planificacionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "No se encontró la planificación con ID: " + id));

        return convertirADTO(planificacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanificacionRespuestaDTO> listarPorFecha(LocalDate fecha) {

        if (fecha == null) {
            throw new IllegalArgumentException(
                "La fecha de planificación es obligatoria.");
        }

        return planificacionRepository.findByFecha(fecha)
            .stream()
            .map(this::convertirADTO)
            .toList();
    }

    private PlanificacionRespuestaDTO convertirADTO(
            Planificacion planificacion) {

    	List<PlanificacionRespuestaDTO.AlternativaDTO> alternativas =
                planificacion.getAlternativas()
                    .stream()	
                    .map(alternativa ->
                        new PlanificacionRespuestaDTO.AlternativaDTO(
                            alternativa.getId(),
                            alternativa.getAlgoritmo().name(),
                            alternativa.getDistanciaInicial(), 
                            alternativa.getDistanciaTotal(),
                            alternativa.getTiempoTotal(),
                            alternativa.getTiempoCalculo(),   
                            alternativa.getCantidadVehiculos(),
                            alternativa.getUtilizacionCapacidad(),
                            alternativa.getVentanasIncumplidas(),
                            alternativa.getPedidosNoAsignados(),
                            alternativa.isAplica2opt(),      
                            alternativa.isRecomendada(),
                            alternativa.isSeleccionada(),      
                            alternativa.getRutas().stream().map(RutaDTO::desdeEntidad).toList() // 5. Pasamos la lista de RutaDTO en lugar de .size()
                        )
                    )
                    .toList();

        return PlanificacionRespuestaDTO.desdeEntidad(planificacion);
    }
}
