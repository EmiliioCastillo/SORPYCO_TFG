
package com.emilio.sorpyco_tfg.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.emilio.sorpyco_tfg.entidades.Alternativa;
import com.emilio.sorpyco_tfg.entidades.Planificacion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PlanificacionRespuestaDTO(
    Long id,
    LocalDate fecha,
    String estado,
    List<AlternativaDTO> alternativas
) {

    public record AlternativaDTO(
        Long id,
        String algoritmo,
        BigDecimal distanciaInicial,
        BigDecimal distanciaTotal,
        Integer tiempoTotal,
        int tiempoCalculo,
        int cantidadVehiculos,
        BigDecimal utilizacionCapacidad,
        int ventanasIncumplidas,
        int pedidosNoAsignados,
        boolean aplica2opt,
        boolean recomendada,
        boolean seleccionada,
        List<RutaDTO> rutas
    ) {

        public static AlternativaDTO desdeEntidad(
               Alternativa alternativa) {

            List<RutaDTO> rutasDTO = alternativa.getRutas()
                .stream()
                .map(RutaDTO::desdeEntidad)
                .toList();

            return new AlternativaDTO(
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
                rutasDTO
            );
        }
    }

    public static PlanificacionRespuestaDTO desdeEntidad(
            Planificacion planificacion) {

        List<AlternativaDTO> alternativasDTO =
            planificacion.getAlternativas()
                .stream()
                .map(AlternativaDTO::desdeEntidad)
                .toList();

        return new PlanificacionRespuestaDTO(
            planificacion.getId(),
            planificacion.getFecha(),
            planificacion.getEstado().name(),
            alternativasDTO
        );
    }
}

