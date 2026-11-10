package com.emilio.sorpyco_tfg.servicio.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.emilio.sorpyco_tfg.dto.RuteoDTO;
import com.emilio.sorpyco_tfg.entidades.Nodo;
import com.emilio.sorpyco_tfg.servicio.RutaService;
@Service
public class RutaServiceImpl implements RutaService{

	 private final RestTemplate restTemplate = new RestTemplate();

	    private final String baseUrl;

	    public RutaServiceImpl(
	            @Value("${osrm.base-url}") String baseUrl) {
	        this.baseUrl = baseUrl;
	    }

	
	
	@Override
	public RuteoDTO calcularMatriz(List<Nodo> nodos) {
		// TODO Auto-generated method stub
	       if (nodos == null || nodos.isEmpty()) {
	            throw new IllegalArgumentException(
	                    "Debe proporcionar al menos un nodo.");
	        }

	        if (nodos.stream().anyMatch(nodo ->
	                nodo.getLatitud() == null
	                || nodo.getLongitud() == null)) {
	            throw new IllegalArgumentException(
	                    "Todos los nodos deben tener coordenadas.");
	        }

	        if (nodos.size() > 100) {
	            throw new IllegalArgumentException(
	                    "La cantidad de nodos supera el límite de OSRM");
	        }

	     // 2. Construcción optimizada del string de coordenadas (lon,lat separados por punto y coma)
	        String coordenadas = nodos.stream()
	                .map(nodo ->
	                        nodo.getLongitud() + "," + nodo.getLatitud())
	                .collect(Collectors.joining(";"));

	      
	     // 3. Construcción limpia de la URI con UriComponentsBuilder
	        var uri =UriComponentsBuilder
	                .fromUriString(baseUrl)
	                .path("/table/v1/driving/" + coordenadas)
	                .queryParam("annotations", "distance,duration")
	                .build()
	                .encode()
	                .toUri();

	        try {
	            RespuestaOSRM respuesta = restTemplate.exchange(
	                    uri,
	                    HttpMethod.GET,
	                    null,
	                    new ParameterizedTypeReference<RespuestaOSRM>() {}
	            ).getBody();

	            if (respuesta == null || !"Ok".equals(respuesta.code())) {
	                throw new RuntimeException(
	                        "OSRM no pudo calcular la matriz de ruteo.");
	            }

	            if (respuesta.distances() == null
	                    || respuesta.durations() == null) {
	                throw new RuntimeException(
	                        "error de OSRM");
	            }
	            // Aqui deberia devolver una matriz en crudo con la distancia y duracion de los nodos
	        	//que enviamos
	            RuteoDTO resultadoCompleto = new RuteoDTO(respuesta.distances, respuesta.durations);
	            
	            return resultadoCompleto;

	        } catch (RestClientException e) {
	            throw new RuntimeException(
	                    "Error al comunicarse con OSRM.", e);
	        }
	    }

	   
	    private record RespuestaOSRM(
	            String code,
	            double[][] distances,
	            double[][] durations
	    ) {
	    	
	}

}
