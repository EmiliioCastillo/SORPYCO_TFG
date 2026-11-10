package com.emilio.sorpyco_tfg.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.emilio.sorpyco_tfg.entidades.Sucursal;


public interface SucursalRepository extends JpaRepository<Sucursal, Long>{
	/*
	 * El metodo FindByClienteId permite consultar las sucursales asociadas a un cliente determinado.
	 * el metodo existsByNodoId para validar un nodo por su ID
	 *El segundo método sirve para actualizar una sucursal sin
	 * que el sistema interprete que su propio nodo ya está ocupado.
	 * */
	
	 List<Sucursal> findByClienteId(Long clienteId);
	 boolean existsByNodoId(Long nodoId);

	 @Query("SELECT COUNT(s) > 0 FROM Sucursal s WHERE s.nodo.id = :nodoId AND s.id <> :sucursalId")
	    boolean existsByNodoIdAndIdNot(@Param("nodoId") Long nodoId, @Param("sucursalId") Long sucursalId);
}
