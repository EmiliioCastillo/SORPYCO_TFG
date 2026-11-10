
import {
  MapContainer,
  TileLayer,
  Marker,
  Popup,
  Polyline,
} from "react-leaflet";

import L from "leaflet";
import "leaflet/dist/leaflet.css";
import "./MapaRuta.css";

// Crea un marcador circular numerado.
const crearIcono = (numero) =>
  L.divIcon({
    className: "marcador-personalizado",
    html: `<div class="marcador-numero">${numero}</div>`,
    iconSize: [34, 34],
    iconAnchor: [17, 17],
  });

function MapaRuta({ paradas }) {
  // Agrupamos las paradas que comparten ubicación.
  const ubicaciones = [];

  (paradas || []).forEach((parada) => {
    if (
      parada.latitud == null ||
      parada.longitud == null
    ) return;

    const lat = Number(parada.latitud);
    const lng = Number(parada.longitud);

    if (!Number.isFinite(lat) || !Number.isFinite(lng)) return;

    const existente = ubicaciones.find(
      (u) => u.lat === lat && u.lng === lng
    );

    if (existente) {
      existente.paradas.push(parada);
    } else {
      ubicaciones.push({
        lat,
        lng,
        nombre: parada.sucursal,
        direccion: parada.direccion,
        paradas: [parada],
      });
    }
  });

  if (ubicaciones.length === 0) {
    return <p>No hay coordenadas disponibles.</p>;
  }

  const puntos = ubicaciones.map((u) => [u.lat, u.lng]);

  return (
    <div className="mapa-container">
      <MapContainer
        bounds={puntos}
        boundsOptions={{ padding: [40, 40] }}
        scrollWheelZoom={true}
        className="mapa"
      >
        <TileLayer
          attribution='&copy; OpenStreetMap'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />

        {/* Línea del recorrido */}
        {puntos.length > 1 && (
          <Polyline
            positions={puntos}
            pathOptions={{
              color: "#2869df",
              weight: 5,
              opacity: 0.8,
            }}
          />
        )}

        {/* Marcadores numerados */}
        {ubicaciones.map((ubicacion, index) => (
          <Marker
            key={`${ubicacion.lat}-${ubicacion.lng}`}
            position={[ubicacion.lat, ubicacion.lng]}
            icon={crearIcono(index + 1)}
          >
            <Popup>
              <strong>{ubicacion.nombre}</strong>
              <p>{ubicacion.direccion}</p>
              <strong>
                {ubicacion.paradas.length} pedido(s)
              </strong>

              <ul>
                {ubicacion.paradas.map((p) => (
                  <li key={p.id}>
                    Pedido #{p.pedidoId}
                  </li>
                ))}
              </ul>
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </div>
  );
}

export default MapaRuta;
