package org.scriptorium.cli.commands;

import org.scriptorium.cli.Command;
import org.scriptorium.core.domain.Book;
import org.scriptorium.core.services.BookImportService;
import org.scriptorium.core.http.SimpleHttpClient;
import java.util.List;
import java.util.Scanner;

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
        String title = scanner.nextLine();

        try {
            List<Book> books = bookImportService.importBooksByTitle(title);
            
            if (books.isEmpty()) {
                System.out.println("No books found for: " + title);
                return;
            }

            // Display detailed numbered list
            System.out.println("\nFound books:");
            System.out.println("-------------------------------------------------------------------------------");
            System.out.printf("%-4s %-30s %-20s %-6s %-15s %-12s%n",
                "No.", "Title", "Author", "Year", "Publisher", "ISBN");
            System.out.println("-------------------------------------------------------------------------------");
            
            for (int i = 0; i < books.size(); i++) {
                Book book = books.get(i);
                System.out.printf("%-4d %-30.30s %-20.20s %-6d %-15.15s %-12.12s%n",
                    i+1,
                    book.getTitle() != null ? book.getTitle() : "Unknown",
                    book.getAuthor() != null ? book.getAuthor().getName() : "Unknown",
                    book.getPublicationYear(),
                    book.getPublisher() != null ? book.getPublisher().getName() : "Unknown",
                    book.getIsbn() != null ? book.getIsbn() : "Unknown");
            }
            System.out.println("-------------------------------------------------------------------------------");

            // Selection prompt
            System.out.print("\nEnter book number to import (0 to cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (choice > 0 && choice <= books.size()) {
                Book selected = books.get(choice-1);
                System.out.printf("Importing: %s by %s%n", selected.getTitle(), selected.getAuthor().getName());
                // TODO: Actual import logic here
            } else {
                System.out.println("Import cancelled");
            }
        } catch (Exception e) {
            System.err.println("Error importing books: " + e.getMessage());
        }
    }
}