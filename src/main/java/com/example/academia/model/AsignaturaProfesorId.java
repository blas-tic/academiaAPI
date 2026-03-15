package com.example.academia.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class AsignaturaProfesorId implements Serializable{

   public Long profesorId;
   public Long asignaturaId;

   
   public AsignaturaProfesorId(Long profesorId, Long asignaturaId) {
      this.profesorId = profesorId;
      this.asignaturaId = asignaturaId;
   }


   public AsignaturaProfesorId() {
   }


   public Long getProfesorId() {
      return profesorId;
   }


   public void setProfesorId(Long profesorId) {
      this.profesorId = profesorId;
   }


   public Long getAsignaturaId() {
      return asignaturaId;
   }


   public void setAsignaturaId(Long asignaturaId) {
      this.asignaturaId = asignaturaId;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((profesorId == null) ? 0 : profesorId.hashCode());
      result = prime * result + ((asignaturaId == null) ? 0 : asignaturaId.hashCode());
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
      AsignaturaProfesorId other = (AsignaturaProfesorId) obj;
      if (profesorId == null) {
         if (other.profesorId != null)
            return false;
      } else if (!profesorId.equals(other.profesorId))
         return false;
      if (asignaturaId == null) {
         if (other.asignaturaId != null)
            return false;
      } else if (!asignaturaId.equals(other.asignaturaId))
         return false;
      return true;
   }




   
}
