package com.example.academia.model;

import java.io.Serializable;

public class MatriculaId implements Serializable {
   private Long alumno;
   private Long asignatura;

   // constructor por defecto, equals, hashCode

   public MatriculaId() {
   }

   public MatriculaId(Long alumno, Long asignatura) {
      this.alumno = alumno;
      this.asignatura = asignatura;
   }

   public Long getAlumno() {
      return alumno;
   }

   public void setAlumno(Long alumno) {
      this.alumno = alumno;
   }

   public Long getAsignatura() {
      return asignatura;
   }

   public void setAsignatura(Long asignatura) {
      this.asignatura = asignatura;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((alumno == null) ? 0 : alumno.hashCode());
      result = prime * result + ((asignatura == null) ? 0 : asignatura.hashCode());
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
      MatriculaId other = (MatriculaId) obj;
      if (alumno == null) {
         if (other.alumno != null)
            return false;
      } else if (!alumno.equals(other.alumno))
         return false;
      if (asignatura == null) {
         if (other.asignatura != null)
            return false;
      } else if (!asignatura.equals(other.asignatura))
         return false;
      return true;
   }

}