# Microservicio de Alertas Zaror

API REST para crear, consultar, actualizar y eliminar alertas. Está desarrollada con Java 17, Spring Boot, Spring Web MVC, Spring Data JPA y MySQL.

## Requisitos

- JDK 17.
- MySQL 8 o compatible para ejecución normal.
- Acceso a internet la primera vez para descargar dependencias Maven.

## Configurar y ejecutar

Crea la base de datos:

```sql
CREATE DATABASE db_alertas_zaror;
```

Desde PowerShell, en la raíz del proyecto, define la conexión y levanta el servicio:

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/db_alertas_zaror"
$env:DB_USERNAME = "tu_usuario_mysql"
$env:DB_PASSWORD = "tu_contraseña_mysql"
.\mvnw.cmd spring-boot:run
```

También puedes usar `.env.example` como referencia; Spring Boot lee variables del entorno, no un archivo `.env` automáticamente. No incluyas contraseñas reales en Git. La API queda disponible en `http://localhost:8081`.

Para la app en el emulador Android estándar, el computador se alcanza como `10.0.2.2`, por lo que la URL base es `http://10.0.2.2:8081/`. Un teléfono físico debe usar la IP local del computador y compartir su red.

## Endpoints

| Método | Ruta | Función |
| --- | --- | --- |
| GET | `/alertas` | Lista alertas |
| GET | `/alertas/{id}` | Busca por ID |
| POST | `/alertas` | Crea una alerta |
| PUT | `/alertas/{id}` | Actualiza una alerta existente |
| DELETE | `/alertas/{id}` | Elimina una alerta existente |

Ejemplo de cuerpo JSON para crear o actualizar:

```json
{
  "titulo": "Incendio en el sector",
  "descripcion": "Humo visible cerca de la plaza",
  "tipo": "Incendio",
  "ubicacion": "Sector norte",
  "estado": "Activa"
}
```

Las operaciones por ID responden `404 Not Found` cuando la alerta no existe. La interfaz OpenAPI está disponible en `/swagger-ui/index.html` y el documento JSON en `/v3/api-docs` mientras el servicio está levantado.

## Pruebas

Las pruebas unitarias del servicio y la prueba de contexto usan una base H2 en memoria, por lo que no requieren una instancia MySQL:

```powershell
.\mvnw.cmd test
```

La configuración normal de ejecución continúa usando MySQL mediante las variables de entorno.

