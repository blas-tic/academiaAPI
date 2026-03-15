package com.example.academia.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.academia.dto.ClaseRequestDTO;
import com.example.academia.dto.ClaseResponseDTO;
import com.example.academia.exception.ResourceNotFoundException;
import com.example.academia.model.Asignatura;
import com.example.academia.model.Aula;
import com.example.academia.model.Clase;
import com.example.academia.model.Profesor;
import com.example.academia.repository.AsignaturaRepository;
import com.example.academia.repository.AulaRepository;
import com.example.academia.repository.ClaseRepository;
import com.example.academia.repository.ProfesorRepository;

@Service
public class ClaseService {
   private final ClaseRepository claseRepository;
   private final AulaRepository aulaRepository;
   private final AsignaturaRepository asignaturaRepository;
   private final ProfesorRepository profesorRepository;

   public ClaseService(ClaseRepository claseRepository, AulaRepository aulaRepository,
         AsignaturaRepository asignaturaRepository, ProfesorRepository profesorRepository) {
      this.claseRepository = claseRepository;
      this.aulaRepository = aulaRepository;
      this.asignaturaRepository = asignaturaRepository;
      this.profesorRepository = profesorRepository;
   }

   public List<ClaseResponseDTO> listarConFiltros(Long asignaturaId, Long aulaId, Long profesorId, Integer diaSemana) {
      List<Clase> clases = claseRepository.findAllWithFilters(asignaturaId, profesorId, aulaId, diaSemana);
      return clases.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
   }

   public ClaseResponseDTO obtenerPorId(Long id) {
      Clase clase = claseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra clase con ID: " + id));

      return mapToResponse(clase);
   }

   @Transactional
   public ClaseResponseDTO crearClase(ClaseRequestDTO dto) {
      // validar entidades relacionadas
      Asignatura asignatura = asignaturaRepository.findById(dto.getAsignaturaId())
            .orElseThrow(
                  () -> new ResourceNotFoundException("No se encuentra Asignatura con ID: " + dto.getAsignaturaId()));
      Profesor profesor = profesorRepository.findById(dto.getProfesorId())
            .orElseThrow(
                  () -> new ResourceNotFoundException("No se encuentra Profesor con ID: " + dto.getProfesorId()));
      Aula aula = null;
      if (dto.getAulaId() != null) {
         aula = aulaRepository.findById(dto.getAulaId())
               .orElseThrow(() -> new ResourceNotFoundException("No se encuentra Aula con ID: " + dto.getAulaId()));
      }

      // validar horario
      validarHorario(dto.getHoraInicio(), dto.getHoraFin());

      // validar fechas
      validarFechas(dto.getFechaInicio(), dto.getFechaFin());

      // validar conflictos
      validarConflictos(null, dto.getProfesorId(), dto.getAulaId(), dto.getDiaSemana(), dto.getHoraInicio(),
            dto.getHoraFin());

      Clase clase = new Clase();
      clase.setAsignatura(asignatura);
      clase.setAula(aula);
      clase.setProfesor(profesor);
      clase.setDiaSemana(dto.getDiaSemana());
      clase.setHoraInicio(dto.getHoraInicio());
      clase.setHoraFin(dto.getHoraFin());
      clase.setFechaInicio(dto.getFechaInicio());
      clase.setFechaFin(dto.getFechaFin());

      clase = claseRepository.save(clase);

      return mapToResponse(clase);
   }

   @Transactional
   public ClaseResponseDTO actualizarClase(Long id, ClaseRequestDTO dto) {
      Clase clase = claseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra Clase con ID: " + id));

      // validar entidades si cambian
      if (!clase.getAsignatura().getId().equals(dto.getAsignaturaId())) {
         Asignatura asignatura = asignaturaRepository.findById(dto.getAsignaturaId())
               .orElseThrow(() -> new ResourceNotFoundException(
                     "No se encuentra Asignatura con ID: " + dto.getAsignaturaId()));
         clase.setAsignatura(asignatura);
      }
      if (!clase.getProfesor().getId().equals(dto.getProfesorId())) {
         Profesor profesor = profesorRepository.findById(dto.getProfesorId())
               .orElseThrow(() -> new ResourceNotFoundException(
                     "No se encuentra Profesor con ID: " + dto.getProfesorId()));
         clase.setProfesor(profesor);
      }
      if (dto.getAulaId() == null) {
         clase.setAula(null);
      } else if (clase.getAula() == null || !clase.getAula().getId().equals(dto.getAulaId())) {
         Aula aula = aulaRepository.findById(dto.getAulaId())
               .orElseThrow(() -> new ResourceNotFoundException(
                     "No se encuentra Aula con ID: " + dto.getAulaId())); 
         clase.setAula(aula);                             
      }

      // validar horarios y fechas
      validarHorario(dto.getHoraInicio(), dto.getHoraFin());
      validarFechas(dto.getFechaInicio(), dto.getFechaFin());

      // validar conflictos excluyendo la propia clase
      validarConflictos(id, dto.getProfesorId(), dto.getAulaId(), dto.getDiaSemana(), dto.getHoraInicio(), dto.getHoraFin());

      clase.setDiaSemana(dto.getDiaSemana());
      clase.setHoraInicio(dto.getHoraInicio());
      clase.setHoraFin(dto.getHoraFin());
      clase.setFechaInicio(dto.getFechaInicio());
      clase.setFechaFin(dto.getFechaFin());

      clase = claseRepository.save(clase);

      return mapToResponse(clase);
   }

   @Transactional
   public void eliminarClase(Long id) {
      if (!claseRepository.existsById(id)) {
         throw new ResourceNotFoundException("No se encuentra Clase con ID: " + id);
      }
      // TODO: Validar que no tenga dependencias (asistencia, etc.) en el futuro
      claseRepository.deleteById(id);
   }

   // validadores y auxiliares

   private void validarHorario(LocalTime horaInicio, LocalTime horaFin) {
      if (!horaFin.isAfter(horaInicio)) {
         throw new IllegalArgumentException("La hora de fin ha de ser mayor que la de inicio");
      }
   }

   private void validarFechas(LocalDate inicio, LocalDate fin) {
      if (inicio != null && fin != null && fin.isBefore(inicio)) {
         throw new IllegalArgumentException("El día de fin ha de ser mayor que el de inicio");
      }
   }

   private void validarConflictos(Long idExcluir, Long profesorId, Long aulaId, Integer diaSemana, LocalTime horaInicio, LocalTime horaFin) {
      // conflictos de profesor
      List<Clase> conflictosProfesor = claseRepository.findConflictosProfesor(profesorId, diaSemana, horaInicio, horaFin);
      if (idExcluir != null) {
         conflictosProfesor = conflictosProfesor.stream().filter(c -> !c.getId().equals(idExcluir))
            .collect(Collectors.toList());
      }
      if (!conflictosProfesor.isEmpty()) {
         throw new IllegalArgumentException("El profesor ya tiene una clase en ese horario");
      }

      // conflictos de Aula, si se especifica
      if (aulaId != null) {
         List<Clase> conflictosAula = claseRepository.findConflictosAula(aulaId, diaSemana, horaInicio, horaFin);
         if (idExcluir != null) {
            conflictosAula = conflictosAula.stream().filter(c -> !c.getId().equals(idExcluir)).collect(Collectors.toList());
         }
         if (!conflictosAula.isEmpty()) {
            throw new IllegalArgumentException("El Aula ya está ocupada en este horario");
         }
      }


   }

   private ClaseResponseDTO mapToResponse(Clase clase) {
      ClaseResponseDTO dto = new ClaseResponseDTO();
      dto.setId(clase.getId());
      dto.setAsignaturaId(clase.getAsignatura().getId());
      dto.setAsignaturaNombre(clase.getAsignatura().getNombre());
      dto.setProfesorId(clase.getProfesor().getId());
      dto.setProfesorNombre(clase.getProfesor().getPersona().getNombre());
      if (clase.getAula() != null) {
         dto.setAulaId(clase.getAula().getId());
         dto.setAulaNombre(clase.getAula().getNombre());
      }
      dto.setDiaSemana(clase.getDiaSemana());
      dto.setHoraInicio(clase.getHoraInicio());
      dto.setHoraFin(clase.getHoraFin());
      dto.setFechaInicio(clase.getFechaInicio());
      dto.setFechaFin(clase.getFechaFin());
      return dto;
   }
}
