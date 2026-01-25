# Lu Alves Decor Catalog - GEMINI Context

## Project Overview
This project is a reactive REST API built with **Kotlin** and **Spring Boot (WebFlux)** for managing decoration items and themes for "Lu Alves Decor". It allows creating, listing, updating, and deleting decoration themes and individual items, persisting data in a PostgreSQL database using **R2DBC**.

## Tech Stack & Architecture
*   **Language:** Kotlin (JVM Target 21)
*   **Framework:** Spring Boot 3.5.6 (WebFlux)
*   **Database Access:** Spring Data R2DBC (Reactive) + PostgreSQL
*   **Database Migration:** Flyway (Note: configured for tests, requires manual or external execution for production/R2DBC environments)
*   **Documentation:** SpringDoc OpenAPI (Swagger UI)
*   **Build Tool:** Gradle (Kotlin DSL)
*   **Testing:** JUnit 5, SpringMockk, WebTestClient, H2 (InMemory)

## Key Directories & Files
*   `src/main/kotlin/nocta/lualvesdecorcatalog/`: Root package.
    *   `controller/`: REST Controllers (Web Layer). Handles HTTP requests and responses.
        *   `dto/`: Data Transfer Objects for API requests/responses.
    *   `core/`: Core business logic.
        *   `entity/`: Domain models (POJOs/Data Classes).
        *   `service/`: Business logic implementations (`@Service`).
        *   `exception/`: Custom exceptions (`ItemNotFoundException`, `DomainValidationException`).
    *   `repository/`: Database access layer (`@Repository`).
        *   `entity/`: Database specific entities (mapped to tables).
        *   `mapper/`: Extension functions to map between Domain and DB entities.
*   `src/main/resources/`: Configuration and resources.
    *   `application.yml`: Main configuration (DB connection, server port).
    *   `db/migration/`: Flyway SQL migration scripts.
*   `src/test/`: Unit and Integration tests.

## Database Schema
The project uses PostgreSQL.
*   **Table `items`** (formerly `decor_items`): Stores individual decoration items.
    *   Mapped by: `nocta.lualvesdecorcatalog.repository.entity.ItemEntity`
    *   Key columns: `id` (UUID), `sku`, `name`, `category`, `quantity_available`.
*   **Table `themes`**: Stores collections of items (Themes).
*   **Table `theme_items`**: Join table/entity for items within a theme.

## Development Workflow

### Building & Running
*   **Build Project:**
    ```bash
    ./gradlew build
    ```
*   **Run Application:**
    ```bash
    ./gradlew bootRun
    ```
    *   API URL: `http://localhost:8080`
    *   Swagger UI: `http://localhost:8080/swagger-ui.html`
    *   **Prerequisite:** A PostgreSQL instance must be running.

### Database Setup (Local)
Run a PostgreSQL container:
```bash
docker run --name decor-db -e POSTGRES_DB=decor_manager -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres:16
```

### Testing
Run all tests (Unit + Integration):
```bash
./gradlew test
```
*   Tests use H2 in-memory database and Flyway for schema initialization.
*   Integration tests are located in `src/test/kotlin/nocta/lualvesdecorcatalog/controller/`.

## Coding Conventions
1.  **Reactive/Coroutines:** Use Kotlin Coroutines (`suspend` functions) for all I/O operations (Controller -> Service -> Repository).
2.  **DTO Pattern:** Always use DTOs for Controller inputs (`Request`) and outputs (`Response`). Do not expose Entities directly.
3.  **Mappers:** Use extension functions (e.g., `toModel()`, `toEntity()`) in the Repository layer to convert between DB Entities and Domain Models.
4.  **Error Handling:** Use `RestExceptionHandler` (`@RestControllerAdvice`) to map exceptions to RFC 7807 `ProblemDetail`.
5.  **Documentation:** Annotate Controllers with `@Operation`, `@ApiResponse`, etc., to generate Swagger documentation.

## Recent Refactoring (Context)
*   **Item Unification:** The concept of `DecorItem` has been merged into a generic `Item`.
    *   Classes like `DecorItemController`, `DecorItemService` were removed.
    *   Use `ItemController`, `ItemService`, `ItemRepository` instead.
    *   Database table renamed from `decor_items` to `items`.
