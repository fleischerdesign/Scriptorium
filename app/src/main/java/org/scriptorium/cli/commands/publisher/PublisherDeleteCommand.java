package org.scriptorium.cli.commands.publisher;

import org.scriptorium.core.services.PublisherService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.concurrent.Callable;

/**
 * Command to delete an publisher from the system by its ID.
 * This command allows users to remove an publisher entry from the database.
 */
@Command(
    name = "delete",
    description = "Deletes an publisher from the system by its ID."
)
public class PublisherDeleteCommand implements Callable<Integer> {

    @ParentCommand
    PublisherCommand parent; // Injects the parent command (PublisherCommand)

    private final PublisherService publisherService;

    /**
     * Constructor for PublisherDeleteCommand.
     * @param publisherService The service responsible for Publisher operations.
     */
    public PublisherDeleteCommand(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the publisher to delete", required = true)
    private Long id;

    /**
     * Executes the publisher deletion command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            // Check if the publisher exists before attempting to delete
            if (publisherService.findById(id).isEmpty()) {
                System.out.println("Publisher with ID " + id + " not found. No deletion performed.");
                return 1;
            }

            publisherService.deleteById(id);
            System.out.println("Publisher with ID " + id + " deleted successfully.");
            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error deleting publisher: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
