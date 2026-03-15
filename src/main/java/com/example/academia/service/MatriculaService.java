package com.example.academia.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.academia.dto.MatriculaRequestDTO;
import com.example.academia.dto.MatriculaResponseDTO;
import com.example.academia.exception.ResourceNotFoundException;
import com.example.academia.model.Alumno;
import com.example.academia.model.Asignatura;
import com.example.academia.model.EstadoMatricula;
import com.example.academia.model.Matricula;
import com.example.academia.model.MatriculaId;
import com.example.academia.repository.AlumnoRepository;
import com.example.academia.repository.AsignaturaRepository;
import com.example.academia.repository.MatriculaRepository;

@Service
public class MatriculaService {

   private final MatriculaRepository matriculaRepository;
   private final AlumnoRepository alumnoRepository;
   private final AsignaturaRepository asignaturaRepository;

   public MatriculaService(MatriculaRepository matriculaRepository, AlumnoRepository alumnoRepository,
         AsignaturaRepository asignaturaRepository) {
      this.matriculaRepository = matriculaRepository;
      this.alumnoRepository = alumnoRepository;
      this.asignaturaRepository = asignaturaRepository;
   }

   @Transactional
   public MatriculaResponseDTO matricular(MatriculaRequestDTO dto) {
      Alumno alumno = alumnoRepository.findById(dto.getAlumnoId())
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra alumno con ID: " + dto.getAlumnoId()));
      Asignatura asignatura = asignaturaRepository.findById(dto.getAsignaturaId())
            .orElseThrow(
                  () -> new ResourceNotFoundException("No se encuentra asignatura con ID: " + dto.getAsignaturaId()));

      // verificar si ya existe una matrícula activa
      if (matriculaRepository.existsByAlumnoIdAndAsignaturaIdAndEstado(dto.getAlumnoId(), dto.getAsignaturaId(),
            EstadoMatricula.activa)) {
         throw new IllegalArgumentException("El alumno ya está matriculado en esa asignatura, con estado activo");
      }

      Matricula matricula = new Matricula();
      matricula.setAlumno(alumno);
      matricula.setAsignatura(asignatura);
      matricula.setEstado(EstadoMatricula.activa);
      matricula.setFechaMatricula(LocalDateTime.now());

      matricula = matriculaRepository.save(matricula);

      return mapToResponse(matricula);
   }

   public List<MatriculaResponseDTO> listarPorAlumno(Long alumnoId) {
      if (!alumnoRepository.existsById(alumnoId)) {
         throw new ResourceNotFoundException("No se encuentra alumno con ID: " + alumnoId);
      }

      return matriculaRepository.findByAlumnoId(alumnoId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
   }

   public List<MatriculaResponseDTO> listarPorAsignatura(Long asignaturaId) {
      if (!asignaturaRepository.existsById(asignaturaId)) {
         throw new ResourceNotFoundException("No se encuentra asignatura con ID: " + asignaturaId);
      }

      return matriculaRepository.findByAsignaturaId(asignaturaId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
   }


   @Transactional
   public void cancelarMatricula(Long alumnoId, Long asignaturaId) {
      Matricula matricula = matriculaRepository.findById(new MatriculaId(alumnoId, asignaturaId))
         .orElseThrow(() -> new ResourceNotFoundException("No se encuentra matricula con ID: " + alumnoId + " - " + asignaturaId));
      
      if (matricula.getEstado() == EstadoMatricula.activa) {
         matricula.setEstado(EstadoMatricula.cancelada);
         matriculaRepository.save(matricula);
      } else {
         throw new IllegalArgumentException("Sólo se pueden cancelar matrículas activas.");
      }
   }


   private MatriculaResponseDTO mapToResponse(Matricula matricula) {
      MatriculaResponseDTO dto = new MatriculaResponseDTO();
      dto.setAlumnoId(matricula.getAlumno().getId());
      dto.setAlumnoNombre(matricula.getAlumno().getPersona().getNombre());
      dto.setAsignaturaId(matricula.getAsignatura().getId());
      dto.setAsignaturaNombre(matricula.getAsignatura().getNombre());
      dto.setFechaMatricula(matricula.getFechaMatricula());
      dto.setEstado(matricula.getEstado().name());

      return dto;
   }

}
