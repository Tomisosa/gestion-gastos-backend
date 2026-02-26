package com.example.gestiongastos.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "gastos")
public class Gasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;     // Concepto (Ej: Movistar, Netflix)
    private Double monto;
    private LocalDate fecha;        // Fecha de Pago Real

    @Column(name = "medio_pago")
    private String medioPago; 
    
    @Column(name = "es_fijo")
    private Boolean esFijo; 

    // --- NUEVOS CAMPOS ---
    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento; 

    @Column(name = "pagado")
    private Boolean pagado; // Si/No
    // ---------------------

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    // --- GETTERS Y SETTERS ---
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
    public Boolean getEsFijo() { return esFijo; }
    public void setEsFijo(Boolean esFijo) { this.esFijo = esFijo; }
    
    // Nuevos
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public Boolean getPagado() { return pagado; }
    public void setPagado(Boolean pagado) { this.pagado = pagado; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}