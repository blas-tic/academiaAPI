package com.example.academia.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "personas")
public class Persona {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(unique = true, nullable = false)
   private String email;

   @Column(nullable = false)
   private String nombre;

   private String telefono;
   private String direccion;

   @Column(name = "fecha_nacimiento")
   private LocalDate fechaNacimiento;

   @Column(name = "fecha_alta", nullable = false)
   private LocalDateTime fechaAlta;


    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    private Alumno alumno;

    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profesor profesor;   

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getNombre() {
      return nombre;
   }

   public void setNombre(String nombre) {
      this.nombre = nombre;
   }

   public String getTelefono() {
      return telefono;
   }

   public void setTelefono(String telefono) {
      this.telefono = telefono;
   }

   public String getDireccion() {
      return direccion;
   }

   public void setDireccion(String direccion) {
      this.direccion = direccion;
   }

   public LocalDate getFechaNacimiento() {
      return fechaNacimiento;
   }

   public void setFechaNacimiento(LocalDate fechaNacimiento) {
      this.fechaNacimiento = fechaNacimiento;
   }

   public LocalDateTime getFechaAlta() {
      return fechaAlta;
   }

   public void setFechaAlta(LocalDateTime fechaAlta) {
      this.fechaAlta = fechaAlta;
   }

   public Alumno getAlumno() {
      return alumno;
   }

   public void setAlumno(Alumno alumno) {
      this.alumno = alumno;
   }

   public Profesor getProfesor() {
      return profesor;
   }

   public void setProfesor(Profesor profesor) {
      this.profesor = profesor;
   }
   
}
