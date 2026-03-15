package com.example.academia.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.academia.dto.UsuarioModificacionDTO;
import com.example.academia.dto.UsuarioRegistroDTO;
import com.example.academia.dto.UsuarioResponseDTO;
import com.example.academia.exception.EmailAlreadyExistsException;
import com.example.academia.exception.ResourceNotFoundException;
import com.example.academia.model.Alumno;
import com.example.academia.model.Persona;
import com.example.academia.model.Profesor;
import com.example.academia.model.RolNombre;
import com.example.academia.model.Usuario;
import com.example.academia.model.Rol;
import com.example.academia.repository.PersonaRepository;
import com.example.academia.repository.RolRepository;
import com.example.academia.repository.UsuarioRepository;

@Service
@Transactional
public class UsuarioService {

   private final PersonaRepository personaRepository;
   private final PasswordEncoder passwordEncoder;
   private final RolRepository rolRepository;
   private final UsuarioRepository usuarioRepository;

   public UsuarioService(PersonaRepository personaRepository, PasswordEncoder passwordEncoder,
         RolRepository rolRepository, UsuarioRepository usuarioRepository) {
      this.personaRepository = personaRepository;
      this.passwordEncoder = passwordEncoder;
      this.rolRepository = rolRepository;
      this.usuarioRepository = usuarioRepository;
   }

   @Transactional
   public UsuarioResponseDTO registrarUsuario(UsuarioRegistroDTO dto) {

      // verificar si ya existe email
      if (personaRepository.existsByEmail(dto.getEmail())) {
         throw new EmailAlreadyExistsException("Email ya registrado");
      }

      Persona persona = new Persona();

      persona.setEmail(dto.getEmail());
      persona.setNombre(dto.getNombre());
      persona.setTelefono(dto.getTelefono());
      persona.setDireccion(dto.getDireccion());
      persona.setFechaNacimiento(dto.getFechaNacimiento());
      persona.setFechaAlta(LocalDateTime.now());

      // segun roles, crear registros correspondientes
      Set<RolNombre> roles = dto.getRoles();
      if (roles.contains(RolNombre.alumno)) {
         Alumno alumno = new Alumno();
         alumno.setPersona(persona);
         alumno.setTutor(dto.getTutor());
         persona.setAlumno(alumno);
      }
      if (roles.contains(RolNombre.profesor)) {
         Profesor profesor = new Profesor();
         profesor.setPersona(persona);
         profesor.setNumEmpleado(dto.getNumEmpleado());
         profesor.setDepartamento(dto.getDepartamento());
         profesor.setCategoria(dto.getCategoria());
         persona.setProfesor(profesor);
      }

      // guardar
      persona = personaRepository.save(persona);

      // crear usuario
      Usuario usuario = new Usuario();
      usuario.setPersona(persona);
      usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
      usuario.setActivo(true);

      // Asignar roles
      Set<Rol> rolesEntities = roles.stream()
            .map(rolNombre -> rolRepository.findByNombre(rolNombre)
                  .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolNombre)))
            .collect(Collectors.toSet());
      usuario.setRoles(rolesEntities);

      // guardar
      usuario = usuarioRepository.save(usuario);

      return mapToResponseDTO(usuario);
   }

   @Transactional
   public List<UsuarioResponseDTO> listarUsuarios() {
      List<Usuario> usuarios = usuarioRepository.findAllWithRoles();
      return usuarios.stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
   }

   public UsuarioResponseDTO obtenerUsuarioPorId(Long id) {
      Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
      return mapToResponseDTO(usuario);
   }

   @Transactional
   public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioModificacionDTO dto) {
      Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

      Persona persona = usuario.getPersona();

      // Actualizar datos de persona si se proporcionan
      if (dto.getEmail() != null && !dto.getEmail().equals(persona.getEmail())) {
         // Verificar que el nuevo email no esté ya en uso por otra persona
         if (personaRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(
                  "El email " + dto.getEmail() + " ya está registrado por otro usuario");
         }
         persona.setEmail(dto.getEmail());
      }

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

      // Actualizar contraseña si se proporciona
      if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
         usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
      }

      // Actualizar estado activo
      if (dto.getActivo() != null) {
         usuario.setActivo(dto.getActivo());
      }

      // Actualizar roles si se proporcionan
      if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
         Set<RolNombre> nuevosRoles = dto.getRoles();
         Set<RolNombre> rolesActuales = usuario.getRoles().stream()
               .map(Rol::getNombre)
               .collect(Collectors.toSet());

         // Alumno
         if (nuevosRoles.contains(RolNombre.alumno) && !rolesActuales.contains(RolNombre.alumno)) {
            // crear alumno si no existía
            Alumno alumno = new Alumno();
            alumno.setPersona(persona);
            alumno.setTutor(dto.getTutor());
            persona.setAlumno(alumno);
         } else if (!nuevosRoles.contains(RolNombre.alumno) && rolesActuales.contains(RolNombre.alumno)) {
            // eliminar alumno
            persona.setAlumno(null);
         } else if (nuevosRoles.contains(RolNombre.alumno) && rolesActuales.contains(RolNombre.alumno)) {
            if (dto.getTutor() != null) {
               persona.getAlumno().setTutor(dto.getTutor());
            }
         }
         // Profesor (análogo)
         if (nuevosRoles.contains(RolNombre.profesor) && !rolesActuales.contains(RolNombre.profesor)) {
            Profesor profesor = new Profesor();
            profesor.setPersona(persona);
            profesor.setNumEmpleado(dto.getNumEmpleado());
            profesor.setDepartamento(dto.getDepartamento());
            profesor.setCategoria(dto.getCategoria());
            persona.setProfesor(profesor);
         } else if (!nuevosRoles.contains(RolNombre.profesor) && rolesActuales.contains(RolNombre.profesor)) {
            persona.setProfesor(null);
         } else if (nuevosRoles.contains(RolNombre.profesor) && rolesActuales.contains(RolNombre.profesor)) {
            // Actualizar datos de profesor
            if (dto.getNumEmpleado() != null)
               persona.getProfesor().setNumEmpleado(dto.getNumEmpleado());
            if (dto.getDepartamento() != null)
               persona.getProfesor().setDepartamento(dto.getDepartamento());
            if (dto.getCategoria() != null)
               persona.getProfesor().setCategoria(dto.getCategoria());
         }

         // actualizar la coleccion de roles de usuario
         Set<Rol> nuevosRolesEntities = nuevosRoles.stream()
            .map(rolNombre -> rolRepository.findByNombre(rolNombre)
               .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado " + rolNombre)))
            .collect(Collectors.toSet()
         );
         usuario.setRoles(nuevosRolesEntities);
      }

      // Guardar cambios (persona se guarda en cascada? Depende del mapeo. Si no,
      // guardar persona explícitamente)
      persona = personaRepository.save(persona); // Opcional, si no hay cascade
      usuario = usuarioRepository.save(usuario);
      return mapToResponseDTO(usuario);
   }

   @Transactional
   public void desactivarUsuario(Long id) {
      Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
      usuario.setActivo(false);
      usuarioRepository.save(usuario);
      // Podríamos también desactivar la persona si se desea, pero no es necesario.
   }

   // Opcional: eliminación física (no recomendada por integridad referencial)
   @Transactional
   public void eliminarUsuarioFisicamente(Long id) {
      Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
      // Antes de eliminar, habría que verificar que no tenga relaciones (matrículas,
      // clases, etc.)
      usuarioRepository.delete(usuario);
      // La persona se borrará en cascada si está configurado, pero cuidado con
      // alumnos/profesores.
   }

   private UsuarioResponseDTO mapToResponseDTO(Usuario usuario) {
      UsuarioResponseDTO dto = new UsuarioResponseDTO();
      dto.setId(usuario.getId());
      dto.setEmail(usuario.getPersona().getEmail());
      dto.setNombre(usuario.getPersona().getNombre());
      dto.setTelefono(usuario.getPersona().getTelefono());
      dto.setDireccion(usuario.getPersona().getDireccion());
      dto.setFechaAlta(usuario.getPersona().getFechaAlta());
      dto.setActivo(usuario.isActivo());

      // extraer nombres de los roles
      Set<String> rolesNombres = usuario.getRoles().stream()
            .map(rol -> rol.getNombre().name())
            .collect(Collectors.toSet());
      dto.setRoles(rolesNombres);

      return dto;
   }

}
