package com.emilio.sorpyco_tfg.dto;

import java.util.List;

import com.emilio.sorpyco_tfg.entidades.Nodo;

//nos ayudara a transportar los datos de la matriz de ruteo, este es != de Ruta
public record RuteoDTO(
		 List<Nodo> nodos,
        double[][] distancias, //para los km
        double[][] duraciones //para los segundos estimados del viaje
)
{
	public RuteoDTO(double[][] distancias, double[][] duraciones) {
        this( List.of(), distancias, duraciones); 
    }
	
}