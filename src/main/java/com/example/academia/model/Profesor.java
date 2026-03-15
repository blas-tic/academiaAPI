package com.example.academia.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "profesores")
public class Profesor {

   @Id
   private Long id;

   @OneToOne
   @MapsId
   @JoinColumn(name = "id")
   Persona persona;
   
   @Column(unique = true)
   private String numEmpleado;

   private String departamento;

   private String categoria;

   @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<AsignaturaProfesor> asignaturasImpartidas = new HashSet<>();

   public String getNumEmpleado() {
      return numEmpleado;
   }

   public void setNumEmpleado(String numEmpleado) {
      this.numEmpleado = numEmpleado;
   }

   public String getDepartamento() {
      return departamento;
   }

   public void setDepartamento(String departamento) {
      this.departamento = departamento;
   }

   public String getCategoria() {
      return categoria;
   }

   public void setCategoria(String categoria) {
      this.categoria = categoria;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Persona getPersona() {
      return persona;
   }

   public void setPersona(Persona persona) {
      this.persona = persona;
   }

   public Set<AsignaturaProfesor> getAsignaturasImpartidas() {
      return asignaturasImpartidas;
   }

   public void setAsignaturasImpartidas(Set<AsignaturaProfesor> asignaturasImpartidas) {
      this.asignaturasImpartidas = asignaturasImpartidas;
   }

}
