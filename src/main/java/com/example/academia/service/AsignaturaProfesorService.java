package com.example.academia.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.academia.dto.AsignaturaProfesorRequestDTO;
import com.example.academia.dto.AsignaturaProfesorResponseDTO;
import com.example.academia.exception.ResourceNotFoundException;
import com.example.academia.model.Asignatura;
import com.example.academia.model.AsignaturaProfesor;
import com.example.academia.model.AsignaturaProfesorId;
import com.example.academia.model.Profesor;
import com.example.academia.repository.AsignaturaProfesorRepository;
import com.example.academia.repository.AsignaturaRepository;
import com.example.academia.repository.ProfesorRepository;

@Service
public class AsignaturaProfesorService {

   private AsignaturaProfesorRepository asignaturaProfesorRepository;
   private AsignaturaRepository asignaturaRepository;
   private ProfesorRepository profesorRepository;

   public AsignaturaProfesorService(AsignaturaProfesorRepository asignaturaProfesorRepository,
         AsignaturaRepository asignaturaRepository, ProfesorRepository profesorRepository) {
      this.asignaturaProfesorRepository = asignaturaProfesorRepository;
      this.asignaturaRepository = asignaturaRepository;
      this.profesorRepository = profesorRepository;
   }

   @Transactional
   public AsignaturaProfesorResponseDTO asignar(AsignaturaProfesorRequestDTO dto) {
      Profesor profesor = profesorRepository.findById(dto.getProfesorId())
            .orElseThrow(
                  () -> new ResourceNotFoundException("No se encuentra profesor con ID: " + dto.getProfesorId()));

      Asignatura asignatura = asignaturaRepository.findById(dto.getAsignaturaId())
            .orElseThrow(
                  () -> new ResourceNotFoundException("No se encuentra asignatura con ID: " + dto.getAsignaturaId()));

      // comprobar que no esté ya asignada
      if (asignaturaProfesorRepository.existsByProfesorIdAndAsignaturaId(dto.getProfesorId(), dto.getAsignaturaId())) {
         throw new IllegalArgumentException("El profesor ya tiene esa asignatura asignada");
      }

      AsignaturaProfesor asignacion = new AsignaturaProfesor();
      asignacion.setId(new AsignaturaProfesorId(dto.getProfesorId(), dto.getAsignaturaId()));
      asignacion.setProfesor(profesor);
      asignacion.setAsignatura(asignatura);
      asignacion.setFechaAsignacion(LocalDateTime.now());

      asignacion = asignaturaProfesorRepository.save(asignacion);

      return mapToResponse(asignacion);

   }

   public List<AsignaturaProfesorResponseDTO> listarPorProfesor(Long profesorId) {
      if (!profesorRepository.existsById(profesorId)) {
         throw new ResourceNotFoundException("No se encuentra profesor con ID: " + profesorId);
      }
      return asignaturaProfesorRepository.findByProfesorId(profesorId).stream()
         .map(this::mapToResponse)
         .collect(Collectors.toList());
   }

   public List<AsignaturaProfesorResponseDTO> listarPorAsignatura(Long asignaturaId) {
      if (!profesorRepository.existsById(asignaturaId)) {
         throw new ResourceNotFoundException("No se encuentra asignatura con ID: " + asignaturaId);
      }
      return asignaturaProfesorRepository.findByAsignaturaId(asignaturaId).stream()
         .map(this::mapToResponse)
         .collect(Collectors.toList());
   }   

   @Transactional
   public void eliminarAsignacion(Long profesorId, Long asignaturaId) {
      if (!asignaturaProfesorRepository.existsByProfesorIdAndAsignaturaId(profesorId, asignaturaId)) {
         throw new ResourceNotFoundException("No existe asignacion para profesor " + profesorId + " y asignatura " + asignaturaId);
      }
      asignaturaProfesorRepository.deleteByProfesorIdAndAsignaturaId(profesorId, asignaturaId);
   }

   private AsignaturaProfesorResponseDTO mapToResponse(AsignaturaProfesor asignacion) {
      AsignaturaProfesorResponseDTO dto = new AsignaturaProfesorResponseDTO();
      dto.setProfesorId(asignacion.getProfesor().getId());
      dto.setProfesorNombre(asignacion.getProfesor().getPersona().getNombre());
      dto.setAsignaturaId(asignacion.getAsignatura().getId());
      dto.setAsignaturaNombre(asignacion.getAsignatura().getNombre());
      dto.setFechaAsignacion(asignacion.getFechaAsignacion());
      return dto;
   }

}
