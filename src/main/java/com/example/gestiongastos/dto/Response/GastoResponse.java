package com.example.gestiongastos.dto.Response;

import java.time.LocalDate;

public class GastoResponse {
    
    private Long id;
    private String descripcion;
    private Double monto;
    private LocalDate fecha;
    private String medioPago;
    private Long usuarioId;
    
    private Long categoriaId;
    private String categoriaNombre; 
    
    private Boolean esFijo;
    
    // --- NUEVOS CAMPOS ---
    private LocalDate fechaVencimiento;
    private Boolean pagado;

    // --- GETTERS Y SETTERS ---

    public Boolean getEsFijo() { return esFijo; }
    public void setEsFijo(Boolean esFijo) { this.esFijo = esFijo; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getMedioPago() { return medioPago; }
    public void setMedioPago(String medioPago) { this.medioPago = medioPago; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }

    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }

    // Nuevos
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public Boolean getPagado() { return pagado; }
    public void setPagado(Boolean pagado) { this.pagado = pagado; }
}