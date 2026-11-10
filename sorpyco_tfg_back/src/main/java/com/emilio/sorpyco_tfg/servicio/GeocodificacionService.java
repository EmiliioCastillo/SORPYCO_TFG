package com.emilio.sorpyco_tfg.servicio;

import com.emilio.sorpyco_tfg.dto.CoordenadasDTO;

public interface GeocodificacionService {

	//Metodo encargado de hacer la consulta y de convertir una dirección en coordenadas geográficas
	CoordenadasDTO geocodificarDireccion(String direccion);
}
