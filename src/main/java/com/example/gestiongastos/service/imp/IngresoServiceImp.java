package com.example.gestiongastos.service.imp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.gestiongastos.Mapper.AppMapper;
import com.example.gestiongastos.dto.Request.IngresoRequest;
import com.example.gestiongastos.dto.Response.IngresoResponse;
import com.example.gestiongastos.model.Categoria;
import com.example.gestiongastos.model.Ingreso;
import com.example.gestiongastos.model.Usuario;
import com.example.gestiongastos.repository.CategoriaRepository;
import com.example.gestiongastos.repository.IngresoRepository;
import com.example.gestiongastos.repository.UsuarioRepository;
import com.example.gestiongastos.services.IngresoService;

@Service
@Transactional
public class IngresoServiceImp implements IngresoService {

    private final IngresoRepository ingresoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    public IngresoServiceImp(IngresoRepository ingresoRepository,
                             UsuarioRepository usuarioRepository,
                             CategoriaRepository categoriaRepository) {
        this.ingresoRepository = ingresoRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public IngresoResponse create(IngresoRequest req) {
        Usuario u = usuarioRepository.findById(req.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        Categoria c = null;
        if (req.getCategoriaId() != null) {
            c = categoriaRepository.findById(req.getCategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
        }

        Ingreso i = new Ingreso(); // Ya no dará error gracias al constructor vacío
        i.setDescripcion(req.getDescripcion());
        
        // --- CORRECCIÓN IMPORTANTE AQUÍ: .doubleValue() ---
        if (req.getMonto() != null) {
            i.setMonto(req.getMonto().doubleValue());
        }
        
        i.setFecha(req.getFecha());
        i.setMedioPago(req.getMedioPago());
        i.setUsuario(u);
        i.setCategoria(c);
        
        Ingreso saved = ingresoRepository.save(i);
        return AppMapper.toIngresoResponse(saved);
    }

    @Override
    public List<IngresoResponse> listByUsuario(Long usuarioId) {
        Usuario u = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        return ingresoRepository.findByUsuarioOrderByFechaDesc(u).stream()
                .map(AppMapper::toIngresoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public IngresoResponse getById(Long id) {
        Ingreso ingreso = ingresoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ingreso no encontrado"));
        return AppMapper.toIngresoResponse(ingreso);
    }

    @Override
    public void delete(Long id) {
        if (!ingresoRepository.existsById(id)) {
            throw new IllegalArgumentException("Ingreso no encontrado");
        }
        ingresoRepository.deleteById(id);
    }

    @Override
    public IngresoResponse update(Long id, IngresoRequest req) {
        Ingreso ingreso = ingresoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingreso no encontrado con id: " + id));

        ingreso.setDescripcion(req.getDescripcion());
        
        // --- CORRECCIÓN IMPORTANTE AQUÍ TAMBIÉN: .doubleValue() ---
        if (req.getMonto() != null) {
            ingreso.setMonto(req.getMonto().doubleValue());
        }
        
        ingreso.setFecha(req.getFecha());
        ingreso.setMedioPago(req.getMedioPago());

        if (req.getCategoriaId() != null) {
            Categoria c = categoriaRepository.findById(req.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            ingreso.setCategoria(c);
        } else {
            ingreso.setCategoria(null);
        }

        Ingreso updated = ingresoRepository.save(ingreso);
        return AppMapper.toIngresoResponse(updated);
    }
}