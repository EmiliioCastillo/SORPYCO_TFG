package com.emilio.sorpyco_tfg.servicio.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emilio.sorpyco_tfg.dto.CoordenadasDTO;
import com.emilio.sorpyco_tfg.entidades.Nodo;
import com.emilio.sorpyco_tfg.enums.TipoNodo;
import com.emilio.sorpyco_tfg.repositorio.NodoRepository;
import com.emilio.sorpyco_tfg.servicio.GeocodificacionService;
import com.emilio.sorpyco_tfg.servicio.NodoService;
@Service
public class NodoServiceImpl implements NodoService{

	@Autowired
	private NodoRepository nodoRepository;
	@Autowired
	private  GeocodificacionService geocodificacionService;
	
   
    
    // Listar todos los nodos
    @Transactional(readOnly = true)
    public List<Nodo> listarTodos() {
        return nodoRepository.findAll();
    }

    // Buscar un nodo por ID
    @Transactional(readOnly = true)
    public Nodo buscarPorId(Long id) {
        return nodoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "No se encontró el nodo con ID: " + id));
    }

    // Listar nodos según su tipo
    @Transactional(readOnly = true)
    public List<Nodo> listarPorTipo(TipoNodo tipoNodo) {

        if (tipoNodo == null) {
            throw new IllegalArgumentException(
                    "El tipo de nodo es obligatorio.");
        }

        return nodoRepository.findByTipoNodo(tipoNodo);
    }

    // Listar depósitos
    @Transactional(readOnly = true)
    public List<Nodo> listarDepositos() {
        return listarPorTipo(TipoNodo.DEPOSITO);
    }

    // Listar puntos de entrega
    @Transactional(readOnly = true)
    public List<Nodo> listarPuntosEntrega() {
        return listarPorTipo(TipoNodo.PUNTO_ENTREGA);
    }

    // Registrar un nuevo nodo
    public Nodo guardar(Nodo nodo) {
    	  validarNodo(nodo);
    	  
    	    CoordenadasDTO coordenadas =
    	            geocodificacionService.geocodificarDireccion(nodo.getDireccion());

    	    nodo.setLatitud(coordenadas.getLatitud());
    	    nodo.setLongitud(coordenadas.getLongitud());

    	    return nodoRepository.save(nodo);
    }

    // Actualizar un nodo existente
    public Nodo actualizar(Long id, Nodo datosActualizados) {

        validarNodo(datosActualizados);

        Nodo nodo = buscarPorId(id);

        nodo.setNombre(datosActualizados.getNombre());
        nodo.setDireccion(datosActualizados.getDireccion());
        nodo.setLatitud(datosActualizados.getLatitud());
        nodo.setLongitud(datosActualizados.getLongitud());
        nodo.setTipoNodo(datosActualizados.getTipoNodo());

        return nodoRepository.save(nodo);
    }

    // Eliminar un nodo
    public void eliminar(Long id) {
        Nodo nodo = buscarPorId(id);
        nodoRepository.delete(nodo);
    }

    // Validar los datos del nodo
    private void validarNodo(Nodo nodo) {

        if (nodo == null) {
            throw new IllegalArgumentException(
                    "Los datos del nodo son obligatorios.");
        }

        if (nodo.getNombre() == null || nodo.getNombre().isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del nodo es obligatorio.");
        }

        if (nodo.getDireccion() == null || nodo.getDireccion().isBlank()) {
            throw new IllegalArgumentException(
                    "La dirección del nodo es obligatoria.");
        }

        if (nodo.getLatitud() == null
                || nodo.getLongitud() == null) {
            throw new IllegalArgumentException(
                    "Las coordenadas geográficas son obligatorias.");
        }

        if (nodo.getLatitud().compareTo(BigDecimal.valueOf(-90)) < 0
                || nodo.getLatitud().compareTo(BigDecimal.valueOf(90)) > 0) {
            throw new IllegalArgumentException(
                    "La latitud debe estar entre -90 y 90.");
        }

        if (nodo.getLongitud().compareTo(BigDecimal.valueOf(-180)) < 0
                || nodo.getLongitud().compareTo(BigDecimal.valueOf(180)) > 0) {
            throw new IllegalArgumentException(
                    "La longitud debe estar entre -180 y 180.");
        }

        if (nodo.getTipoNodo() == null) {
            throw new IllegalArgumentException(
                    "El tipo de nodo es obligatorio.");
        }

        nodo.setNombre(nodo.getNombre().trim());
        nodo.setDireccion(nodo.getDireccion().trim());
    }
}
