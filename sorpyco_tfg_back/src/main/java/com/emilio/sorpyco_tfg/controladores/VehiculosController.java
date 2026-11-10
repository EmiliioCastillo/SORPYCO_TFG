package com.emilio.sorpyco_tfg.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.emilio.sorpyco_tfg.dto.VehiculoDTO;
import com.emilio.sorpyco_tfg.enums.TipoVehiculo;
import com.emilio.sorpyco_tfg.servicio.VehiculoService;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculosController {

	@Autowired
    private VehiculoService vehiculoService;


    // Registrar vehículo
    @PostMapping("/guardar")
    public ResponseEntity<?> registrar(
            @RequestBody VehiculoDTO vehiculo) {

        vehiculoService.registrar(vehiculo);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Listar todos los vehículos
    @GetMapping
    public ResponseEntity<List<VehiculoDTO>> listarTodos() {

        return ResponseEntity.ok(
            vehiculoService.listarTodos());
    }

    // Buscar vehículo por ID
    @GetMapping("/{id}")
    public ResponseEntity<VehiculoDTO> buscarPorId(
            @PathVariable Long id) {

        try {
            return ResponseEntity.ok(
                vehiculoService.buscarPorId(id));

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Listar vehículos disponibles
    @GetMapping("/disponibles")
    public ResponseEntity<List<VehiculoDTO>> listarDisponibles() {

        return ResponseEntity.ok(
            vehiculoService.listarDisponibles());
    }

    // Filtrar vehículos por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<VehiculoDTO>> listarPorTipo(
            @PathVariable TipoVehiculo tipo) {

        return ResponseEntity.ok(
            vehiculoService.listarPorTipo(tipo));
    }

    // Listar disponibles por tipo
    @GetMapping("/disponibles/tipo/{tipo}")
    public ResponseEntity<List<VehiculoDTO>> listarDisponiblesPorTipo(
            @PathVariable TipoVehiculo tipo) {

        return ResponseEntity.ok(
            vehiculoService.listarDisponiblesPorTipo(tipo));
    }

    // Actualizar vehículo
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestBody VehiculoDTO vehiculo) {

        vehiculoService.actualizar(id, vehiculo);

        return ResponseEntity.noContent().build();
    }

    // Cambiar disponibilidad en caso de que queden inutilizables
    //No vamos a eliminarlos porque es probable que se de el caso de que quede en mantenimiento
    //o pase algo entonces queda disponibilidad = false
    @PatchMapping("/{id}/disponibilidad")
    public ResponseEntity<?> cambiarDisponibilidad(
            @PathVariable Long id,
            @RequestParam boolean disponibilidad) {

        vehiculoService.cambiarDisponibilidad(
            id, disponibilidad);

        return ResponseEntity.noContent().build();
    }

    // Eliminar vehículo
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @PathVariable Long id) {

        vehiculoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
