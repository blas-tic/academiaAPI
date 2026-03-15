package com.example.academia.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.academia.dto.ClaseResponseDTO;
import com.example.academia.service.CalendarioService;

@RestController
@RequestMapping("/api/admin/calendario")
@PreAuthorize("hasRole('admin')")
public class CalendarioAdminController {
   private final CalendarioService calendarioService;

   public CalendarioAdminController(CalendarioService calendarioService) {
      this.calendarioService = calendarioService;
   }

   @GetMapping
   public ResponseEntity<List<ClaseResponseDTO>> verCalendarioAdmin(
      @RequestParam(required = false) Long asignaturaId,
      @RequestParam(required = false) Long profesorId,
      @RequestParam(required = false) Long aulaId,
      @RequestParam(required = false) Integer diaSemana,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
   ) {
      if (fechaInicio == null)  fechaInicio = LocalDate.now();
      if (fechaFin == null) fechaFin = fechaInicio.plusWeeks(1);

      List<ClaseResponseDTO> clases = calendarioService.calendarioAdmin(asignaturaId, profesorId, aulaId, diaSemana, fechaInicio, fechaFin);
      
      return ResponseEntity.ok(clases);
   }

}
