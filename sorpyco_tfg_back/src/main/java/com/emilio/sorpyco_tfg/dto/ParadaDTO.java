package com.emilio.sorpyco_tfg.dto;

import java.math.BigDecimal;
import java.time.LocalTime;

import com.emilio.sorpyco_tfg.entidades.Parada;
import com.emilio.sorpyco_tfg.enums.EstadoParada;

public record ParadaDTO(
    Long id,
    int posicion,
    Long pedidoId,
    String sucursal,
    String direccion,
    BigDecimal latitud,
    BigDecimal longitud,
    LocalTime horaEstimada,
    EstadoParada estado
) {

    public static ParadaDTO desdeEntidad(Parada parada) {
        var pedido = parada.getPedido();
        var sucursal = pedido.getSucursal();
        var nodo = sucursal.getNodo();

        return new ParadaDTO(
            parada.getId(),
            parada.getPosicion(),
            pedido.getId(),
            sucursal.getNombre(),
            nodo.getDireccion(),
            nodo.getLatitud(),
            nodo.getLongitud(),
            parada.getHoraEstimada(),
            parada.getEstado()
        );
    }
}
