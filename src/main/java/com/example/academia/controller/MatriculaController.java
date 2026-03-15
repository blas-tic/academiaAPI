package com.example.academia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.academia.dto.MatriculaRequestDTO;
import com.example.academia.dto.MatriculaResponseDTO;
import com.example.academia.service.MatriculaService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(name = "10. Matriculas", description = "Gestión de Matrículas de Alumnos (admin)")
@RestController
@RequestMapping("/api/admin/matriculas")
@PreAuthorize("hasRole('admin')")
public class MatriculaController {

   private final MatriculaService matriculaService;

   public MatriculaController(MatriculaService matriculaService) {
      this.matriculaService = matriculaService;
   }

   @PostMapping
   public ResponseEntity<MatriculaResponseDTO> matricular(@Valid @RequestBody MatriculaRequestDTO dto) {
      MatriculaResponseDTO response = matriculaService.matricular(dto);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
   }

   @GetMapping("/alumno/{alumnoId}")
   public ResponseEntity<List<MatriculaResponseDTO>> listarPorAlumno(@PathVariable Long alumnoId) {
      return ResponseEntity.ok(matriculaService.listarPorAlumno(alumnoId));
   }
   @GetMapping("/asignatura/{asignaturaId}")
   public ResponseEntity<List<MatriculaResponseDTO>> listarPorAsignatura(@PathVariable Long asignaturaId) {
      return ResponseEntity.ok(matriculaService.listarPorAsignatura(asignaturaId));
   }

   @PatchMapping("/{alumnoId}/{asignaturaId}/cancelar")
   public ResponseEntity<Void> cancelarMatricula(@PathVariable Long alumnoId, @PathVariable Long asignaturaId) {
      matriculaService.cancelarMatricula(alumnoId, asignaturaId);
      return ResponseEntity.noContent().build();
   }
}
