package com.emilio.sorpyco_tfg.servicio;

import java.util.List;

import org.springframework.stereotype.Service;

import com.emilio.sorpyco_tfg.dto.RuteoDTO;
import com.emilio.sorpyco_tfg.entidades.Nodo;

@Service
public interface RutaService {
	RuteoDTO calcularMatriz(List<Nodo> nodos);
}
