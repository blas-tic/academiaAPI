package com.example.academia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AsignaturaRequestDTO {
    @NotBlank
    @Size(max = 255)
    private String nombre;

    private String descripcion;

    @Size(max = 7, message = "El color debe tener formato hexadecimal (ej. #FF0000)")
    private String color; // código hexadecimal, ej. #FF0000
    
    // getters y setters

    public String getNombre() {
       return nombre;
    }

    public void setNombre(String nombre) {
       this.nombre = nombre;
    }

    public String getDescripcion() {
       return descripcion;
    }

    public void setDescripcion(String descripcion) {
       this.descripcion = descripcion;
    }

    public String getColor() {
       return color;
    }

    public void setColor(String color) {
       this.color = color;
    }

}