package org.scriptorium.cli.commands.copy;

import org.scriptorium.core.services.CopyService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.concurrent.Callable;

/**
 * Command to delete a copy from the system by its ID.
 * This command allows users to remove a copy entry from the database.
 */
@Command(
    name = "delete",
    description = "Deletes a copy from the system by its ID."
)
public class CopyDeleteCommand implements Callable<Integer> {

    @ParentCommand
    CopyCommand parent; // Injects the parent command (CopyCommand)

    private final CopyService copyService;

    /**
     * Constructor for CopyDeleteCommand.
     * @param copyService The service responsible for copy operations.
     */
    public CopyDeleteCommand(CopyService copyService) {
        this.copyService = copyService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the copy to delete", required = true)
    private Long id;

    /**
     * Executes the copy deletion command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            // Check if the copy exists before attempting to delete
            if (copyService.findCopyById(id).isEmpty()) {
                System.out.println("Copy with ID " + id + " not found. No deletion performed.");
                return 1;
            }

            copyService.deleteCopy(id);
            System.out.println("Copy with ID " + id + " deleted successfully.");
            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error deleting copy: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
