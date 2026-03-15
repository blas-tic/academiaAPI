package com.example.academia.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

public class PerfilUpdateDTO {
    @Size(max = 255)
    private String nombre;

    private String telefono;
    private String direccion;
    private LocalDate fechaNacimiento;

    // Campos de alumno
    private String tutor;

    // Campos de profesor
    private String numEmpleado;
    private String departamento;
    private String categoria;

    
    public PerfilUpdateDTO() {
    }


    public PerfilUpdateDTO(@Size(max = 255) String nombre, String telefono, String direccion, LocalDate fechaNacimiento,
         String tutor, String numEmpleado, String departamento, String categoria) {
      this.nombre = nombre;
      this.telefono = telefono;
      this.direccion = direccion;
      this.fechaNacimiento = fechaNacimiento;
      this.tutor = tutor;
      this.numEmpleado = numEmpleado;
      this.departamento = departamento;
      this.categoria = categoria;
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
