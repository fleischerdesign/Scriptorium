package org.scriptorium.cli.commands.copy;

import org.scriptorium.core.domain.Copy;
import org.scriptorium.core.services.CopyService;
import org.scriptorium.core.services.BookService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Command to display details of a specific copy by its ID.
 * This command allows users to retrieve and view information about a copy
 * stored in the system.
 */
@Command(
    name = "show",
    description = "Displays details of a copy by its ID."
)
public class CopyShowCommand implements Callable<Integer> {

    @ParentCommand
    CopyCommand parent; // Injects the parent command (CopyCommand)

    private final CopyService copyService;
    private final BookService bookService; // To fetch book details for display

    /**
     * Constructor for CopyShowCommand.
     * @param copyService The service responsible for copy operations.
     * @param bookService The service responsible for book operations.
     */
    public CopyShowCommand(CopyService copyService, BookService bookService) {
        this.copyService = copyService;
        this.bookService = bookService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the copy to show", required = true)
    private Long id;

    /**
     * Executes the copy show command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            Optional<Copy> copyOptional = copyService.findById(id);

            if (copyOptional.isPresent()) {
                Copy copy = copyOptional.get();
                System.out.println("Copy Details:");
                System.out.println("ID: " + copy.getId());
                System.out.println("Item ID: " + copy.getItemId());
                System.out.println("Media Type: " + copy.getMediaType());
                System.out.println("Barcode: " + (copy.getBarcode() != null ? copy.getBarcode() : "N/A"));
                System.out.println("Status: " + copy.getStatus());

                // Display associated book details if it's a book copy
                if (copy.getMediaType() == Copy.MediaType.BOOK) {
                    bookService.findById(copy.getItemId()).ifPresent(book -> {
                        System.out.println("Associated Book Title: " + book.getTitle());
                        System.out.println("Associated Book ISBN: " + (book.getIsbns().isEmpty() ? "N/A" : book.getIsbns().get(0)));
                    });
                }

            } else {
                System.out.println("Copy with ID " + id + " not found.");
                return 1;
            }
            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error retrieving copy: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
