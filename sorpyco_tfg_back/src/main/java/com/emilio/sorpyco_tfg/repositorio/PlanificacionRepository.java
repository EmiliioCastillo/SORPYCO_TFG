package com.emilio.sorpyco_tfg.repositorio;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emilio.sorpyco_tfg.entidades.Planificacion;
import com.emilio.sorpyco_tfg.enums.EstadoPlanificacion;

public interface PlanificacionRepository extends JpaRepository<Planificacion, Long>{
	
	/* findByFecha: Buscar planificaciones por fecha, es decir a un dia determinado
	 * 	findByEstado. Buscamos el estado de esa planificacion, segun sea su situacion
	 *  el metodo que combina los 2, fechaAndEstado nos va a permitir recuperar las planificaciones
	 *  confirmadas para esa fecha.
	 * 
	 * */
	
	 List<Planificacion> findByFecha(LocalDate fecha);

	    List<Planificacion> findByEstado(EstadoPlanificacion estado);

	    List<Planificacion> findByFechaAndEstado(
	        LocalDate fecha,
	        EstadoPlanificacion estado
	        );
}
