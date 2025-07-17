package org.scriptorium.cli.commands.book;

import org.scriptorium.core.services.BookService;
import org.scriptorium.cli.BookCommand;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.concurrent.Callable;

/**
 * Command to delete a book from the system by its ID.
 * This command allows users to remove a book entry from the database.
 */
@Command(
    name = "delete",
    description = "Deletes a book from the system by its ID."
)
public class BookDeleteCommand implements Callable<Integer> {

    @ParentCommand
    BookCommand parent; // Injects the parent command (BookCommand)

    private final BookService bookService;

    /**
     * Constructor for BookDeleteCommand.
     * @param bookService The service responsible for book operations.
     */
    public BookDeleteCommand(BookService bookService) {
        this.bookService = bookService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the book to delete", required = true)
    private Long id;

    /**
     * Executes the book deletion command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            // Check if the book exists before attempting to delete
            if (bookService.findBookById(id).isEmpty()) {
                System.out.println("Book with ID " + id + " not found. No deletion performed.");
                return 1;
            }

            bookService.deleteBook(id);
            System.out.println("Book with ID " + id + " deleted successfully.");
            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
