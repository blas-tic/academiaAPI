package com.example.academia.dto;

import java.time.LocalDateTime;

public class MatriculaResponseDTO {
    private Long alumnoId;
    private String alumnoNombre;
    private Long asignaturaId;
    private String asignaturaNombre;
    private LocalDateTime fechaMatricula;
    private String estado;

    
    public Long getAlumnoId() {
       return alumnoId;
    }
    public void setAlumnoId(Long alumnoId) {
       this.alumnoId = alumnoId;
    }
    public String getAlumnoNombre() {
       return alumnoNombre;
    }
    public void setAlumnoNombre(String alumnoNombre) {
       this.alumnoNombre = alumnoNombre;
    }
    public Long getAsignaturaId() {
       return asignaturaId;
    }
    public void setAsignaturaId(Long asignaturaId) {
       this.asignaturaId = asignaturaId;
    }
    public String getAsignaturaNombre() {
       return asignaturaNombre;
    }
    public void setAsignaturaNombre(String asignaturaNombre) {
       this.asignaturaNombre = asignaturaNombre;
    }
    public LocalDateTime getFechaMatricula() {
       return fechaMatricula;
    }
    public void setFechaMatricula(LocalDateTime fechaMatricula) {
       this.fechaMatricula = fechaMatricula;
    }
    public String getEstado() {
       return estado;
    }
    public void setEstado(String estado) {
       this.estado = estado;
    }

    
}
