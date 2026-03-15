package com.example.academia.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ClaseResponseDTO {
   public ClaseResponseDTO() {
   }

   private Long id;
   private Long asignaturaId;
   private String asignaturaNombre;
   private Long profesorId;
   private String profesorNombre;
   private Long aulaId;
   private String aulaNombre;
   private Integer diaSemana;
   private LocalTime horaInicio;
   private LocalTime horaFin;
   private LocalDate fechaInicio;
   private LocalDate fechaFin;


   public Long getId() {
      return id;
   }
   public void setId(Long id) {
      this.id = id;
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
   public Long getAulaId() {
      return aulaId;
   }
   public void setAulaId(Long aulaId) {
      this.aulaId = aulaId;
   }
   public String getAulaNombre() {
      return aulaNombre;
   }
   public void setAulaNombre(String aulaNombre) {
      this.aulaNombre = aulaNombre;
   }
   public Integer getDiaSemana() {
      return diaSemana;
   }
   public void setDiaSemana(Integer diaSemana) {
      this.diaSemana = diaSemana;
   }
   public LocalTime getHoraInicio() {
      return horaInicio;
   }
   public void setHoraInicio(LocalTime horaInicio) {
      this.horaInicio = horaInicio;
   }
   public LocalTime getHoraFin() {
      return horaFin;
   }
   public void setHoraFin(LocalTime horaFin) {
      this.horaFin = horaFin;
   }
   public LocalDate getFechaInicio() {
      return fechaInicio;
   }
   public void setFechaInicio(LocalDate fechaInicio) {
      this.fechaInicio = fechaInicio;
   }
   public LocalDate getFechaFin() {
      return fechaFin;
   }
   public void setFechaFin(LocalDate fechaFin) {
      this.fechaFin = fechaFin;
   }
  
}
