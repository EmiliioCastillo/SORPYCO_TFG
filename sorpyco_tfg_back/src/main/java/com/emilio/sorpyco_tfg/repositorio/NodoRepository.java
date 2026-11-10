package com.emilio.sorpyco_tfg.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emilio.sorpyco_tfg.entidades.Nodo;
import com.emilio.sorpyco_tfg.enums.TipoNodo;

public interface NodoRepository extends JpaRepository<Nodo, Long>{
	
	/*
	 * el metodo findByTipoNodo nos permite obtener los nodos según su clasificación.
	 * Por ejemplo si hacemos.
	 * List<Nodo> depositos =
    nodoRepository.findByTipoNodo(TipoNodo.DEPOSITO);
    esto nos devuelve todos los nodos cuyo tipo sea DEPOSITO
    o enves de tipoNodo.DEPOSITO hacemos punto_entrega nos devuelve los nodos cuyo tipo sea puntos de entrega
    
	 * */
	
	  List<Nodo> findByTipoNodo(TipoNodo tipoNodo);
	  
}
