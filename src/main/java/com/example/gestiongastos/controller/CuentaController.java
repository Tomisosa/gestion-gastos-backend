package com.example.gestiongastos.controller;

import com.example.gestiongastos.model.Cuenta;
import com.example.gestiongastos.services.CuentaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping("/{usuarioId}")
    public List<Cuenta> listarCuentas(@PathVariable Long usuarioId) {
        return cuentaService.obtenerCuentas(usuarioId);
    }

    @PostMapping
    public Cuenta crearCuenta(@RequestBody Cuenta cuenta) {
        return cuentaService.crearCuenta(cuenta);
    }
}
