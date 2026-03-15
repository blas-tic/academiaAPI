package com.example.academia.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "asignatura_profesor")
public class AsignaturaProfesor {

   @EmbeddedId
   private AsignaturaProfesorId id;
  

    @ManyToOne
    @MapsId("profesorId")
    @JoinColumn(name = "profesor_id", insertable = false, updatable = false)
    private Profesor profesor;

    @ManyToOne
    @MapsId("asignaturaId")
    @JoinColumn(name = "asignatura_id", insertable = false, updatable = false)
    private Asignatura asignatura;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fechaAsignacion;

    // Getters y setters



    public Profesor getProfesor() {
       return profesor;
    }

    public void setProfesor(Profesor p) {
      this.profesor = p;
    }
    
    public Asignatura getAsignatura() {
       return asignatura;
    }

    public void setAsignatura (Asignatura a) {
      this.asignatura = a;
    }

    public LocalDateTime getFechaAsignacion() {
       return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
       this.fechaAsignacion = fechaAsignacion;
    }

    public AsignaturaProfesorId getId() {
       return id;
    }

    public void setId(AsignaturaProfesorId id) {
       this.id = id;
    }

}