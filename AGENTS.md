# AGENTS.md - Order Service AI Guidance

## Architecture Overview

**Order Service** is a Spring Boot 3.2 REST API for e-commerce order management. It uses:
- **Framework**: Spring Boot with Spring Data JPA
- **Database**: H2 in-memory database (development) with auto-schema creation
- **Java**: JDK 21 with Jakarta persistence API
- **API Documentation**: OpenAPI 3.0 (Swagger UI at `http://localhost:8080/swagger-ui.html`)

### Core Components

```
Controller → Service → Repository → Entity
OrderController → OrderService → OrderRepository → Order
```

**Key Principle**: Strict layering - controllers handle HTTP, services handle business logic, repositories handle data access.

## Data Model & Constraints

The `Order` entity has hard database constraints enforced both at the **database level** (SQL CHECK constraints) and **application level** (Jakarta validation):

```
Order {
  orderId: Long (PK, auto-increment)
  userId: Long (NOT NULL, index: idx_user_id)
  productId: Long (NOT NULL, index: idx_product_id)
  quantity: int (NOT NULL, CHECK > 0)
  price: double (NOT NULL, CHECK > 0)
  status: OrderStatus enum (NOT NULL, CHECK IN ('CREATED','PAID','CANCELLED'))
  createdAt: LocalDateTime (NOT NULL, auto-generated on creation, immutable)
}
```

**Critical Pattern**: Use `OrderStatus` enum (not strings) for type safety. Status defaults to `CREATED`.

### Validation Layer

- **Request DTOs** use Jakarta validation annotations (`@NotNull`, `@Min`) with custom messages
- **Database constraints** provide additional safety via CHECK constraints
- Validation errors return HTTP 400 with field-level error map

## Developer Workflows

### Build & Run

```bash
# Maven wrapper (Windows)
mvnw.cmd clean install
mvnw.cmd spring-boot:run

# Application starts on port 8080
# H2 Console accessible at http://localhost:8080/h2-console
# Swagger UI at http://localhost:8080/swagger-ui.html
```

**Key Setting**: `spring.jpa.hibernate.ddl-auto=create-drop` - tables are **recreated on startup** and **destroyed on shutdown**. Data doesn't persist between runs in development.

### Database Access

- **H2 Console**: User=`sa`, Password=`password`, URL=`jdbc:h2:mem:testdb`
- **SQL Logging**: Enabled by default (`spring.jpa.show-sql=true`). Check console to debug queries.
- **Indexes**: Four defined for common queries - use them when adding filters

### Testing Strategy

Entity validation happens in this order:
1. Field validation in DTOs (`@Valid` on request binding)
2. Database constraints enforcement (CHECK, NOT NULL)
3. Custom exception handling (`OrderNotFoundException`)

**If adding new validations**: Add to `OrderRequestDto` first, then add database constraints to `Order` entity's `columnDefinition` attributes.

## Project-Specific Conventions

### DTO Pattern (Critical)

- **Request**: `OrderRequestDto` (userId, productId, quantity, price) - NO status field
- **Response**: `OrderResponseDto` (includes orderId, status, createdAt)
- Service handles mapping: `mapToResponseDto()` is the single conversion point

**Pattern**: Always map entities to DTOs - never expose entities directly over HTTP.

### Exception Handling

- Custom `OrderNotFoundException` extends `RuntimeException` (unchecked)
- `GlobalExceptionHandler` (@RestControllerAdvice) catches all exceptions centrally
- Validation errors return `Map<String, String>` with field names as keys

**When adding endpoints**: Throw `OrderNotFoundException` for missing resources (404). GlobalExceptionHandler catches it automatically.

### Entity Immutability

- `createdAt` field has `updatable = false` annotation - prevents accidental modifications
- Uses `@CreationTimestamp` to auto-populate from Hibernate
- No setter modifications should occur post-creation for audit trails

## Integration Points & API Endpoints

All endpoints return consistent JSON structures using DTOs:

```
POST   /orders                    → Create order (returns 201)
GET    /orders/{id}               → Get single order (returns 200 or 404)
GET    /orders?page=0&size=10     → List orders with pagination
```

**Pagination**: Page 0 is the first page, default size 10. Returns `Page<OrderResponseDto>` with metadata.

### OpenAPI Configuration

- Centralized config in `OpenApiConfig.java` (Spring `@Configuration`)
- All endpoints documented with `@Operation` and `@ApiResponses` annotations
- Swagger UI auto-generated from code annotations

**When adding endpoints**: Add `@Operation` summary + `@ApiResponses` with HTTP codes. Swagger auto-populates from DTOs.

## Build & Dependencies

**Maven Project Structure**:
- Java 21 target version
- Spring Boot 3.2.0 (starter-web, starter-data-jpa, starter-validation)
- H2 runtime dependency
- SpringDoc OpenAPI 2.2.0 for Swagger integration
- No custom dependency management needed for standard operations

**Build Artifact**: `orderservice-0.0.1-SNAPSHOT.jar`

## Critical DO's & DON'Ts

✅ **DO**:
- Use `@Valid` on request parameters and bind DTO fields
- Map all entities to DTOs before HTTP response
- Throw `OrderNotFoundException` for missing resources
- Let `GlobalExceptionHandler` catch exceptions
- Use repository methods as-is (JpaRepository provides CRUD)

❌ **DON'T**:
- Return entity objects directly in endpoints
- Add custom SQL queries to `OrderRepository` without a specific reason
- Modify `createdAt` after creation
- Use invalid `OrderStatus` values (only CREATED, PAID, CANCELLED)
- Bypass the service layer for business logic

## Common Development Tasks

**Add a new GET filter** (e.g., by status):
1. Add method to `OrderRepository`: `List<Order> findByStatus(OrderStatus status)`
2. Add service method that calls repository + maps to DTO
3. Add controller endpoint with `@GetMapping` + OpenAPI annotations

**Add a new field to Order**:
1. Add to `Order` entity with proper JPA annotations + CHECK constraints
2. Add to both DTOs (RequestDto if it's settable, ResponseDto always)
3. Update service's `mapToResponseDto()` method
4. Database schema auto-recreates on next run due to `create-drop` setting

**Validate a constraint worked**:
- Check application console for SQL logs
- Visit H2 console and run `SHOW CONSTRAINTS` or query the INFORMATION_SCHEMA
- Unit test validation by catching `DataIntegrityViolationException` when appropriate

---

**Last Updated**: April 5, 2026 | **Status**: Development Environment

