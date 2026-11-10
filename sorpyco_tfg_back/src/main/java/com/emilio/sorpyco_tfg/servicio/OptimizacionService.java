package com.emilio.sorpyco_tfg.servicio;

import java.util.List;

import com.emilio.sorpyco_tfg.dto.MatrizCostosDTO;
import com.emilio.sorpyco_tfg.entidades.Alternativa;
import com.emilio.sorpyco_tfg.entidades.Pedido;
import com.emilio.sorpyco_tfg.entidades.Planificacion;
import com.emilio.sorpyco_tfg.entidades.Vehiculo;

public interface OptimizacionService {
	   List<Alternativa> generarAlternativas(
		        Planificacion planificacion,
		        List<Pedido> pedidos,
		        List<Vehiculo> vehiculos,
		        MatrizCostosDTO matrizCostos
		    );
}
