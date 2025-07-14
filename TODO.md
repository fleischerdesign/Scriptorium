# Project TODOs

This file lists tasks and potential improvements for the Scriptorium CLI application.

## Current / Immediate Tasks

*   **Implement actual book saving logic:**
    *   In `ImportBookCommand.java`, the selected book is currently only printed to the console. Implement the logic to save the book to a persistent storage (e.g., a database).

## Core Library System Features

*   **Book Management (CRUD):**
    *   `add-book`: Manually add a book to the library (for books not found via import).
    *   `list-books`: Display all books in the library, with filtering and sorting options (e.g., by author, title, genre).
    *   `update-book`: Modify details of an existing book.
    *   `delete-book`: Remove a book from the library.
    *   `search-books`: Search local library for books by various criteria.

*   **User Management:**
    *   `add-user`: Register new library users.
    *   `list-users`: Display all registered users.
    *   `update-user`: Modify user details.
    *   `delete-user`: Remove a user.

*   **Loan Management:**
    *   `borrow-book`: Record a book loan to a user.
    *   `return-book`: Mark a book as returned.
    *   `list-loans`: View current and past loans, filter by user or book.
    *   `overdue-loans`: Identify and list overdue books.

*   **Reservation Management:**
    *   `reserve-book`: Allow users to reserve books that are currently on loan.
    *   `cancel-reservation`: Cancel an existing reservation.
    *   `list-reservations`: View all active reservations.

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
