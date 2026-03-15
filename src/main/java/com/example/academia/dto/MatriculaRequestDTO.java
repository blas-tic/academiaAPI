package com.example.academia.dto;

public class MatriculaRequestDTO {

   private Long alumnoId;
   private Long asignaturaId;

   public MatriculaRequestDTO(Long alumnoId, Long asignaturaId) {
      this.alumnoId = alumnoId;
      this.asignaturaId = asignaturaId;
   }

   public MatriculaRequestDTO() {
      super();
   }

   public Long getAlumnoId() {
      return alumnoId;
   }

   public void setAlumnoId(Long alumnoId) {
      this.alumnoId = alumnoId;
   }

   public Long getAsignaturaId() {
      return asignaturaId;
   }

   public void setAsignaturaId(Long asignaturaId) {
      this.asignaturaId = asignaturaId;
   }



}
