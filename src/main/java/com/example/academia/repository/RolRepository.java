package com.example.academia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.academia.model.Rol;
import com.example.academia.model.RolNombre;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByNombre(RolNombre nombre);
}
