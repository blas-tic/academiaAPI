package com.example.academia.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class PerfilResponseDTO {
    private Long id;
    private String email;
    private String nombre;
    private String telefono;
    private String direccion;
    private LocalDate fechaNacimiento;
    private LocalDateTime fechaAlta;
    private Set<String> roles;
    // Campos específicos de alumno
    private String tutor;
    // Campos específicos de profesor
    private String numEmpleado;
    private String departamento;
    private String categoria;

    public PerfilResponseDTO() {
    }
    public PerfilResponseDTO(Long id, String email, String nombre, String telefono, String direccion,
         LocalDate fechaNacimiento, LocalDateTime fechaAlta, Set<String> roles, String tutor, String numEmpleado,
         String departamento, String categoria) {
      this.id = id;
      this.email = email;
      this.nombre = nombre;
      this.telefono = telefono;
      this.direccion = direccion;
      this.fechaNacimiento = fechaNacimiento;
      this.fechaAlta = fechaAlta;
      this.roles = roles;
      this.tutor = tutor;
      this.numEmpleado = numEmpleado;
      this.departamento = departamento;
      this.categoria = categoria;
    }

    
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
    public LocalDate getFechaNacimiento() {
       return fechaNacimiento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
       this.fechaNacimiento = fechaNacimiento;
    }
    public LocalDateTime getFechaAlta() {
       return fechaAlta;
    }
    public void setFechaAlta(LocalDateTime fechaAlta) {
       this.fechaAlta = fechaAlta;
    }
    public Set<String> getRoles() {
       return roles;
    }
    public void setRoles(Set<String> roles) {
       this.roles = roles;
    }
    public String getTutor() {
       return tutor;
    }
    public void setTutor(String tutor) {
       this.tutor = tutor;
    }
    public String getNumEmpleado() {
       return numEmpleado;
    }
    public void setNumEmpleado(String numEmpleado) {
       this.numEmpleado = numEmpleado;
    }
    public String getDepartamento() {
       return departamento;
    }
    public void setDepartamento(String departamento) {
       this.departamento = departamento;
    }
    public String getCategoria() {
       return categoria;
    }
    public void setCategoria(String categoria) {
       this.categoria = categoria;
    }

    

    
}
