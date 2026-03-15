package com.example.academia.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.academia.dto.RegistroRequestDTO;
import com.example.academia.dto.UsuarioResponseDTO;
import com.example.academia.exception.EmailAlreadyExistsException;
import com.example.academia.exception.ResourceNotFoundException;
import com.example.academia.model.Alumno;
import com.example.academia.model.Persona;
import com.example.academia.model.Profesor;
import com.example.academia.model.Rol;
import com.example.academia.model.RolNombre;
import com.example.academia.model.Usuario;
import com.example.academia.repository.AlumnoRepository;
import com.example.academia.repository.PersonaRepository;
import com.example.academia.repository.ProfesorRepository;
import com.example.academia.repository.RolRepository;
import com.example.academia.repository.UsuarioRepository;

@Service
public class RegistroService {

   private final PersonaRepository personaRepository;
   private final UsuarioRepository usuarioRepository;
   private final AlumnoRepository alumnoRepository;
   private final ProfesorRepository profesorRepository;
   private final RolRepository rolRepository;
   private final PasswordEncoder passwordEncoder;
   private final EmailService emailService;

   public RegistroService(PersonaRepository personaRepository, UsuarioRepository usuarioRepository,
         AlumnoRepository alumnoRepository, ProfesorRepository profesorRepository, RolRepository rolRepository,
         PasswordEncoder passwordEncoder, EmailService emailService) {
      this.personaRepository = personaRepository;
      this.usuarioRepository = usuarioRepository;
      this.alumnoRepository = alumnoRepository;
      this.profesorRepository = profesorRepository;
      this.rolRepository = rolRepository;
      this.passwordEncoder = passwordEncoder;
      this.emailService = emailService;
   }

   @Transactional
   public UsuarioResponseDTO registrar(RegistroRequestDTO dto) {
      // validar email unico
      if (personaRepository.existsByEmail(dto.getEmail())) {
         throw new EmailAlreadyExistsException("El email " + dto.getEmail() + " ya está registrado");
      }

      // crear persona
      Persona persona = new Persona();
      persona.setEmail(dto.getEmail());
      persona.setNombre(dto.getNombre());
      persona.setTelefono(dto.getTelefono());
      persona.setDireccion(dto.getDireccion());
      persona.setFechaNacimiento(dto.getFechaNacimiento());
      persona.setFechaAlta(LocalDateTime.now());

      persona = personaRepository.save(persona);

      // crear entidad hija segun Roles
      Set<RolNombre> roles = dto.getRoles();
      // alumnos
      if (roles.contains(RolNombre.alumno)) {
         Alumno alumno = new Alumno();
         alumno.setPersona(persona);
         alumno.setTutor(dto.getTutor());
         alumnoRepository.save(alumno);
      }
      // profesores
      if (roles.contains(RolNombre.profesor)) {
         Profesor profesor = new Profesor();
         profesor.setPersona(persona);
         profesor.setCategoria(dto.getCategoria());
         profesor.setDepartamento(dto.getDepartamento());
         profesor.setNumEmpleado(dto.getNumEmpleado());
         profesorRepository.save(profesor);
      }
      // no se permite el rol admin en autoregistro

      String rawPassword = generarPasswordAleatorio();

      // crear usuario inactivo
      Usuario usuario = new Usuario();
      usuario.setPersona(persona);
      usuario.setPasswordHash(passwordEncoder.encode(rawPassword));
      usuario.setActivo(false);

      // asignar Roles
      Set<Rol> rolesEntities = new HashSet<>();
      for (RolNombre rolNombre : roles) {
         Rol rol = rolRepository.findByNombre(rolNombre)
               .orElseThrow(() -> new ResourceNotFoundException("No se encuentra Rol: " + rolNombre));
         rolesEntities.add(rol);
      }
      usuario.setRoles(rolesEntities);
      usuario = usuarioRepository.save(usuario);

      // enviar email bienvenida con contraseña
      emailService.enviarPassword(persona.getEmail(), rawPassword);

      return mapToResponse(usuario);

   }


   @Transactional
   public void activarCuenta(String email, String currentPassword, String newPassword) {
      Usuario usuario = usuarioRepository.findByPersonaEmail(email)
         .orElseThrow(() -> new ResourceNotFoundException("No se encuentra Usuario con email: " + email));

         if (usuario.isActivo()) {
            throw new IllegalArgumentException("La cuenta ya está activa");
         }

         if (!passwordEncoder.matches(currentPassword, usuario.getPasswordHash())) {
            throw new BadCredentialsException("Contraseña actual incorrecta");
         }

         usuario.setPasswordHash(passwordEncoder.encode(newPassword));

         usuario.setActivo(true);

         usuarioRepository.save(usuario);

         emailService.enviarConfirmacion(email);
   }

   private String generarPasswordAleatorio() {
      return UUID.randomUUID().toString().substring(0, 8);
   }

   private UsuarioResponseDTO mapToResponse(Usuario usuario) {
      UsuarioResponseDTO dto = new UsuarioResponseDTO();
      dto.setId(usuario.getId());
      dto.setEmail(usuario.getPersona().getEmail());
      dto.setNombre(usuario.getPersona().getNombre());
      dto.setTelefono(usuario.getPersona().getTelefono());
      dto.setDireccion(usuario.getPersona().getDireccion());
      dto.setFechaAlta(usuario.getPersona().getFechaAlta());
      dto.setActivo(usuario.isActivo());
      dto.setRoles(usuario.getRoles().stream().map(r -> r.getNombre().name()).collect(Collectors.toSet()));
      return dto;
   }



}
