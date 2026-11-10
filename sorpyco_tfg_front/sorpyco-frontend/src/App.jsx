
import { useState } from "react";
import { generarPlanificacion } from "../../sorpyco-frontend/src/services/PlanificacionService";
import "./App.css";
import MapaRuta from "../src/components/MapaRuta";
function App() {
  const [fecha, setFecha] = useState("2026-11-11");
  const [planificacion, setPlanificacion] = useState(null);
  const [cargando, setCargando] = useState(false);
  const [error, setError] = useState("");

  const generar = async () => {
    try {
      setCargando(true);
      setError("");
      const resultado = await generarPlanificacion(fecha);
      setPlanificacion(resultado);
    } catch (e) {
      setError("No se pudo generar la planificación.");
    } finally {
      setCargando(false);
    }
  };

  const alternativa = planificacion?.alternativas?.[0];

  const distanciaInicial = Number(alternativa?.distanciaInicial ?? 0);
  const distanciaOptimizada = Number(alternativa?.distanciaTotal ?? 0);
  
  const ahorroKm = distanciaInicial - distanciaOptimizada;
  
  const porcentajeAhorro =
    distanciaInicial > 0
      ? (ahorroKm / distanciaInicial) * 100
      : 0;
  
  const formatoNumero = (valor) =>
    Number(valor ?? 0).toLocaleString("es-AR", {
      minimumFractionDigits: 0,
      maximumFractionDigits: 2,
    });
  
  const formatoTiempo = (segundos) => {
    if (segundos == null) return "No disponible";
    const minutos = Math.floor(segundos / 60);
    return `${minutos} min`;
  };

  return (
    <div className="app">
      <aside className="sidebar">
        <div className="brand">S</div>
        <span className="brand-name">SORPYCO</span>
        <nav>
          <div className="nav-item active">▦ <span>Planificación</span></div>
          <div className="nav-item">⌖ <span>Operaciones</span></div>
          <div className="nav-item">▤ <span>Vehículos</span></div>
        </nav>
        <div className="sidebar-footer">Prototipo académico</div>
      </aside>

      <main className="main">
        <header className="topbar">
          <div>
            <p className="eyebrow">GESTIÓN LOGÍSTICA</p>
            <h1>Planificación de distribución</h1>
            <p className="subtitle">
              Organizá pedidos y optimizá las operaciones de entrega.
            </p>
          </div>
          <span className="status">● Sistema operativo</span>
        </header>

        <section className="panel">
          <div>
            <h2>Nueva planificación</h2>
            <p>Seleccioná la fecha para organizar las entregas.</p>
          </div>

          <div className="controls">
            <input
              type="date"
              value={fecha}
              onChange={(e) => setFecha(e.target.value)}
            />
            <button onClick={generar} disabled={cargando}>
              {cargando ? "Procesando..." : "Generar planificación"}
            </button>
          </div>
        </section>

        {error && <div className="error">{error}</div>}

        {alternativa && (
          <>
            <div className="section-heading">
              <div>
                <h2>Resultado de optimización</h2>
                <p>Planificación del {planificacion.fecha}</p>
              </div>
              <span className="algorithm">{alternativa.algoritmo}</span>
            </div>

           
<section className="metrics">

<article className="metric-card">
  <span>Distancia inicial</span>
  <strong>{formatoNumero(distanciaInicial)} km</strong>
  <small>Antes de optimizar</small>
</article>

<article className="metric-card">
  <span>Distancia optimizada</span>
  <strong>{formatoNumero(distanciaOptimizada)} km</strong>
  <small>Recorrido resultante</small>
</article>

<article className="metric-card highlight-card">
  <span>Ahorro de distancia</span>
  <strong>{formatoNumero(ahorroKm)} km</strong>
  <small>{formatoNumero(porcentajeAhorro)} % de reducción</small>
</article>

<article className="metric-card">
  <span>Utilización de capacidad</span>
  <strong>{formatoNumero(alternativa.utilizacionCapacidad)} %</strong>
  <small>Capacidad de la flota utilizada</small>
</article>

<article className="metric-card">
  <span>Tiempo estimado</span>
  <strong>{formatoTiempo(alternativa.tiempoTotal)}</strong>
  <small>Duración estimada del recorrido</small>
</article>

<article className="metric-card">
  <span>Vehículos utilizados</span>
  <strong>{alternativa.cantidadVehiculos}</strong>
  <small>Vehículos asignados</small>
</article>

<article className="metric-card">
  <span>Pedidos sin asignar</span>
  <strong>{alternativa.pedidosNoAsignados}</strong>
  <small>Pedidos pendientes de asignación</small>
</article>

<article className="metric-card">
  <span>Tiempo de cálculo</span>
  <strong>{alternativa.tiempoCalculo} ms</strong>
  <small>Procesamiento del algoritmo</small>
</article>

</section>


            <section className="panel routes-panel">
              <div>
                <h2>Rutas generadas</h2>
                <p>Detalle de los vehículos y sus paradas asignadas.</p>
              </div>

              {alternativa.rutas?.map((ruta) => (
                <article className="route-card" key={ruta.id}>
                  <div className="route-header">
                    <div>
                      <span className="vehicle-label">VEHÍCULO ASIGNADO</span>
                      <h3>{ruta.patente}</h3>
                      <p>{ruta.tipoVehiculo} · Capacidad: {ruta.capacidad} kg</p>
                    </div>
                    <div className="route-distance">
                      <strong>{ruta.distancia} km</strong>
                      <span>Distancia de ruta</span>
                    </div>
                  </div>

                                <h4>Recorrido en el mapa</h4>

              <MapaRuta paradas={ruta.paradas} />

              <h4>Secuencia de entregas</h4>
              <ol className="stops">
                                  {ruta.paradas?.map((parada) => (
                      <li key={parada.id}>
                        <div className="stop-number">{parada.posicion}</div>
                        <div>
                          <strong>{parada.sucursal}</strong>
                          <p>{parada.direccion}</p>
                        </div>
                      </li>
                    ))}
                  </ol>
                </article>
              ))}
            </section>
          </>
        )}

        {!alternativa && (
          <section className="empty-state">
            <div className="empty-icon">⌖</div>
            <h2>Sin planificación generada</h2>
            <p>Seleccioná una fecha y generá una planificación para visualizar los resultados.</p>
          </section>
        )}
      </main>
    </div>
  );
}

export default App;
