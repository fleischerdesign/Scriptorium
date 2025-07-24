package org.scriptorium.cli.commands.copy;

import org.scriptorium.core.domain.Copy;
import org.scriptorium.core.services.BookService;
import org.scriptorium.core.services.CopyService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.concurrent.Callable;

/**
 * Command to create a new copy in the system.
 * This command allows users to add a new physical copy for an existing book.
 */
@Command(
    name = "create",
    description = "Creates a new book copy."
)
public class CopyCreateCommand implements Callable<Integer> {

    @ParentCommand
    CopyCommand parent; // Injects the parent command (CopyCommand)

    private final CopyService copyService;
    private final BookService bookService;

    /**
     * Constructor for CopyCreateCommand.
     * @param copyService The service responsible for copy operations.
     * @param bookService The service responsible for book operations.
     */
    public CopyCreateCommand(CopyService copyService, BookService bookService) {
        this.copyService = copyService;
        this.bookService = bookService;
    }

    @Option(names = {"-b", "--book-id"}, description = "ID of the book for which to create a copy", required = true)
    private Long bookId;

    @Option(names = {"-c", "--barcode"}, description = "Barcode of the copy (optional)")
    private String barcode;

    /**
     * Executes the copy creation command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            // Verify book existence
            bookService.findById(bookId)
                    .orElseThrow(() -> new IllegalArgumentException("Book with ID " + bookId + " not found."));

            Copy newCopy = new Copy(bookId, barcode, Copy.CopyStatus.AVAILABLE, Copy.MediaType.BOOK);
            Copy createdCopy = copyService.save(newCopy);

            System.out.println("Copy created successfully:");
            System.out.println("ID: " + newCopy.getId());
            System.out.println("Book ID: " + newCopy.getItemId());
            System.out.println("Barcode: " + (newCopy.getBarcode() != null ? newCopy.getBarcode() : "N/A"));
            System.out.println("Status: " + newCopy.getStatus());
            System.out.println("Media Type: " + newCopy.getMediaType());

            return 0;
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            return 1;
        } catch (DataAccessException e) {
            System.err.println("Error creating copy: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
