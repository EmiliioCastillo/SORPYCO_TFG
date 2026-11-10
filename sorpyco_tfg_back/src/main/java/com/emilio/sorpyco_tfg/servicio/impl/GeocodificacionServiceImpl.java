package com.emilio.sorpyco_tfg.servicio.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.emilio.sorpyco_tfg.dto.CoordenadasDTO;
import com.emilio.sorpyco_tfg.servicio.GeocodificacionService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/*Tengamos lo siguiente:
 * Caché en memoria (ConcurrentHashMap): Evitemos pegarle dos veces a la misma dirección a Nominatim.
 * Rate Limiting (esperarIntervalo): Protegamos el service de ser bloqueada 
 * por los servidores de OpenStreetMap por saturación de peticiones.
 * Mapeo tipado (Jackson): Evitemos depender de parseos manuales
 * propensos a errores de índice o nulos.
 * 
 * 
 * */
@Service
public class GeocodificacionServiceImpl implements GeocodificacionService{
	//URL de nominatim si o si
	  private static final String URL_NOMINATIM =
	            "https://nominatim.openstreetmap.org/search";

	    private final RestTemplate restTemplate = new RestTemplate();

	    private final String userAgent;

	    // Almacena resultados para evitar consultas repetidas
	    private final Map<String, CoordenadasDTO> cache =
	            new ConcurrentHashMap<>();

	    private long ultimaSolicitud = 0;

	    public GeocodificacionServiceImpl(
	            @Value("${nominatim.user-agent}") String userAgent) {
	        this.userAgent = userAgent;
	    }
	    
	
	@Override
	public CoordenadasDTO geocodificarDireccion(String direccion) {
		// TODO Auto-generated method stub
		//Validacion de direccion
		  if (direccion == null || direccion.isBlank()) {
	            throw new IllegalArgumentException(
	                    "La dirección es obligatoria.");
	        }
		  //normalizamos la direccion 
		  String direccionNormalizada =
	                direccion.trim().replaceAll("\\s+", " ");
		  //cache en memoria para evitar las llamadas duplicadas
		  String claveCache = direccionNormalizada.toLowerCase();
		  
		   // Consultar primero la caché
	        CoordenadasDTO coordenadasGuardadas = cache.get(claveCache);

	        if (coordenadasGuardadas != null) {
	            return coordenadasGuardadas;
	        }
	        
	        // Construir la solicitud a Nominatim
	        var uri = UriComponentsBuilder
	                .fromUriString(URL_NOMINATIM)
	                .queryParam("q", direccionNormalizada)
	                .queryParam("format", "jsonv2")
	                .queryParam("limit", 1)
	                .queryParam("countrycodes", "ar")
	                .queryParam("accept-language", "es")
	                .build()
	                .encode()
	                .toUri();

	        // Identificar nuestra aplicación
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("User-Agent", userAgent);
	        //creamos el contenedor que va a encapsular los headers
	        HttpEntity<Void> solicitud = new HttpEntity<>(headers);
	        
	        try {
	        	//Indispensable hacer el metodo que va a limitar
	        	esperarIntervalo();
	        	
	        	  List<RespuestaNominatim> resultados =
	                      restTemplate.exchange(
	                              uri,
	                              HttpMethod.GET,
	                              solicitud,
	                              new ParameterizedTypeReference<
	                                      List<RespuestaNominatim>>() {}
	                      ).getBody();

	        	  if (resultados == null || resultados.isEmpty()) {
	                  throw new RuntimeException(
	                          "Nominatim no encontró resultados para la dirección: "
	                                  + direccionNormalizada);
	              }

	              RespuestaNominatim resultado = resultados.get(0);

	              CoordenadasDTO coordenadas = new CoordenadasDTO(
	                      new BigDecimal(resultado.getLat()),
	                      new BigDecimal(resultado.getLon())
	              );

	              // Guardar el resultado en caché
	              cache.put(claveCache, coordenadas);

	              return coordenadas;
	              
	        	  
	        }catch(RestClientException e) {
	            throw new RuntimeException(
	                    "Error al comunicarse con Nominatim.", e);

	        } catch (NumberFormatException e) {
	            throw new RuntimeException(
	                    "Nominatim devolvió coordenadas inválidas.", e);
	        }
	        
		
	}
	 private synchronized void esperarIntervalo() {
		 	
	        long ahora = System.currentTimeMillis();
	        long tiempoTranscurrido = ahora - ultimaSolicitud;
	        long espera = 1000 - tiempoTranscurrido;

	        if (espera > 0) {
	            try {
	                Thread.sleep(espera);
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	                throw new RuntimeException(
	                        "Se interrumpió la consulta a Nominatim.", e);
	            }
	        }

	        ultimaSolicitud = System.currentTimeMillis();
	    }

	
	 // Representación de un resultado JSON de Nominatim con la clase interna
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class RespuestaNominatim {

        private String lat;
        private String lon;

        public String getLat() {
            return lat;
        }

        @JsonProperty("lat")
        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        @JsonProperty("lon")
        public void setLon(String lon) {
            this.lon = lon;
        }
    }
}
