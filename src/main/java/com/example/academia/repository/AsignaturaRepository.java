package com.example.academia.repository;

import com.example.academia.model.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {
    // Podríamos añadir método para verificar si existe por nombre (opcional)
    boolean existsByNombre(String nombre);
}