package com.example.academia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.academia.dto.UsuarioModificacionDTO;
import com.example.academia.dto.UsuarioRegistroDTO;
import com.example.academia.dto.UsuarioResponseDTO;
import com.example.academia.service.UsuarioService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Tag(name = "03. Usuarios", description = "Operaciones relacionadas con Usuarios (admin)")
@RestController
@RequestMapping("/api/admin/usuarios")
@PreAuthorize("hasRole('admin')")
public class AdminController {

   private final UsuarioService usuarioService;

   public AdminController(UsuarioService usuarioService) {
      this.usuarioService = usuarioService;
   }

   @GetMapping
   public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
      List<UsuarioResponseDTO> usuarios = usuarioService.listarUsuarios();
      return ResponseEntity.ok(usuarios);
   }

   @GetMapping("/{id}")
   public ResponseEntity<UsuarioResponseDTO> obtenerUsuario(@PathVariable Long id) {
      return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
   }

   @PostMapping
   public ResponseEntity<?> registrarUsuario(@Valid @RequestBody UsuarioRegistroDTO dto) {
      try {
         UsuarioResponseDTO usuario = usuarioService.registrarUsuario(dto);

         return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado con ID: " + usuario.getId());
      } catch (RuntimeException e) {
         return ResponseEntity.badRequest().body(e.getLocalizedMessage());
      }
   }

   @PutMapping("/{id}")
   public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
         @PathVariable Long id,
         @Valid @RequestBody UsuarioModificacionDTO dto) {
      UsuarioResponseDTO response = usuarioService.actualizarUsuario(id, dto);
      return ResponseEntity.ok(response);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> desactivarUsuario(@PathVariable Long id) {
      usuarioService.desactivarUsuario(id);
      return ResponseEntity.noContent().build();
   }

   // Endpoint para cambio de contraseña (si se quiere separado)
   @PatchMapping("/{id}/password")
   public ResponseEntity<Void> cambiarPassword(
         @PathVariable Long id,
         @RequestBody @NotBlank String nuevaPassword) {
      // Implementar si se desea
      return ResponseEntity.ok().build();
   }

}
