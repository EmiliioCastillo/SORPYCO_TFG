
package com.emilio.sorpyco_tfg.dto;

import java.util.List;
import com.emilio.sorpyco_tfg.entidades.Nodo;

public record MatrizCostosDTO(
        List<Nodo> nodos,
        double[][] distancias,
        double[][] duraciones
) {
}
