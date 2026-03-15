package com.example.academia.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.academia.model.Profesor;

public interface ProfesorRepository extends JpaRepository<Profesor, Long> {

    @Query("SELECT p FROM Profesor p WHERE p.persona.email = :email")
    Optional<Profesor> findByPersonaEmail(@Param("email") String email);
    
    Optional<Profesor> findByNumEmpleado(String numEmpleado); // este se mantiene
}
