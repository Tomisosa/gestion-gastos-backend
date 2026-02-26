package com.example.gestiongastos.service.imp;

import com.example.gestiongastos.model.Cuenta;
import com.example.gestiongastos.repository.CuentaRepository;
import com.example.gestiongastos.services.CuentaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuentaServiceImp implements CuentaService {

    private final CuentaRepository cuentaRepository;

    public CuentaServiceImp(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public List<Cuenta> obtenerCuentas(Long usuarioId) {
        return cuentaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public Cuenta crearCuenta(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }
}
