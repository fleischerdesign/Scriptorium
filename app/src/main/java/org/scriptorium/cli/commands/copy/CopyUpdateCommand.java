package org.scriptorium.cli.commands.copy;

import org.scriptorium.core.domain.Copy;
import org.scriptorium.core.domain.Copy.CopyStatus;
import org.scriptorium.core.services.CopyService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Command to update an existing copy in the system.
 * This command allows users to modify the barcode or status of a copy.
 */
@Command(
    name = "update",
    description = "Updates an existing copy."
)
public class CopyUpdateCommand implements Callable<Integer> {

    @ParentCommand
    CopyCommand parent; // Injects the parent command (CopyCommand)

    private final CopyService copyService;

    /**
     * Constructor for CopyUpdateCommand.
     * @param copyService The service responsible for copy operations.
     */
    public CopyUpdateCommand(CopyService copyService) {
        this.copyService = copyService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the copy to update", required = true)
    private Long id;

    @Option(names = {"-c", "--barcode"}, description = "New barcode of the copy")
    private String barcode;

    @Option(names = {"-s", "--status"}, description = "New status of the copy (AVAILABLE, ON_LOAN, LOST, DAMAGED, RESERVED)")
    private CopyStatus status;

    /**
     * Executes the copy update command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            Optional<Copy> existingCopyOptional = copyService.findCopyById(id);

            if (existingCopyOptional.isEmpty()) {
                System.out.println("Copy with ID " + id + " not found. Cannot update.");
                return 1;
            }

            Copy existingCopy = existingCopyOptional.get();
            boolean updated = false;

            if (barcode != null) {
                existingCopy.setBarcode(barcode);
                updated = true;
            }

            if (status != null) {
                existingCopy.setStatus(status);
                updated = true;
            }

            if (!updated) {
                System.out.println("No update parameters provided. Nothing to update for copy ID " + id + ".");
                return 0;
            }

            Copy updatedCopy = copyService.save(existingCopy);

            System.out.println("Copy updated successfully:");
            System.out.println("ID: " + updatedCopy.getId());
            System.out.println("Item ID: " + updatedCopy.getItemId());
            System.out.println("Media Type: " + updatedCopy.getMediaType());
            System.out.println("Barcode: " + (updatedCopy.getBarcode() != null ? updatedCopy.getBarcode() : "N/A"));
            System.out.println("Status: " + updatedCopy.getStatus());

            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error updating copy: " + e.getMessage());
            return 1;
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
