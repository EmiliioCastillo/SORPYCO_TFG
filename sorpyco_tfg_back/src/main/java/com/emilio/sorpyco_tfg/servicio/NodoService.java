package com.emilio.sorpyco_tfg.servicio;

import java.util.List;

import com.emilio.sorpyco_tfg.entidades.Nodo;
import com.emilio.sorpyco_tfg.enums.TipoNodo;


public interface NodoService {
	List<Nodo> listarTodos();
	Nodo buscarPorId(Long id);
	
	 List<Nodo> listarPorTipo(TipoNodo tipoNodo);
	 Nodo guardar(Nodo nodo);
	 void eliminar(Long id);
	 Nodo actualizar(Long id, Nodo datosActualizados);
	 List<Nodo> listarDepositos();
	 List<Nodo> listarPuntosEntrega();
}
