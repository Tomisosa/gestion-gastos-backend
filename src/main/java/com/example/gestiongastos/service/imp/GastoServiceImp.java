package com.example.gestiongastos.service.imp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.gestiongastos.dto.Request.GastoRequest;
import com.example.gestiongastos.dto.Response.GastoResponse;
import com.example.gestiongastos.model.Categoria;
import com.example.gestiongastos.model.Gasto;
import com.example.gestiongastos.model.Usuario;
import com.example.gestiongastos.repository.CategoriaRepository;
import com.example.gestiongastos.repository.GastoRepository;
import com.example.gestiongastos.repository.UsuarioRepository;
import com.example.gestiongastos.services.GastoService; 

@Service
public class GastoServiceImp implements GastoService {

    private final GastoRepository gastoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    public GastoServiceImp(GastoRepository gastoRepository, UsuarioRepository usuarioRepository, CategoriaRepository categoriaRepository) {
        this.gastoRepository = gastoRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public GastoResponse create(GastoRequest req) {
        Usuario usuario = usuarioRepository.findById(req.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Gasto gasto = new Gasto();
        gasto.setDescripcion(req.getDescripcion());
        gasto.setMonto(req.getMonto());
        gasto.setFecha(req.getFecha());
        gasto.setMedioPago(req.getMedioPago());
        
        // Fijo por defecto false
        gasto.setEsFijo(req.getEsFijo() != null ? req.getEsFijo() : false);
        
        // GUARDAMOS NUEVOS CAMPOS
        gasto.setFechaVencimiento(req.getFechaVencimiento());
        gasto.setPagado(req.getPagado() != null ? req.getPagado() : false);
        
        gasto.setUsuario(usuario);

        if (req.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(req.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            gasto.setCategoria(categoria);
        }

        Gasto guardado = gastoRepository.save(gasto);
        return mapToResponse(guardado);
    }

    @Override
    public List<GastoResponse> listByUsuario(Long usuarioId) {
        return gastoRepository.findByUsuarioId(usuarioId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GastoResponse getById(Long id) {
        Gasto gasto = gastoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gasto no encontrado"));
        return mapToResponse(gasto);
    }

    @Override
    public void delete(Long id) {
        if (!gastoRepository.existsById(id)) {
            throw new RuntimeException("Gasto no encontrado");
        }
        gastoRepository.deleteById(id);
    }

    @Override
    public GastoResponse update(Long id, GastoRequest req) {
        Gasto gasto = gastoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gasto no encontrado con id: " + id));

        gasto.setDescripcion(req.getDescripcion());
        gasto.setMonto(req.getMonto());
        gasto.setFecha(req.getFecha());
        gasto.setMedioPago(req.getMedioPago()); 
        
        if (req.getEsFijo() != null) gasto.setEsFijo(req.getEsFijo());
        
        // ACTUALIZAR NUEVOS CAMPOS
        if(req.getFechaVencimiento() != null) gasto.setFechaVencimiento(req.getFechaVencimiento());
        if(req.getPagado() != null) gasto.setPagado(req.getPagado());

        if (req.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(req.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            gasto.setCategoria(categoria);
        } else {
            gasto.setCategoria(null);
        }

        Gasto gastoActualizado = gastoRepository.save(gasto);
        return mapToResponse(gastoActualizado);
    }

    private GastoResponse mapToResponse(Gasto g) {
        GastoResponse res = new GastoResponse();
        res.setId(g.getId());
        res.setDescripcion(g.getDescripcion());
        res.setMonto(g.getMonto());
        res.setFecha(g.getFecha());
        res.setMedioPago(g.getMedioPago());
        res.setEsFijo(g.getEsFijo());
        
        // ENVIAR AL FRONT
        res.setFechaVencimiento(g.getFechaVencimiento());
        res.setPagado(g.getPagado());
        
        if(g.getUsuario() != null) res.setUsuarioId(g.getUsuario().getId());
        
        if (g.getCategoria() != null) {
            res.setCategoriaNombre(g.getCategoria().getNombre());
            res.setCategoriaId(g.getCategoria().getId());
        }
        return res;
    }
}