# Device Management Service

This service exposes CRUD and lifecycle management APIs for devices,
enforcing business rules around device state transitions and immutability.

------------------------------------------------------------------------

## Tech Stack

-   Java 21
-   Spring Boot 3.5.9
-   Maven
-   OpenAPI / Swagger (springdoc)

------------------------------------------------------------------------

## Build & Run

### Run locally

``` bash
./mvnw clean spring-boot:run
```

The application will start on:

    http://localhost:8080

------------------------------------------------------------------------

## Domain Design

### Device

A `Device` represents a physical device managed by the system.

**Fields** 
- `id` (UUID): Unique identifier
- `name` (String): Device name
- `brand` (String): Manufacturer / brand
- `state` (enum): `AVAILABLE`, `IN_USE`, `INACTIVE`
- `createdAt` (Instant): Creation timestamp (immutable)

### State Model

-   `AVAILABLE` --- device can be assigned or used
-   `IN_USE` --- device is currently in use
-   `INACTIVE` --- device is disabled or retired

### Business Rules

-   `createdAt` cannot be updated after creation.
-   When a device is `IN_USE`:
    -   `name` and `brand` cannot be updated.
    -   the device cannot be deleted.

### API Contract Notes

-   **PUT** `/v1/devices/{id}` replaces all mutable fields; unspecified fields are overwritten
-   **PATCH** `/v1/devices/{id}` updates only the provided fields

------------------------------------------------------------------------

## API Documentation (OpenAPI / Swagger)

After starting the application, API documentation is available at:

-   Swagger UI\
    http://localhost:8080/swagger-ui/index.html

-   OpenAPI specification\
    http://localhost:8080/v3/api-docs

------------------------------------------------------------------------

## API Versioning

All endpoints are versioned under:

    /v1

------------------------------------------------------------------------

## API Endpoints

### Devices API

-   **POST** `/v1/devices`\
    Create a new device

-   **GET** `/v1/devices/{id}`\
    Fetch a single device by id

-   **GET** `/v1/devices`\
    Fetch all devices\
    Supports optional filtering:

    -   `brand` → `/v1/devices?brand=Apple`
    -   `state` → `/v1/devices?state=IN_USE`
    -   combined → `/v1/devices?brand=Apple&state=AVAILABLE`

-   **PUT** `/v1/devices/{id}`\
    Fully update an existing device

-   **PATCH** `/v1/devices/{id}`\
    Partially update an existing device

-   **DELETE** `/v1/devices/{id}`\
    Delete a device

------------------------------------------------------------------------
