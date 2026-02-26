package com.example.gestiongastos.services;

import com.example.gestiongastos.dto.Request.UsuarioLoginRequest;
import com.example.gestiongastos.dto.Request.UsuarioRegisterRequestDto;
import com.example.gestiongastos.dto.Response.UsuarioResponse;
import com.example.gestiongastos.model.Usuario;

public interface UsuarioService {

	UsuarioResponse register(UsuarioRegisterRequestDto request);
    UsuarioResponse login(UsuarioLoginRequest request); // para MVP básico
    UsuarioResponse findById(Long id);
	Usuario findByEmail(String email);
}
public void updatePassword(Long userId, String newPassword) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Encriptamos la clave usando el mismo encoder que usás en el register
        usuario.setPassword(passwordEncoder.encode(newPassword));
        
        usuarioRepository.save(usuario);
    }
