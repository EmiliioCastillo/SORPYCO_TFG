package com.emilio.sorpyco_tfg.dto;

import java.math.BigDecimal;

import com.emilio.sorpyco_tfg.entidades.Vehiculo;
import com.emilio.sorpyco_tfg.enums.TipoVehiculo;

public record VehiculoDTO(
    Long id,
    String patente,
    TipoVehiculo tipoVehiculo,
    BigDecimal capacidad,
    boolean disponibilidad
) {
    public static VehiculoDTO desdeEntidad(Vehiculo vehiculo) {
        return new VehiculoDTO(
            vehiculo.getId(),
            vehiculo.getPatente(),
            vehiculo.getTipoVehiculo(),
            vehiculo.getCapacidad(),
            vehiculo.isDisponibilidad()
        );
    }
}
