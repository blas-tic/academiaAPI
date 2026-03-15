package com.example.academia.config;

import com.example.academia.model.Rol;
import com.example.academia.model.RolNombre;
import com.example.academia.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initRoles(RolRepository rolRepository) {
        return args -> {
            for (RolNombre rolNombre : RolNombre.values()) {
                if (!rolRepository.findByNombre(rolNombre).isPresent()) {
                    Rol rol = new Rol();
                    rol.setNombre(rolNombre);
                    rol.setDescripcion(rolNombre.name());
                    rolRepository.save(rol);
                }
            }
        };
    }
}