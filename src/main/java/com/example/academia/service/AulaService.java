package com.example.academia.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.academia.dto.AulaRequestDTO;
import com.example.academia.dto.AulaResponseDTO;
import com.example.academia.exception.ResourceNotFoundException;
import com.example.academia.model.Aula;
import com.example.academia.repository.AulaRepository;

@Service
public class AulaService {

   private final AulaRepository aulaRepository;

   public AulaService(AulaRepository aulaRepository) {
      this.aulaRepository = aulaRepository;
   }

   public List<AulaResponseDTO> listarTodas() {
      return aulaRepository.findAll().stream()
         .map(this::mapToResponse)
         .collect(Collectors.toList());
   }

   public AulaResponseDTO obtenerPorId(Long id) {
      Aula aula = aulaRepository.findById(id)
         .orElseThrow(() -> new ResourceNotFoundException("No se encuentra aula con ID: " + id));
      
      return mapToResponse(aula);
   }

   @Transactional
   public AulaResponseDTO crearAula(AulaRequestDTO dto) {
      if (aulaRepository.existsByNombre(dto.getNombre())) {
         throw new IllegalArgumentException("Ya existe un Aula con ese nombre");
      }
      Aula aula = new Aula();
      aula.setNombre(dto.getNombre());
      aula.setCapacidad(dto.getCapacidad());
      aula.setUbicacion(dto.getUbicacion());

      aula = aulaRepository.save(aula);

      return mapToResponse(aula);
   }

   @Transactional
   public AulaResponseDTO actualizarAula(Long id, AulaRequestDTO dto) {
      Aula aula = aulaRepository.findById(id)
         .orElseThrow(() -> new ResourceNotFoundException("No se encuentra Aula con ID: " + id));

      if (!aula.getNombre().equals(dto.getNombre()) && aulaRepository.existsByNombre(dto.getNombre()) ) {
         throw new IllegalArgumentException("Ya existe un Aula con ese nombre");
      }

      aula.setNombre(dto.getNombre());
      aula.setCapacidad(dto.getCapacidad());
      aula.setUbicacion(dto.getUbicacion());

      aula = aulaRepository.save(aula);

      return mapToResponse(aula);
   }

   @Transactional
   public void eliminarAula(Long id) {
     Aula aula = aulaRepository.findById(id)
         .orElseThrow(() -> new ResourceNotFoundException("No se encuentra Aula con ID: " + id));
        // TODO: Validar que no tenga clases asociadas antes de eliminar (cuando exista la entidad Clase)      

      aulaRepository.delete(aula);
   }

   private AulaResponseDTO mapToResponse(Aula aula) {
      AulaResponseDTO dto = new AulaResponseDTO();
      dto.setCapacidad(aula.getCapacidad());
      dto.setId(aula.getId());
      dto.setNombre(aula.getNombre());
      dto.setUbicacion(aula.getUbicacion());

      return dto;
   }
}
