# Microservicio de Alertas Zaror

API REST desarrollada con Java 17, Spring Boot, Spring Data JPA y MySQL.

## Requisitos

- JDK 17
- MySQL 8 o compatible
- Conexión a internet la primera vez para descargar dependencias Maven

## Configurar MySQL

Crea la base de datos antes de iniciar la aplicación:

```sql
CREATE DATABASE db_alertas_zaror;
```

Configura las variables de entorno con la conexión y credenciales propias:

```text
DB_URL=jdbc:mysql://localhost:3306/db_alertas_zaror
DB_USERNAME=tu_usuario_mysql
DB_PASSWORD=tu_contraseña_mysql
```

Puedes copiar `.env.example` como referencia. Spring Boot lee estas variables del entorno; no subas contraseñas a Git.

## Ejecutar

En Windows PowerShell, desde la raíz del repositorio:

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/db_alertas_zaror"
$env:DB_USERNAME = "tu_usuario_mysql"
$env:DB_PASSWORD = "tu_contraseña_mysql"
.\mvnw.cmd spring-boot:run
```

La API queda disponible en `http://localhost:8081`. Desde el emulador Android estándar, el host del computador se alcanza mediante `http://10.0.2.2:8081`.

## Endpoints

| Método | Ruta | Acción |
|---|---|---|
| GET | `/alertas` | Lista alertas |
| GET | `/alertas/{id}` | Busca una alerta por ID |
| POST | `/alertas` | Crea una alerta |
| PUT | `/alertas/{id}` | Actualiza una alerta |
| DELETE | `/alertas/{id}` | Elimina una alerta |

Ejemplo de cuerpo para `POST /alertas`:

```json
{
  "titulo": "Incendio en el sector",
  "descripcion": "Humo visible cerca de la plaza",
  "tipo": "Incendio",
  "ubicacion": "Sector norte",
  "estado": "Activa"
}
```

La documentación OpenAPI está disponible en `/swagger-ui.html` mientras la aplicación está ejecutándose.

## Pruebas

```powershell
.\mvnw.cmd test
```
