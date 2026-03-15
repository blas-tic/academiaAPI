insert into my_schema.personas (email, nombre, fecha_alta)
values ('admin@example.com', 'Administrador', now());


insert into my_schema.usuarios (activo, password_hash, persona_id)
values (true, '$2a$12$nUlLuhPlgpsHDF.PH7FvieUuDPibgDdz9IyDLYpHO7dcvtpJjWv8O', 1);

insert into my_schema.usuario_rol (usuario_id, rol_id)
values (1, 3);

select * from my_schema.personas p ;

select * from my_schema.usuarios;
select * from my_schema.roles;

select * from my_schema.usuario_rol ur ;

SELECT DISTINCT u FROM Usuario u JOIN FETCH u.roles

-- ----------------------------------

# PROFESORES
select p2.id, p2.nombre, p2.email, p.departamento, p.categoria,
		a.id as asignaturaId, a.nombre as asignatura
from profesores p 
join personas p2 on p.id = p2.id
left join asignatura_profesor ap on p.id = ap.profesor_id 
left join asignaturas a on ap.asignatura_id = a.id 

# ALUMNOS
select p.id, p.nombre , p.email , a.tutor 
from alumnos a 
join personas p on a.id = p.id 

# ALUMNOS ASIGNATURAS
select p.id, p.nombre , p.email , a.tutor, a2.id as asignaturaId, a2.nombre as asignatura
from alumnos a 
join personas p on a.id = p.id 
left join matriculas m on a.id = m.alumno_id 
left join asignaturas a2 on m.asignatura_id = a2.id

# AULAS
select * from aulas a 

# ASIGNATURAS
select * from asignaturas

# CLASES
select * from clases c 
delete from clases
insert into clases (dia_semana, fecha_inicio, fecha_fin, hora_inicio, hora_fin, asignatura_id, aula_id, profesor_id)
values 
(1, '2025-09-08', '2026-06-30', '08:00', '09:00', 1, 4, 4), -- Mates 1 primera hora
(2, '2025-09-08', '2026-06-30', '08:00', '09:00', 1, 4, 4), -- Mates 1 primera hora
(3, '2025-09-08', '2026-06-30', '08:00', '09:00', 1, 4, 4), -- Mates 1 primera hora
(4, '2025-09-08', '2026-06-30', '08:00', '09:00', 1, 4, 4), -- Mates 1 primera hora
(5, '2025-09-08', '2026-06-30', '08:00', '09:00', 1, 4, 4), -- Mates 1 primera hora

(1, '2025-09-08', '2026-06-30', '09:00', '10:00', 2, 5, 12), -- Mates 2 segunda hora
(2, '2025-09-08', '2026-06-30', '09:00', '10:00', 2, 5, 12), -- Mates 2 segunda hora
(3, '2025-09-08', '2026-06-30', '09:00', '10:00', 2, 5, 12), -- Mates 2 segunda hora
(4, '2025-09-08', '2026-06-30', '09:00', '10:00', 2, 5, 12), -- Mates 2 segunda hora
(5, '2025-09-08', '2026-06-30', '09:00', '10:00', 2, 5, 12), -- Mates 2 segunda hora

(1, '2025-09-08', '2026-06-30', '10:00', '12:00', 7, 6, 6), -- Dibujos tercera hora
(2, '2025-09-08', '2026-06-30', '10:00', '12:00', 7, 6, 6), -- Dibujos tercera hora
(3, '2025-09-08', '2026-06-30', '10:00', '12:00', 8, 7, 7), -- Dibujos tercera hora
(4, '2025-09-08', '2026-06-30', '10:00', '12:00', 8, 7, 7), -- Dibujos tercera hora
(5, '2025-09-08', '2026-06-30', '10:00', '12:00', 8, 7, 7), -- Dibujos tercera hora

(1, '2025-09-08', '2026-06-30', '12:30', '13:30', 6, 1, 8), -- Violin I cuarta hora
(2, '2025-09-08', '2026-06-30', '12:30', '13:30', 6, 1, 8), -- Violin I cuarta hora
(3, '2025-09-08', '2026-06-30', '12:30', '13:30', 6, 1, 8), -- Violin I cuarta hora
(4, '2025-09-08', '2026-06-30', '12:30', '13:30', 6, 1, 8), -- Violin I cuarta hora
(5, '2025-09-08', '2026-06-30', '12:30', '13:30', 6, 1, 8), -- Violin I cuarta hora

(1, '2025-09-08', '2026-06-30', '13:30', '14:30', 4, 2, 5), -- Violin II quinta hora
(2, '2025-09-08', '2026-06-30', '13:30', '14:30', 4, 2, 5), -- Violin II quinta hora
(3, '2025-09-08', '2026-06-30', '13:30', '14:30', 4, 2, 5), -- Violin II quinta hora
(4, '2025-09-08', '2026-06-30', '13:30', '14:30', 4, 2, 5), -- Violin II quinta hora
(5, '2025-09-08', '2026-06-30', '13:30', '14:30', 4, 2, 5) -- Violin II quinta hora






select * from my_schema.profesores p 

select p.id , p.nombre , p.email , a.tutor  from my_schema.alumnos a 
join my_schema.personas p on a.id = p.id 

select a.id, p.email , a2.id as asignaturaId, a2.nombre as asignatura
from matriculas m 
join alumnos a on m.alumno_id = a.id 
join asignaturas a2 on m.asignatura_id = a2.id 
join personas p on a.id = p.id 

	select * from my_schema.asignaturas a 

select * from my_schema.matriculas m 

select * from my_schema.asignatura_profesor ap 
insert into asignatura_profesor (asignatura_id, profesor_id, fecha_asignacion)
values (8, 7, LOCALTIMESTAMP);

select * from my_schema.aulas a ;
select * from my_schema.clases c ;

select p.* from my_schema.alumnos a 
join my_schema.personas p on a.id =p.id;




select a.id as asignaturaId, a.nombre as asignatura , p.id as profesorId, p2.nombre 
from my_schema.asignaturas a
join my_schema.asignatura_profesor ap on a.id = ap.asignatura_id 
join my_schema.profesores p on ap.profesor_id = p.id 
join my_schema.personas p2 on p.id = p2.id 


