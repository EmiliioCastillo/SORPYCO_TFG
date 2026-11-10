package com.emilio.sorpyco_tfg.entidades;

import jakarta.persistence.*;
import java.math.BigDecimal;

import com.emilio.sorpyco_tfg.enums.TipoNodo;

/**
 * Punto geografico relevante para el ruteo: deposito de origen o punto de entrega.
 */
@Entity
@Table(name = "nodo")
public class Nodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "direccion", nullable = false, length = 255)
    private String direccion;

    @Column(name = "latitud", nullable = false, precision = 10, scale = 7)
    private BigDecimal latitud;

    @Column(name = "longitud", nullable = false, precision = 10, scale = 7)
    private BigDecimal longitud;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_nodo", nullable = false)
    private TipoNodo tipoNodo;

    public Nodo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public TipoNodo getTipoNodo() {
        return tipoNodo;
    }

    public void setTipoNodo(TipoNodo tipoNodo) {
        this.tipoNodo = tipoNodo;
    }
}
