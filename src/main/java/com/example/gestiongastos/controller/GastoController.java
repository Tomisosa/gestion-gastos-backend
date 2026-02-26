package com.example.gestiongastos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Importa todo para ahorrar líneas

import com.example.gestiongastos.dto.Request.GastoRequest;
import com.example.gestiongastos.dto.Response.GastoResponse;
import com.example.gestiongastos.services.GastoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/gastos")
public class GastoController {
    
    private final GastoService gastoService;

    public GastoController(GastoService gastoService) {
        this.gastoService = gastoService;
    }

    @PostMapping
    public ResponseEntity<GastoResponse> create(@Valid @RequestBody GastoRequest req) {
        return ResponseEntity.ok(gastoService.create(req));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<GastoResponse>> listByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(gastoService.listByUsuario(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GastoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(gastoService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gastoService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    // --- ESTE ES EL NUEVO MÉTODO CORREGIDO (PUT) ---
    @PutMapping("/{id}")
    public ResponseEntity<GastoResponse> update(@PathVariable Long id, @Valid @RequestBody GastoRequest req) {
        // Ahora el controlador solo delega al servicio. ¡Limpio!
        return ResponseEntity.ok(gastoService.update(id, req));
    }
}