package com.example.academia.dto;

import java.time.LocalDateTime;

public class AsignaturaProfesorResponseDTO {
    private Long profesorId;
    private String profesorNombre;
    private Long asignaturaId;
    private String asignaturaNombre;
    private LocalDateTime fechaAsignacion;

    
    public Long getProfesorId() {
       return profesorId;
    }
    public void setProfesorId(Long profesorId) {
       this.profesorId = profesorId;
    }
    public String getProfesorNombre() {
       return profesorNombre;
    }
    public void setProfesorNombre(String profesorNombre) {
       this.profesorNombre = profesorNombre;
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
    public LocalDateTime getFechaAsignacion() {
       return fechaAsignacion;
    }
    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
       this.fechaAsignacion = fechaAsignacion;
    }
    
}
