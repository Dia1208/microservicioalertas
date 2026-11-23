# Microservicio de Alertas Zaror

API REST para compartir alertas, fallas, comunicados y cuentas entre dispositivos. Está desarrollada con Java 17, Spring Boot, Spring Web MVC, Spring Data JPA y MySQL.

## Integrantes

- Diego Alejandro Gonzalez Ballesteros
- Emanuel Orellana Gaete
- Stefani Francisca Verdugo Toledo

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

La versión Android de la evaluación usa `https://52-4-125-119.sslip.io/`, tanto en el emulador como en un teléfono físico. La instancia EC2 debe permanecer encendida para iniciar sesión y sincronizar los datos. Despliega este microservicio actualizado antes de instalar la nueva APK: la versión anterior rechaza los endpoints de fallas y comunicados.

## Servidor de demostración

La instancia EC2 publica la API mediante Nginx y un certificado HTTPS de Let's Encrypt.
MySQL y Java corren en la misma instancia; Java escucha solo en `127.0.0.1:8081` y
el puerto 3306 no está abierto a Internet.

Los archivos de `deploy/` documentan la instalación. `bootstrap-db.sh` crea la base
de datos y un usuario con contraseña aleatoria guardada solo en
`/etc/alertas-zaror/app.env`. No vuelvas a ejecutarlo en una actualización normal:
cambiaría la contraseña de MySQL. `alertas-zaror.service` inicia el JAR mediante systemd.

Si la IP pública cambia al detener y reiniciar EC2, hay que actualizar el nombre del
servidor, el certificado HTTPS y la URL Android antes de generar otra APK.

## Endpoints

| Método | Ruta | Función |
| --- | --- | --- |
| POST | `/auth/register` | Registra un Vecino/a |
| GET | `/auth/me` | Devuelve correo y rol del usuario autenticado |
| GET | `/alertas` | Lista alertas |
| GET | `/alertas/{id}` | Busca por ID |
| POST | `/alertas` | Crea una alerta |
| PUT | `/alertas/{id}` | Actualiza una alerta existente |
| DELETE | `/alertas/{id}` | Elimina una alerta existente |
| GET | `/fallas` | Lista fallas |
| POST | `/fallas` | Registra una falla (Encargado/a) |
| PUT | `/fallas/{id}/resuelta` | Cambia su estado (Encargado/a) |
| GET | `/comunicados` | Lista comunicados |
| POST | `/comunicados` | Publica un comunicado (Directiva) |

Las rutas de alertas requieren HTTP Basic sobre HTTPS. Vecino/a puede consultar y crear;
Encargado/a y Directiva también pueden actualizar y eliminar. Los GET de fallas y comunicados requieren autenticación. El registro público
siempre asigna Vecino/a y no acepta un rol enviado por el cliente.

Ejemplo de cuerpo JSON para crear o actualizar:

```json
{
  "titulo": "Incendio en el sector",
  "descripcion": "Humo visible cerca de la plaza",
  "tipo": "Incendio",
  "ubicacion": "Sector norte",
  "estado": "Activa",
  "fotoBase64": null
}
```

Las operaciones por ID responden `404 Not Found` cuando la alerta no existe. La interfaz OpenAPI está disponible en `/swagger-ui/index.html` y el documento JSON en `/v3/api-docs` mientras el servicio está levantado.

## Pruebas

Las pruebas unitarias del servicio y la prueba de contexto usan una base H2 en memoria, por lo que no requieren una instancia MySQL:

```powershell
.\mvnw.cmd test
```

La configuración normal de ejecución continúa usando MySQL mediante las variables de entorno.

## Cuentas de demostración

Al iniciar el servicio se crean o actualizan estas cuentas para la evaluación académica:

| Rol | Correo | Contraseña |
| --- | --- | --- |
| Encargado/a | `alejandro@encargado.cl` | `Duoc.2026@` |
| Directiva | `alejandro@directiva.cl` | `Duoc.2026@` |

La contraseña puede cambiarse con la variable `DEMO_PASSWORD`. Las dos cuentas pueden
consultar, crear, actualizar y eliminar alertas. Encargado/a registra y resuelve fallas;
Directiva publica comunicados. Los usuarios registrados desde la app reciben únicamente
el rol Vecino/a y pueden consultar y crear alertas. Los datos anteriores a esta versión
que solo existen en Room no se envían automáticamente al servidor.

