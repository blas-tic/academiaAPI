package com.example.academia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.academia.model.EstadoMatricula;
import com.example.academia.model.Matricula;
import com.example.academia.model.MatriculaId;

public interface MatriculaRepository extends JpaRepository<Matricula, MatriculaId> {

    List<Matricula> findByAlumnoId(Long alumnoId);

    List<Matricula> findByAsignaturaId(Long asignaturaId);

    Optional<Matricula> findByAlumnoIdAndAsignaturaId(Long alumnoId, Long asignaturaId);

    Optional<Matricula> findByAlumnoIdAndAsignaturaIdAndEstado(Long alumnoId, Long asignaturaId,
            EstadoMatricula estado);

    boolean existsByAlumnoIdAndAsignaturaIdAndEstado(Long alumnoId, Long asignaturaId, EstadoMatricula estado);
}
