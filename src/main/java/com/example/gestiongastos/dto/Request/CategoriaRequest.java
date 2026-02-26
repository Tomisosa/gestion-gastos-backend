package com.example.gestiongastos.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CategoriaRequest {

    @NotBlank
    private String nombre;

    @NotNull
    private Long usuarioId; // <--- IMPRESCINDIBLE

    // --- GETTERS Y SETTERS ---
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}