package com.emilio.sorpyco_tfg.repositorio;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emilio.sorpyco_tfg.entidades.Pedido;
import com.emilio.sorpyco_tfg.enums.EstadoPedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
	
	/* *findByFechaEntrega*: Permite obtener todos los pedidos cuya fecha de entrega coincida con la 
	 * indicada por el usuario en la interfaz.
	 *  *findByFechaEntregaAndEstado*: Permite no solo obtener la fecha de entrega sino
	 * tambien el estado, un metodo imprescindible para el modulo de planificacion.
	 * 
	 * 
	 * 
	 * */
	
	 List<Pedido> findByFechaEntrega(LocalDate fechaEntrega);

	    List<Pedido> findByFechaEntregaAndEstado(
	        LocalDate fechaEntrega,
	        EstadoPedido estado
	    );
}
