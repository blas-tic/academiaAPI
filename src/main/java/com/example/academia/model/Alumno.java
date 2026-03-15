package com.example.academia.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "alumnos")
public class Alumno {

   @Id
   private Long id;

   private String tutor;

   @OneToOne
   @MapsId
   @JoinColumn(name = "id")
   Persona persona;

   @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<Matricula> matriculas = new HashSet<>();

   public String getTutor() {
      return tutor;
   }

   public void setTutor(String tutor) {
      this.tutor = tutor;
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

   public Set<Matricula> getMatriculas() {
      return matriculas;
   }

   public void setMatriculas(Set<Matricula> matriculas) {
      this.matriculas = matriculas;
   }

}
