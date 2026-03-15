package com.example.academia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.academia.dto.PerfilResponseDTO;
import com.example.academia.dto.PerfilUpdateDTO;
import com.example.academia.security.UserDetailsImpl;
import com.example.academia.service.PerfilService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/perfil")
public class PerfilController {
   private final PerfilService perfilService;

   public PerfilController(PerfilService perfilService) {
      this.perfilService = perfilService;
   }

   @GetMapping
   public ResponseEntity<PerfilResponseDTO> obtenerMiPerfil(@AuthenticationPrincipal UserDetailsImpl user) {
      PerfilResponseDTO response = perfilService.obtenerPerfil(user.getId(), user.getId(), false);
      return ResponseEntity.ok(response);
   }

   @PutMapping
   public ResponseEntity<PerfilResponseDTO> actualizarMiPerfil(
         @AuthenticationPrincipal UserDetailsImpl user,
         @Valid @RequestBody PerfilUpdateDTO dto) {
      PerfilResponseDTO response = perfilService.actualizarPerfil(user.getId(), user.getId(), false, dto);
      return ResponseEntity.ok(response);
   }

}
