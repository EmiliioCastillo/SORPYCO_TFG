package com.emilio.sorpyco_tfg.servicio;

import java.util.List;

import com.emilio.sorpyco_tfg.entidades.Sucursal;

public interface SucursalService {
	List<Sucursal> listarTodas();
	Sucursal buscarPorId(Long id);
	 List<Sucursal> listarPorCliente(Long clienteId);
	 
	 Sucursal guardar(Sucursal sucursal, Long clienteId, Long nodoId);
	 
	 Sucursal actualizar(
	            Long id, Sucursal datosActualizados,
	            Long clienteId, Long nodoId);
	 void eliminar(Long id);
	 
}
