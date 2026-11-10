package com.emilio.sorpyco_tfg.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emilio.sorpyco_tfg.entidades.Ruta;

public interface RutaRepository extends JpaRepository<Ruta, Long>{
	/*findByAlternativaId:Recuperamos todas las rutas pertenecientes a una alternativa.
	 * findByVehiculoId: Recupera las rutas asociadas a un vehículo.
	 * findByAlternativaIdAndVehiculoId: Busca las rutas de un vehículo dentro de 
	 * una alternativa determinada.
	 * 
	 * 
	 * */
	
	  List<Ruta> findByAlternativaId(Long alternativaId);

	    List<Ruta> findByVehiculoId(Long vehiculoId);

	    List<Ruta> findByAlternativaIdAndVehiculoId(
	        Long alternativaId,
	        Long vehiculoId
	    );
	
}
