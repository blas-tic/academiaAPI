package com.example.academia.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "asignaturas")
public class Asignatura {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false)
   private String nombre;

   private String descripcion;
   private String color;

   @OneToMany(mappedBy = "asignatura", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<Matricula> matriculas = new HashSet<>();

   @OneToMany(mappedBy = "asignatura", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<AsignaturaProfesor> profesoresAsignados = new HashSet<>();
   
   @OneToMany(mappedBy = "asignatura")
   private Set<Clase> clases;

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

   public Set<Clase> getClases() {
      return clases;
   }

   public void setClases(Set<Clase> clases) {
      this.clases = clases;
   }

   public Set<Matricula> getMatriculas() {
      return matriculas;
   }

   public void setMatriculas(Set<Matricula> matriculas) {
      this.matriculas = matriculas;
   }

   public Set<AsignaturaProfesor> getProfesoresAsignados() {
      return profesoresAsignados;
   }

   public void setProfesoresAsignados(Set<AsignaturaProfesor> profesoresAsignados) {
      this.profesoresAsignados = profesoresAsignados;
   }

}
