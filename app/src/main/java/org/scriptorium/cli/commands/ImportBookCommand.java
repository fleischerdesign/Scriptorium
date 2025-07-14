package org.scriptorium.cli.commands;

import org.scriptorium.cli.Command;
import org.scriptorium.core.domain.Author; // Make sure to import Author
import org.scriptorium.core.domain.Book;
import org.scriptorium.core.services.BookImportService;
import org.scriptorium.core.http.SimpleHttpClient;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors; // Needed for stream operations

public class ImportBookCommand implements Command {
    private final BookImportService bookImportService;
    private final Scanner scanner;

    public ImportBookCommand() {
        this.bookImportService = new BookImportService(new SimpleHttpClient());
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String getName() {
        return "import-book";
    }

    @Override
    public void execute() {
        System.out.print("Enter book title to import: ");
        String titleSearchQuery = scanner.nextLine(); // Renamed variable for clarity

        try {
            List<Book> books = bookImportService.importBooksByTitle(titleSearchQuery);
            
            if (books.isEmpty()) {
                System.out.println("No books found for: " + titleSearchQuery);
                return;
            }

            // Display detailed numbered list
            System.out.println("\nFound books:");
            System.out.println("----------------------------------------------------------------------------------------------------"); // Adjusted width
            System.out.printf("%-4s %-30s %-28s %-6s %-15s %-15s%n",
                "No.", "Title", "Author(s)", "Year", "Publisher", "ISBN (First)"); // Updated header for authors and ISBN
            System.out.println("----------------------------------------------------------------------------------------------------");
            
            for (int i = 0; i < books.size(); i++) {
                Book book = books.get(i);

                // --- Format Authors ---
                // Get all author names and join them with ", "
                String authorsDisplay = book.getAuthors().stream()
                                            .map(Author::getName)
                                            .collect(Collectors.joining(", "));
                // Provide a fallback if no author names are available (though Book entity should ensure at least one)
                if (authorsDisplay.isEmpty()) {
                    authorsDisplay = "Unknown";
                }

                // --- Format ISBN ---
                // Display the first ISBN found, or "N/A" if the list is empty
                String isbnDisplay = book.getIsbns().isEmpty() ? "N/A" : book.getIsbns().get(0);

                // --- Format Publisher ---
                // Access the main publisher and get its name, with a null check
                String publisherDisplay = book.getMainPublisher() != null ?
                                          book.getMainPublisher().getName() : "Unknown";

                System.out.printf("%-4d %-30.30s %-28.28s %-6d %-15.15s %-15.15s%n", // Adjusted formatting width
                    i + 1,
                    book.getTitle() != null ? book.getTitle() : "Unknown", // Fallback for title
                    authorsDisplay,
                    book.getPublicationYear(),
                    publisherDisplay,
                    isbnDisplay);
            }
            System.out.println("----------------------------------------------------------------------------------------------------");

            // Selection prompt
            System.out.print("\nEnter book number to import (0 to cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (choice > 0 && choice <= books.size()) {
                Book selectedBook = books.get(choice - 1);
                // Displaying selected book with all authors
                String selectedAuthorsDisplay = selectedBook.getAuthors().stream()
                                                    .map(Author::getName)
                                                    .collect(Collectors.joining(", "));
                System.out.printf("Selected for import (but not yet saved): %s by %s%n",
                                  selectedBook.getTitle(), selectedAuthorsDisplay);
                
                // TODO: Actual import (saving) logic here
                System.out.println("This book would now be saved to your library.");

            } else {
                System.out.println("Import cancelled.");
            }
        } catch (IllegalArgumentException e) {
            // Catch specific validation errors from Book entity construction via factory
            System.err.println("Validation Error during book processing: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during import: " + e.getMessage());
            e.printStackTrace(); // Keep for debugging, remove in production
        }
    }
}