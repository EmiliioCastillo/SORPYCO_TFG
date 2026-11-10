package com.emilio.sorpyco_tfg.servicio;

import java.util.List;

import org.springframework.stereotype.Service;

import com.emilio.sorpyco_tfg.entidades.Cliente;

@Service
public interface ClienteService {

	List<Cliente> listarTodos();
	Cliente buscarPorId(Long id);
	Cliente guardarCliente(Cliente cliente);
	Cliente actualizarCliente(Long id, Cliente clienteActualizado);
	void eliminarCliente(Long id);
	 Cliente buscarPorRazonSocial(String razonSocial);
	 void validarCliente(Cliente cliente);
	 
}
