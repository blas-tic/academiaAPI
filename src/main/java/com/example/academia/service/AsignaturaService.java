package com.example.academia.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.academia.dto.AsignaturaRequestDTO;
import com.example.academia.dto.AsignaturaResponseDTO;
import com.example.academia.exception.ResourceNotFoundException;
import com.example.academia.model.Asignatura;
import com.example.academia.repository.AsignaturaRepository;

@Service
public class AsignaturaService {

   private final AsignaturaRepository asignaturaRepository;

   public AsignaturaService(AsignaturaRepository asignaturaRepository) {
      this.asignaturaRepository = asignaturaRepository;
   }

   public List<AsignaturaResponseDTO> listarTodas() {
      return asignaturaRepository.findAll().stream()
         .map(this::mapToResponse)
         .collect(Collectors.toList());
   }

   public AsignaturaResponseDTO obtenerPorId(Long id) {
      Asignatura asignatura = asignaturaRepository.findById(id)
         .orElseThrow(() -> new ResourceNotFoundException("Asignatura con ID: " + id + " no existe."));
      return mapToResponse(asignatura);
   }

   @Transactional
   public AsignaturaResponseDTO crearAsignatura(AsignaturaRequestDTO dto) {
      // validar que no exista una con el mismo nombre
      if (asignaturaRepository.existsByNombre(dto.getNombre())) {
         throw new IllegalArgumentException("Ya existe una asignatura de nombre " + dto.getNombre());
      }

      Asignatura asignatura = new Asignatura();
      asignatura.setNombre(dto.getNombre());
      asignatura.setDescripcion(dto.getDescripcion());
      asignatura.setColor(dto.getColor());

      asignatura = asignaturaRepository.save(asignatura);

      return mapToResponse(asignatura);
   }

   @Transactional
   public AsignaturaResponseDTO actualizarAsignatura(Long id, AsignaturaRequestDTO dto) {
      Asignatura asignatura = asignaturaRepository.findById(id)
         .orElseThrow(() -> new ResourceNotFoundException("No se encuentra asignatura con ID: " + id));
      
      if (!asignatura.getNombre().equals(dto.getNombre()) && asignaturaRepository.existsByNombre(dto.getNombre())) {
         throw new IllegalArgumentException("Ya existe otra asignatura con el nombre: " + dto.getNombre());
      }
      asignatura.setNombre(dto.getNombre());
      asignatura.setDescripcion(dto.getDescripcion());
      asignatura.setColor(dto.getColor());

      asignatura = asignaturaRepository.save(asignatura);

      return mapToResponse(asignatura);
   }

   @Transactional
   public void eliminarAsignatura(Long id) {
     Asignatura asignatura = asignaturaRepository.findById(id)
         .orElseThrow(() -> new ResourceNotFoundException("No se encuentra asignatura con ID: " + id));
      // aquí hay que validar que no tenga clases o matrículas asociadas. Si las tiene, no se puede eliminar

      asignaturaRepository.delete(asignatura);
   }


   private AsignaturaResponseDTO mapToResponse(Asignatura asignatura) {
      AsignaturaResponseDTO dto = new AsignaturaResponseDTO();
      dto.setId(asignatura.getId());
      dto.setNombre(asignatura.getNombre());
      dto.setDescripcion(asignatura.getDescripcion());
      dto.setColor(asignatura.getColor());
      return dto;
   }
}
