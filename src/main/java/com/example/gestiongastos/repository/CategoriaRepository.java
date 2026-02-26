package com.example.gestiongastos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Opcional pero buena práctica
import com.example.gestiongastos.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    // Esta línea es OBLIGATORIA para que funcione el 'if' de tu servicio
    boolean existsByNombreAndUsuarioId(String nombre, Long usuarioId);
}