package com.emilio.sorpyco_tfg.servicio.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.emilio.sorpyco_tfg.entidades.Cliente;
import com.emilio.sorpyco_tfg.repositorio.ClienteRepository;
import com.emilio.sorpyco_tfg.servicio.ClienteService;

import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class ClienteServiceImpl implements ClienteService{
    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

	@Override
	 @Transactional(readOnly = true)
	public List<Cliente> listarTodos() {
		return clienteRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente buscarPorId(Long id) {
		 return clienteRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException(
	                        "No se encontró el cliente con ID: " + id));
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente guardarCliente(Cliente cliente) {
		   validarCliente(cliente);

	        if (clienteRepository.findByRazonSocialIgnoreCase(
	                cliente.getRazonSocial()).isPresent()) {
	            throw new RuntimeException(
	                    "Ya existe un cliente con esa razón social.");
	        }

	        return clienteRepository.save(cliente);
	}

	@Override
	public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
		   validarCliente(clienteActualizado);

	        Cliente cliente = buscarPorId(id);

	        clienteRepository.findByRazonSocialIgnoreCase(
	        		clienteActualizado.getRazonSocial())
	                .filter(existente -> !existente.getId().equals(id))
	                .ifPresent(existente -> {
	                    throw new RuntimeException(
	                            "Ya existe otro cliente con esa razón social.");
	                });

	        cliente.setRazonSocial(clienteActualizado.getRazonSocial());
	        cliente.setNombre(clienteActualizado.getNombre());

	        return clienteRepository.save(cliente);
	}

	@Override
	public void eliminarCliente(Long id) {
		// TODO Auto-generated method stub
		  Cliente cliente = buscarPorId(id);
	        clienteRepository.delete(cliente);
	}

	@Override
	public Cliente buscarPorRazonSocial(String razonSocial) {
		// TODO Auto-generated method stub
		  return clienteRepository.findByRazonSocialIgnoreCase(razonSocial)
	                .orElseThrow(() -> new RuntimeException(
	                        "No se encontró un cliente con esa razón social."));
	}

	@Override
	public void validarCliente(Cliente cliente) {
		// TODO Auto-generated method stub
		  if (cliente == null) {
	            throw new IllegalArgumentException(
	                    "Los datos del cliente son obligatorios.");
	        }

	        if (cliente.getRazonSocial() == null
	                || cliente.getRazonSocial().isBlank()) {
	            throw new IllegalArgumentException(
	                    "La razón social es obligatoria.");
	        }

	        cliente.setRazonSocial(cliente.getRazonSocial().trim());

	        if (cliente.getNombre() != null) {
	            cliente.setNombre(cliente.getNombre().trim());
	        }
	}

	
	


}
