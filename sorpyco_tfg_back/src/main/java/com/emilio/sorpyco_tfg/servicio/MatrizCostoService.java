package com.emilio.sorpyco_tfg.servicio;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.emilio.sorpyco_tfg.dto.MatrizCostosDTO;
import com.emilio.sorpyco_tfg.dto.RuteoDTO;

@Service
public interface MatrizCostoService {

	
	 MatrizCostosDTO generarMatriz(LocalDate fechaEntrega);
}
