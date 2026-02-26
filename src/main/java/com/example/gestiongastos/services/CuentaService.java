package com.example.gestiongastos.services;

import com.example.gestiongastos.model.Cuenta;
import java.util.List;

public interface CuentaService {
    List<Cuenta> obtenerCuentas(Long usuarioId);
    Cuenta crearCuenta(Cuenta cuenta);
}
