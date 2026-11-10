package com.emilio.sorpyco_tfg.entidades;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.emilio.sorpyco_tfg.enums.EstadoPlanificacion;

/**
 * Proceso de planificacion de la distribucion para una fecha. Agrupa las alternativas generadas.
 */
@Entity
@Table(name = "planificacion")
public class Planificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPlanificacion estado = EstadoPlanificacion.GENERADA;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion = LocalDateTime.now();
/*orphanRemoval para gestionar el ciclo de vida de las entidades hijas dependientes en este caso alternativa.
 * */
    @OneToMany(mappedBy = "planificacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alternativa> alternativas = new ArrayList<>();

    public Planificacion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public EstadoPlanificacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoPlanificacion estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public List<Alternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<Alternativa> alternativas) {
        this.alternativas = alternativas;
    }

    public void addAlternativa(Alternativa alternativa) {
        alternativas.add(alternativa);
        alternativa.setPlanificacion(this);
    }

    public void removeAlternativa(Alternativa alternativa) {
        alternativas.remove(alternativa);
        alternativa.setPlanificacion(null);
    }
}
