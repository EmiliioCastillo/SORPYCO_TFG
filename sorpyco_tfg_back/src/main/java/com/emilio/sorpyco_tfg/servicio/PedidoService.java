
package com.emilio.sorpyco_tfg.servicio;

import java.time.LocalDate;
import java.util.List;

import com.emilio.sorpyco_tfg.entidades.Pedido;

public interface PedidoService {

    // Registrar un nuevo pedido
    Pedido registrar(Pedido pedido, Long sucursalId);

    // Consultar todos los pedidos
    List<Pedido> listarTodos();

    // Buscar un pedido por su identificador
    Pedido buscarPorId(Long id);

    // Consultar pedidos por fecha de entrega
    List<Pedido> listarPorFecha(LocalDate fecha);

    // Consultar pedidos pendientes de una fecha
    List<Pedido> listarPendientesPorFecha(LocalDate fecha);

    // Modificar un pedido existente
    Pedido actualizar(Long id, Pedido datos, Long sucursalId);

    // Eliminar un pedido
    void eliminar(Long id);
}
