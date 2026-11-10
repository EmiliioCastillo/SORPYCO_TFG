package com.emilio.sorpyco_tfg.dto;

import java.math.BigDecimal;

public record SucursalUbicacionDTO(
        Long id,
        String nombre,
        String direccion,
        BigDecimal latitud,
        BigDecimal longitud
) {}