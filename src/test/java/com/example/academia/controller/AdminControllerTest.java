package com.example.academia.controller;

import com.example.academia.dto.UsuarioModificacionDTO;
import com.example.academia.dto.UsuarioRegistroDTO;
import com.example.academia.dto.UsuarioResponseDTO;
import com.example.academia.exception.EmailAlreadyExistsException;
import com.example.academia.exception.ResourceNotFoundException;
import com.example.academia.model.RolNombre;
import com.example.academia.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdminController Unit Tests")
class AdminControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private AdminController adminController;

    private UsuarioResponseDTO usuarioResponse;
    private UsuarioRegistroDTO usuarioRegistroDTO;
    private UsuarioModificacionDTO usuarioModificacionDTO;

    @BeforeEach
    void setUp() {
        usuarioResponse = new UsuarioResponseDTO();
        usuarioResponse.setId(1L);
        usuarioResponse.setEmail("test@example.com");
        usuarioResponse.setNombre("Juan Pérez");
        usuarioResponse.setTelefono("+34 600 000 000");
        usuarioResponse.setDireccion("Calle Falsa 123");
        usuarioResponse.setFechaAlta(LocalDateTime.now());
        usuarioResponse.setActivo(true);
        usuarioResponse.setRoles(Set.of("alumno"));

        usuarioRegistroDTO = new UsuarioRegistroDTO();
        usuarioRegistroDTO.setEmail("nuevo@example.com");
        usuarioRegistroDTO.setNombre("Nuevo Usuario");
        usuarioRegistroDTO.setPassword("securePass123");
        usuarioRegistroDTO.setRoles(Set.of(RolNombre.alumno));
        usuarioRegistroDTO.setTutor("María García");

        usuarioModificacionDTO = new UsuarioModificacionDTO();
        usuarioModificacionDTO.setNombre("Juan P. Modificado");
        usuarioModificacionDTO.setTelefono("+34 611 111 111");
    }

    // ──────────────────────────────────────────────
    // GET /api/admin/usuarios
    // ──────────────────────────────────────────────
    @Nested
    @DisplayName("GET /api/admin/usuarios")
    class ListarUsuariosTests {

        @Test
        @DisplayName("Should return 200 OK with a list of users")
        void listarUsuarios_ReturnsUserList() {
            // given
            UsuarioResponseDTO user2 = new UsuarioResponseDTO();
            user2.setId(2L);
            user2.setEmail("user2@example.com");
            user2.setNombre("María López");
            user2.setActivo(true);
            user2.setRoles(Set.of("profesor"));

            List<UsuarioResponseDTO> expected = List.of(usuarioResponse, user2);
            when(usuarioService.listarUsuarios()).thenReturn(expected);

            // when
            ResponseEntity<List<UsuarioResponseDTO>> response = adminController.listarUsuarios();

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(2);
            assertThat(response.getBody().get(0).getEmail()).isEqualTo("test@example.com");
            assertThat(response.getBody().get(1).getEmail()).isEqualTo("user2@example.com");

            verify(usuarioService).listarUsuarios();
        }

        @Test
        @DisplayName("Should return empty list when there are no users")
        void listarUsuarios_NoUsers_ReturnsEmptyList() {
            // given
            when(usuarioService.listarUsuarios()).thenReturn(List.of());

            // when
            ResponseEntity<List<UsuarioResponseDTO>> response = adminController.listarUsuarios();

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEmpty();

            verify(usuarioService).listarUsuarios();
        }
    }

    // ──────────────────────────────────────────────
    // GET /api/admin/usuarios/{id}
    // ──────────────────────────────────────────────
    @Nested
    @DisplayName("GET /api/admin/usuarios/{id}")
    class ObtenerUsuarioTests {

        @Test
        @DisplayName("Should return 200 OK with user data when user exists")
        void obtenerUsuario_ExistingId_ReturnsUser() {
            // given
            Long userId = 1L;
            when(usuarioService.obtenerUsuarioPorId(userId)).thenReturn(usuarioResponse);

            // when
            ResponseEntity<UsuarioResponseDTO> response = adminController.obtenerUsuario(userId);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getId()).isEqualTo(1L);
            assertThat(response.getBody().getEmail()).isEqualTo("test@example.com");

            verify(usuarioService).obtenerUsuarioPorId(userId);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when user does not exist")
        void obtenerUsuario_NonExistingId_ThrowsException() {
            // given
            Long nonExistingId = 999L;
            when(usuarioService.obtenerUsuarioPorId(nonExistingId))
                    .thenThrow(new ResourceNotFoundException("Usuario no encontrado con id: " + nonExistingId));

            // when / then
            assertThatThrownBy(() -> adminController.obtenerUsuario(nonExistingId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Usuario no encontrado con id: " + nonExistingId);

            verify(usuarioService).obtenerUsuarioPorId(nonExistingId);
        }
    }

    // ──────────────────────────────────────────────
    // POST /api/admin/usuarios
    // ──────────────────────────────────────────────
    @Nested
    @DisplayName("POST /api/admin/usuarios")
    class RegistrarUsuarioTests {

        @Test
        @DisplayName("Should return 201 Created with success message when registration succeeds")
        void registrarUsuario_ValidData_ReturnsCreated() {
            // given
            UsuarioResponseDTO created = new UsuarioResponseDTO();
            created.setId(3L);
            created.setEmail("nuevo@example.com");
            created.setRoles(Set.of("alumno"));

            when(usuarioService.registrarUsuario(any(UsuarioRegistroDTO.class))).thenReturn(created);

            // when
            ResponseEntity<?> response = adminController.registrarUsuario(usuarioRegistroDTO);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isEqualTo("Usuario creado con ID: 3");

            verify(usuarioService).registrarUsuario(any(UsuarioRegistroDTO.class));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when email already exists")
        void registrarUsuario_DuplicateEmail_ReturnsBadRequest() {
            // given
            when(usuarioService.registrarUsuario(any(UsuarioRegistroDTO.class)))
                    .thenThrow(new EmailAlreadyExistsException("Email ya registrado"));

            // when
            ResponseEntity<?> response = adminController.registrarUsuario(usuarioRegistroDTO);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).asString().contains("Email ya registrado");

            verify(usuarioService).registrarUsuario(any(UsuarioRegistroDTO.class));
        }

        @Test
        @DisplayName("Should return 400 Bad Request with generic Runtime error message")
        void registrarUsuario_RuntimeException_ReturnsBadRequest() {
            // given
            when(usuarioService.registrarUsuario(any(UsuarioRegistroDTO.class)))
                    .thenThrow(new RuntimeException("Error de validación interno"));

            // when
            ResponseEntity<?> response = adminController.registrarUsuario(usuarioRegistroDTO);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).asString().contains("Error de validación interno");
        }
    }

    // ──────────────────────────────────────────────
    // PUT /api/admin/usuarios/{id}
    // ──────────────────────────────────────────────
    @Nested
    @DisplayName("PUT /api/admin/usuarios/{id}")
    class ActualizarUsuarioTests {

        @Test
        @DisplayName("Should return 200 OK with updated user data")
        void actualizarUsuario_ValidData_ReturnsUpdatedUser() {
            // given
            Long userId = 1L;
            UsuarioResponseDTO updated = new UsuarioResponseDTO();
            updated.setId(userId);
            updated.setEmail("test@example.com");
            updated.setNombre("Juan P. Modificado");
            updated.setTelefono("+34 611 111 111");
            updated.setActivo(true);
            updated.setRoles(Set.of("alumno"));

            when(usuarioService.actualizarUsuario(eq(userId), any(UsuarioModificacionDTO.class)))
                    .thenReturn(updated);

            // when
            ResponseEntity<UsuarioResponseDTO> response = adminController.actualizarUsuario(
                    userId, usuarioModificacionDTO);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getNombre()).isEqualTo("Juan P. Modificado");
            assertThat(response.getBody().getTelefono()).isEqualTo("+34 611 111 111");

            verify(usuarioService).actualizarUsuario(eq(userId), any(UsuarioModificacionDTO.class));
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when user to update does not exist")
        void actualizarUsuario_NonExistingId_ThrowsException() {
            // given
            Long nonExistingId = 999L;
            when(usuarioService.actualizarUsuario(eq(nonExistingId), any(UsuarioModificacionDTO.class)))
                    .thenThrow(new ResourceNotFoundException("Usuario no encontrado con id: " + nonExistingId));

            // when / then
            assertThatThrownBy(() -> adminController.actualizarUsuario(nonExistingId, usuarioModificacionDTO))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Usuario no encontrado con id: " + nonExistingId);

            verify(usuarioService).actualizarUsuario(eq(nonExistingId), any(UsuarioModificacionDTO.class));
        }
    }

    // ──────────────────────────────────────────────
    // DELETE /api/admin/usuarios/{id}
    // ──────────────────────────────────────────────
    @Nested
    @DisplayName("DELETE /api/admin/usuarios/{id}")
    class DesactivarUsuarioTests {

        @Test
        @DisplayName("Should return 204 No Content when deactivation succeeds")
        void desactivarUsuario_ExistingId_ReturnsNoContent() {
            // given
            Long userId = 1L;
            doNothing().when(usuarioService).desactivarUsuario(userId);

            // when
            ResponseEntity<Void> response = adminController.desactivarUsuario(userId);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(response.getBody()).isNull();

            verify(usuarioService).desactivarUsuario(userId);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when user to deactivate does not exist")
        void desactivarUsuario_NonExistingId_ThrowsException() {
            // given
            Long nonExistingId = 999L;
            doThrow(new ResourceNotFoundException("Usuario no encontrado con id: " + nonExistingId))
                    .when(usuarioService).desactivarUsuario(nonExistingId);

            // when / then
            assertThatThrownBy(() -> adminController.desactivarUsuario(nonExistingId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Usuario no encontrado con id: " + nonExistingId);

            verify(usuarioService).desactivarUsuario(nonExistingId);
        }
    }

    // ──────────────────────────────────────────────
    // PATCH /api/admin/usuarios/{id}/password
    // ──────────────────────────────────────────────
    @Nested
    @DisplayName("PATCH /api/admin/usuarios/{id}/password")
    class CambiarPasswordTests {

        @Test
        @DisplayName("Should return 200 OK when password change is requested (stub)")
        void cambiarPassword_ReturnsOk() {
            // given
            Long userId = 1L;
            String newPassword = "newSecurePassword123";

            // when
            ResponseEntity<Void> response = adminController.cambiarPassword(userId, newPassword);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNull();
            // NOTE: This endpoint is a stub and does not call any service yet.
        }
    }
}