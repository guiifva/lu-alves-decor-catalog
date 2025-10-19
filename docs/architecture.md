# Project Architecture: Lu Alves Decor Catalog

## 1. Project Overview

This project is a catalog manager for party decorations for "Lu Alves Decor". The main goal is to provide a system to manage themes and their associated items, allowing for easy creation, visualization, updating, and deletion of decoration themes.

## 2. Architecture

The application will follow a classic 3-layer architecture to ensure separation of concerns, maintainability, and scalability.

### 2.1. Presentation Layer (Controller)

This layer is responsible for handling user requests and returning responses. It will expose a RESTful API for the front-end (or any other client) to interact with.

*   **Endpoints:**
    *   `POST /themes`: Creates a new theme with its items.
    *   `GET /themes`: Lists all themes. Supports pagination (`page`, `size`) and filtering by `name`.
    *   `GET /themes/{id}`: Retrieves a specific theme by its ID.
    *   `PUT /themes/{id}`: Updates a theme's information and its items.
    *   `DELETE /themes/{id}`: Deletes a theme.

### 2.2. Domain/Business Layer (Service)

This layer contains the core business logic of the application. It will handle the validation, processing, and orchestration of data between the presentation and data layers.

*   **Services:**
    *   `ThemeService`: Implements the business rules for managing themes.
        *   `createTheme(theme)`: Validates and creates a new theme.
        *   `getAllThemes(page, size, name)`: Retrieves a paginated and/or filtered list of themes.
        *   `getThemeById(id)`: Retrieves a single theme.
        *   `updateTheme(id, theme)`: Validates and updates an existing theme. When updating, the existing list of items will be completely replaced by the new list provided.
        *   `deleteTheme(id)`: Deletes a theme and its associated items.

### 2.3. Data/Persistence Layer (Repository)

This layer is responsible for all data access and storage. It will interact with the database to perform CRUD operations. We will use R2DBC for reactive database access.

*   **Repositories:**
    *   `ThemeRepository`: Manages the persistence of `Theme` entities.
    *   `ItemRepository`: Manages the persistence of `Item` entities.

*   **Entities:**
    *   `Theme`: Represents a decoration theme.
        *   `id`: Unique identifier.
        *   `name`: Theme name (e.g., "Jungle", "Princesses").
        *   `description`: A brief description of the theme.
    *   `Item`: Represents an item within a theme.
        *   `id`: Unique identifier.
        *   `name`: Item name (e.g., "Lion plush", "Pink castle").
        *   `quantity`: The number of units of the item (e.g., 1, 50).
        *   `description`: A brief description of the item (optional).
        *   `themeId`: Foreign key to the `Theme` entity.

## 3. Functionalities

The core functionality is the CRUD for themes and their items.

*   **Create Theme:** The user provides a theme name, description, and a list of items (name, quantity, and an optional description for each). The system saves the theme and then saves the items, associating them with the newly created theme.
*   **Read Themes:** The system can return a paginated list of all themes. This list can be filtered by the theme's name. It can also return a single theme with its complete list of items.
*   **Update Theme:** The user can update the name and description of a theme. For the items, the existing list will be deleted and a new list of items will be created and associated with the theme. This simplifies the process by avoiding complex item synchronization logic for now.
*   **Delete Theme:** When a theme is deleted, all its associated items are also deleted.

## 4. Step-by-step Implementation Plan

1.  **Setup Database:** Configure and run a PostgreSQL database instance.
2.  **Entities:** Create the `Theme` and `Item` data classes.
3.  **Repositories:** Create the `ThemeRepository` and `ItemRepository` interfaces using Spring Data R2DBC.
4.  **Services:** Implement the `ThemeService` with the business logic for the CRUD operations.
5.  **Controllers:** Create the `ThemeController` to expose the REST endpoints.
6.  **Testing:** Create unit and integration tests for the services and controllers.
