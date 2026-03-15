package com.example.academia.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "matriculas")
@IdClass(MatriculaId.class)
public class Matricula {
   @Id
   @ManyToOne
   @JoinColumn(name = "alumno_id")
   private Alumno alumno;

   @Id
   @ManyToOne
   @JoinColumn(name = "asignatura_id")
   private Asignatura asignatura;

   @Column(name = "fecha_matricula", nullable = false)
   private LocalDateTime fechaMatricula;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private EstadoMatricula estado = EstadoMatricula.activa;

   // Getters y setters

   public Alumno getAlumno() {
      return alumno;
   }

   public void setAlumno(Alumno alumno) {
      this.alumno = alumno;
   }

   public Asignatura getAsignatura() {
      return asignatura;
   }

   public void setAsignatura(Asignatura asignatura) {
      this.asignatura = asignatura;
   }

   public LocalDateTime getFechaMatricula() {
      return fechaMatricula;
   }

   public void setFechaMatricula(LocalDateTime fechaMatricula) {
      this.fechaMatricula = fechaMatricula;
   }

   public EstadoMatricula getEstado() {
      return estado;
   }

   public void setEstado(EstadoMatricula estado) {
      this.estado = estado;
   }

}