package com.miProyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miProyecto.model.Pelicula;

public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {

}
