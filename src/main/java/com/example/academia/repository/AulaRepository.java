package com.example.academia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.academia.model.Aula;

public interface AulaRepository extends JpaRepository<Aula, Long> {
   boolean existsByNombre(String nombre);

}
