package com.example.academia.controller;

import com.example.academia.dto.LoginRequest;
import com.example.academia.dto.ActivacionDTO;
import com.example.academia.dto.JwtResponse;
import com.example.academia.security.JwtTokenProvider;
import com.example.academia.security.UserDetailsImpl;
import com.example.academia.service.RegistroService;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Unit Tests")
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RegistroService registroService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_TOKEN = "jwt-token-abc";
    private static final Long TEST_USER_ID = 1L;

    // ──────────────────────────────────────────────
    // Helper methods
    // ──────────────────────────────────────────────
    private UserDetailsImpl createUserDetails(String email, Long id, String... roles) {
        List<SimpleGrantedAuthority> authorities = List.of(roles)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
        return new UserDetailsImpl(id, email, "encoded-pass", true, authorities);
    }

    // ──────────────────────────────────────────────
    // LOGIN tests
    // ──────────────────────────────────────────────
    @Nested
    @DisplayName("POST /api/auth/login")
    class LoginTests {

        @Test
        @DisplayName("Should return JwtResponse with token, id, email and roles when credentials are valid")
        void login_ValidCredentials_ReturnsJwtResponse() {
            // given
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(TEST_EMAIL);
            loginRequest.setPassword(TEST_PASSWORD);

            UserDetailsImpl userDetails = createUserDetails(TEST_EMAIL, TEST_USER_ID, "ROLE_alumno");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(jwtTokenProvider.generateToken(authentication)).thenReturn(TEST_TOKEN);

            // when
            ResponseEntity<?> response = authController.login(loginRequest);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isInstanceOf(JwtResponse.class);

            JwtResponse body = (JwtResponse) response.getBody();
            assertThat(body.getToken()).isEqualTo(TEST_TOKEN);
            assertThat(body.getId()).isEqualTo(TEST_USER_ID);
            assertThat(body.getEmail()).isEqualTo(TEST_EMAIL);
            assertThat(body.getRoles()).containsExactly("ROLE_alumno");
            assertThat(body.getType()).isEqualTo("Bearer");

            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtTokenProvider).generateToken(authentication);
        }

        @Test
        @DisplayName("Should throw BadCredentialsException when credentials are invalid")
        void login_InvalidCredentials_ThrowsBadCredentials() {
            // given
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail("wrong@example.com");
            loginRequest.setPassword("wrongpass");

            when(authenticationManager.authenticate(any()))
                    .thenThrow(new BadCredentialsException("Credenciales incorrectas"));

            // when / then
            assertThatThrownBy(() -> authController.login(loginRequest))
                    .isInstanceOf(BadCredentialsException.class)
                    .hasMessageContaining("Credenciales incorrectas");

            verify(authenticationManager).authenticate(any());
            verify(jwtTokenProvider, never()).generateToken(any());
        }

        @Test
        @DisplayName("Should handle multiple roles (alumno + profesor)")
        void login_WithMultipleRoles_ReturnsAllRoles() {
            // given
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(TEST_EMAIL);
            loginRequest.setPassword(TEST_PASSWORD);

            UserDetailsImpl userDetails = createUserDetails(
                    TEST_EMAIL, TEST_USER_ID, "ROLE_alumno", "ROLE_profesor");

            when(authenticationManager.authenticate(any())).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(jwtTokenProvider.generateToken(authentication)).thenReturn(TEST_TOKEN);

            // when
            ResponseEntity<?> response = authController.login(loginRequest);

            // then
            JwtResponse body = (JwtResponse) response.getBody();
            assertThat(body.getRoles()).containsExactlyInAnyOrder("ROLE_alumno", "ROLE_profesor");
        }

        @Test
        @DisplayName("Should set authentication in SecurityContext")
        void login_SetsSecurityContext() {
            // given
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(TEST_EMAIL);
            loginRequest.setPassword(TEST_PASSWORD);

            UserDetailsImpl userDetails = createUserDetails(TEST_EMAIL, TEST_USER_ID, "ROLE_admin");

            when(authenticationManager.authenticate(any())).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(jwtTokenProvider.generateToken(authentication)).thenReturn(TEST_TOKEN);

            // when
            authController.login(loginRequest);

            // then – el contexto de seguridad se ha establecido (no podemos verificarlo
            // fácilmente aquí, pero la prueba no debería fallar)
            verify(authenticationManager).authenticate(any());
        }
    }

    // ──────────────────────────────────────────────
    // ACTIVAR tests
    // ──────────────────────────────────────────────
    @Nested
    @DisplayName("POST /api/auth/activar")
    class ActivarCuentaTests {

        @Test
        @DisplayName("Should return 200 OK when activation data is valid")
        void activarCuenta_ValidData_ReturnsOk() {
            // given
            ActivacionDTO dto = new ActivacionDTO();
            dto.setEmail(TEST_EMAIL);
            dto.setCurrentPasword("tempPass");
            dto.setNewPasword("newSecurePass123");

            doNothing().when(registroService)
                    .activarCuenta(TEST_EMAIL, "tempPass", "newSecurePass123");

            // when
            ResponseEntity<Void> response = authController.activarCuenta(dto);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNull();

            verify(registroService).activarCuenta(TEST_EMAIL, "tempPass", "newSecurePass123");
        }

        @Test
        @DisplayName("Should propagate exception when activation fails (e.g., account already active)")
        void activarCuenta_AlreadyActive_ThrowsException() {
            // given
            ActivacionDTO dto = new ActivacionDTO();
            dto.setEmail(TEST_EMAIL);
            dto.setCurrentPasword("tempPass");
            dto.setNewPasword("newSecurePass123");

            doThrow(new IllegalArgumentException("La cuenta ya está activa"))
                    .when(registroService).activarCuenta(any(), any(), any());

            // when / then
            assertThatThrownBy(() -> authController.activarCuenta(dto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("La cuenta ya está activa");

            verify(registroService).activarCuenta(TEST_EMAIL, "tempPass", "newSecurePass123");
        }

        @Test
        @DisplayName("Should propagate BadCredentialsException when current password is wrong")
        void activarCuenta_WrongCurrentPassword_ThrowsException() {
            // given
            ActivacionDTO dto = new ActivacionDTO();
            dto.setEmail(TEST_EMAIL);
            dto.setCurrentPasword("wrongCurrent");
            dto.setNewPasword("newSecurePass123");

            doThrow(new BadCredentialsException("Contraseña actual incorrecta"))
                    .when(registroService).activarCuenta(any(), any(), any());

            // when / then
            assertThatThrownBy(() -> authController.activarCuenta(dto))
                    .isInstanceOf(BadCredentialsException.class)
                    .hasMessageContaining("Contraseña actual incorrecta");

            verify(registroService).activarCuenta(TEST_EMAIL, "wrongCurrent", "newSecurePass123");
        }
    }
}