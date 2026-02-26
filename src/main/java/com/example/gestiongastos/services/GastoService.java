package com.example.gestiongastos.services;

import java.util.List;
import com.example.gestiongastos.dto.Request.GastoRequest;
import com.example.gestiongastos.dto.Response.GastoResponse;

public interface GastoService {
    
    GastoResponse create(GastoRequest req);
    
    List<GastoResponse> listByUsuario(Long usuarioId);
    
    GastoResponse getById(Long id);
    
    void delete(Long id);

    // --- AGREGAMOS LA FIRMA DEL MÉTODO UPDATE ---
    GastoResponse update(Long id, GastoRequest req);
}