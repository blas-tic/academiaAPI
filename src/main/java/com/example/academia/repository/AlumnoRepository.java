package com.example.academia.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.academia.model.Alumno;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

   @Query("SELECT a FROM Alumno a WHERE a.persona.email = :email")
   Optional<Alumno> findByPersonaEmail(@Param("email") String email);

}
