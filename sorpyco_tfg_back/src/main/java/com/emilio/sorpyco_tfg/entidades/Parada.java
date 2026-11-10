package com.emilio.sorpyco_tfg.entidades;

import jakarta.persistence.*;
import java.time.LocalTime;

import com.emilio.sorpyco_tfg.enums.EstadoParada;

/**
 * Visita planificada dentro de una ruta; define el orden de entrega de un pedido.
 */
@Entity
@Table(name = "parada")
public class Parada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ruta_id", nullable = false)
    private Ruta ruta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Column(name = "posicion", nullable = false)
    private int posicion;

    @Column(name = "hora_estimada")
    private LocalTime horaEstimada;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoParada estado = EstadoParada.PENDIENTE;

    public Parada() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public LocalTime getHoraEstimada() {
        return horaEstimada;
    }

    public void setHoraEstimada(LocalTime horaEstimada) {
        this.horaEstimada = horaEstimada;
    }

    public EstadoParada getEstado() {
        return estado;
    }

    public void setEstado(EstadoParada estado) {
        this.estado = estado;
    }
}
