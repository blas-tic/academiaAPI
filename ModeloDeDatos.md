# Modelo de Datos

## Diagrama Entidad-Relación (Mermaid)

```mermaid
erDiagram
    Persona {
        bigint id PK
        string email UK
        string nombre
        string telefono
        string direccion
        date fecha_nacimiento
        datetime fecha_alta
    }

    Usuario {
        bigint id PK
        bigint persona_id FK
        string password_hash
        boolean activo
    }

    Rol {
        int id PK
        string nombre UK
        string descripcion
    }

    usuario_rol {
        bigint usuario_id FK
        int rol_id FK
    }

    Alumno {
        bigint id PK, FK
        string tutor
    }

    Profesor {
        bigint id PK, FK
        string num_empleado UK
        string departamento
        string categoria
    }

    Asignatura {
        bigint id PK
        string nombre UK
        string descripcion
        string color
    }

    Aula {
        bigint id PK
        string nombre UK
        int capacidad
        string ubicacion
    }

    Clase {
        bigint id PK
        bigint asignatura_id FK
        bigint profesor_id FK
        bigint aula_id FK
        int dia_semana
        time hora_inicio
        time hora_fin
        date fecha_inicio
        date fecha_fin
    }

    Matricula {
        bigint alumno_id PK, FK
        bigint asignatura_id PK, FK
        datetime fecha_matricula
        enum estado
    }

    AsignaturaProfesor {
        bigint profesor_id PK, FK
        bigint asignatura_id PK, FK
        datetime fecha_asignacion
    }

    %% Relaciones
    Persona ||--o| Usuario : tiene
    Persona ||--o| Alumno : "es (opcional)"
    Persona ||--o| Profesor : "es (opcional)"
    Usuario ||--o{ usuario_rol : tiene
    Rol ||--o{ usuario_rol : asignado
    Alumno ||--o{ Matricula : realiza
    Asignatura ||--o{ Matricula : recibe
    Asignatura ||--o{ AsignaturaProfesor : asignada
    Profesor ||--o{ AsignaturaProfesor : imparte
    Asignatura ||--o{ Clase : tiene
    Profesor ||--o{ Clase : dicta
    Aula ||--o{ Clase : acoge
```

## Descripción de Entidades

- **Persona**: Datos comunes de cualquier persona (alumnos, profesores, administradores). Su `id` es la clave primaria. El `email` es único.
- **Usuario**: Cuenta de acceso asociada a una persona. Contiene la contraseña cifrada y el estado (activo/inactivo).
- **Rol**: Roles del sistema (`admin`, `profesor`, `alumno`). Relación muchos a muchos con `Usuario` a través de `usuario_rol`.
- **Alumno**: Extiende a `Persona` (clave primaria igual al `id` de persona). Añade atributos específicos como `tutor`.
- **Profesor**: Extiende a `Persona` con atributos profesionales (`num_empleado`, `departamento`, `categoria`).
- **Asignatura**: Materias ofrecidas por la academia. `nombre` único.
- **Aula**: Espacios físicos. `nombre` único.
- **Clase**: Sesión periódica de una asignatura en un día y horario fijos, impartida por un profesor y opcionalmente en un aula.
- **Matricula**: Inscripción de un alumno en una asignatura. Clave compuesta por `alumno_id` y `asignatura_id`. Incluye fecha y estado.
- **AsignaturaProfesor**: Asignación de un profesor a una asignatura (relación muchos a muchos con atributo `fecha_asignacion`).

## Notas sobre el modelo

- Se ha optado por **composición en lugar de herencia** para `Alumno` y `Profesor`: ambas tablas tienen una clave primaria que a la vez es clave foránea a `Persona`. Esto permite que una persona pueda ser simultáneamente alumno y profesor.
- Las tablas `Matricula` y `AsignaturaProfesor` usan **claves compuestas** para reflejar correctamente las relaciones muchos a muchos con atributos adicionales.
- La tabla `Clase` permite que `aula_id` sea nulo (clase online o sin aula fija). Las restricciones de unicidad de horario para profesor y aula se implementan a nivel de aplicación (servicio), no en la base de datos.
