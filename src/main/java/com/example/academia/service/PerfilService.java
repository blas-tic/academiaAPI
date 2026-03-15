package com.example.academia.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.academia.dto.PerfilResponseDTO;
import com.example.academia.dto.PerfilUpdateDTO;
import com.example.academia.exception.ResourceNotFoundException;
import com.example.academia.exception.AccessDeniedException;
import com.example.academia.model.Alumno;
import com.example.academia.model.Persona;
import com.example.academia.model.Profesor;
import com.example.academia.model.Rol;
import com.example.academia.model.RolNombre;
import com.example.academia.model.Usuario;
import com.example.academia.repository.AlumnoRepository;
import com.example.academia.repository.PersonaRepository;
import com.example.academia.repository.ProfesorRepository;
import com.example.academia.repository.UsuarioRepository;

import jakarta.validation.constraints.Email;

@Service
public class PerfilService {

   private final PersonaRepository personaRepository;
   private final AlumnoRepository alumnoRepository;
   private final ProfesorRepository profesorRepository;
   private final UsuarioRepository usuarioRepository;
   private final PasswordEncoder passwordEncoder;
   private final EmailService emailService;

   public PerfilService(PersonaRepository personaRepository, AlumnoRepository alumnoRepository,
         ProfesorRepository profesorRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
      this.personaRepository = personaRepository;
      this.alumnoRepository = alumnoRepository;
      this.profesorRepository = profesorRepository;
      this.usuarioRepository = usuarioRepository;
      this.passwordEncoder = passwordEncoder;
      this.emailService = emailService;
   }

   public PerfilResponseDTO obtenerPerfil(Long personaId, Long usuarioAutenticadoId, boolean esAdmin) {
      if (!esAdmin && !personaId.equals(usuarioAutenticadoId)) {
         throw new AccessDeniedException("No tienes permiso para ver este perfil");
      }

      Persona persona = personaRepository.findById(personaId)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra Persona con ID: " + personaId));

      Usuario usuario = usuarioRepository.findByPersonaId(personaId)
            .orElseThrow(
                  () -> new ResourceNotFoundException("No se encuentra Usuario para la Persona con ID: " + personaId));

      return mapToResponse(persona, usuario);

   }

   @Transactional
   public PerfilResponseDTO actualizarPerfil(Long personaId, long usuarioAutenticadoId, boolean esAdmin,
         PerfilUpdateDTO dto) {
      if (!esAdmin && !personaId.equals(usuarioAutenticadoId)) {
         throw new AccessDeniedException("No tienes permiso para ver este perfil");
      }
      Persona persona = personaRepository.findById(personaId)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra Persona con ID: " + personaId));

      Usuario usuario = usuarioRepository.findByPersonaId(personaId)
            .orElseThrow(
                  () -> new ResourceNotFoundException("No se encuentra Usuario para la Persona con ID: " + personaId));

      // actualizar campos comunes
      if (dto.getNombre() != null) {
         persona.setNombre(dto.getNombre());
      }
      if (dto.getTelefono() != null) {
         persona.setTelefono(dto.getTelefono());
      }
      if (dto.getDireccion() != null) {
         persona.setDireccion(dto.getDireccion());
      }
      if (dto.getFechaNacimiento() != null) {
         persona.setFechaNacimiento(dto.getFechaNacimiento());
      }

      // guardar persona
      persona = personaRepository.save(persona);

      // sacar los roles
      Set<RolNombre> roles = usuario.getRoles().stream()
            .map(Rol::getNombre)
            .collect(Collectors.toSet());

      // si es alumno y vienen datos, actualizar campos
      if (roles.contains(RolNombre.alumno)) {
         Alumno alumno = alumnoRepository.findById(personaId)
               .orElseThrow(() -> new ResourceNotFoundException("No se encuentra Alumno con ID: " + personaId));
         if (dto.getTutor() != null) {
            alumno.setTutor(dto.getTutor());
         }
         alumnoRepository.save(alumno);
      }

      // si es profesor y tiene datos, actualizar campos
      if (roles.contains(RolNombre.profesor)) {
         Profesor profesor = profesorRepository.findById(personaId)
               .orElseThrow(() -> new ResourceNotFoundException("No se encuentra Profesor con ID: " + personaId));
         if (dto.getNumEmpleado() != null) {
            // controlar que el numero de empleado no exista
            profesorRepository.findByNumEmpleado(dto.getNumEmpleado())
                  .ifPresent(p -> {
                     if (!p.getId().equals(personaId)) {
                        throw new IllegalArgumentException("Ese número de empleado ya está en uso");
                     }
                  });
            profesor.setNumEmpleado(dto.getNumEmpleado());
         }
         if (dto.getDepartamento() != null) {
            profesor.setDepartamento(dto.getDepartamento());
         }
         if (dto.getCategoria() != null) {
            profesor.setCategoria(dto.getCategoria());
         }
         profesorRepository.save(profesor);
      }

      // si es admin y no tiene otros roles, sólo se actualizan los campos comunes de
      // arriba

      return mapToResponse(persona, usuario);
   }

   private PerfilResponseDTO mapToResponse(Persona persona, Usuario usuario) {
      PerfilResponseDTO dto = new PerfilResponseDTO();
      dto.setId(persona.getId());
      dto.setEmail(persona.getEmail());
      dto.setNombre(persona.getNombre());
      dto.setTelefono(persona.getTelefono());
      dto.setDireccion(persona.getDireccion());
      dto.setFechaNacimiento(persona.getFechaNacimiento());
      dto.setFechaAlta(persona.getFechaAlta());

      // Roles
      Set<String> roles = usuario.getRoles().stream()
            .map(rol -> rol.getNombre().name())
            .collect(Collectors.toSet());
      dto.setRoles(roles);

      // Cargar datos específicos si existen
      if (roles.contains("alumno")) {
         alumnoRepository.findById(persona.getId()).ifPresent(alumno -> {
            dto.setTutor(alumno.getTutor());
         });
      }
      if (roles.contains("profesor")) {
         profesorRepository.findById(persona.getId()).ifPresent(profesor -> {
            dto.setNumEmpleado(profesor.getNumEmpleado());
            dto.setDepartamento(profesor.getDepartamento());
            dto.setCategoria(profesor.getCategoria());
         });
      }
      // Si es admin, no hay campos adicionales
      return dto;
   }

   @Transactional
   public void cambiarMiPassword(Long usuarioId, String oldPassword, String newPassword) {
      Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra Usuario con ID: " + usuarioId));

      if (!passwordEncoder.matches(oldPassword, usuario.getPasswordHash())) {
         throw new IllegalArgumentException("La contraseña actual no es correcta");
      }

      usuario.setPasswordHash(passwordEncoder.encode(newPassword));
      if (!usuario.isActivo()) {
         usuario.setActivo(true);
      }
      usuarioRepository.save(usuario);

      // enviar email de confirmación
      emailService.enviarConfirmacion(usuario.getPersona().getEmail());

   }

   @Transactional
   public void cambiarPasswordComoAdmin(Long usuarioId, String newPassword) {
      Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra Usuario con ID: " + usuarioId));

      usuario.setPasswordHash(passwordEncoder.encode(newPassword));
      usuarioRepository.save(usuario);

   }

}
