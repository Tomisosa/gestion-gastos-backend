package com.example.gestiongastos.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.gestiongastos.model.Gasto;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Long> {
    
    // Este es el método que te daba error "undefined"
    List<Gasto> findByUsuarioId(Long usuarioId);
}