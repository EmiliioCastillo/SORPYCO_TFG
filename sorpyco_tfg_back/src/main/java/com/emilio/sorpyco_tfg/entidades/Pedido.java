package com.emilio.sorpyco_tfg.entidades;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.emilio.sorpyco_tfg.enums.EstadoPedido;
import com.emilio.sorpyco_tfg.enums.Prioridad;


/**
 * Solicitud de entrega asociada a una sucursal.
 */
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sucursal_id", nullable = false)
    private Sucursal sucursal;

    @Column(name = "fecha_entrega", nullable = false)
    private LocalDate fechaEntrega;

    @Column(name = "peso", nullable = false, precision = 10, scale = 2)
    private BigDecimal peso;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridad", nullable = false)
    private Prioridad prioridad;

    @Column(name = "ventana_desde")
    private LocalTime ventanaDesde;

    @Column(name = "ventana_hasta")
    private LocalTime ventanaHasta;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPedido estado = EstadoPedido.PENDIENTE;

    public Pedido() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public LocalTime getVentanaDesde() {
        return ventanaDesde;
    }

    public void setVentanaDesde(LocalTime ventanaDesde) {
        this.ventanaDesde = ventanaDesde;
    }

    public LocalTime getVentanaHasta() {
        return ventanaHasta;
    }

    public void setVentanaHasta(LocalTime ventanaHasta) {
        this.ventanaHasta = ventanaHasta;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }
}
