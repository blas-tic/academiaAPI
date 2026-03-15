# Academia Management API

API REST para la gestión de una pequeña academia. Permite administrar alumnos, profesores, asignaturas, aulas, clases y matriculaciones, con control de acceso basado en roles (administrador, profesor, alumno).

## Características

- Autenticación mediante JWT (login con email/contraseña).
- Roles: `admin`, `profesor`, `alumno` (un usuario puede tener varios roles).
- Gestión de usuarios (CRUD) por parte de administradores.
- Gestión de asignaturas, aulas y clases (CRUD) con validación de conflictos horarios.
- Asignación de profesores a asignaturas.
- Matriculación de alumnos en asignaturas.
- Visualización de calendarios personalizados (alumno, profesor) y global con filtros (admin).
- Manejo global de excepciones con respuestas descriptivas.
- Base de datos PostgreSQL con modelo relacional (claves compuestas, herencia simulada mediante composición).

## Tecnologías

- Java 21
- Spring Boot 3.5.11
- Spring Security (JWT)
- Spring Data JPA (Hibernate)
- PostgreSQL
- Maven
- Lombok (opcional, si se usa)

## Requisitos previos

- JDK 21
- Maven 3.9+
- PostgreSQL 15+ (o Docker)
- Postman (para probar la API)

## Instalación y configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/academia-api.git
cd academia-api
```

### 2. Crear la base de datos

Accede a PostgreSQL y crea una base de datos (por ejemplo, `academia_db`). Puedes usar pgAdmin o línea de comandos:

```sql
CREATE DATABASE academia_db;
```

### 3. Configurar las propiedades de la aplicación

Edita el archivo `src/main/resources/application.properties` con los datos de tu conexión:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/academia_db
spring.datasource.username=postgres
spring.datasource.password=tu_contraseña
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Logging (opcional para depuración)
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# JWT
app.jwt.secret=miClaveSecretaSuperSeguraQueDebeSerLargaParaHMAC256
app.jwt.expiration=86400000
```

> **Nota:** Cambia `miClaveSecreta...` por una clave segura. El tiempo de expiración está en milisegundos (86400000 = 24 horas).

### 4. Compilar y ejecutar

```bash
./mvnw spring-boot:run
```

La aplicación arrancará en `http://localhost:8080`.

## Datos iniciales

Al arrancar la aplicación por primera vez, se crean automáticamente los roles `admin`, `profesor` y `alumno` si no existen. También se puede crear un usuario administrador por defecto mediante un `CommandLineRunner` (implementado en la clase `DataInitializer`). Por defecto, se crea:

- Email: `admin@academia.com`
- Contraseña: `admin123`

> **Recomendación:** Cambia esta contraseña tras el primer acceso.

## Estructura del proyecto

```
src/main/java/com/example/academia/
├── AcademiaApplication.java
├── config/                 # Configuraciones de seguridad, JWT
├── controller/             # Controladores REST
│   ├── AuthController.java
│   ├── AdminController.java (usuarios)
│   ├── AsignaturaController.java
│   ├── AulaController.java
│   ├── ClaseController.java
│   ├── MatriculaController.java
│   ├── AsignaturaProfesorController.java
│   └── calendario/         # Controladores de calendario (alumno, profesor, admin)
├── dto/                    # Objetos de transferencia de datos
├── exception/              # Excepciones personalizadas y manejador global
├── model/                  # Entidades JPA
├── repository/             # Repositorios Spring Data
├── security/               # Filtros JWT, UserDetailsService, UserDetailsImpl
└── service/                # Lógica de negocio
```

## API Endpoints

La API requiere autenticación mediante token JWT (excepto `/api/auth/login`). El token debe enviarse en el header `Authorization: Bearer <token>`.

### Autenticación

| Método | URL                | Descripción                | Rol      |
|--------|--------------------|----------------------------|----------|
| POST   | `/api/auth/login`  | Inicia sesión y obtiene token | Público |

### Usuarios (solo admin)

| Método | URL                         | Descripción                     |
|--------|-----------------------------|---------------------------------|
| GET    | `/api/admin/usuarios`       | Lista todos los usuarios        |
| GET    | `/api/admin/usuarios/{id}`  | Obtiene un usuario por ID       |
| POST   | `/api/admin/usuarios`       | Crea un nuevo usuario           |
| PUT    | `/api/admin/usuarios/{id}`  | Actualiza un usuario            |
| DELETE | `/api/admin/usuarios/{id}`  | Desactiva un usuario            |

### Asignaturas (admin)

| Método | URL                          | Descripción                     |
|--------|------------------------------|---------------------------------|
| GET    | `/api/admin/asignaturas`     | Lista todas                     |
| GET    | `/api/admin/asignaturas/{id}`| Obtiene una por ID              |
| POST   | `/api/admin/asignaturas`     | Crea una nueva                  |
| PUT    | `/api/admin/asignaturas/{id}`| Actualiza                       |
| DELETE | `/api/admin/asignaturas/{id}`| Elimina (si no tiene dependencias) |

### Aulas (admin) – similar a asignaturas

### Clases (admin)

| Método | URL                    | Descripción                          |
|--------|------------------------|--------------------------------------|
| GET    | `/api/admin/clases`    | Lista con filtros opcionales         |
| GET    | `/api/admin/clases/{id}`| Obtiene una clase                   |
| POST   | `/api/admin/clases`    | Crea una clase (con validación de conflictos) |
| PUT    | `/api/admin/clases/{id}`| Actualiza una clase                 |
| DELETE | `/api/admin/clases/{id}`| Elimina una clase                   |

### Asignación profesor-asignatura (admin)

| Método | URL                                               | Descripción                     |
|--------|---------------------------------------------------|---------------------------------|
| POST   | `/api/admin/asignaturas-profesor`                 | Asigna profesor a asignatura    |
| GET    | `/api/admin/asignaturas-profesor/profesor/{id}`   | Asignaciones de un profesor     |
| GET    | `/api/admin/asignaturas-profesor/asignatura/{id}` | Asignaciones de una asignatura  |
| DELETE | `/api/admin/asignaturas-profesor/{profId}/asignatura/{asigId}` | Elimina asignación |

### Matriculación (admin)

| Método | URL                                   | Descripción                     |
|--------|---------------------------------------|---------------------------------|
| POST   | `/api/admin/matriculas`               | Matricula alumno en asignatura  |
| GET    | `/api/admin/matriculas/alumno/{id}`   | Matrículas de un alumno         |
| GET    | `/api/admin/matriculas/asignatura/{id}`| Alumnos matriculados en asignatura |
| DELETE | `/api/admin/matriculas/{alumnoId}/asignatura/{asignaturaId}` | Cancela matrícula |

### Calendarios

| Rol     | Método | URL                                        | Descripción                          |
|---------|--------|--------------------------------------------|--------------------------------------|
| Alumno  | GET    | `/api/alumno/calendario?fechaInicio=&fechaFin=` | Calendario del alumno autenticado    |
| Profesor| GET    | `/api/profesor/calendario?fechaInicio=&fechaFin=` | Calendario del profesor autenticado |
| Admin   | GET    | `/api/admin/calendario?filtros`            | Calendario global con filtros        |

## Ejemplos de peticiones (Postman)

Se incluye una colección de Postman con requests para todos los EndPoints:  `Academia.postman_collection.json`

### Login

```http
POST /api/auth/login
Content-Type: application/json

{
    "email": "admin@academia.com",
    "password": "admin123"
}
```

Respuesta:

```json
{
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "type": "Bearer",
    "id": 1,
    "email": "admin@academia.com"
}
```

### Crear un alumno (admin)

```http
POST /api/admin/usuarios
Authorization: Bearer <token>
Content-Type: application/json

{
    "email": "alumno1@example.com",
    "password": "123456",
    "nombre": "Juan Pérez",
    "telefono": "123456789",
    "tipo": "ALUMNO",
    "tutor": "María Pérez",
    "roles": ["alumno"]
}
```

### Crear una clase

```http
POST /api/admin/clases
Authorization: Bearer <token>
Content-Type: application/json

{
    "asignaturaId": 1,
    "profesorId": 2,
    "aulaId": 1,
    "diaSemana": 1,
    "horaInicio": "10:00",
    "horaFin": "12:00",
    "fechaInicio": "2026-03-01",
    "fechaFin": "2026-06-30"
}
```

## Seguridad

- Las contraseñas se almacenan cifradas con BCrypt.
- Los tokens JWT incluyen el email y el ID del usuario.
- El acceso a los endpoints se controla mediante `@PreAuthorize` y reglas en `SecurityConfig`.
- Los endpoints públicos son solo `/api/auth/**` y `/error/**`.

## Posibles mejoras futuras

- Paginación en listados.
- Documentación con Swagger/OpenAPI.
- Gestión de asistencia a clases.
- Notificaciones por email.
- Perfil de usuario (modificar datos propios).
- Reportes básicos.

## Contribución

Si deseas contribuir, por favor abre un issue o envía un pull request. Se agradecen las sugerencias y mejoras.

## Licencia

Este proyecto está bajo la licencia MIT. Ver archivo [LICENSE](LICENSE) para más detalles.

