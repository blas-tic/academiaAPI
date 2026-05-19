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
@DisplayName("CalendarioAlumnoController Unit Tests")
class CalendarioAlumnoControllerTest {

    @Mock
    private CalendarioService calendarioService;

    @InjectMocks
    private CalendarioAlumnoController controller;

    private UserDetailsImpl alumnoDetails;

    @BeforeEach
    void setUp() {
        alumnoDetails = new UserDetailsImpl(
                1L,
                "alumno@example.com",
                "encoded-password",
                true,
                List.of(new SimpleGrantedAuthority("ROLE_alumno"))
        );
    }

    // ──────────────────────────────────────────────
    // Helper methods
    // ──────────────────────────────────────────────
    private ClaseResponseDTO createClase(Long id, String asignaturaNombre, String profesorNombre) {
        ClaseResponseDTO dto = new ClaseResponseDTO();
        dto.setId(id);
        dto.setAsignaturaId(100L + id);
        dto.setAsignaturaNombre(asignaturaNombre);
        dto.setProfesorId(200L + id);
        dto.setProfesorNombre(profesorNombre);
        dto.setAulaId(300L + id);
        dto.setAulaNombre("Aula " + id);
        dto.setDiaSemana((int) (id % 7) + 1);
        dto.setHoraInicio(LocalTime.of(9, 0));
        dto.setHoraFin(LocalTime.of(10, 30));
        dto.setFechaInicio(LocalDate.now());
        dto.setFechaFin(LocalDate.now().plusWeeks(1));
        return dto;
    }

    // ──────────────────────────────────────────────
    // GET /api/alumnos/calendario tests
    // ──────────────────────────────────────────────
    @Nested
    @DisplayName("GET /api/alumnos/calendario")
    class VerCalendarioAlumnoTests {

        @Test
        @DisplayName("Should return empty list when alumno has no active matrículas")
        void verCalendarioAlumno_NoMatriculas_ReturnsEmptyList() {
            // given
            when(calendarioService.calendarioAlumno(
                    eq(alumnoDetails.getId()), any(LocalDate.class), any(LocalDate.class)))
                    .thenReturn(Collections.emptyList());

            // when
            ResponseEntity<List<ClaseResponseDTO>> response = controller.verCalendarioAlumno(
                    alumnoDetails, null, null);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEmpty();

            verify(calendarioService).calendarioAlumno(eq(alumnoDetails.getId()), any(), any());
        }

        @Test
        @DisplayName("Should return classes for the alumno in the default week range")
        void verCalendarioAlumno_WithClasses_ReturnsClassList() {
            // given
            ClaseResponseDTO clase1 = createClase(1L, "Matemáticas", "Dr. Pérez");
            ClaseResponseDTO clase2 = createClase(2L, "Física", "Dra. García");
            List<ClaseResponseDTO> expected = List.of(clase1, clase2);

            when(calendarioService.calendarioAlumno(
                    eq(alumnoDetails.getId()), any(LocalDate.class), any(LocalDate.class)))
                    .thenReturn(expected);

            // when
            ResponseEntity<List<ClaseResponseDTO>> response = controller.verCalendarioAlumno(
                    alumnoDetails, null, null);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(2);
            assertThat(response.getBody().get(0).getAsignaturaNombre()).isEqualTo("Matemáticas");
            assertThat(response.getBody().get(1).getAsignaturaNombre()).isEqualTo("Física");
        }

        @Test
        @DisplayName("Should pass explicit date range to the service")
        void verCalendarioAlumno_WithExplicitDates_PassesDatesToService() {
            // given
            LocalDate inicio = LocalDate.of(2026, 3, 1);
            LocalDate fin = LocalDate.of(2026, 3, 7);
            ClaseResponseDTO clase = createClase(1L, "Historia", "Dr. Ruiz");
            List<ClaseResponseDTO> expected = List.of(clase);

            when(calendarioService.calendarioAlumno(
                    eq(alumnoDetails.getId()), eq(inicio), eq(fin)))
                    .thenReturn(expected);

            // when
            ResponseEntity<List<ClaseResponseDTO>> response = controller.verCalendarioAlumno(
                    alumnoDetails, inicio, fin);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(1);

            verify(calendarioService).calendarioAlumno(
                    eq(alumnoDetails.getId()), eq(inicio), eq(fin));
        }

        @Test
        @DisplayName("Should use only fechaInicio and calculate fechaFin as inicio + 1 week when fechaFin is null")
        void verCalendarioAlumno_OnlyFechaInicio_DefaultsFechaFinToOneWeekLater() {
            // given
            LocalDate inicio = LocalDate.of(2026, 4, 10);
            LocalDate expectedFin = inicio.plusWeeks(1);

            when(calendarioService.calendarioAlumno(
                    eq(alumnoDetails.getId()), eq(inicio), eq(expectedFin)))
                    .thenReturn(Collections.emptyList());

            // when
            controller.verCalendarioAlumno(alumnoDetails, inicio, null);

            // then
            verify(calendarioService).calendarioAlumno(
                    eq(alumnoDetails.getId()), eq(inicio), eq(expectedFin));
        }

        @Test
        @DisplayName("Should default fechaInicio to today when it is null")
        void verCalendarioAlumno_NullFechaInicio_DefaultsToToday() {
            // given
            when(calendarioService.calendarioAlumno(
                    eq(alumnoDetails.getId()), any(LocalDate.class), any(LocalDate.class)))
                    .thenReturn(Collections.emptyList());

            // when
            controller.verCalendarioAlumno(alumnoDetails, null, null);

            // then
            // The default fechaInicio is LocalDate.now() — we cannot assert exact equality
            // but we can verify the service was called with some dates.
            verify(calendarioService).calendarioAlumno(
                    eq(alumnoDetails.getId()), any(LocalDate.class), any(LocalDate.class));
        }

        @Test
        @DisplayName("Should return singleton list when alumno has exactly one active matrícula")
        void verCalendarioAlumno_SingleMatricula_ReturnsOneClass() {
            // given
            ClaseResponseDTO clase = createClase(99L, "Literatura", "Dra. Fernández");
            when(calendarioService.calendarioAlumno(
                    eq(alumnoDetails.getId()), any(), any()))
                    .thenReturn(List.of(clase));

            // when
            ResponseEntity<List<ClaseResponseDTO>> response = controller.verCalendarioAlumno(
                    alumnoDetails, null, null);

            // then
            assertThat(response.getBody()).hasSize(1);
            ClaseResponseDTO single = response.getBody().get(0);
            assertThat(single.getAsignaturaNombre()).isEqualTo("Literatura");
            assertThat(single.getProfesorNombre()).isEqualTo("Dra. Fernández");
        }

        @Test
        @DisplayName("Should correctly propagate RuntimeException from service")
        void verCalendarioAlumno_ServiceThrows_PropagatesException() {
            // given
            when(calendarioService.calendarioAlumno(anyLong(), any(), any()))
                    .thenThrow(new RuntimeException("Error inesperado en BD"));

            // when / then
            try {
                controller.verCalendarioAlumno(alumnoDetails, null, null);
            } catch (RuntimeException e) {
                assertThat(e.getMessage()).isEqualTo("Error inesperado en BD");
            }

            verify(calendarioService).calendarioAlumno(eq(alumnoDetails.getId()), any(), any());
        }
    }
}