package com.example.gestiongastos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Importamos todo para que funcione Delete

import com.example.gestiongastos.dto.Request.CategoriaRequest;
import com.example.gestiongastos.dto.Response.CategoriaResponse;
import com.example.gestiongastos.services.CategoriaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> create(@Valid @RequestBody CategoriaRequest req) {
        return ResponseEntity.ok(categoriaService.create(req));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listAll() {
        return ResponseEntity.ok(categoriaService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.getById(id));
    }
    
    // --- ESTA ES LA PARTE NUEVA PARA QUE EL BOTÓN FUNCIONE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
