package com.gamercomunity.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 300)
    private String direccion;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    @Column(length = 300)
    private String organizadores;

    @Column(name = "expositores_principales", length = 500)
    private String expositoresPrincipales;

    @Column(length = 100)
    private String categoria;

    @Column(name = "tiene_estacionamiento", nullable = false)
    private boolean tieneEstacionamiento = false;

    @Column(name = "tiene_locomocion", nullable = false)
    private boolean tieneLocomocion = false;

    @Column(name = "tiene_comidas", nullable = false)
    private boolean tieneComidas = false;

    @Column(name = "tiene_alojamiento", nullable = false)
    private boolean tieneAlojamiento = false;

    public Evento() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getOrganizadores() { return organizadores; }
    public void setOrganizadores(String organizadores) { this.organizadores = organizadores; }

    public String getExpositoresPrincipales() { return expositoresPrincipales; }
    public void setExpositoresPrincipales(String expositoresPrincipales) { this.expositoresPrincipales = expositoresPrincipales; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public boolean isTieneEstacionamiento() { return tieneEstacionamiento; }
    public void setTieneEstacionamiento(boolean tieneEstacionamiento) { this.tieneEstacionamiento = tieneEstacionamiento; }

    public boolean isTieneLocomocion() { return tieneLocomocion; }
    public void setTieneLocomocion(boolean tieneLocomocion) { this.tieneLocomocion = tieneLocomocion; }

    public boolean isTieneComidas() { return tieneComidas; }
    public void setTieneComidas(boolean tieneComidas) { this.tieneComidas = tieneComidas; }

    public boolean isTieneAlojamiento() { return tieneAlojamiento; }
    public void setTieneAlojamiento(boolean tieneAlojamiento) { this.tieneAlojamiento = tieneAlojamiento; }
}