
package com.emilio.sorpyco_tfg.controladores;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.emilio.sorpyco_tfg.dto.PedidoDTO;
import com.emilio.sorpyco_tfg.entidades.Pedido;
import com.emilio.sorpyco_tfg.servicio.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
	
	
	@Autowired
    private PedidoService pedidoService;

   
    // Registrar un pedido
    @PostMapping("/guardar")
    public ResponseEntity<String> registrar(
            @RequestBody Pedido pedido,
            @RequestParam Long sucursalId) {
        try {
        	pedidoService.registrar(pedido, sucursalId);
        	return ResponseEntity.ok("{\"response\": \"200\"}");
        }catch(HibernateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar registro");
        }
        
        
    }

    // Listar todos los pedidos
    @GetMapping("/listar")
    public ResponseEntity<List<PedidoDTO>> listarTodos() {

        List<PedidoDTO> pedidos =
            pedidoService.listarTodos()
                .stream()
                .map(PedidoDTO::desdeEntidad)
                .toList();

        return ResponseEntity.ok(pedidos);
    }

    // Buscar pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> buscarPorId(
            @PathVariable Long id) {

        Pedido pedido = pedidoService.buscarPorId(id);

        return ResponseEntity.ok(
            PedidoDTO.desdeEntidad(pedido));
    }

    // Listar pedidos por fecha
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<PedidoDTO>> listarPorFecha(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha) {

        List<PedidoDTO> pedidos =
            pedidoService.listarPorFecha(fecha)
                .stream()
                .map(PedidoDTO::desdeEntidad)
                .toList();

        return ResponseEntity.ok(pedidos);
    }

    // Listar pedidos pendientes por fecha
    @GetMapping("/pendientes/{fecha}")
    public ResponseEntity<List<PedidoDTO>> listarPendientes(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha) {

        List<PedidoDTO> pedidos =
            pedidoService.listarPendientesPorFecha(fecha)
                .stream()
                .map(PedidoDTO::desdeEntidad)
                .toList();

        return ResponseEntity.ok(pedidos);
    }

    // Actualizar un pedido
    @PutMapping("/{id}")
    public ResponseEntity<PedidoDTO> actualizar(
            @PathVariable Long id,
            @RequestParam Long sucursalId,
            @RequestBody Pedido pedido) {

        Pedido pedidoActualizado =
            pedidoService.actualizar(id, pedido, sucursalId);

        return ResponseEntity.ok(
            PedidoDTO.desdeEntidad(pedidoActualizado));
    }

    // Eliminar un pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        pedidoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
