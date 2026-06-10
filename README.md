# BankCore Lite API

API REST bancaria desarrollada con **Java 17** y **Spring Boot 3** para demostrar competencias de backend junior orientadas a ofertas de Java, Spring Boot, microservicios, banca, SQL, testing, CI/CD y cloud.

## Objetivo del proyecto

BankCore Lite simula un sistema bancario básico para gestionar clientes, cuentas, movimientos, transferencias y alertas antifraude. No es una aplicación bancaria real ni debe usarse en producción, pero sí muestra lógica de negocio, validaciones, arquitectura por capas y buenas prácticas de backend.

## Tecnologías utilizadas

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Hibernate
- H2 Database
- MySQL preparado como alternativa
- Bean Validation
- Swagger / OpenAPI
- JUnit 5
- Docker
- GitHub Actions

## Funcionalidades

### Clientes

- Crear cliente
- Listar clientes
- Consultar cliente por ID
- Actualizar cliente
- Desactivar cliente

### Cuentas

- Crear cuenta bancaria asociada a un cliente
- Consultar cuentas
- Consultar saldo
- Bloquear cuenta
- Activar cuenta

### Movimientos

- Registrar ingresos
- Registrar reintegros
- Consultar movimientos de una cuenta

### Transferencias

- Realizar transferencias entre cuentas
- Validar saldo suficiente
- Rechazar transferencias desde cuentas bloqueadas
- Registrar movimientos en cuenta origen y destino
- Dejar operaciones sospechosas en revisión

### Antifraude básico

Reglas incluidas:

- Si una transferencia supera **3.000 €**, queda en estado `PENDIENTE_REVISION`.
- Si una cuenta realiza más de **3 transferencias en menos de 10 minutos**, se genera una alerta.

## Arquitectura

```text
src/main/java/com/sergio/bankcore/
├── config/          Configuración de OpenAPI
├── controller/      Endpoints REST
├── dto/             Objetos de entrada y salida
├── exception/       Manejo global de errores
├── model/           Entidades JPA y enums
├── repository/      Repositorios Spring Data JPA
└── service/         Lógica de negocio
```

## Cómo ejecutar el proyecto

### Opción 1: Maven

```bash
mvn spring-boot:run
```

### Opción 2: JAR

```bash
mvn clean package
java -jar target/bankcore-lite-api-1.0.0.jar
```

### Opción 3: Docker

```bash
docker compose up --build
```

## URLs útiles

- API: `http://localhost:8080`
- Swagger: `http://localhost:8080/swagger-ui.html`
- H2 Console: `http://localhost:8080/h2-console`
- JDBC URL H2: `jdbc:h2:mem:bankcoredb`
- Usuario H2: `sa`
- Contraseña H2: vacía

## Endpoints principales

### Clientes

```text
GET    /api/clientes
POST   /api/clientes
GET    /api/clientes/{id}
PUT    /api/clientes/{id}
DELETE /api/clientes/{id}
```

### Cuentas

```text
GET /api/cuentas
POST /api/cuentas
GET /api/cuentas/{id}
GET /api/cuentas/{id}/saldo
PUT /api/cuentas/{id}/bloquear
PUT /api/cuentas/{id}/activar
```

### Movimientos

```text
GET  /api/cuentas/{cuentaId}/movimientos
POST /api/cuentas/{cuentaId}/ingresos
POST /api/cuentas/{cuentaId}/reintegros
```

### Transferencias

```text
GET  /api/transferencias
POST /api/transferencias
```

### Alertas antifraude

```text
GET /api/alertas-fraude
PUT /api/alertas-fraude/{id}/revisar
```

## Ejemplos de peticiones

### Crear cliente

```json
{
  "nombre": "Sergio",
  "apellidos": "Bernal Galvez",
  "dni": "12345678A",
  "email": "sergio@example.com",
  "telefono": "600111222"
}
```

### Crear cuenta

```json
{
  "iban": "ES7620770024003102575766",
  "clienteId": 1,
  "saldoInicial": 2500.00,
  "tipoCuenta": "CORRIENTE"
}
```

### Realizar transferencia

```json
{
  "cuentaOrigenId": 1,
  "cuentaDestinoId": 2,
  "cantidad": 150.00,
  "concepto": "Pago de servicio"
}
```

## Tests incluidos

El proyecto incluye pruebas con JUnit para validar:

- Ingreso que aumenta correctamente el saldo.
- Transferencia rechazada por saldo insuficiente.
- Transferencia superior a 3.000 € en revisión antifraude.
- Transferencia normal completada con actualización de saldos.
- Transferencia rechazada desde cuenta bloqueada.

Ejecutar tests:

```bash
mvn test
```

## CI/CD básico

Incluye un workflow de GitHub Actions en:

```text
.github/workflows/ci.yml
```

Este workflow ejecuta:

```bash
mvn test
mvn package -DskipTests
```

## Cómo presentarlo en el CV

**BankCore Lite API — Backend Java Spring Boot**  
API REST bancaria desarrollada con Java 17 y Spring Boot para gestionar clientes, cuentas, movimientos y transferencias. Incluye validaciones de negocio, control de saldo, bloqueo de cuentas, reglas antifraude básicas, documentación Swagger, pruebas con JUnit, Docker y pipeline básico con GitHub Actions.

**Tecnologías:** Java 17 · Spring Boot · REST API · Spring Data JPA · Hibernate · SQL · JUnit · Swagger · Docker · GitHub Actions
