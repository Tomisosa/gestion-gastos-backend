package com.example.gestiongastos.repository;

import com.example.gestiongastos.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    List<Cuenta> findByUsuarioId(Long usuarioId);
}
