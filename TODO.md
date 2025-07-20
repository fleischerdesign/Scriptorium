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
*   **Audit Log / History Tracking:**
    *   A system that logs all important actions (e.g., book loan, return, creation/modification of records), including the user who performed the action and the timestamp.
    *   This would require a new `AuditLogEntry` entity, an `AuditLogRepository`, and an `AuditLogService`. Existing service methods would need to be extended to create audit entries. A new `AuditCommand.java` could allow viewing the log.
*   **Fee and Fine Management:**
    *   Implementation of a system for calculating and managing fees for overdue books or damaged copies.
    *   This could include a `Fine` entity, a `FineService`, and corresponding commands such as `FineCalculateCommand.java`, `FinePayCommand.java`, and `FineListCommand.java`. The `LoanService` would need to be adjusted to calculate fees for late returns.
*   **Wishlist / Acquisition Suggestions:**
    *   Allow users to suggest books that the library should acquire, or maintain an internal wishlist for the librarian.
    *   This would require a `WishlistItem` entity, a `WishlistService`, and commands such as `WishlistAddCommand.java`, `WishlistListCommand.java`, and `WishlistFulfillCommand.java`.
*   **Data Validation Rules and Constraints:**
    *   Implement more robust data validation at the service or domain layer to ensure data integrity. This could include validating ISBN formats, ensuring dates are logical (e.g., publication date not in the future), or enforcing uniqueness constraints beyond just primary keys (e.g., unique email addresses for users).
    *   This would involve adding validation logic within the `core/services/` classes and potentially custom annotations or utility classes for common validation patterns.
*   **Customizable Output Formats:**
    *   Extend `*ListCommand`s to allow users to specify the output format (e.g., CSV, JSON, or a custom delimited format) for easier scripting and integration with other tools.
    *   This would involve adding options to the command-line arguments (e.g., `--format=json`) and implementing formatters within the CLI layer or a new `OutputFormatter` utility.
*   **Backup and Restore Functionality:**
    *   Provide commands to easily back up the entire database or specific datasets to a file, and to restore from a backup.
    *   This is crucial for data safety and recovery. It would involve commands like `BackupCommand.java` and `RestoreCommand.java`, and corresponding logic in a `BackupService.java` that interacts with the database.
*   **Advanced Reporting and Analytics:**
    *   Develop more sophisticated reporting capabilities, possibly including custom report generation based on user-defined criteria, or graphical representations (even if text-based for CLI) of data trends (e.g., growth of library collection over time, loan patterns).
    *   This would expand on the "Reports and Statistics" idea, requiring more complex queries and data aggregation in a dedicated `ReportingService.java` and new `ReportGenerateCommand.java` with various sub-options.
*   **Integration with External Catalog APIs (beyond OpenLibrary):**
    *   Expand the `BookImportService` or create a more generic `CatalogIntegrationService` to fetch metadata from other external sources (e.g., WorldCat, Google Books API) for richer book information or to support different types of media if the library expands.
    *   This would involve new API clients, DTOs for different external data structures, and potentially a strategy pattern for selecting the appropriate catalog source.
*   **Barcode/RFID Integration:**
    *   Implement functionality to read barcodes or RFID tags for books and copies, streamlining operations like checking out, returning, or inventory management.
    *   This would involve integrating with external hardware (e.g., a barcode scanner via standard input or a serial port) and new commands like `ScanCheckoutCommand.java` or `ScanReturnCommand.java`.
*   **Performance Monitoring and Metrics:**
    *   Add basic performance monitoring to track the execution time of commands or service operations, helping to identify bottlenecks.
    *   This could involve simple logging of execution times or integrating a lightweight metrics library. A new `MetricsCommand.java` could display aggregated performance data.
*   **User Feedback and Rating System:**
    *   Allow users to rate books they have read and provide feedback or reviews. This could help other users discover new books and provide valuable insights for librarians.
    *   This would involve new `Rating` and `Review` entities, corresponding services, and commands like `BookRateCommand.java` and `BookReviewCommand.java`.
*   **Automated Data Cleanup/Archiving:**
    *   Implement commands or scheduled tasks to automatically clean up old, irrelevant data (e.g., very old loan records, inactive user accounts) or archive them to a separate storage for historical purposes.
    *   This would involve new `CleanupCommand.java` and `ArchiveService.java` to manage the lifecycle of data, ensuring the database remains performant and relevant.
*   **Event Logging and Monitoring:**
    *   Beyond just audit logs, implement a more comprehensive event logging system that captures application events (e.g., system startup/shutdown, errors, warnings, significant user actions) for operational monitoring and debugging.
    *   This would involve configuring a logging framework (if not already robustly in place), defining log levels, and potentially a `LogViewerCommand.java` for administrators.
*   **Resource Booking/Room Reservation:**
    *   If the library has study rooms, equipment, or other resources that can be booked, implement a system for users to reserve these resources.
    *   This would involve new entities for `Resource` and `Booking`, a `BookingService`, and commands like `ResourceListCommand.java`, `ResourceBookCommand.java`, and `BookingCancelCommand.java`.
*   **Scheduled Tasks/Automation:**
    *   Implement a mechanism to schedule recurring tasks, such as generating daily reports, sending out overdue reminders, or performing database backups.
    *   This could involve integrating with a job scheduling library (e.g., Quartz Scheduler) and new commands to define and manage scheduled jobs.
*   **Suggestive Search/Autocompletion for Input:**
    *   Beyond command autocompletion, implement suggestive search for data inputs (e.g., when typing an author's name, suggest existing authors from the database; when typing a book title, suggest existing titles).
    *   This would enhance user experience by reducing typing errors and speeding up data entry, requiring integration with the CLI's input handling and database lookups.
*   **Testing:**
    *   Add integration tests for the `BookImportService` and other services.
    *   Ensure comprehensive unit test coverage for all core logic.