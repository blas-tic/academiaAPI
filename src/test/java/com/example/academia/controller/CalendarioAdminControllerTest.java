package com.example.academia.controller;

import com.example.academia.dto.ClaseResponseDTO;
import com.example.academia.service.CalendarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CalendarioAdminController Unit Tests")
class CalendarioAdminControllerTest {

    @Mock
    private CalendarioService calendarioService;

    @InjectMocks
    private CalendarioAdminController controller;

    // ──────────────────────────────────────────────
    // Helper methods
    // ──────────────────────────────────────────────
    private ClaseResponseDTO createClase(Long id, Long asignaturaId, String asignaturaNombre,
                                         Long profesorId, String profesorNombre,
                                         Long aulaId, String aulaNombre,
                                         Integer diaSemana) {
        ClaseResponseDTO dto = new ClaseResponseDTO();
        dto.setId(id);
        dto.setAsignaturaId(asignaturaId);
        dto.setAsignaturaNombre(asignaturaNombre);
        dto.setProfesorId(profesorId);
        dto.setProfesorNombre(profesorNombre);
        dto.setAulaId(aulaId);
        dto.setAulaNombre(aulaNombre);
        dto.setDiaSemana(diaSemana);
        dto.setHoraInicio(LocalTime.of(9, 0));
        dto.setHoraFin(LocalTime.of(10, 30));
        dto.setFechaInicio(LocalDate.now());
        dto.setFechaFin(LocalDate.now().plusWeeks(1));
        return dto;
    }

    // ──────────────────────────────────────────────
    // GET /api/admin/calendario tests
    // ──────────────────────────────────────────────
    @Nested
    @DisplayName("GET /api/admin/calendario")
    class VerCalendarioAdminTests {

        @Test
        @DisplayName("Should return empty list when no classes exist in the date range")
        void verCalendarioAdmin_NoClasses_ReturnsEmptyList() {
            // given – no params => default dates: now to now+1week
            when(calendarioService.calendarioAdmin(
                    isNull(), isNull(), isNull(), isNull(), any(LocalDate.class), any(LocalDate.class)))
                    .thenReturn(Collections.emptyList());

            // when
            ResponseEntity<List<ClaseResponseDTO>> response = controller.verCalendarioAdmin(
                    null, null, null, null, null, null);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEmpty();

            verify(calendarioService).calendarioAdmin(
                    isNull(), isNull(), isNull(), isNull(), any(LocalDate.class), any(LocalDate.class));
        }

        @Test
        @DisplayName("Should return classes filtered by asignaturaId")
        void verCalendarioAdmin_ByAsignatura_ReturnsFilteredList() {
            // given
            Long asignaturaId = 10L;
            ClaseResponseDTO clase = createClase(1L, asignaturaId, "Matemáticas", 2L, "Dr. Pérez", 3L, "Aula 101", 1);
            List<ClaseResponseDTO> expected = List.of(clase);

            when(calendarioService.calendarioAdmin(
                    eq(asignaturaId), isNull(), isNull(), isNull(), any(), any()))
                    .thenReturn(expected);

            // when
            ResponseEntity<List<ClaseResponseDTO>> response = controller.verCalendarioAdmin(
                    asignaturaId, null, null, null, null, null);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(1);
            assertThat(response.getBody().get(0).getAsignaturaId()).isEqualTo(asignaturaId);
            assertThat(response.getBody().get(0).getAsignaturaNombre()).isEqualTo("Matemáticas");

            verify(calendarioService).calendarioAdmin(
                    eq(asignaturaId), isNull(), isNull(), isNull(), any(), any());
        }

        @Test
        @DisplayName("Should return classes filtered by profesorId")
        void verCalendarioAdmin_ByProfesor_ReturnsFilteredList() {
            // given
            Long profesorId = 20L;
            ClaseResponseDTO clase = createClase(2L, 10L, "Física", profesorId, "Dra. García", 4L, "Lab 1", 3);
            List<ClaseResponseDTO> expected = List.of(clase);

            when(calendarioService.calendarioAdmin(
                    isNull(), eq(profesorId), isNull(), isNull(), any(), any()))
                    .thenReturn(expected);

            // when
            ResponseEntity<List<ClaseResponseDTO>> response = controller.verCalendarioAdmin(
                    null, profesorId, null, null, null, null);

            // then
            assertThat(response.getBody()).hasSize(1);
            assertThat(response.getBody().get(0).getProfesorId()).isEqualTo(profesorId);

            verify(calendarioService).calendarioAdmin(
                    isNull(), eq(profesorId), isNull(), isNull(), any(), any());
        }

        @Test
        @DisplayName("Should return classes filtered by aulaId")
        void verCalendarioAdmin_ByAula_ReturnsFilteredList() {
            // given
            Long aulaId = 30L;
            ClaseResponseDTO clase = createClase(3L, 11L, "Química", 21L, "Dr. López", aulaId, "Aula Magna", 5);
            List<ClaseResponseDTO> expected = List.of(clase);

            when(calendarioService.calendarioAdmin(
                    isNull(), isNull(), eq(aulaId), isNull(), any(), any()))
                    .thenReturn(expected);

            // when
            ResponseEntity<List<ClaseResponseDTO>> response = controller.verCalendarioAdmin(
                    null, null, aulaId, null, null, null);

            // then
            assertThat(response.getBody()).hasSize(1);
            assertThat(response.getBody().get(0).getAulaId()).isEqualTo(aulaId);

            verify(calendarioService).calendarioAdmin(
                    isNull(), isNull(), eq(aulaId), isNull(), any(), any());
        }

        @Test
        @DisplayName("Should return classes filtered by diaSemana")
        void verCalendarioAdmin_ByDiaSemana_ReturnsFilteredList() {
            // given
            Integer diaSemana = 5; // Friday
            ClaseResponseDTO clase = createClase(4L, 12L, "Historia", 22L, "Dra. Ruiz", 31L, "Aula 202", diaSemana);
            List<ClaseResponseDTO> expected = List.of(clase);

            when(calendarioService.calendarioAdmin(
                    isNull(), isNull(), isNull(), eq(diaSemana), any(), any()))
                    .thenReturn(expected);

            // when
            ResponseEntity<List<ClaseResponseDTO>> response = controller.verCalendarioAdmin(
                    null, null, null, diaSemana, null, null);

            // then
            assertThat(response.getBody()).hasSize(1);
            assertThat(response.getBody().get(0).getDiaSemana()).isEqualTo(diaSemana);

            verify(calendarioService).calendarioAdmin(
                    isNull(), isNull(), isNull(), eq(diaSemana), any(), any());
        }

        @Test
        @DisplayName("Should pass explicit date range to service when fechaInicio and fechaFin are provided")
        void verCalendarioAdmin_WithExplicitDates_PassesDatesToService() {
            // given
            LocalDate inicio = LocalDate.of(2026, 1, 5);
            LocalDate fin = LocalDate.of(2026, 1, 12);
            ClaseResponseDTO clase = createClase(5L, 13L, "Inglés", 23L, "Dr. Martínez", 32L, "Aula 303", 2);
            List<ClaseResponseDTO> expected = List.of(clase);

            when(calendarioService.calendarioAdmin(
                    isNull(), isNull(), isNull(), isNull(), eq(inicio), eq(fin)))
                    .thenReturn(expected);

            // when
            ResponseEntity<List<ClaseResponseDTO>> response = controller.verCalendarioAdmin(
                    null, null, null, null, inicio, fin);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(1);

            verify(calendarioService).calendarioAdmin(
                    isNull(), isNull(), isNull(), isNull(), eq(inicio), eq(fin));
        }

        @Test
        @DisplayName("Should combine all filters")
        void verCalendarioAdmin_AllFiltersCombined_ReturnsFilteredList() {
            // given
            Long asignaturaId = 100L;
            Long profesorId = 200L;
            Long aulaId = 300L;
            Integer diaSemana = 3;
            LocalDate inicio = LocalDate.of(2026, 2, 1);
            LocalDate fin = LocalDate.of(2026, 2, 28);

            ClaseResponseDTO clase = createClase(99L, asignaturaId, "Filosofía", profesorId, "Dr. Sánchez", aulaId, "Aula 404", diaSemana);
            List<ClaseResponseDTO> expected = List.of(clase);

            when(calendarioService.calendarioAdmin(
                    eq(asignaturaId), eq(profesorId), eq(aulaId), eq(diaSemana), eq(inicio), eq(fin)))
                    .thenReturn(expected);

            // when
            ResponseEntity<List<ClaseResponseDTO>> response = controller.verCalendarioAdmin(
                    asignaturaId, profesorId, aulaId, diaSemana, inicio, fin);

            // then
            assertThat(response.getBody()).hasSize(1);
            ClaseResponseDTO body = response.getBody().get(0);
            assertThat(body.getAsignaturaId()).isEqualTo(asignaturaId);
            assertThat(body.getProfesorId()).isEqualTo(profesorId);
            assertThat(body.getAulaId()).isEqualTo(aulaId);
            assertThat(body.getDiaSemana()).isEqualTo(diaSemana);

            verify(calendarioService).calendarioAdmin(
                    eq(asignaturaId), eq(profesorId), eq(aulaId), eq(diaSemana), eq(inicio), eq(fin));
        }

        @Test
        @DisplayName("Should return multiple classes when there are several")
        void verCalendarioAdmin_MultipleClasses_ReturnsAll() {
            // given
            ClaseResponseDTO clase1 = createClase(1L, 10L, "Matemáticas", 2L, "Dr. Pérez", 3L, "Aula 101", 1);
            ClaseResponseDTO clase2 = createClase(2L, 11L, "Física", 3L, "Dra. García", 4L, "Aula 102", 2);
            ClaseResponseDTO clase3 = createClase(3L, 12L, "Química", 4L, "Dr. López", 5L, "Lab 1", 3);
            List<ClaseResponseDTO> expected = List.of(clase1, clase2, clase3);

            when(calendarioService.calendarioAdmin(
                    isNull(), isNull(), isNull(), isNull(), any(), any()))
                    .thenReturn(expected);

            // when
            ResponseEntity<List<ClaseResponseDTO>> response = controller.verCalendarioAdmin(
                    null, null, null, null, null, null);

            // then
            assertThat(response.getBody()).hasSize(3);
        }
    }
}