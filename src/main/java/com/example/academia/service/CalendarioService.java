package com.example.academia.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.academia.dto.ClaseResponseDTO;
import com.example.academia.model.Clase;
import com.example.academia.model.EstadoMatricula;
import com.example.academia.model.Matricula;
import com.example.academia.repository.ClaseRepository;
import com.example.academia.repository.MatriculaRepository;

@Service
public class CalendarioService {

   private final ClaseRepository claseRepository;
   private final MatriculaRepository matriculaRepository;
   private final AsignaturaProfesorService asignaturaProfesorService;

   public CalendarioService(ClaseRepository claseRepository, MatriculaRepository matriculaRepository,
         AsignaturaProfesorService asignaturaProfesorService) {
      this.claseRepository = claseRepository;
      this.matriculaRepository = matriculaRepository;
      this.asignaturaProfesorService = asignaturaProfesorService;
   }

   /**
    * Obtiene las clases de un alumno en un rango de fechas
    * Se basa en las asignaturas en las que el alumno tiene matricula activa
    */
   public List<ClaseResponseDTO> calendarioAlumno(Long alumnoId, LocalDate fechaInicio, LocalDate fechaFin) {
      // verificar que el alumno existe
      List<Matricula> matriculas = matriculaRepository.findByAlumnoId(alumnoId).stream()
            .filter(m -> m.getEstado() == EstadoMatricula.activa)
            .collect(Collectors.toList());

      List<Long> asignaturaIds = matriculas.stream()
            .map(m -> m.getAsignatura().getId())
            .collect(Collectors.toList());

      if (asignaturaIds.isEmpty()) {
         return List.of();
      }

      // buscar clases de esas asignaturas activas
      List<Clase> clases = claseRepository.findByAsignaturaIdInAndFechaRango(asignaturaIds, fechaInicio, fechaFin);

      return clases.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
   }

   /**
    * Obtiene las clases que imparte un profesor en un rango de fechas.
    */
   public List<ClaseResponseDTO> calendarioProfesor(Long profesorId, LocalDate fechaInicio, LocalDate fechaFin) {
      List<Clase> clases = claseRepository.findByProfesorIdAndFechaRango(profesorId, fechaInicio, fechaFin);
      return clases.stream().map(this::mapToResponse).collect(Collectors.toList());
   }

   /**
    * Obtiene todas las clases con filtros opcionales (para admin).
    */
   public List<ClaseResponseDTO> calendarioAdmin(Long asignaturaId, Long profesorId, Long aulaId,
         Integer diaSemana, LocalDate fechaInicio, LocalDate fechaFin) {
      // Usamos el método del repositorio que ya tiene filtros, pero añadiendo rango
      // de fechas
      // Necesitamos un método en el repositorio que combine todos los filtros.
      // Por simplicidad, podemos filtrar en memoria o crear una consulta más
      // compleja.
      // Aquí implementaremos una consulta con JPQL que incluya fechas.
      /*List<Clase> clases = claseRepository.findAllWithFiltersAndFechaRango(
            asignaturaId, profesorId, aulaId, diaSemana, fechaInicio, fechaFin);*/
      List<Clase> clases = claseRepository.findAllWithFiltersAndFechaRango(
            asignaturaId, profesorId, aulaId, diaSemana);            
      return clases.stream().map(this::mapToResponse).collect(Collectors.toList());
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
