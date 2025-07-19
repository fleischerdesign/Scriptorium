package org.scriptorium.cli.commands.book;

import org.scriptorium.core.domain.Author;
import org.scriptorium.core.domain.Book;
import org.scriptorium.core.services.BookService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Command to list all books in the system.
 * This command retrieves and displays a summary of all books currently
 * stored in the application's database.
 */
@Command(
    name = "list",
    description = "Lists all books in the system."
)
public class BookListCommand implements Callable<Integer> {

    @ParentCommand
    BookCommand parent; // Injects the parent command (BookCommand)

    private final BookService bookService;

    /**
     * Constructor for BookListCommand.
     * @param bookService The service responsible for book operations.
     */
    public BookListCommand(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Executes the book list command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            List<Book> books = bookService.findAllBooks();

            if (books.isEmpty()) {
                System.out.println("No books found in the system.");
            } else {
                System.out.println("Listing all books:");
                System.out.printf("%-5s %-40s %-15s %-15s %-20s %-20s%n", "ID", "Title", "ISBN", "Pub Year", "Author", "Publisher");
                System.out.println("-------------------------------------------------------------------------------------------------------------------");
                for (Book book : books) {
                    System.out.printf("%-5d %-40s %-15s %-15s %-20s %-20s%n",
                        book.getId(),
                        book.getTitle(),
                        book.getIsbns().isEmpty() ? "N/A" : book.getIsbns().get(0), // Display first ISBN
                        book.getPublicationYear(),
                        book.getAuthors().isEmpty() ? "N/A" : book.getAuthors().stream().map(Author::getName).collect(Collectors.joining(", ")), // Display all authors
                        book.getMainPublisher() != null ? book.getMainPublisher().getName() : "N/A"
                    );
                }
            }
            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error retrieving books: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
