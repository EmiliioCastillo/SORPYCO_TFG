package com.emilio.sorpyco_tfg.entidades;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.emilio.sorpyco_tfg.enums.Algoritmo;



/**
 * Solucion completa de distribucion generada por un algoritmo, con las metricas para compararla.
 */
@Entity
@Table(name = "alternativa")
public class Alternativa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "planificacion_id", nullable = false)
    private Planificacion planificacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "algoritmo", nullable = false)
    private Algoritmo algoritmo;

    @Column(name = "aplica_2opt", nullable = false)
    private boolean aplica2opt = false;

    @Column(name = "distancia_inicial", precision = 10, scale = 2)
    private BigDecimal distanciaInicial;

    @Column(name = "distancia_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal distanciaTotal;

    @Column(name = "tiempo_total")
    private Integer tiempoTotal;

    @Column(name = "tiempo_calculo", nullable = false)
    private int tiempoCalculo;

    @Column(name = "cantidad_vehiculos", nullable = false)
    private int cantidadVehiculos;

    @Column(name = "utilizacion_capacidad", nullable = false, precision = 5, scale = 2)
    private BigDecimal utilizacionCapacidad;

    @Column(name = "ventanas_incumplidas", nullable = false)
    private int ventanasIncumplidas = 0;

    @Column(name = "pedidos_no_asignados", nullable = false)
    private int pedidosNoAsignados = 0;

    @Column(name = "recomendada", nullable = false)
    private boolean recomendada = false;

    @Column(name = "seleccionada", nullable = false)
    private boolean seleccionada = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alternativa_origen_id")
    private Alternativa alternativaOrigen;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion = LocalDateTime.now();

    @OneToMany(mappedBy = "alternativa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ruta> rutas = new ArrayList<>();

    public Alternativa() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Planificacion getPlanificacion() {
        return planificacion;
    }

    public void setPlanificacion(Planificacion planificacion) {
        this.planificacion = planificacion;
    }

    public Algoritmo getAlgoritmo() {
        return algoritmo;
    }

    public void setAlgoritmo(Algoritmo algoritmo) {
        this.algoritmo = algoritmo;
    }

    public boolean isAplica2opt() {
        return aplica2opt;
    }

    public void setAplica2opt(boolean aplica2opt) {
        this.aplica2opt = aplica2opt;
    }

    public BigDecimal getDistanciaInicial() {
        return distanciaInicial;
    }

    public void setDistanciaInicial(BigDecimal distanciaInicial) {
        this.distanciaInicial = distanciaInicial;
    }

    public BigDecimal getDistanciaTotal() {
        return distanciaTotal;
    }

    public void setDistanciaTotal(BigDecimal distanciaTotal) {
        this.distanciaTotal = distanciaTotal;
    }

    public Integer getTiempoTotal() {
        return tiempoTotal;
    }

    public void setTiempoTotal(Integer tiempoTotal) {
        this.tiempoTotal = tiempoTotal;
    }

    public int getTiempoCalculo() {
        return tiempoCalculo;
    }

    public void setTiempoCalculo(int tiempoCalculo) {
        this.tiempoCalculo = tiempoCalculo;
    }

    public int getCantidadVehiculos() {
        return cantidadVehiculos;
    }

    public void setCantidadVehiculos(int cantidadVehiculos) {
        this.cantidadVehiculos = cantidadVehiculos;
    }

    public BigDecimal getUtilizacionCapacidad() {
        return utilizacionCapacidad;
    }

    public void setUtilizacionCapacidad(BigDecimal utilizacionCapacidad) {
        this.utilizacionCapacidad = utilizacionCapacidad;
    }

    public int getVentanasIncumplidas() {
        return ventanasIncumplidas;
    }

    public void setVentanasIncumplidas(int ventanasIncumplidas) {
        this.ventanasIncumplidas = ventanasIncumplidas;
    }

    public int getPedidosNoAsignados() {
        return pedidosNoAsignados;
    }

    public void setPedidosNoAsignados(int pedidosNoAsignados) {
        this.pedidosNoAsignados = pedidosNoAsignados;
    }

    public boolean isRecomendada() {
        return recomendada;
    }

    public void setRecomendada(boolean recomendada) {
        this.recomendada = recomendada;
    }

    public boolean isSeleccionada() {
        return seleccionada;
    }

    public void setSeleccionada(boolean seleccionada) {
        this.seleccionada = seleccionada;
    }

    public Alternativa getAlternativaOrigen() {
        return alternativaOrigen;
    }

    public void setAlternativaOrigen(Alternativa alternativaOrigen) {
        this.alternativaOrigen = alternativaOrigen;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public List<Ruta> getRutas() {
        return rutas;
    }

    public void setRutas(List<Ruta> rutas) {
        this.rutas = rutas;
    }

    public void addRuta(Ruta ruta) {
        rutas.add(ruta);
        ruta.setAlternativa(this);
    }

    public void removeRuta(Ruta ruta) {
        rutas.remove(ruta);
        ruta.setAlternativa(null);
    }
}
