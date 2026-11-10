
package com.emilio.sorpyco_tfg.controladores;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.emilio.sorpyco_tfg.dto.SucursalUbicacionDTO;
import com.emilio.sorpyco_tfg.entidades.Sucursal;
import com.emilio.sorpyco_tfg.servicio.SucursalService;

@RestController
@RequestMapping("/api/sucursales")
public class SucursalController {

    private final SucursalService sucursalService;

    public SucursalController(SucursalService sucursalService) {
        this.sucursalService = sucursalService;
    }

    @GetMapping("/listar")
    public ResponseEntity<List<SucursalUbicacionDTO>> listarTodas() {

        List<SucursalUbicacionDTO> respuesta =
                sucursalService.listarTodas()
                        .stream()
                        .map(this::convertirADTO)
                        .toList();

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SucursalUbicacionDTO> buscarPorId(
            @PathVariable Long id) {

        Sucursal sucursal = sucursalService.buscarPorId(id);

        return ResponseEntity.ok(convertirADTO(sucursal));
    }

    
    private SucursalUbicacionDTO convertirADTO(Sucursal sucursal) {

        var nodo = sucursal.getNodo();

        return new SucursalUbicacionDTO(
                sucursal.getId(),
                sucursal.getNombre(),
                nodo.getDireccion(),
                nodo.getLatitud(),
                nodo.getLongitud()
        );
    }

}
