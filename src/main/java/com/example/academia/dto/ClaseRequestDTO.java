package com.example.academia.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ClaseRequestDTO {

   @NotNull(message = "El ID de la asignatura es obligatorio")
   private Long asignaturaId;

   @NotNull(message = "El ID del profesor es obligatorio")
   private Long profesorId;

   private Long aulaId;

   @NotNull(message = "El día de la semana es obligatorio")
   @Min(value = 1, message = "El día debe ser entre 1 y 5")
   @Max(value = 5, message = "El día debe ser entre 1 y 5")
   private Integer diaSemana;

   @NotNull(message = "La hora de inicio es obligatoria")
   private LocalTime horaInicio;

   @NotNull(message = "La hora de fin es obligatoria")
   private LocalTime horaFin;

   private LocalDate fechaInicio;
   private LocalDate fechaFin;

   
   public ClaseRequestDTO(@NotNull(message = "El ID de la asignatura es obligatorio") Long asignaturaId,
         @NotNull(message = "El ID del profesor es obligatorio") Long profesorId, Long aulaId,
         @NotNull(message = "El día de la semana es obligatorio") @Min(value = 1, message = "El día debe ser entre 1 y 5") @Max(value = 5, message = "El día debe ser entre 1 y 5") Integer diaSemana,
         @NotNull(message = "La hora de inicio es obligatoria") LocalTime horaInicio,
         @NotNull(message = "La hora de fin es obligatoria") LocalTime horaFin, LocalDate fechaInicio,
         LocalDate fechaFin) {
      this.asignaturaId = asignaturaId;
      this.profesorId = profesorId;
      this.aulaId = aulaId;
      this.diaSemana = diaSemana;
      this.horaInicio = horaInicio;
      this.horaFin = horaFin;
      this.fechaInicio = fechaInicio;
      this.fechaFin = fechaFin;
   }


   public ClaseRequestDTO() {
   }


   public Long getAsignaturaId() {
      return asignaturaId;
   }


   public void setAsignaturaId(Long asignaturaId) {
      this.asignaturaId = asignaturaId;
   }


   public Long getProfesorId() {
      return profesorId;
   }


   public void setProfesorId(Long profesorId) {
      this.profesorId = profesorId;
   }


   public Long getAulaId() {
      return aulaId;
   }


   public void setAulaId(Long aulaId) {
      this.aulaId = aulaId;
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
