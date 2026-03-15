package com.example.academia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.academia.dto.AsignaturaProfesorRequestDTO;
import com.example.academia.dto.AsignaturaProfesorResponseDTO;
import com.example.academia.service.AsignaturaProfesorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/asignatura-profesor")
@PreAuthorize("hasRole('admin')")
public class AsignaturaProfesorController {

   final AsignaturaProfesorService service;

   public AsignaturaProfesorController(AsignaturaProfesorService service) {
      this.service = service;
   }

   @PostMapping
   public ResponseEntity<AsignaturaProfesorResponseDTO> asignar(@Valid @RequestBody AsignaturaProfesorRequestDTO dto) {
      AsignaturaProfesorResponseDTO response = service.asignar(dto);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
   }

   @GetMapping("/profesor/{profesorId}")
   public ResponseEntity<List<AsignaturaProfesorResponseDTO>> listarPorProfesor(@PathVariable Long profesorId) {
      return ResponseEntity.ok(service.listarPorProfesor(profesorId));
   }

   @GetMapping("/asignatura/{asignaturaId}")
   public ResponseEntity<List<AsignaturaProfesorResponseDTO>> listarPorAsignatura(@PathVariable Long asignaturaId) {
      return ResponseEntity.ok(service.listarPorAsignatura(asignaturaId));
   }

   @DeleteMapping("/profesor/{profesorId}/asignatura/{asignaturaId}")
   public ResponseEntity<Void> eliminarAsignacion(@PathVariable Long profesorId, @PathVariable Long asignaturaId) {
      service.eliminarAsignacion(profesorId, asignaturaId);
      return ResponseEntity.noContent().build();
   }
}
