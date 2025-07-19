# Project TODOs

This file lists tasks and potential improvements for the Scriptorium CLI application.

## Current / Immediate Tasks


## Core Library System Features


## Future Enhancements / Ideas

*   **Database Integration:**
    *   Implement a `BookRepository` to handle CRUD operations with a database.
    *   Integrate a database (e.g., SQLite, H2) to persist book data.
    *   Add commands for listing, updating, and deleting books from the library.
*   **Configuration Management:**
    *   Load application configuration (e.g., API keys, database connection strings) from a file (e.g., `config.properties`, `application.yml`).
    *   Utilize the `DependencyFactory` to inject configured values into services.
*   **Caching for API Calls:**
    *   Implement a caching mechanism (e.g., in-memory cache, Redis) for `BookImportService` to reduce redundant API calls to OpenLibrary.
*   **More Robust Error Handling:**
    *   Improve user-facing error messages for network issues, parsing errors, etc.
    *   Consider more specific exception types where appropriate.
*   **User Management & Loans/Reservations:**
    *   Expand the domain model (`User`, `Loan`, `Reservation`) and implement corresponding services and commands.
*   **Advanced CLI Features:**
    *   Implement command auto-completion for subcommands and parameters (JLine3 supports this).
    *   Add more sophisticated output formatting (e.g., tables for `list-books`).
*   **Testing:**
    *   Add integration tests for the `BookImportService` and other services.
    *   Ensure comprehensive unit test coverage for all core logic.