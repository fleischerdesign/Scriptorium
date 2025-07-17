package org.scriptorium.cli.commands.publisher;

import org.scriptorium.cli.PublisherCommand;
import org.scriptorium.core.domain.Publisher;
import org.scriptorium.core.services.PublisherService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Command to list all publishers in the system.
 * This command retrieves and displays a summary of all publishers currently
 * stored in the application's database.
 */
@Command(
    name = "list",
    description = "Lists all publishers in the system."
)
public class PublisherListCommand implements Callable<Integer> {

    @ParentCommand
    PublisherCommand publishers; // Injects the parent command (PublisherCommand)

    private final PublisherService publisherService;

    /**
     * Constructor for PublisherListCommand.
     * @param publisherService The service responsible for publisher operations.
     */
    public PublisherListCommand(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    /**
     * Executes the publisher list command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            List<Publisher> publishers = publisherService.findAllPublishers();

            if (publishers.isEmpty()) {
                System.out.println("No publishers found in the system.");
            } else {
                System.out.println("Listing all publishers:");
                System.out.printf("%-5s %-30s%n", "ID", "Name");
                System.out.println("---------------------------------------");
                for (Publisher publisher : publishers) {
                    System.out.printf("%-5d %-30s%n",
                        publisher.getId(),
                        publisher.getName()
                    );
                }
            }
            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error retrieving publishers: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
