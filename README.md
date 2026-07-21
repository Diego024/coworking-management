# Coworking Management API

Backend desarrollado en **Java 17** y **Spring Boot 3** para la administración de espacios de coworking y reservas.

El proyecto fue construido como solución a una prueba técnica, priorizando una arquitectura limpia, separación de responsabilidades y buenas prácticas de desarrollo.

---

# Tabla de contenidos

- [Características](#Características)
- [Arquitectura](#Arquitectura)
- [Tecnologías utilizadas](#Tecnologías-utilizadas)
- [Estructura del proyecto](#Estructura-del-proyecto)
- [Modelo de datos](#Modelo-de-datos)
- [Flujo de creación de reservas](#Flujo-de-creación-de-reservas)
- [Patrones de diseño implementados](#Patrones-de-diseño-implementados)
- [Seguridad](#Seguridad)
- [Integración con servicio de pagos](#Integración-con-servicio-de-pagos)
- [Reporte de ocupación](#Reporte-de-ocupación)
- [Caché](#Caché)
- [Notificaciones asíncronas](#Notificaciones-asíncronas)
- [API REST](#API-REST)
- [Ejecución del proyecto](#Ejecución-del-proyecto)
- [Docker](#Docker)
- [Colección Postman](#Colección-Postman)
- [Pruebas](#Pruebas)
- [Mejoras futuras](#posibles-mejoras)

---

# Características

El sistema permite:

- Registro y autenticación mediante JWT.
- Administración de espacios de coworking.
- Creación de reservas.
- Prevención de reservas solapadas.
- Cancelación de reservas.
- Gestión diferenciada por roles.
- Reporte de ocupación por rango de fechas.
- Integración simulada con un gateway de pagos.
- Circuit Breaker con Resilience4j.
- Notificaciones asíncronas mediante eventos de dominio.
- Cache para optimizar los reportes.

---

# Arquitectura

El proyecto sigue una arquitectura en capas con responsabilidades claramente separadas.

| Capa        | Responsabilidad                                |
| ----------- | ---------------------------------------------- |
| Controller  | Expone los endpoints REST                      |
| Service     | Implementa los casos de uso                    |
| Repository  | Acceso a datos mediante Spring Data JPA        |
| Entity      | Modelo persistente                             |
| DTO         | Comunicación entre cliente y servidor          |
| Mapper      | Conversión Entity ↔ DTO mediante MapStruct     |
| Validation  | Reglas de negocio reutilizables                |
| Pipeline    | Orquestación del flujo de creación de reservas |
| Strategy    | Cálculo del precio según tipo de espacio       |
| Integration | Integraciones externas                         |
| Event       | Publicación y consumo de eventos               |
| Security    | JWT y autorización                             |
| Exception   | Manejo centralizado de errores                 |

El objetivo principal fue mantener cada componente con una única responsabilidad, facilitando el mantenimiento y la evolución del sistema.

---

# Tecnologías utilizadas

- Java 17
- Spring Boot 3
- Spring Security
- Spring Data JPA
- PostgreSQL 16
- Flyway
- MapStruct
- Lombok
- JWT (JJWT)
- Spring Cache
- Resilience4j
- WireMock
- Docker
- Docker Compose
- OpenAPI / Swagger
- Maven

---

# Estructura del proyecto

```text
src/main/java/com/company/coworking/management

├── common
├── config
├── controller
├── dto
├── entity
├── enums
├── event
├── exception
├── integration
├── mapper
├── pipeline
├── repository
├── security
└── service
```

Cada paquete agrupa componentes pertenecientes a una misma responsabilidad funcional.

---

# Modelo de datos

El sistema está compuesto por tres entidades principales.

```text
User
 1
 │
 *
Reservation
 *
 │
 1
Space
```

### User

Representa los usuarios autenticados del sistema.

### Space

Representa los espacios disponibles para reservar.

### Reservation

Relaciona usuarios y espacios almacenando toda la información de la reserva.

---

# Flujo de creación de reservas

La creación de una reserva se implementó utilizando un Pipeline para desacoplar cada etapa del proceso.

```text
ReservationController
↓
ReservationService
↓
ReservationPipeline
↓
LoadUserStep
↓
LoadSpaceStep
↓
ValidateReservationStep
↓
CalculateReservationPriceStep
↓
ValidatePaymentStep
↓
CreateReservationStep
↓
PublishReservationEventStep
```

## 1. LoadUserStep

Obtiene el usuario autenticado utilizando Spring Security.

---

## 2. LoadSpaceStep

Carga el espacio solicitado desde la base de datos.

---

## 3. ValidateReservationStep

Ejecuta todas las validaciones de negocio.

Entre ellas:

- Existencia del espacio
- Rango de fechas válido
- Duración mínima
- Disponibilidad del espacio

---

## 4. CalculateReservationPriceStep

Calcula el precio utilizando la estrategia correspondiente al tipo de espacio.

---

## 5. ValidatePaymentStep

Invoca el servicio externo de validación de pago.

Los posibles resultados son:

- APPROVED
- DECLINED
- PENDING_CIRCUIT_OPEN

---

## 6. CreateReservationStep

Persiste la reserva.

Si el pago fue exitoso:

```text
CONFIRMED
```

Si el Circuit Breaker se encuentra abierto:

```text
PENDING_PAYMENT
```

---

## 7. PublishReservationEventStep

Publica un evento de dominio que posteriormente será utilizado para notificar al usuario.

---

# Patrones de diseño implementados

## Pipeline

La creación de reservas fue dividida en pasos independientes.

Esto permite:

- Reducir el tamaño de los servicios
- Facilitar pruebas unitarias
- Simplificar el mantenimiento
- Extender el flujo fácilmente

---

## Strategy

Se utiliza para calcular el precio dependiendo del tipo de espacio.

```text
PricingStrategy
 ├── DeskPricingStrategy
 ├── MeetingRoomPricingStrategy
 └── AuditoriumPricingStrategy
```

Agregar un nuevo tipo de espacio únicamente requiere implementar una nueva estrategia.

---

## Chain of Responsibility

Las reglas de validación se ejecutan mediante una cadena de validaciones independientes.

```text
ReservationValidationChain
├── SpaceExistsValidation
├── ReservationDateValidation
├── BasicReservationValidation
└── ReservationOverlapValidation
```

Cada validación posee una única responsabilidad.

---

## Observer

Cuando una reserva es creada exitosamente se publica un evento.

```text
ReservationConfirmedEvent
```

Actualmente existe un listener encargado de enviar una notificación simulada sin bloquear la respuesta HTTP.

---

# Seguridad

La autenticación fue implementada utilizando JWT.

## Flujo

```text
Login

↓
AuthenticationManager
↓
JwtService
↓
JWT
↓
Authorization: Bearer <token>
```

Las solicitudes protegidas son interceptadas por:

```
JwtAuthenticationFilter
```

---

## Roles

### USER

Puede:

- Crear reservas
- Consultar únicamente sus reservas
- Cancelar sus reservas

---

### ADMIN

Puede:

- Administrar espacios
- Consultar todas las reservas
- Cancelar cualquier reserva
- Consultar reporte de ocupación

---

# Integración con el Gateway de Pago

Para simular un proveedor externo de pagos se utiliza WireMock.

```text
Reservation
↓
PaymentGatewayClient
↓
WireMock
↓
Respuesta simulada
```

---

## Circuit Breaker

La integración está protegida mediante Resilience4j.

Configuraciones implementadas:

- Sliding Window
- Failure Rate Threshold
- Slow Call Threshold
- Retry
- Half Open
- Health Indicator

Cuando el servicio presenta fallos consecutivos:

```text
Closed
↓
Open
↓
Half Open
↓
Closed
```

Si el circuito permanece abierto la reserva no falla.

Se registra con estado:

```
PENDING_PAYMENT
```

---

# Reporte de ocupación

El sistema permite consultar el porcentaje de ocupación de cada espacio para un rango de fechas.

El cálculo se realiza dinámicamente utilizando únicamente las reservas activas del rango solicitado.

Fórmula utilizada:

```text
    Horas Reservadas
_______________________  ×  100
Horas Totales del Rango
```

Los resultados son cacheados para evitar recalcular el reporte continuamente.

---

# Caché

Se utiliza Spring Cache mediante:

```
@Cacheable
```

para almacenar temporalmente el reporte de ocupación.

Cuando una reserva es creada o cancelada se limpia automáticamente el caché mediante:

```
@CacheEvict
```

garantizando que el reporte siempre refleje información actualizada.

---

# Notificaciones asíncronas

El envío de notificaciones no forma parte del flujo principal de creación de reservas.

Una vez confirmada la reserva se publica un evento:

```
ReservationConfirmedEvent
```

que posteriormente es consumido por un listener asíncrono.

Actualmente la notificación consiste en un log simulando el envío de un correo electrónico.

Esta implementación desacopla completamente la lógica de reservas del mecanismo de notificación.

---

# API REST

## Autenticación

| Método | Endpoint       |
| ------ | -------------- |
| POST   | /auth/register |
| POST   | /auth/login    |

---

## Espacios

| Método | Endpoint    |
| ------ | ----------- |
| POST   | /space      |
| GET    | /space      |
| PUT    | /space/{id} |
| DELETE | /space/{id} |

---

## Reservas

| Método | Endpoint                 |
| ------ | ------------------------ |
| POST   | /reservation             |
| GET    | /reservation             |
| GET    | /reservation/all         |
| PATCH  | /reservation/{id}/cancel |

---

## Reportes

| Método | Endpoint                  |
| ------ | ------------------------- |
| GET    | /reports/spaces/occupancy |

---

# Ejecución del proyecto

## Clonar

```bash
git clone https://github.com/Diego024/coworking-management.git
```

---

## Levantar con Docker Compose

```bash
docker compose up
```

Se iniciarán automáticamente:

- PostgreSQL
- WireMock
- Coworking API

---

## Swagger

```
http://localhost:8080/coworking-api/swagger-ui.html
```

---

## Actuator

```
http://localhost:8080/coworking-api/actuator/health
```

```
http://localhost:8080/coworking-api/actuator/circuitbreakers
```

---

# Docker

El proyecto utiliza Docker Compose para levantar todo el entorno necesario.

## PostgreSQL

- Base de datos principal.

## WireMock

- Simulación del proveedor de pagos.

## Coworking API

- Aplicación Spring Boot.

La aplicación recibe su configuración mediante variables de entorno:

```
DATABASE_URL
DATABASE_USERNAME
DATABASE_PASSWORD

PAYMENT_GATEWAY_URL
```

---

# Colección Postman

El repositorio incluye una colección de Postman con todos los endpoints implementados.

La colección permite:

- Registrar usuarios
- Autenticarse
- Administrar espacios
- Crear reservas
- Cancelar reservas
- Consultar reportes

---

# Pruebas

Se implementaron:

### Pruebas unitarias

Utilizando:

- JUnit 5
- Mockito

Cubriendo principalmente:

- Creación de reservas
- Cálculo de tarifas
- Cancelación de reservas
- Reportes

---

### Pruebas de integración

Utilizando:

- SpringBootTest
- Testcontainers

Validando el comportamiento completo de los principales casos de uso contra una base de datos real.

---

# Posibles mejoras

Con más tiempo se podrían incorporar mejoras como:

- Paginación para consultas de reservas y espacios;
- Refresh Tokens para JWT;
- Ampliar la cobertura de pruebas automatizadas.
- Integración con un proveedor de correo electrónico real;
- Auditoría funcional para registrar cambios sobre las reservas;
- Incorporación de pruebas de carga y concurrencia para validar escenarios de alta demanda.
- Implementar flujo de CI/CD para asegurar la integración continua.
