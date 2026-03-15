package com.example.academia.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.academia.dto.RegistroRequestDTO;
import com.example.academia.dto.UsuarioResponseDTO;
import com.example.academia.service.RegistroService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "02. Autenticación", description = "Registro")
@RestController
@RequestMapping("/api/auth")
public class RegistroController {
   private final RegistroService registroService;

   public RegistroController(RegistroService registroService) {
      this.registroService = registroService;
   }

   @PostMapping("/registro")
   public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody RegistroRequestDTO dto) {
      UsuarioResponseDTO response = registroService.registrar(dto);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
   }
}
