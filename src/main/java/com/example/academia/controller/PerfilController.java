package com.example.academia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.academia.dto.PasswordChangeDTO;
import com.example.academia.dto.PerfilResponseDTO;
import com.example.academia.dto.PerfilUpdateDTO;
import com.example.academia.security.UserDetailsImpl;
import com.example.academia.service.PerfilService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "11. Perfiles", description = "Gestión de Perfiles de usuarios")
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

   @PostMapping("/cambiar-password")
   public ResponseEntity<Void> cambiarMiPassword(
      @AuthenticationPrincipal UserDetailsImpl user,
      @Valid @RequestBody PasswordChangeDTO dto) {
         perfilService.cambiarMiPassword(user.getId(), dto.getOldPassword(), dto.getNewPassword());
         return ResponseEntity.ok().build();
      }

}
