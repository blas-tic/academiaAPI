package com.example.academia.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.academia.model.Clase;

public interface ClaseRepository extends JpaRepository<Clase, Long> {
      List<Clase> findByProfesorIdAndDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(
                  Long profesorId, Integer diaSemana, LocalTime horaFin, LocalTime horaInicio);

      @Query("SELECT c FROM Clase c WHERE " +
                  "(:asignaturaId IS NULL OR c.asignatura.id = :asignaturaId) AND " +
                  "(:profesorId IS NULL OR c.profesor.id = :profesorId) AND " +
                  "(:aulaId IS NULL OR c.aula.id = :aulaId) AND " +
                  "(:diaSemana IS NULL OR c.diaSemana = :diaSemana)")
      List<Clase> findAllWithFilters(@Param("asignaturaId") Long asignaturaId,
                  @Param("profesorId") Long profesorId,
                  @Param("aulaId") Long aulaId,
                  @Param("diaSemana") Integer diaSemana);

      @Query("SELECT c FROM Clase c WHERE c.profesor.id = :profesorId AND c.diaSemana = :diaSemana " +
                  "AND c.horaInicio < :horaFin AND c.horaFin > :horaInicio")
      List<Clase> findConflictosProfesor(@Param("profesorId") Long profesorId,
                  @Param("diaSemana") Integer diaSemana,
                  @Param("horaInicio") LocalTime horaInicio,
                  @Param("horaFin") LocalTime horaFin);

      @Query("SELECT c FROM Clase c WHERE c.aula.id = :aulaId AND c.diaSemana = :diaSemana " +
                  "AND c.horaInicio < :horaFin AND c.horaFin > :horaInicio")
      List<Clase> findConflictosAula(@Param("aulaId") Long aulaId,
                  @Param("diaSemana") Integer diaSemana,
                  @Param("horaInicio") LocalTime horaInicio,
                  @Param("horaFin") LocalTime horaFin);

      @Query("SELECT c FROM Clase c WHERE c.asignatura.id IN :asignaturaIds " +
                  "AND (c.fechaInicio IS NULL OR c.fechaInicio <= :fechaFin) " +
                  "AND (c.fechaFin IS NULL OR c.fechaFin >= :fechaInicio)")
      List<Clase> findByAsignaturaIdInAndFechaRango(@Param("asignaturaIds") List<Long> asignaturaIds,
                  @Param("fechaInicio") LocalDate fechaInicio,
                  @Param("fechaFin") LocalDate fechaFin);

      @Query("SELECT c FROM Clase c WHERE c.profesor.id = :profesorId " +
                  "AND (c.fechaInicio IS NULL OR c.fechaInicio <= :fechaFin) " +
                  "AND (c.fechaFin IS NULL OR c.fechaFin >= :fechaInicio)")
      List<Clase> findByProfesorIdAndFechaRango(@Param("profesorId") Long profesorId,
                  @Param("fechaInicio") LocalDate fechaInicio,
                  @Param("fechaFin") LocalDate fechaFin);

      @Query("SELECT DISTINCT c FROM Clase c " +
                  "LEFT JOIN FETCH c.asignatura " +
                  "LEFT JOIN FETCH c.profesor " +
                  "LEFT JOIN FETCH c.aula " +
                  "WHERE (:asignaturaId IS NULL OR c.asignatura.id = :asignaturaId) " +
                  "AND (:profesorId IS NULL OR c.profesor.id = :profesorId) " +
                  "AND (:aulaId IS NULL OR c.aula.id = :aulaId) " +
                  "AND (:diaSemana IS NULL OR c.diaSemana = :diaSemana)")
      List<Clase> findAllWithFiltersAndFechaRango(
                  @Param("asignaturaId") Long asignaturaId,
                  @Param("profesorId") Long profesorId,
                  @Param("aulaId") Long aulaId,
                  @Param("diaSemana") Integer diaSemana);
}
