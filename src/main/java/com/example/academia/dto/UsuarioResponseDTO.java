package com.example.academia.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class UsuarioResponseDTO {
    private Long id;
    private String email;
    private String nombre;
    private String telefono;
    private String direccion;
    private LocalDateTime fechaAlta;
    private boolean activo;
    private Set<String> roles;  // Solo los nombres de los roles

    // Constructor, getters y setters

    public UsuarioResponseDTO() {}



    public Long getId() {
       return id;
    }

    public void setId(Long id) {
       this.id = id;
    }

    public String getEmail() {
       return email;
    }

    public void setEmail(String email) {
       this.email = email;
    }

    public String getNombre() {
       return nombre;
    }

    public void setNombre(String nombre) {
       this.nombre = nombre;
    }

    public String getTelefono() {
       return telefono;
    }

    public void setTelefono(String telefono) {
       this.telefono = telefono;
    }

    public String getDireccion() {
       return direccion;
    }

    public void setDireccion(String direccion) {
       this.direccion = direccion;
    }

    public LocalDateTime getFechaAlta() {
       return fechaAlta;
    }

    public void setFechaAlta(LocalDateTime fechaAlta) {
       this.fechaAlta = fechaAlta;
    }

    public boolean isActivo() {
       return activo;
    }

    public void setActivo(boolean activo) {
       this.activo = activo;
    }

    public Set<String> getRoles() {
       return roles;
    }

    public void setRoles(Set<String> roles) {
       this.roles = roles;
    }

    // Getters y setters...
    
}
