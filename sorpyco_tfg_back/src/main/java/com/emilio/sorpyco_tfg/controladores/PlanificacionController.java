package com.emilio.sorpyco_tfg.controladores;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.emilio.sorpyco_tfg.servicio.PlanificacionService;
import com.emilio.sorpyco_tfg.dto.PlanificacionRespuestaDTO;


import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.emilio.sorpyco_tfg.dto.PlanificacionRespuestaDTO;
import com.emilio.sorpyco_tfg.servicio.PlanificacionService;

@RestController
@RequestMapping("/api/planificaciones")
public class PlanificacionController {

	@Autowired
    private  PlanificacionService planificacionService;



    // Generar una planificación
	@PostMapping("/generar")
	public ResponseEntity<PlanificacionRespuestaDTO> generarPlanificacion(
	        @RequestParam LocalDate fecha) {

        PlanificacionRespuestaDTO respuesta =
            planificacionService.generarPlanificacion(fecha);

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    // Buscar una planificación por ID
    @GetMapping("/{id}")
    public ResponseEntity<PlanificacionRespuestaDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
            planificacionService.buscarPorId(id)
        );
    }

    // Listar planificaciones de una fecha
    @GetMapping("/listar/fecha/{fecha}")
    public ResponseEntity<List<PlanificacionRespuestaDTO>> listarPorFecha(
            @RequestParam LocalDate fecha) {

        return ResponseEntity.ok(
            planificacionService.listarPorFecha(fecha)
        );
    }
}

