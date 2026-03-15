package com.example.academia.dto;

import com.example.academia.model.RolNombre;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

public class UsuarioModificacionDTO {

    @Email
    private String email;           // Opcional, pero si se envía debe ser único

    private String nombre;

    private String telefono;

    private String direccion;

    private LocalDate fechaNacimiento;

    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;         // Opcional, si se quiere cambiar

    private Boolean activo;           // Opcional, para activar/desactivar

    // para los alumnos
    private String tutor;

    // para los profesores
    private String numEmpleado;
    private String departamento;
    private String categoria;
    

    private Set<RolNombre> roles;     // Opcional, para modificar los roles

    public String getEmail() {
       return email;
    }

    public void setEmail(String email) {
       this.email = email;
    }

    public String getNombre() {
       return nombre;
    }

    public void setNombre(String nombre) {
       this.nombre = nombre;
    }

    public String getTelefono() {
       return telefono;
    }

    public void setTelefono(String telefono) {
       this.telefono = telefono;
    }

    public String getDireccion() {
       return direccion;
    }

    public void setDireccion(String direccion) {
       this.direccion = direccion;
    }

    public LocalDate getFechaNacimiento() {
       return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
       this.fechaNacimiento = fechaNacimiento;
    }

    public String getPassword() {
       return password;
    }

    public void setPassword(String password) {
       this.password = password;
    }

    public Boolean getActivo() {
       return activo;
    }

    public void setActivo(Boolean activo) {
       this.activo = activo;
    }

    public Set<RolNombre> getRoles() {
       return roles;
    }

    public void setRoles(Set<RolNombre> roles) {
       this.roles = roles;
    }

    public String getTutor() {
       return tutor;
    }

    public void setTutor(String tutor) {
       this.tutor = tutor;
    }

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

}
