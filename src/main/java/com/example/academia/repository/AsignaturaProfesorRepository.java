package com.example.academia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.academia.model.AsignaturaProfesor;

public interface AsignaturaProfesorRepository extends JpaRepository<AsignaturaProfesor, Long> {
    List<AsignaturaProfesor> findByProfesorId(Long profesorId);
    List<AsignaturaProfesor> findByAsignaturaId(Long asignaturaId);
    Optional<AsignaturaProfesor> findByProfesorIdAndAsignaturaId(Long profesorId, Long asignaturaId);
    boolean existsByProfesorIdAndAsignaturaId(Long profesorId, Long asignaturaId);
    void deleteByProfesorIdAndAsignaturaId(Long profesorId, Long asignaturaId);
}
