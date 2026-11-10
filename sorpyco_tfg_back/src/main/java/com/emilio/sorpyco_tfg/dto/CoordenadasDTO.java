package com.emilio.sorpyco_tfg.dto;

import java.math.BigDecimal;

public class CoordenadasDTO {
	 private BigDecimal latitud;
      private BigDecimal longitud;
      
      
      
      
      
	public CoordenadasDTO(BigDecimal latitud, BigDecimal longitud) {
		super();
		this.latitud = latitud;
		this.longitud = longitud;
	}
	public BigDecimal getLatitud() {
		return latitud;
	}
	public void setLatitud(BigDecimal latitud) {
		this.latitud = latitud;
	}
	public BigDecimal getLongitud() {
		return longitud;
	}
	public void setLongitud(BigDecimal longitud) {
		this.longitud = longitud;
	}
      
      
      
      
}
