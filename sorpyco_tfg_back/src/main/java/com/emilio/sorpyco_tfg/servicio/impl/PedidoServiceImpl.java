package com.emilio.sorpyco_tfg.servicio.impl;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emilio.sorpyco_tfg.entidades.Pedido;
import com.emilio.sorpyco_tfg.entidades.Sucursal;
import com.emilio.sorpyco_tfg.enums.EstadoPedido;
import com.emilio.sorpyco_tfg.repositorio.PedidoRepository;
import com.emilio.sorpyco_tfg.repositorio.SucursalRepository;
import com.emilio.sorpyco_tfg.servicio.PedidoService;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final SucursalRepository sucursalRepository;

    public PedidoServiceImpl(
            PedidoRepository pedidoRepository,
            SucursalRepository sucursalRepository) {

        this.pedidoRepository = pedidoRepository;
        this.sucursalRepository = sucursalRepository;
    }

    @Override
    public Pedido registrar(Pedido pedido, Long sucursalId) {

        validarPedido(pedido);

        Sucursal sucursal = buscarSucursal(sucursalId);

        pedido.setSucursal(sucursal);
        pedido.setEstado(EstadoPedido.PENDIENTE);

        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Pedido buscarPorId(Long id) {

        return pedidoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "No se encontró el pedido con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarPorFecha(LocalDate fecha) {

        if (fecha == null) {
            throw new IllegalArgumentException(
                "La fecha de entrega es obligatoria.");
        }

        return pedidoRepository.findByFechaEntrega(fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarPendientesPorFecha(LocalDate fecha) {

        if (fecha == null) {
            throw new IllegalArgumentException(
                "La fecha de entrega es obligatoria.");
        }

        return pedidoRepository.findByFechaEntregaAndEstado(
            fecha, EstadoPedido.PENDIENTE);
    }

    @Override
    public Pedido actualizar(
            Long id,
            Pedido datos,
            Long sucursalId) {

        Pedido pedidoExistente = buscarPorId(id);

        validarPedido(datos);

        Sucursal sucursal = buscarSucursal(sucursalId);

        pedidoExistente.setSucursal(sucursal);
        pedidoExistente.setFechaEntrega(datos.getFechaEntrega());
        pedidoExistente.setPeso(datos.getPeso());
        pedidoExistente.setPrioridad(datos.getPrioridad());
        pedidoExistente.setVentanaDesde(datos.getVentanaDesde());
        pedidoExistente.setVentanaHasta(datos.getVentanaHasta());

        if (datos.getEstado() != null) {
            pedidoExistente.setEstado(datos.getEstado());
        }

        return pedidoRepository.save(pedidoExistente);
    }

    @Override
    public void eliminar(Long id) {

        Pedido pedido = buscarPorId(id);
        pedidoRepository.delete(pedido);
    }

    private Sucursal buscarSucursal(Long sucursalId) {

        if (sucursalId == null) {
            throw new IllegalArgumentException(
                "Debe indicar una sucursal.");
        }

        return sucursalRepository.findById(sucursalId)
            .orElseThrow(() -> new IllegalArgumentException(
                "No se encontró la sucursal con ID: " + sucursalId));
    }

    private void validarPedido(Pedido pedido) {

        if (pedido == null) {
            throw new IllegalArgumentException(
                "Los datos del pedido son obligatorios.");
        }

        if (pedido.getFechaEntrega() == null) {
            throw new IllegalArgumentException(
                "La fecha de entrega es obligatoria.");
        }

        if (pedido.getPeso() == null
                || pedido.getPeso().signum() <= 0) {
            throw new IllegalArgumentException(
                "El peso debe ser mayor que cero.");
        }

        if (pedido.getPrioridad() == null) {
            throw new IllegalArgumentException(
                "Debe indicar la prioridad del pedido.");
        }

        boolean tieneDesde = pedido.getVentanaDesde() != null;
        boolean tieneHasta = pedido.getVentanaHasta() != null;

        if (tieneDesde != tieneHasta) {
            throw new IllegalArgumentException(
                "Debe indicar ambas horas de la ventana horaria.");
        }

        if (tieneDesde
                && !pedido.getVentanaHasta()
                    .isAfter(pedido.getVentanaDesde())) {
            throw new IllegalArgumentException(
                "La hora de finalización debe ser posterior a la hora de inicio.");
        }
    }
}
