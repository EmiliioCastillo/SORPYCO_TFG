package com.emilio.sorpyco_tfg.servicio.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emilio.sorpyco_tfg.entidades.Cliente;
import com.emilio.sorpyco_tfg.entidades.Nodo;
import com.emilio.sorpyco_tfg.entidades.Sucursal;
import com.emilio.sorpyco_tfg.repositorio.ClienteRepository;
import com.emilio.sorpyco_tfg.repositorio.NodoRepository;
import com.emilio.sorpyco_tfg.repositorio.SucursalRepository;
import com.emilio.sorpyco_tfg.servicio.SucursalService;
@Service
public class SucursalServiceImpl implements SucursalService{
	 private  SucursalRepository sucursalRepository;
	    private  ClienteRepository clienteRepository;
	    private  NodoRepository nodoRepository;
		public SucursalServiceImpl(SucursalRepository sucursalRepository, 
				ClienteRepository clienteRepository,
				NodoRepository nodoRepository) {
			super();
			this.sucursalRepository = sucursalRepository;
			this.clienteRepository = clienteRepository;
			this.nodoRepository = nodoRepository;
		}
	    
		// Listar todas las sucursales
	    @Transactional(readOnly = true)
	    public List<Sucursal> listarTodas() {
	        return sucursalRepository.findAll();
	    }

	    // Buscar una sucursal por ID
	    @Transactional(readOnly = true)
	    public Sucursal buscarPorId(Long id) {
	        return sucursalRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException(
	                        "No se encontró la sucursal con ID: " + id));
	    }

	    // Listar sucursales de un cliente
	    @Transactional(readOnly = true)
	    public List<Sucursal> listarPorCliente(Long clienteId) {

	        if (!clienteRepository.existsById(clienteId)) {
	            throw new RuntimeException(
	                    "No existe el cliente con ID: " + clienteId);
	        }

	        return sucursalRepository.findByClienteId(clienteId);
	    }

	    // Registrar una nueva sucursal
	    public Sucursal guardar(
	            Sucursal sucursal, Long clienteId, Long nodoId) {

	        validarSucursal(sucursal);

	        Cliente cliente = clienteRepository.findById(clienteId)
	                .orElseThrow(() -> new RuntimeException(
	                        "No existe el cliente con ID: " + clienteId));

	        Nodo nodo = nodoRepository.findById(nodoId)
	                .orElseThrow(() -> new RuntimeException(
	                        "No existe el nodo con ID: " + nodoId));

	        if (sucursalRepository.existsByNodoId(nodoId)) {
	            throw new RuntimeException(
	                    "El nodo seleccionado ya está asociado a otra sucursal.");
	        }

	        sucursal.setCliente(cliente);
	        sucursal.setNodo(nodo);

	        return sucursalRepository.save(sucursal);
	    }

	    // Actualizar una sucursal existente
	    public Sucursal actualizar(
	            Long id, Sucursal datosActualizados,
	            Long clienteId, Long nodoId) {

	        validarSucursal(datosActualizados);

	        Sucursal sucursal = buscarPorId(id);

	        Cliente cliente = clienteRepository.findById(clienteId)
	                .orElseThrow(() -> new RuntimeException(
	                        "No existe el cliente con ID: " + clienteId));

	        Nodo nodo = nodoRepository.findById(nodoId)
	                .orElseThrow(() -> new RuntimeException(
	                        "No existe el nodo con ID: " + nodoId));

	        if (sucursalRepository.existsByNodoIdAndIdNot(nodoId, id)) {
	        	  throw new RuntimeException(
	                      "El nodo seleccionado ya está asociado a otra sucursal.");
	        }

	        sucursal.setNombre(datosActualizados.getNombre());
	        sucursal.setCliente(cliente);
	        sucursal.setNodo(nodo);

	        return sucursalRepository.save(sucursal);
	    }

	    // Eliminar una sucursal
	    public void eliminar(Long id) {
	        Sucursal sucursal = buscarPorId(id);
	        sucursalRepository.delete(sucursal);
	    }

	    // Validar los datos de la sucursal
	    private void validarSucursal(Sucursal sucursal) {

	        if (sucursal == null) {
	            throw new IllegalArgumentException(
	                    "Los datos de la sucursal son obligatorios.");
	        }

	        if (sucursal.getNombre() == null
	                || sucursal.getNombre().isBlank()) {
	            throw new IllegalArgumentException(
	                    "El nombre de la sucursal es obligatorio.");
	        }

	        sucursal.setNombre(sucursal.getNombre().trim());
	    }

	    
	    
}
