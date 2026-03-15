package com.example.academia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class AulaRequestDTO {

   @NotBlank(message = "El nombre del Aula es obligatorio")
   @Size(max = 50)
   private String nombre;

   @PositiveOrZero(message = "La capacidad del Aula ha de ser un numero mayor o igual a cero")
   private Integer capacidad;

   @Size(max = 100)
   private String ubicacion;

   public AulaRequestDTO(@NotBlank(message = "El nombre del Aula es obligatorio") @Size(max = 50) String nombre,
         @PositiveOrZero(message = "La capacidad del Aula ha de ser un numero mayor o igual a cero") Integer capacidad,
         @Size(max = 100) String ubicacion) {
      this.nombre = nombre;
      this.capacidad = capacidad;
      this.ubicacion = ubicacion;
   }

   public AulaRequestDTO() {
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

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
      result = prime * result + ((ubicacion == null) ? 0 : ubicacion.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      AulaRequestDTO other = (AulaRequestDTO) obj;
      if (nombre == null) {
         if (other.nombre != null)
            return false;
      } else if (!nombre.equals(other.nombre))
         return false;
      if (ubicacion == null) {
         if (other.ubicacion != null)
            return false;
      } else if (!ubicacion.equals(other.ubicacion))
         return false;
      return true;
   }

   

}
