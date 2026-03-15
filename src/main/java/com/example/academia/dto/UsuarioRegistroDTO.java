package com.example.academia.dto;

import com.example.academia.model.RolNombre;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

public class UsuarioRegistroDTO {
   @NotBlank
   @Email
   private String email;

   @NotBlank
   private String nombre;

   private String telefono;
   private String direccion;
   private LocalDate fechaNacimiento;

   @NotBlank
   private String password;

   @NotEmpty
   private Set<RolNombre> roles;

   // Campos específicos de alumno
   private String tutor;

   // Campos específicos de profesor
   private String numEmpleado;
   private String departamento;
   private String categoria;

   // getters y setters

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

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public Set<RolNombre> getRoles() {
      return roles;
   }

   public void setRoles(Set<RolNombre> roles) {
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