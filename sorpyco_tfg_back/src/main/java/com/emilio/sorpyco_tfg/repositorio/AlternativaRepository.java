package com.emilio.sorpyco_tfg.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emilio.sorpyco_tfg.entidades.Alternativa;
import com.emilio.sorpyco_tfg.enums.Algoritmo;

public interface AlternativaRepository extends JpaRepository<Alternativa, Long>{
	/*findByPlanificacionId: Obtiene todas las alternativas de una planificación.
	 * findByPlanificacionIdOrderByDistanciaTotalAsc: Obtiene las alternativas ordenadas de menor
	 * a mayor distancia.
	 * findByAlgoritmo: Filtra las alternativas según el algoritmo utilizado.
	 * findByPlanificacionIdAndSeleccionadaTrue: Busca la alternativa seleccionada de una planificación.
	 * findByPlanificacionIdAndRecomendadaTrue: Busca la alternativa recomendada de una planificación.
	 * 
	 * Aclaremos algo: Una re-planificación se almacena como una nueva alternativa vinculada
	 *  a la original (heredando un identificador de origen alternativa_origen_id) eso significa que 
	 * la acción forma una nueva alternativa en el historial de esa misma planificación diaria.
	 * Entonces:
	 * findByAlternativaOrigenId: Recupera las alternativas que derivan de una alternativa determinada.
	 * findByPlanificacionIdAndAlternativaOrigenIsNotNull: Recupera las alternativas de una planificación que 
	 * fueron generadas mediante replanificación.
	 * 
	 * */
	
    List<Alternativa> findByPlanificacionId(Long planificacionId);

    List<Alternativa> findByPlanificacionIdOrderByDistanciaTotalAsc(
        Long planificacionId
    );

    List<Alternativa> findByAlgoritmo(Algoritmo algoritmo);

    Optional<Alternativa> findByPlanificacionIdAndSeleccionadaTrue(
        Long planificacionId
    );

    Optional<Alternativa> findByPlanificacionIdAndRecomendadaTrue(
        Long planificacionId
    );

    List<Alternativa> findByAlternativaOrigenId(Long alternativaOrigenId);

    List<Alternativa> findByPlanificacionIdAndAlternativaOrigenIsNotNull(
        Long planificacionId
    );

	    
}
