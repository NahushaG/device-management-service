# Device Management Service

The **Device Management Service** is a RESTful backend application that provides
CRUD and lifecycle management capabilities for devices.  
It enforces **strict domain rules**, exposes a **clean, versioned API**, and is
designed with **production-readiness, testability, and extensibility** in mind.

---

## Table of Contents

- Overview
- Tech Stack
- Architecture Overview
- Domain Model
- Business Rules
- API Design
- Error Handling
- Build & Run
- Testing Strategy
- Manual API Test Matrix
- Docker & Docker Compose
- Implementation Status
- Future Enhancements
- Notes for Reviewers

---

## Overview

This service manages devices and their lifecycle states while enforcing
business constraints such as immutability and state-based restrictions.

Key goals of the implementation:
- Clear domain modeling
- Strong validation and error handling
- Testability at multiple layers
- Production-grade structure and documentation
- Readiness for scaling and future evolution

---

## Tech Stack

| Category | Technology |
|-------|------------|
| Language | Java 21 |
| Framework | Spring Boot 3.5.9 |
| Build Tool | Maven |
| Database | PostgreSQL 16 |
| ORM | Spring Data JPA / Hibernate |
| Migrations | Flyway |
| API Docs | OpenAPI / Swagger |
| Testing | JUnit 5, Mockito, Testcontainers |
| Containerization | Docker, Docker Compose |

---

## Architecture Overview

```
Controller → Service → Repository → Database
```

Responsibilities are clearly separated to ensure maintainability and testability.

---

## Domain Model

### Device

| Field | Type | Description |
|-----|------|-------------|
| id | UUID | Auto-generated identifier |
| name | String | Device name |
| brand | String | Manufacturer |
| state | Enum | AVAILABLE, IN_USE, INACTIVE |
| createdAt | Instant | Immutable creation timestamp |

---

## Business Rules

- `createdAt` is immutable
- When device is `IN_USE`:
    - name and brand cannot be updated
    - device cannot be deleted
- PUT replaces all mutable fields
- PATCH updates only provided fields
- Empty PATCH requests are rejected

---

## API Endpoints

| Method | Endpoint | Description |
|------|----------|-------------|
| POST | /v1/devices | Create device |
| GET | /v1/devices/{id} | Get device |
| GET | /v1/devices | List devices |
| PUT | /v1/devices/{id} | Full update |
| PATCH | /v1/devices/{id} | Partial update |
| DELETE | /v1/devices/{id} | Delete device |

---

## Error Handling

Errors follow **RFC 7807 Problem Details** format.

---

## Build & Run

### Prerequisites

* Java 21+
* Maven 3.9+ (or Maven Wrapper `./mvnw`)
* Docker & Docker Compose
### Run with Docker Compose (Application + PostgreSQL)

This mode runs the full stack in containers and closely resembles a production setup.

#### Build and start all services

```bash
docker compose up --build
```

#### Access the application

* Swagger UI:
  [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

#### Stop services

```bash
docker compose down
```

#### Reset database (fresh start)

```bash
docker compose down -v
```


## Quick API Test

Create a device:

```bash
curl -X POST http://localhost:8080/v1/devices \
  -H "Content-Type: application/json" \
  -d '{"name":"iPhone 15","brand":"Apple","state":"AVAILABLE"}'
```

Fetch all devices:

```bash
curl http://localhost:8080/v1/devices
```

---

## OpenAPI / Swagger Documentation

The API is fully documented using **OpenAPI 3**.

- Swagger UI  
  http://localhost:8080/swagger-ui/index.html

- OpenAPI JSON  
  http://localhost:8080/v3/api-docs

Includes:
- Endpoints
- Schemas
- Validation rules
- Error responses

---

## Testing Strategy

| Layer | Approach |
|-----|----------|
| Controller | @WebMvcTest |
| Service | Unit tests |
| Repository | @DataJpaTest |
| Integration | Testcontainers |

---

## Manual API Test Matrix

| Method | Scenario | Expected |
|------|----------|----------|
| POST | Valid device | 201 |
| POST | Invalid enum | 400 |
| GET | Not found | 404 |
| PATCH | Empty body | 400 |
| DELETE | IN_USE device | 409 |

---

## Implementation Status

### Completed

- CRUD APIs
- Validation
- Exception handling
- Repository & integration tests
- Docker setup

---

## Future Enhancements

### Security
- JWT / OAuth2
- RBAC
- Rate limiting

### Scalability
- Pagination
- Horizontal scaling
- Read replicas

### Non-Blocking
- Spring WebFlux
- Reactive DB access

### Observability
- Metrics
- Tracing
- Centralized logging

