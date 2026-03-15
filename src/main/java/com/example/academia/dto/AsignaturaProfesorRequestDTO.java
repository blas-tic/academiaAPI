package com.example.academia.dto;

import jakarta.validation.constraints.NotNull;

public class AsignaturaProfesorRequestDTO {

   @NotNull(message = "El ID del profesor es obligatorio")
   private Long profesorId;

   @NotNull(message = "El ID de la asignatura es obligatorio")
   private Long asignaturaId;

   public Long getProfesorId() {
      return profesorId;
   }

   public void setProfesorId(Long profesorId) {
      this.profesorId = profesorId;
   }

   public Long getAsignaturaId() {
      return asignaturaId;
   }

   public void setAsignaturaId(Long asignaturaId) {
      this.asignaturaId = asignaturaId;
   }


   
}
