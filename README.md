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
- API Contract Notes
- Error Handling
- Build & Run
- OpenAPI Documentation
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
|--------|------------|
| Language | Java 21 |
| Framework | Spring Boot 3.5.x |
| Build Tool | Maven |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Migrations | Flyway |
| API Docs | OpenAPI 3 / Swagger |
| Testing | JUnit 5, Mockito, Testcontainers |
| Containerization | Docker, Docker Compose |

---

## Architecture Overview

```
Controller → Service → Repository → Database
```

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

- `createdAt` is immutable after creation
- When a device is in `IN_USE` state:
  - `name` and `brand` cannot be updated
  - the device cannot be deleted
- **PUT** replaces all mutable fields
- **PATCH** updates only provided fields
- Empty PATCH requests are rejected with `400 Bad Request`

---

## API Contract Notes

- `state` is **mandatory** when creating a device.
- `createdAt` is system-managed and cannot be provided or modified by clients.
- **PUT** performs a full update and overwrites all mutable fields.
- **PATCH** performs a partial update; empty PATCH is rejected.
- Domain rule violations return **409 Conflict**.
- Invalid UUIDs or malformed requests return **400 Bad Request**.
- Non-existing resources return **404 Not Found**.

---

## Error Handling

The API uses **RFC 7807 – Problem Details for HTTP APIs**.

---

## Build & Run

### Prerequisites

- Java 21+
- Maven 3.9+
- Docker & Docker Compose

### Run with Docker Compose

```bash
docker compose up --build -d
```

Stop:

```bash
docker compose down
```

Reset DB:

```bash
docker compose down -v
```

---

## OpenAPI Documentation

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI spec: http://localhost:8080/v3/api-docs

---

## Testing Strategy

| Layer | Tool |
|------|------|
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

## Docker & Docker Compose

- App and DB in separate containers
- Environment variables externalized
- Flyway migrations on startup

---

## Implementation Status

### Completed

- CRUD APIs
- Domain validations
- Exception handling
- Repository & integration tests
- Docker setup

---

## Future Enhancements

### Security
- OAuth2 / OIDC
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
- Metrics, tracing, centralized logging

---

## Notes for Reviewers

This project was implemented as part of a technical assessment
with focus on correctness, clarity, and production readiness.
