package com.example.gestiongastos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categorias")
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    // --- ESTO ES LO QUE FALTABA (La Relación) ---
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // --- GETTERS Y SETTERS OBLIGATORIOS ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}