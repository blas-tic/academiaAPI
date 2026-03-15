package com.example.academia.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.academia.dto.ClaseResponseDTO;
import com.example.academia.security.UserDetailsImpl;
import com.example.academia.service.CalendarioService;

import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "08. Calendarios 3", description = "Gestión de Calendarios por el Profesor")
@RestController
@RequestMapping("/api/profesores/calendario")
@PreAuthorize("hasRole('profesor')")
public class CalendarioProfesorController {

   private CalendarioService calendarioService;

   public CalendarioProfesorController(CalendarioService calendarioService) {
      this.calendarioService = calendarioService;
   }

   @GetMapping
   public ResponseEntity<List<ClaseResponseDTO>> verCalendarioProfesor(
         @AuthenticationPrincipal UserDetailsImpl user,
         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
      // por defecto, si no se especifican fechas, se toma la semana actual
      if (fechaInicio == null) fechaInicio = LocalDate.now();
      if (fechaFin == null) fechaFin = fechaInicio.plusWeeks(1);

      List<ClaseResponseDTO> clases = calendarioService.calendarioProfesor(user.getId(), fechaInicio, fechaFin);
      return ResponseEntity.ok(clases);
   }

}
