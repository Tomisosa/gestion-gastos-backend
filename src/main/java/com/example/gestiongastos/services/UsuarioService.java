package com.example.gestiongastos.services;

import com.example.gestiongastos.dto.Request.UsuarioLoginRequest;
import com.example.gestiongastos.dto.Request.UsuarioRegisterRequestDto;
import com.example.gestiongastos.dto.Response.UsuarioResponse;
import com.example.gestiongastos.model.Usuario;

public interface UsuarioService {
    UsuarioResponse register(UsuarioRegisterRequestDto request);
    UsuarioResponse login(UsuarioLoginRequest request);
    UsuarioResponse findById(Long id);
    Usuario findByEmail(String email);
    
    // Agregamos esta línea para que el Controller la vea
    void updatePassword(Long userId, String newPassword); 
}
