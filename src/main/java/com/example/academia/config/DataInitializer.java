package com.example.academia.config;

import com.example.academia.model.Persona;
import com.example.academia.model.Rol;
import com.example.academia.model.RolNombre;
import com.example.academia.model.Usuario;
import com.example.academia.repository.PersonaRepository;
import com.example.academia.repository.RolRepository;
import com.example.academia.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository,
            PersonaRepository personaRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            for (RolNombre rolNombre : RolNombre.values()) {
                if (!rolRepository.findByNombre(rolNombre).isPresent()) {
                    Rol rol = new Rol();
                    rol.setNombre(rolNombre);
                    rol.setDescripcion(rolNombre.name());
                    rolRepository.save(rol);
                }
            }

            // Crear admin si no existe
            String adminEmail = "admin@academia.com";
            if (usuarioRepository.findByPersonaEmail(adminEmail).isEmpty()) {
                Persona adminPersona = new Persona();
                adminPersona.setEmail(adminEmail);
                adminPersona.setNombre("Administrador");
                adminPersona.setFechaAlta(LocalDateTime.now());
                personaRepository.save(adminPersona);

                Usuario adminUsuario = new Usuario();
                adminUsuario.setPersona(adminPersona);
                adminUsuario.setPasswordHash(passwordEncoder.encode("admin123"));
                adminUsuario.setActivo(true);
                Rol adminRol = rolRepository.findByNombre(RolNombre.admin)
                        .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));
                adminUsuario.setRoles(Set.of(adminRol));
                usuarioRepository.save(adminUsuario);
                System.out.println("Usuario administrador creado por defecto: " + adminEmail);
            } else {
                System.out.println("El usuario administrador ya existe.");
            }
        };
    };
}
