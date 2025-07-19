package org.scriptorium.cli.commands.book;

import org.scriptorium.core.domain.Author;
import org.scriptorium.core.domain.Book;
import org.scriptorium.core.services.BookImportService;
import org.scriptorium.core.services.GenreService;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Picocli command for importing books from the OpenLibrary API.
 *
 * This command allows users to search for books by title and select one to "import"
 * (currently, this is a placeholder for future saving functionality).
 * It supports both direct command-line arguments and interactive input.
 */
@Command(name = "import", description = "Imports a book from OpenLibrary.")
public class BookImportCommand implements Runnable {

    private final BookImportService bookImportService;
    private final Scanner scanner;
    private final GenreService genreService;

    /**
     * Optional command-line parameter for the book title.
     * If not provided, the command will prompt the user for input.
     */
    @Parameters(index = "0", description = "The title of the book to search for.", arity = "0..1")
    private String title;

    /**
     * Constructs an {@code ImportBookCommand} with its required dependencies.
     *
     * @param bookImportService The service responsible for fetching book data.
     * @param scanner The scanner used for interactive user input.
     * @param genreService The service for managing genre entities.
     */
    public BookImportCommand(BookImportService bookImportService, Scanner scanner, GenreService genreService) {
        this.bookImportService = bookImportService;
        this.scanner = scanner;
        this.genreService = genreService;
    }

    /**
     * Executes the import book command.
     * It either uses the provided command-line argument or prompts the user for a title,
     * fetches books, displays them, and allows the user to select one.
     */
    @Override
    public void run() {
        String titleSearchQuery = title;
        if (titleSearchQuery == null) {
            System.out.print("Enter book title to import: ");
            titleSearchQuery = scanner.nextLine();
        }

        try {
            List<Book> books = bookImportService.importBooksByTitle(titleSearchQuery);

            if (books.isEmpty()) {
                System.out.println("No books found for: " + titleSearchQuery);
                return;
            }

            // Display detailed numbered list
            System.out.println("\nFound books:");
            System.out.println("----------------------------------------------------------------------------------------------------");
            System.out.printf("%-4s %-30s %-28s %-6s %-15s %-15s%n",
                "No.", "Title", "Author(s)", "Year", "Publisher", "ISBN (First)");
            System.out.println("----------------------------------------------------------------------------------------------------");

            for (int i = 0; i < books.size(); i++) {
                Book book = books.get(i);

                String authorsDisplay = book.getAuthors().stream()
                                            .map(Author::getName)
                                            .collect(Collectors.joining(", "));
                if (authorsDisplay.isEmpty()) {
                    authorsDisplay = "Unknown";
                }

                String isbnDisplay = book.getIsbns().isEmpty() ? "N/A" : book.getIsbns().get(0);

                String publisherDisplay = book.getMainPublisher() != null ?
                                          book.getMainPublisher().getName() : "Unknown";

                System.out.printf("%-4d %-30.30s %-28.28s %-6d %-15.15s %-15.15s%n",
                    i + 1,
                    book.getTitle() != null ? book.getTitle() : "Unknown",
                    authorsDisplay,
                    book.getPublicationYear(),
                    publisherDisplay,
                    isbnDisplay);
            }
            System.out.println("----------------------------------------------------------------------------------------------------");

            System.out.print("\nEnter book number to import (0 to cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (choice > 0 && choice <= books.size()) {
                Book selectedBook = books.get(choice - 1);
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
            System.err.println("Validation Error during book processing: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during import: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
