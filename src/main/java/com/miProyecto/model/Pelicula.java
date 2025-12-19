package com.miProyecto.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "peliculas")
public class Pelicula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String director;
    private Integer anio;
    // Mapeo para la columna precioCompra
    @Column(name = "precioCompra", precision = 10, scale = 2)
    private BigDecimal precioCompra;

    // Mapeo para la columna precioAlquiler
    @Column(name = "precioAlquiler", precision = 10, scale = 2)
    private BigDecimal precioAlquiler;
    private Integer totalInventario;
    private Integer unidadesVendidas = 0;

    public Pelicula() {

    }

    public Pelicula(Long id, String titulo, String director, Integer anio, BigDecimal precioCompra,
            BigDecimal precioAlquiler, Integer totalInventario, Integer unidadesVendidas) {
        this.id = id;
        this.titulo = titulo;
        this.director = director;
        this.anio = anio;
        this.precioCompra = precioCompra;
        this.precioAlquiler = precioAlquiler;
        this.totalInventario = totalInventario;
        this.unidadesVendidas = unidadesVendidas;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDirector() {
        return this.director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Integer getAnio() {
        return this.anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public BigDecimal getPrecioCompra() {
        return this.precioCompra;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    public BigDecimal getPrecioAlquiler() {
        return this.precioAlquiler;
    }

    public void setPrecioAlquiler(BigDecimal precioAlquiler) {
        this.precioAlquiler = precioAlquiler;
    }

    public Integer getTotalInventario() {
        return totalInventario;
    }

    public void setTotalInventario(Integer totalInventario) {
        this.totalInventario = totalInventario;
    }

    public Integer getUnidadesVendidas() {
        return unidadesVendidas;
    }

    public void setUnidadesVendidas(Integer unidadesVendidas) {
        this.unidadesVendidas = unidadesVendidas;
    }

}
