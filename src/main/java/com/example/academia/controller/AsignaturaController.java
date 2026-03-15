package com.example.academia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.academia.dto.AsignaturaRequestDTO;
import com.example.academia.dto.AsignaturaResponseDTO;
import com.example.academia.service.AsignaturaService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "05. Asignaturas", description = "Gestión de asignaturas (admin)")
@RestController
@RequestMapping("/api/admin/asignaturas")
@PreAuthorize("hasRole('admin')")
public class AsignaturaController {

   private final AsignaturaService asignaturaService;

   public AsignaturaController(AsignaturaService asignaturaService) {
      this.asignaturaService = asignaturaService;
   }

   @GetMapping
   public ResponseEntity<List<AsignaturaResponseDTO>> listarTodas() {
      return ResponseEntity.ok(asignaturaService.listarTodas());
   }

   @GetMapping("/{id}")
   public ResponseEntity<AsignaturaResponseDTO> obtenerPorId(@PathVariable Long id) {
      return ResponseEntity.ok(asignaturaService.obtenerPorId(id));
   }

   @PostMapping
   public ResponseEntity<AsignaturaResponseDTO> crear(@Valid @RequestBody AsignaturaRequestDTO dto) {
      AsignaturaResponseDTO response = asignaturaService.crearAsignatura(dto);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
   }

   @PutMapping("/{id}")
   public ResponseEntity<AsignaturaResponseDTO> actualizar(
         @PathVariable Long id,
         @Valid @RequestBody AsignaturaRequestDTO dto) {
      AsignaturaResponseDTO response = asignaturaService.actualizarAsignatura(id, dto);
      return ResponseEntity.ok(response);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> eliminar(@PathVariable Long id) {
      asignaturaService.eliminarAsignatura(id);
      return ResponseEntity.noContent().build();
   }

}
