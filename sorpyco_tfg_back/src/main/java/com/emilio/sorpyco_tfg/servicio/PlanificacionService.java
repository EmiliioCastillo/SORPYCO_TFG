package com.emilio.sorpyco_tfg.servicio;



import java.time.LocalDate;
import java.util.List;

import com.emilio.sorpyco_tfg.dto.PlanificacionRespuestaDTO;
import com.emilio.sorpyco_tfg.entidades.Planificacion;

public interface PlanificacionService {

    // Genera una planificación para una fecha determinada.
    PlanificacionRespuestaDTO generarPlanificacion(LocalDate fecha);

    // Busca una planificación por su identificador.
    PlanificacionRespuestaDTO buscarPorId(Long id);

    // Lista las planificaciones de una fecha.
    List<PlanificacionRespuestaDTO> listarPorFecha(LocalDate fecha);
}
