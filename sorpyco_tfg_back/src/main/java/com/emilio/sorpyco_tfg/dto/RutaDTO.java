package com.emilio.sorpyco_tfg.dto;

import java.math.BigDecimal;
import java.util.List;

import com.emilio.sorpyco_tfg.entidades.Ruta;
import com.emilio.sorpyco_tfg.enums.EstadoRuta;

public record RutaDTO(
    Long id,
    Long vehiculoId,
    String patente,
    String tipoVehiculo,
    BigDecimal capacidad,
    BigDecimal distancia,
    Integer tiempoEstimado,
    EstadoRuta estado,
    List<ParadaDTO> paradas
) {

    public static RutaDTO desdeEntidad(Ruta ruta) {

        var vehiculo = ruta.getVehiculo();

        List<ParadaDTO> paradasDTO = ruta.getParadas()
            .stream()
            .map(ParadaDTO::desdeEntidad)
            .toList();

        return new RutaDTO(
            ruta.getId(),
            vehiculo.getId(),
            vehiculo.getPatente(),
            vehiculo.getTipoVehiculo().name(),
            vehiculo.getCapacidad(),
            ruta.getDistancia(),
            ruta.getTiempoEstimado(),
            ruta.getEstado(),
            paradasDTO
        );
    }
}
