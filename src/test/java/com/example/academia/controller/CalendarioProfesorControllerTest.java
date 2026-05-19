package com.example.academia.controller;

import com.example.academia.dto.ClaseResponseDTO;
import com.example.academia.security.UserDetailsImpl;
import com.example.academia.service.CalendarioService;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CalendarioProfesorController Unit Tests")
class CalendarioProfesorControllerTest {

    @Mock
    private CalendarioService calendarioService;

    @InjectMocks
    private CalendarioProfesorController controller;

    private UserDetailsImpl profesorDetails;

    @BeforeEach
    void setUp() {
        profesorDetails = new UserDetailsImpl(
                50L,
                "profesor@example.com",
                "encoded-password",
                true,
                List.of(new SimpleGrantedAuthority("ROLE_profesor"))
        );
    }

    // ──────────────────────────────────────────────
    // Helper methods
    // ──────────────────────────────────────────────
    private ClaseResponseDTO createClase(Long id, String asignaturaNombre, String aulaNombre, Integer diaSemana) {
        ClaseResponseDTO dto = new ClaseResponseDTO();
        dto.setId(id);
        dto.setAsignaturaId(100L + id);
        dto.setAsignaturaNombre(asignaturaNombre);
        dto.setProfesorId(profesorDetails.getId());
        dto.setProfesorNombre("Profesor Propio");
        dto.setAulaId(300L + id);
        dto.setAulaNombre(aulaNombre);
        dto.setDiaSemana(diaSemana);
        dto.setHoraInicio(LocalTime.of(8, 0));
        dto.setHoraFin(LocalTime.of(10, 0));
        dto.setFechaInicio(LocalDate.now());
        dto.setFechaFin(LocalDate.now().plusWeeks(1));
        return dto;
    }

    // ──────────────────────────────────────────────
    // GET /api/profesores/calendario tests
    // ──────────────────────────────────────────────
    @Nested
    @DisplayName("GET /api/profesores/calendario")
    class VerCalendarioProfesorTests {

        @Test
        @DisplayName("Should return empty list when professor has no classes assigned")
        void verCalendarioProfesor_NoClasses_ReturnsEmptyList() {
            // given
            when(calendarioService.calendarioProfesor(
                    eq(profesorDetails.getId()), any(LocalDate.class), any(LocalDate.class)))
                    .thenReturn(Collections.emptyList());

            // when
            ResponseEntity<List<ClaseResponseDTO>> response = controller.verCalendarioProfesor(
                    profesorDetails, null, null);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEmpty();

            verify(calendarioService).calendarioProfesor(eq(profesorDetails.getId()), any(), any());
        }

        @Test
        @DisplayName("Should return list of classes for the professor within the default week")
        void verCalendarioProfesor_WithClasses_ReturnsClassList() {
            // given
            ClaseResponseDTO clase1 = createClase(1L, "Matemáticas", "Aula 101", 1);
            ClaseResponseDTO clase2 = createClase(2L, "Álgebra", "Aula 102", 2);
            List<ClaseResponseDTO> expected = List.of(clase1, clase2);

            when(calendarioService.calendarioProfesor(
                    eq(profesorDetails.getId()), any(LocalDate.class), any(LocalDate.class)))
                    .thenReturn(expected);

            // when
            ResponseEntity<List<ClaseResponseDTO>> response = controller.verCalendarioProfesor(
                    profesorDetails, null, null);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(2);
            assertThat(response.getBody().get(0).getAsignaturaNombre()).isEqualTo("Matemáticas");
            assertThat(response.getBody().get(1).getAsignaturaNombre()).isEqualTo("Álgebra");
        }

        @Test
        @DisplayName("Should pass explicit date range to the service")
        void verCalendarioProfesor_WithExplicitDates_PassesDatesToService() {
            // given
            LocalDate inicio = LocalDate.of(2026, 5, 1);
            LocalDate fin = LocalDate.of(2026, 5, 31);
            ClaseResponseDTO clase = createClase(10L, "Estadística", "Sala 5", 4);
            List<ClaseResponseDTO> expected = List.of(clase);

            when(calendarioService.calendarioProfesor(
                    eq(profesorDetails.getId()), eq(inicio), eq(fin)))
                    .thenReturn(expected);

            // when
            ResponseEntity<List<ClaseResponseDTO>> response = controller.verCalendarioProfesor(
                    profesorDetails, inicio, fin);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(1);

            verify(calendarioService).calendarioProfesor(
                    eq(profesorDetails.getId()), eq(inicio), eq(fin));
        }

        @Test
        @DisplayName("Should default fechaFin to fechaInicio + 1 week when fechaFin is null")
        void verCalendarioProfesor_OnlyFechaInicio_DefaultsFechaFinToOneWeekLater() {
            // given
            LocalDate inicio = LocalDate.of(2026, 6, 15);
            LocalDate expectedFin = inicio.plusWeeks(1);

            when(calendarioService.calendarioProfesor(
                    eq(profesorDetails.getId()), eq(inicio), eq(expectedFin)))
                    .thenReturn(Collections.emptyList());

            // when
            controller.verCalendarioProfesor(profesorDetails, inicio, null);

            // then
            verify(calendarioService).calendarioProfesor(
                    eq(profesorDetails.getId()), eq(inicio), eq(expectedFin));
        }

        @Test
        @DisplayName("Should default both dates when both are null")
        void verCalendarioProfesor_NullDates_DefaultsBoth() {
            // given
            when(calendarioService.calendarioProfesor(
                    eq(profesorDetails.getId()), any(LocalDate.class), any(LocalDate.class)))
                    .thenReturn(Collections.emptyList());

            // when
            ResponseEntity<List<ClaseResponseDTO>> response = controller.verCalendarioProfesor(
                    profesorDetails, null, null);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(calendarioService).calendarioProfesor(
                    eq(profesorDetails.getId()), any(LocalDate.class), any(LocalDate.class));
        }

        @Test
        @DisplayName("Should use the correct professor ID from authentication principal")
        void verCalendarioProfesor_UsesAuthenticatedProfessorId() {
            // given
            UserDetailsImpl anotherProfesor = new UserDetailsImpl(
                    999L,
                    "otroprofesor@example.com",
                    "encoded-password",
                    true,
                    List.of(new SimpleGrantedAuthority("ROLE_profesor"))
            );

            when(calendarioService.calendarioProfesor(eq(999L), any(), any()))
                    .thenReturn(Collections.emptyList());

            // when
            controller.verCalendarioProfesor(anotherProfesor, null, null);

            // then
            verify(calendarioService).calendarioProfesor(eq(999L), any(), any());
        }

        @Test
        @DisplayName("Should propagate RuntimeException from service")
        void verCalendarioProfesor_ServiceThrows_PropagatesException() {
            // given
            when(calendarioService.calendarioProfesor(anyLong(), any(), any()))
                    .thenThrow(new RuntimeException("Error de conexión a BD"));

            // when / then
            try {
                controller.verCalendarioProfesor(profesorDetails, null, null);
            } catch (RuntimeException e) {
                assertThat(e.getMessage()).isEqualTo("Error de conexión a BD");
            }

            verify(calendarioService).calendarioProfesor(eq(profesorDetails.getId()), any(), any());
        }
    }
}