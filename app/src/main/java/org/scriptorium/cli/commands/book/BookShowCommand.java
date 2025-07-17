package org.scriptorium.cli.commands.book;

import org.scriptorium.cli.BookCommand;
import org.scriptorium.core.domain.Book;
import org.scriptorium.core.services.BookService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Command to display details of a specific book by its ID.
 * This command allows users to retrieve and view information about a book
 * stored in the system.
 */
@Command(
    name = "show",
    description = "Displays details of a book by its ID."
)
public class BookShowCommand implements Callable<Integer> {

    @ParentCommand
    BookCommand parent; // Injects the parent command (BookCommand)

    private final BookService bookService;

    /**
     * Constructor for BookShowCommand.
     * @param bookService The service responsible for book operations.
     */
    public BookShowCommand(BookService bookService) {
        this.bookService = bookService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the book to show", required = true)
    private Long id;

    /**
     * Executes the book show command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            Optional<Book> bookOptional = bookService.findBookById(id);

            if (bookOptional.isPresent()) {
                Book book = bookOptional.get();
                System.out.println("Book Details:");
                System.out.println("ID: " + book.getId());
                System.out.println("Title: " + book.getTitle());
                System.out.println("ISBNs: " + book.getIsbns());
                System.out.println("Publication Year: " + book.getPublicationYear());
                System.out.println("Authors: " + book.getAuthors().get(0).getName()); // Display first author
                System.out.println("Publisher: " + book.getMainPublisher().getName());
                System.out.println("Genre: " + book.getGenre());
            } else {
                System.out.println("Book with ID " + id + " not found.");
                return 1;
            }
            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error retrieving book: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
