package com.example.academia.dto;

public class AulaResponseDTO {
   private Long id;
   private String nombre;
   private Integer capacidad;
   private String ubicacion;
   
   public AulaResponseDTO(Long id, String nombre, Integer capacidad, String ubicacion) {
      this.id = id;
      this.nombre = nombre;
      this.capacidad = capacidad;
      this.ubicacion = ubicacion;
   }

   public AulaResponseDTO() {
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
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
      AulaResponseDTO other = (AulaResponseDTO) obj;
      if (id == null) {
         if (other.id != null)
            return false;
      } else if (!id.equals(other.id))
         return false;
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

   
}
