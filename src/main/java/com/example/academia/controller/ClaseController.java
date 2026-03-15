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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.academia.dto.ClaseRequestDTO;
import com.example.academia.dto.ClaseResponseDTO;
import com.example.academia.service.ClaseService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(name = "09. Clases", description = "Gestión de Clases (admin)")
@RestController
@RequestMapping("/api/admin/clases")
@PreAuthorize("hasRole('admin')")
public class ClaseController {

   private final ClaseService claseService;

   public ClaseController(ClaseService claseService) {
      this.claseService = claseService;
   }

   @GetMapping
   public ResponseEntity<List<ClaseResponseDTO>> listar(
         @RequestParam(required = false) Long asignaturaId,
         @RequestParam(required = false) Long profesorId,
         @RequestParam(required = false) Long aulaId,
         @RequestParam(required = false) Integer diaSemana) {
      return ResponseEntity.ok(claseService.listarConFiltros(asignaturaId, aulaId, profesorId, diaSemana));
   }

   @GetMapping("/{id}")
   public ResponseEntity<ClaseResponseDTO> obtenenerPorId(@PathVariable Long id) {
      return ResponseEntity.ok(claseService.obtenerPorId(id));
   }

   @PostMapping
   public ResponseEntity<ClaseResponseDTO> crear(@Valid @RequestBody ClaseRequestDTO dto) {
      ClaseResponseDTO response = claseService.crearClase(dto);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
   }

   @PutMapping("/{id}")
   public ResponseEntity<ClaseResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ClaseRequestDTO dto) {
      ClaseResponseDTO response = claseService.actualizarClase(id, dto);
      return ResponseEntity.ok(response);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> eliminar(@PathVariable Long id) {
      claseService.eliminarClase(id);
      return ResponseEntity.noContent().build();
   }


}
