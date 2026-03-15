package com.example.academia.dto;

public class AsignaturaResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String color;

    // constructor, getters y setters
    public AsignaturaResponseDTO(Long id, String nombre, String descripcion, String color) {
      this.id = id;
      this.nombre = nombre;
      this.descripcion = descripcion;
      this.color = color;
    }

    public AsignaturaResponseDTO() {
      super();
    }

    public Long getId() {
       return id;
    }

    public void setId(Long id) {
       this.id = id;
    }

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