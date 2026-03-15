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

import com.example.academia.dto.AulaRequestDTO;
import com.example.academia.dto.AulaResponseDTO;
import com.example.academia.service.AulaService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "07. Aulas", description = "Gestión de Aulas (admin)")
@RestController
@RequestMapping("/api/admin/aulas")
@PreAuthorize("hasRole('admin')")   
public class AulaController {

   private final AulaService service;

   public AulaController(AulaService service) {
      this.service = service;
   }

   @GetMapping
   public ResponseEntity<List<AulaResponseDTO>> listarTodas() {
      return ResponseEntity.ok(service.listarTodas());
   }

   @GetMapping("/{id}")
   public ResponseEntity<AulaResponseDTO> obtenerPorId(@PathVariable Long id) {
      return ResponseEntity.ok(service.obtenerPorId(id));
   }

   @PostMapping
   public ResponseEntity<AulaResponseDTO> crear(@Valid @RequestBody AulaRequestDTO dto) {
      AulaResponseDTO response = service.crearAula(dto);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
   }

   @PutMapping("/{id}")
   public ResponseEntity<AulaResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody AulaRequestDTO dto) {
      AulaResponseDTO response = service.actualizarAula(id, dto);
      return ResponseEntity.ok(response);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> eliminar(@PathVariable Long id) {
      service.eliminarAula(id);
      return ResponseEntity.noContent().build();
   }

}
