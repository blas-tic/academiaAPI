package com.example.academia.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "clases")
public class Clase {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne
   @JoinColumn(name = "asignatura_id", nullable = false)
   private Asignatura asignatura;

   @ManyToOne
   @JoinColumn(name = "profesor_id", nullable = false)
   private Profesor profesor;

   @ManyToOne
   @JoinColumn(name = "aula_id")
   private Aula aula;

   @Column(name = "dia_semana", nullable = false)
   private Integer diaSemana; // 1=lunes...5=viernes

   @Column(name = "hora_inicio", nullable = false)
   private LocalTime horaInicio;

   @Column(name = "hora_fin", nullable = false)
   private LocalTime horaFin;

   @Column(name = "fecha_inicio", nullable = true)
   private LocalDate fechaInicio;

   @Column(name = "fecha_fin", nullable = true)
   private LocalDate fechaFin;

   // Getters y setters

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Asignatura getAsignatura() {
      return asignatura;
   }

   public void setAsignatura(Asignatura asignatura) {
      this.asignatura = asignatura;
   }

   public Profesor getProfesor() {
      return profesor;
   }

   public void setProfesor(Profesor profesor) {
      this.profesor = profesor;
   }

   public Aula getAula() {
      return aula;
   }

   public void setAula(Aula aula) {
      this.aula = aula;
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