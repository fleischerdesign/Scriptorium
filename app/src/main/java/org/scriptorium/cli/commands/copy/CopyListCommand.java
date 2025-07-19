package org.scriptorium.cli.commands.copy;

import org.scriptorium.core.domain.Copy;
import org.scriptorium.core.services.CopyService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Command to list all copies or copies filtered by book ID.
 * This command retrieves and displays a summary of copies currently
 * stored in the application's database.
 */
@Command(
    name = "list",
    description = "Lists all copies or copies filtered by book ID."
)
public class CopyListCommand implements Callable<Integer> {

    @ParentCommand
    CopyCommand parent; // Injects the parent command (CopyCommand)

    private final CopyService copyService;

    /**
     * Constructor for CopyListCommand.
     * @param copyService The service responsible for copy operations.
     */
    public CopyListCommand(CopyService copyService) {
        this.copyService = copyService;
    }

    @Option(names = {"-b", "--book-id"}, description = "Filter copies by associated book ID")
    private Long bookId;

    /**
     * Executes the copy list command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            List<Copy> copies;

            if (bookId != null) {
                copies = copyService.findCopiesByBookId(bookId);
            } else {
                copies = copyService.findAllCopies();
            }

            if (copies.isEmpty()) {
                System.out.println("No copies found.");
            } else {
                System.out.println("Listing copies:");
                System.out.printf("%-5s %-10s %-15s %-15s %-15s%n", "ID", "Item ID", "Media Type", "Barcode", "Status");
                System.out.println("-----------------------------------------------------------------------------------------");
                for (Copy copy : copies) {
                    System.out.printf("%-5d %-10d %-15s %-15s %-15s%n",
                        copy.getId(),
                        copy.getItemId(),
                        copy.getMediaType(),
                        copy.getBarcode() != null ? copy.getBarcode() : "N/A",
                        copy.getStatus()
                    );
                }
            }
            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error retrieving copies: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
