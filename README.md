# Academia Management API

API REST para la gestión de una pequeña academia. Permite administrar alumnos, profesores, asignaturas, aulas, clases y matriculaciones, con control de acceso basado en roles (administrador, profesor, alumno). Incluye un sistema de registro público con activación por email (simulado con MailHog) y documentación interactiva con Swagger UI.

## Características

- Autenticación mediante JWT (login con email/contraseña).
- Roles: `admin`, `profesor`, `alumno` (un usuario puede tener varios roles).
- Registro público de alumnos y profesores con generación de contraseña temporal y envío de email.
- Activación de cuenta mediante cambio de contraseña (primer acceso).
- Gestión de usuarios (CRUD) por parte de administradores.
- Gestión de asignaturas, aulas y clases (CRUD) con validación de conflictos horarios.
- Asignación de profesores a asignaturas.
- Matriculación de alumnos en asignaturas.
- Visualización de calendarios personalizados (alumno, profesor) y global con filtros (admin).
- Manejo global de excepciones con respuestas descriptivas.
- Simulación de envío de emails mediante **MailHog** (entorno de desarrollo).
- **Documentación interactiva** con Swagger UI (OpenAPI 3.0).
- Base de datos PostgreSQL con modelo relacional (claves compuestas, composición en lugar de herencia).

## Tecnologías

- Java 21
- Spring Boot 3.5.11
- Spring Security (JWT)
- Spring Data JPA (Hibernate)
- Spring Mail (para simulación con MailHog)
- PostgreSQL
- Maven
- Lombok (opcional)
- MailHog (servidor de email falso para desarrollo)
- **springdoc-openapi** (Swagger UI)

## Requisitos previos

- JDK 21
- Maven 3.9+
- PostgreSQL 15+ (o Docker)
- Docker (opcional, para levantar MailHog fácilmente)
- Postman o navegador para probar la API

## Instalación y configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/academia-api.git
cd academia-api
```

### 2. Crear la base de datos

Accede a PostgreSQL y crea una base de datos (por ejemplo, `academia_db`):

```sql
CREATE DATABASE academia_db;
```

### 3. Configurar MailHog (opcional pero recomendado)

MailHog es un servidor de email falso que captura los mensajes y los muestra en una interfaz web. Puedes levantarlo con Docker:

```bash
docker run -d -p 1025:1025 -p 8025:8025 mailhog/mailhog
```

- SMTP: `localhost:1025`
- Web UI: `http://localhost:8025`

### 4. Configurar las propiedades de la aplicación

Edita `src/main/resources/application.properties`:

```properties
# Base de datos
spring.datasource.url=jdbc:postgresql://localhost:5432/academia_db
spring.datasource.username=postgres
spring.datasource.password=tu_contraseña
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Logging (opcional para depuración)
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
logging.level.org.springframework.security=DEBUG

# JWT
app.jwt.secret=miClaveSecretaSuperSeguraQueDebeSerLargaParaHMAC256
app.jwt.expiration=86400000

# Mail (para MailHog)
spring.mail.host=localhost
spring.mail.port=1025
spring.mail.username=
spring.mail.password=
spring.mail.properties.mail.smtp.auth=false
spring.mail.properties.mail.smtp.starttls.enable=false
```

### 5. Compilar y ejecutar

```bash
./mvnw spring-boot:run
```

La aplicación arrancará en `http://localhost:8080`.

## Datos iniciales

Al arrancar la aplicación, se crean automáticamente los roles `admin`, `profesor` y `alumno` si no existen. También se crea un usuario administrador por defecto:

- **Email:** `admin@academia.com`
- **Contraseña:** `admin123`

> **Recomendación:** Cambia esta contraseña tras el primer acceso.

## Documentación de la API (Swagger UI)

La API cuenta con documentación interactiva generada automáticamente. Puedes acceder a Swagger UI en:

**[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

Desde allí podrás:

- Explorar todos los endpoints disponibles, agrupados por controladores (tags).
- Probar las operaciones directamente desde el navegador.
- Autenticarte mediante el botón **Authorize** (introduce tu token JWT).

El documento OpenAPI en formato JSON está disponible en:  
`http://localhost:8080/v3/api-docs`

## Estructura del proyecto

```
src/main/java/com/example/academia/
├── AcademiaApplication.java
├── config/                 # Configuraciones de seguridad, JWT, OpenAPI
├── controller/             # Controladores REST
│   ├── AuthController.java (login, activación)
│   ├── RegistroController.java (registro público)
│   ├── AdminController.java (usuarios)
│   ├── AsignaturaController.java
│   ├── AulaController.java
│   ├── ClaseController.java
│   ├── MatriculaController.java
│   ├── AsignaturaProfesorController.java
│   ├── PerfilController.java (propio perfil)
│   ├── AdminPerfilController.java (perfiles por admin)
│   └── calendario/         # Controladores de calendario
├── dto/                    # Objetos de transferencia de datos
├── exception/              # Excepciones personalizadas y manejador global
├── model/                  # Entidades JPA
├── repository/             # Repositorios Spring Data
├── security/               # Filtros JWT, UserDetailsService, UserDetailsImpl
└── service/                # Lógica de negocio
```

## API Endpoints

La API requiere autenticación mediante token JWT (excepto `/api/auth/login`, `/api/auth/registro` y `/api/auth/activar`). El token debe enviarse en el header `Authorization: Bearer <token>`.

### Autenticación y registro

| Método | URL                | Descripción                                | Rol      |
|--------|--------------------|--------------------------------------------|----------|
| POST   | `/api/auth/login`  | Inicia sesión, devuelve token              | Público  |
| POST   | `/api/auth/registro` | Registro de nuevo alumno/profesor         | Público  |
| POST   | `/api/auth/activar` | Activa la cuenta (primer cambio de password) | Público (con password temporal) |

### Usuarios (solo admin)

| Método | URL                         | Descripción                     |
|--------|-----------------------------|---------------------------------|
| GET    | `/api/admin/usuarios`       | Lista todos los usuarios        |
| GET    | `/api/admin/usuarios/{id}`  | Obtiene un usuario por ID       |
| POST   | `/api/admin/usuarios`       | Crea un nuevo usuario           |
| PUT    | `/api/admin/usuarios/{id}`  | Actualiza un usuario            |
| DELETE | `/api/admin/usuarios/{id}`  | Desactiva un usuario            |

### Perfiles

| Método | URL                          | Descripción                               | Rol      |
|--------|------------------------------|-------------------------------------------|----------|
| GET    | `/api/perfil`                | Obtiene el perfil del usuario autenticado | Cualquiera |
| PUT    | `/api/perfil`                | Actualiza su propio perfil                | Cualquiera |
| POST   | `/api/perfil/cambiar-password` | Cambia su contraseña (requiere actual)    | Cualquiera |
| GET    | `/api/admin/perfil/{id}`     | Obtiene perfil de cualquier usuario       | Admin    |
| PUT    | `/api/admin/perfil/{id}`     | Actualiza cualquier perfil                | Admin    |
| POST   | `/api/admin/perfil/{id}/cambiar-password` | Cambia contraseña de cualquier usuario | Admin |

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
| Admin   | GET    | `/api/admin/calendario?asignaturaId=&profesorId=&aulaId=&diaSemana=&fechaInicio=&fechaFin=` | Calendario global con filtros |

## Flujo de registro y activación

1. **Registro**: El usuario envía sus datos a `POST /api/auth/registro` (incluyendo roles `["alumno"]`, `["profesor"]` o ambos). La aplicación:
   - Crea la `Persona`.
   - Crea `Alumno` y/o `Profesor` según los roles.
   - Crea un `Usuario` con estado `inactivo` y una contraseña aleatoria de 8 caracteres.
   - Envía un email (vía MailHog) con la contraseña temporal.
   - Responde con los datos del usuario (sin contraseña).

2. **Intento de login**: Si el usuario intenta login con la contraseña temporal, Spring Security lanza `LockedException` y el manejador global devuelve 401 con mensaje "Usuario inactivo...".

3. **Activación**: El usuario debe llamar a `POST /api/auth/activar` con su email, contraseña temporal y una nueva contraseña. La aplicación:
   - Verifica que la contraseña temporal sea correcta.
   - Cambia la contraseña a la nueva.
   - Marca el usuario como `activo`.
   - (Opcional) Envía un email de confirmación.
   - Responde 200 OK.

4. **Login exitoso**: A partir de ese momento, el usuario puede hacer login con su nueva contraseña y obtendrá un token JWT.

## Ejemplos de peticiones (Postman)

Se incluye una colección de Postman en `Academia.postman_collection.json`, que se puede importar.

### Registro de un alumno

```http
POST /api/auth/registro
Content-Type: application/json

{
    "email": "alumno@example.com",
    "nombre": "Juan Pérez",
    "telefono": "123456789",
    "direccion": "Calle Falsa 123",
    "fechaNacimiento": "2000-01-01",
    "roles": ["alumno"],
    "tutor": "María Pérez"
}
```

### Registro de un profesor

```http
POST /api/auth/registro
Content-Type: application/json

{
    "email": "profesor@example.com",
    "nombre": "Ana García",
    "telefono": "987654321",
    "direccion": "Avenida Siempre Viva 742",
    "fechaNacimiento": "1980-05-15",
    "roles": ["profesor"],
    "numEmpleado": "EMP001",
    "departamento": "Matemáticas",
    "categoria": "Titular"
}
```

### Login (fallo por inactivo)

```http
POST /api/auth/login
Content-Type: application/json

{
    "email": "alumno@example.com",
    "password": "abc123"
}
```

Respuesta esperada (código 401):

```json
{
    "timestamp": "2026-03-15T17:30:00",
    "status": 401,
    "error": "Unauthorized",
    "message": "Usuario inactivo. Por favor, active su cuenta o contacte con el administrador.",
    "path": "/api/auth/login"
}
```

### Activación de cuenta

```http
POST /api/auth/activar
Content-Type: application/json

{
    "email": "alumno@example.com",
    "currentPassword": "a1b2c3d4",  // la contraseña temporal recibida por email
    "newPassword": "MiNuevaPasswordSegura123"
}
```

### Login correcto (después de activar)

```http
POST /api/auth/login
Content-Type: application/json

{
    "email": "alumno@example.com",
    "password": "MiNuevaPasswordSegura123"
}
```

Respuesta:

```json
{
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "type": "Bearer",
    "id": 15,
    "email": "alumno@example.com"
}
```

## Configuración de MailHog

- Interfaz web: [http://localhost:8025](http://localhost:8025)
- Los emails enviados por la aplicación aparecerán aquí. Puedes ver su contenido y comprobar que las contraseñas temporales se envían correctamente.

## Seguridad

- Las contraseñas se almacenan cifradas con BCrypt.
- Los tokens JWT incluyen el email y el ID del usuario.
- El acceso a los endpoints se controla mediante `@PreAuthorize` y reglas en `SecurityConfig`.
- Los endpoints públicos son `/api/auth/**` (login, registro, activación) y los de documentación `/swagger-ui.html`, `/swagger-ui/**`, `/v3/api-docs/**`.

## Posibles mejoras futuras

- Paginación en listados.
- Gestión de asistencia a clases.
- Notificaciones por email reales (usando SMTP de verdad).
- Recuperación de contraseña olvidada.
- Reportes básicos.

## Contribución

Si deseas contribuir, por favor abre un issue o envía un pull request. Se agradecen las sugerencias y mejoras.

Si te ha gustado o te ha sido útil, pon alguna estrella, anda :)

## Licencia

Este proyecto está bajo la licencia MIT. Ver archivo [LICENSE](LICENSE) para más detalles.
