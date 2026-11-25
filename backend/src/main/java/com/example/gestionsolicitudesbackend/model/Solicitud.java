
package com.example.gestionsolicitudesbackend.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "solicitudes")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 2000)
    private String descripcion;

    @Column(nullable = false)
    private String categoria;

    private String servicioRelacionado;

    @Column(nullable = false)
    private String estado = "Recibida";

    @Column(nullable = false)
    private Long fechaCreacion;

    public Solicitud() {}

    @PrePersist
    public void prepararDatos() {
        if (codigo == null || codigo.isBlank()) {
            codigo = UUID.randomUUID().toString();
        }
        if (estado == null || estado.isBlank()) {
            estado = "Recibida";
        }
        if (fechaCreacion == null) {
            fechaCreacion = System.currentTimeMillis();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getServicioRelacionado() { return servicioRelacionado; }
    public void setServicioRelacionado(String servicioRelacionado) {
        this.servicioRelacionado = servicioRelacionado;
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Long getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Long fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
