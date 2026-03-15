package com.example.academia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.academia.dto.AdminPasswordChangeDTO;
import com.example.academia.dto.PerfilResponseDTO;
import com.example.academia.dto.PerfilUpdateDTO;
import com.example.academia.security.UserDetailsImpl;
import com.example.academia.service.PerfilService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "04. Perfiles", description = "Gestión de perfiles de usuarios y cambio de password (admin)")
@RestController
@RequestMapping("/api/admin/perfil")
@PreAuthorize("hasRole('admin')")
public class AdminPerfilController {

   private final PerfilService perfilService;

   public AdminPerfilController(PerfilService perfilService) {
      this.perfilService = perfilService;
   }

   @GetMapping("/{id}")
   public ResponseEntity<PerfilResponseDTO> obtenerPerfil(
         @PathVariable Long id,
         @AuthenticationPrincipal UserDetailsImpl user) {
      PerfilResponseDTO response = perfilService.obtenerPerfil(id, user.getId(), true);
      return ResponseEntity.ok(response);
   }

   @PutMapping("/{id}")
   public ResponseEntity<PerfilResponseDTO> actualizarPerfil(
         @PathVariable Long id,
         @AuthenticationPrincipal UserDetailsImpl user,
         @Valid @RequestBody PerfilUpdateDTO dto) {
      PerfilResponseDTO response = perfilService.actualizarPerfil(id, user.getId(), true, dto);
      return ResponseEntity.ok(response);
   }

   @PostMapping("/{id}/cambiar-password")
   public ResponseEntity<Void> cambiarPasswordUsuario(
      @PathVariable Long id,
      @Valid @RequestBody AdminPasswordChangeDTO dto) {
         perfilService.cambiarPasswordComoAdmin(id, dto.getNewPassword());
         return ResponseEntity.ok().build();
      }
}
