package com.emilio.sorpyco_tfg.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emilio.sorpyco_tfg.entidades.Vehiculo;
import com.emilio.sorpyco_tfg.enums.TipoVehiculo;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long>{
	/*FindByPatente: Buscamos un vehiculo por su patente.
	 * FindByDisponibilidadTrue: Nos permite buscar los vehiculos que esten disponibles 
	 * devolviendo true o false.
	 * FindByTipoVehiculo: Filtramos vehiculos por tipo, segun sea combi o camion.
	 * findByDisponibilidadTrueAndTipoVehiculo: Nos permite obtener vehiculos disponibles
	 *  de un tipo especifico.
	 * 
	 * 
	 * 
	 * */
	
	Optional<Vehiculo> findByPatente(String patente);

    List<Vehiculo> findByDisponibilidadTrue();

    List<Vehiculo> findByTipoVehiculo(TipoVehiculo tipoVehiculo);

    List<Vehiculo> findByDisponibilidadTrueAndTipoVehiculo(
        TipoVehiculo tipoVehiculo
    );
}
