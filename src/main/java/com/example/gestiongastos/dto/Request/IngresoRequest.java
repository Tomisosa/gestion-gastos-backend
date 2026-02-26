package com.example.gestiongastos.dto.Request;

import java.math.BigDecimal;
import java.time.LocalDate;

public class IngresoRequest {
    private String descripcion;
    private BigDecimal monto;
    private LocalDate fecha;
    private Long usuarioId;
    private Long categoriaId;
    
    // AGREGA ESTO:
    private String medioPago;

    // Getters y Setters
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
    
    // Y ESTOS DOS:
    public String getMedioPago() { return medioPago; }
    public void setMedioPago(String medioPago) { this.medioPago = medioPago; }
}