package com.emilio.sorpyco_tfg.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.emilio.sorpyco_tfg.entidades.Pedido;
import com.emilio.sorpyco_tfg.enums.EstadoPedido;
import com.emilio.sorpyco_tfg.enums.Prioridad;

public record PedidoDTO(
        Long id,
        Long sucursalId,
        String nombreSucursal, 
        LocalDate fechaEntrega,
        BigDecimal peso,
        Prioridad prioridad,
        LocalTime ventanaDesde,
        LocalTime ventanaHasta,
        EstadoPedido estado
) {
    public static PedidoDTO desdeEntidad(Pedido pedido) {
        return new PedidoDTO(
                pedido.getId(),
                pedido.getSucursal() != null ? pedido.getSucursal().getId() : null,
                pedido.getSucursal() != null ? pedido.getSucursal().getNombre() : null,
                pedido.getFechaEntrega(),
                pedido.getPeso(),
                pedido.getPrioridad(),
                pedido.getVentanaDesde(),
                pedido.getVentanaHasta(),
                pedido.getEstado()
        );
    }
}