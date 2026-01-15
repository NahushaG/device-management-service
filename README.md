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

The layered architecture ensures separation of concerns, maintainability,
and ease of testing.

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

## API Design

- RESTful, resource-oriented endpoints
- Explicit versioning via `/v1`
- Consistent HTTP status codes
- Validation-driven request handling

---

## Error Handling

The API uses **RFC 7807 – Problem Details for HTTP APIs** for all error responses.

Common scenarios:
- Validation failures → `400 Bad Request`
- Invalid UUID format → `400 Bad Request`
- Resource not found → `404 Not Found`
- Domain rule violation → `409 Conflict`

---

## Build & Run

### Prerequisites

- Java 21+
- Maven 3.9+ (or Maven Wrapper `./mvnw`)
- Docker & Docker Compose

---

### Run with Docker Compose (recommended)

Runs the full stack (application + PostgreSQL) in containers.

```bash
docker compose up --build -d
```

Access:
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- API Base URL: http://localhost:8080/v1

Stop services:

```bash
docker compose down
```

Reset database:

```bash
docker compose down -v
```

---

## OpenAPI / Swagger Documentation

The API is fully documented using **OpenAPI 3**.

- Swagger UI  
  http://localhost:8080/swagger-ui/index.html

- OpenAPI specification  
  http://localhost:8080/v3/api-docs

Includes:
- Endpoints
- Request/response schemas
- Validation constraints
- Error responses

---

## Testing Strategy

| Layer | Approach |
|------|---------|
| Controller | `@WebMvcTest` |
| Service | Unit tests |
| Repository | `@DataJpaTest` |
| Integration | `@SpringBootTest` + Testcontainers |

---

## Manual API Test Matrix

| Method | Scenario | Expected Result |
|------|----------|-----------------|
| POST | Valid device | 201 Created |
| POST | Invalid enum value | 400 Bad Request |
| GET | Non-existing device | 404 Not Found |
| PATCH | Empty request body | 400 Bad Request |
| DELETE | Device in IN_USE state | 409 Conflict |

---

## Docker & Docker Compose

- Application and database run in separate containers
- Environment variables externalized via `.env`
- Flyway migrations applied on startup

---

## Implementation Status

### Completed

- CRUD APIs
- Domain validations
- Global exception handling
- Standardized error responses
- Repository & integration tests
- Docker image and Compose setup

---

## Future Enhancements

### Security
- OAuth2 / OpenID Connect
- Role-Based Access Control (RBAC)
- API rate limiting

### Scalability
- Pagination and sorting
- Horizontal scaling
- Read replicas

### Non-Blocking Architecture
- Spring WebFlux
- Reactive database access

### Observability
- Metrics (Micrometer + Prometheus)
- Distributed tracing (OpenTelemetry)
- Centralized logging

---

## Notes for Reviewers

This project was implemented as part of a technical assessment.
The focus was on correctness, clarity, testability, and production readiness,
while leaving clear paths for future enhancements.
