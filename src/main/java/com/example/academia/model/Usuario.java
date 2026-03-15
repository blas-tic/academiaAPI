package com.example.academia.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @OneToOne
   @JoinColumn(name = "persona_id", unique = true, nullable = false)
   private Persona persona;

   @Column(name = "password_hash", nullable = false)
   private String passwordHash;

   private boolean activo = true;

   @ManyToMany
   @JoinTable(
      name = "usuario_rol",
      joinColumns = @JoinColumn(name = "usuario_id"),
      inverseJoinColumns = @JoinColumn(name = "rol_id")
   )
   private Set<Rol> roles;

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

   public String getPasswordHash() {
      return passwordHash;
   }

   public void setPasswordHash(String passwordHash) {
      this.passwordHash = passwordHash;
   }

   public boolean isActivo() {
      return activo;
   }

   public void setActivo(boolean activo) {
      this.activo = activo;
   }

   public Set<Rol> getRoles() {
      return roles;
   }

   public void setRoles(Set<Rol> roles) {
      this.roles = roles;
   }
   
   
}
