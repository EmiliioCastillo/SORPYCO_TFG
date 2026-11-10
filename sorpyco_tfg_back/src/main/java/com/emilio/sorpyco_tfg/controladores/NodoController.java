
package com.emilio.sorpyco_tfg.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.emilio.sorpyco_tfg.entidades.Nodo;
import com.emilio.sorpyco_tfg.enums.TipoNodo;
import com.emilio.sorpyco_tfg.servicio.NodoService;

@RestController
@RequestMapping("/api/nodos")
public class NodoController {

	@Autowired
    private  NodoService nodoService;

  

    // Listar todos los nodos
    @GetMapping("/listar")
    public ResponseEntity<List<Nodo>> listarTodos() {
        return ResponseEntity.ok(nodoService.listarTodos());
    }

    // Buscar un nodo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Nodo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(nodoService.buscarPorId(id));
    }

    // Listar nodos por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Nodo>> listarPorTipo(
            @PathVariable TipoNodo tipo) {
        return ResponseEntity.ok(nodoService.listarPorTipo(tipo));
    }

    // Listar depósitos
    @GetMapping("/depositos")
    public ResponseEntity<List<Nodo>> listarDepositos() {
        return ResponseEntity.ok(nodoService.listarDepositos());
    }

    // Listar puntos de entrega
    @GetMapping("/puntos-entrega")
    public ResponseEntity<List<Nodo>> listarPuntosEntrega() {
        return ResponseEntity.ok(nodoService.listarPuntosEntrega());
    }

    // Registrar un nodo
    @PostMapping
    public ResponseEntity<Nodo> guardar(@RequestBody Nodo nodo) {
        Nodo nuevoNodo = nodoService.guardar(nodo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoNodo);
    }

    // Actualizar un nodo
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestBody Nodo nodo) {
        nodoService.actualizar(id, nodo);
        return ResponseEntity.noContent().build();
    }

    // Eliminar un nodo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        nodoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
