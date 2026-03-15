package com.example.academia.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.academia.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

   Optional<Usuario> findByPersonaEmail(String email);

   @Query("SELECT DISTINCT u FROM Usuario u JOIN FETCH u.roles")
   List<Usuario> findAllWithRoles();

   @Query("SELECT u FROM Usuario u JOIN FETCH u.roles WHERE u.persona.email = :email")
   Optional<Usuario> findByPersonaEmailWithRoles(@Param("email") String email);

   Optional<Usuario> findByPersonaId(Long personaId);

}
