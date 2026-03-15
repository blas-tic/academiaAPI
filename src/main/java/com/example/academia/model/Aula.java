package com.example.academia.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "aulas")
public class Aula {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false)
   private String nombre;

   private Integer capacidad;
   private String ubicacion;

   @OneToMany(mappedBy = "aula")
   private Set<Clase> clases;

   // Getters y setters

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

   public Integer getCapacidad() {
      return capacidad;
   }

   public void setCapacidad(Integer capacidad) {
      this.capacidad = capacidad;
   }

   public String getUbicacion() {
      return ubicacion;
   }

   public void setUbicacion(String ubicacion) {
      this.ubicacion = ubicacion;
   }

   public Set<Clase> getClases() {
      return clases;
   }

   public void setClases(Set<Clase> clases) {
      this.clases = clases;
   }

}
