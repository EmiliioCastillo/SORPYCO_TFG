package com.emilio.sorpyco_tfg.entidades;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.emilio.sorpyco_tfg.enums.EstadoRuta;


/**
 * Recorrido asignado a un vehiculo dentro de una alternativa.
 */
@Entity
@Table(name = "ruta")
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "alternativa_id", nullable = false)
    private Alternativa alternativa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @Column(name = "distancia", precision = 10, scale = 2)
    private BigDecimal distancia;

    @Column(name = "tiempo_estimado")
    private Integer tiempoEstimado;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoRuta estado = EstadoRuta.PLANIFICADA;

    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("posicion ASC")
    private List<Parada> paradas = new ArrayList<>();

    public Ruta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Alternativa getAlternativa() {
        return alternativa;
    }

    public void setAlternativa(Alternativa alternativa) {
        this.alternativa = alternativa;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public BigDecimal getDistancia() {
        return distancia;
    }

    public void setDistancia(BigDecimal distancia) {
        this.distancia = distancia;
    }

    public Integer getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(Integer tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public EstadoRuta getEstado() {
        return estado;
    }

    public void setEstado(EstadoRuta estado) {
        this.estado = estado;
    }

    public List<Parada> getParadas() {
        return paradas;
    }

    public void setParadas(List<Parada> paradas) {
        this.paradas = paradas;
    }

    public void addParada(Parada parada) {
        paradas.add(parada);
        parada.setRuta(this);
    }

    public void removeParada(Parada parada) {
        paradas.remove(parada);
        parada.setRuta(null);
    }
}
