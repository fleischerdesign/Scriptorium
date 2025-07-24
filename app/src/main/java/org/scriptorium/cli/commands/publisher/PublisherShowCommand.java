package org.scriptorium.cli.commands.publisher;

import org.scriptorium.core.domain.Publisher;
import org.scriptorium.core.services.PublisherService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Command to display details of a specific publisher by ID or name.
 * This command allows users to retrieve and view information about a publisher
 * stored in the system.
 */
@Command(
    name = "show",
    description = "Displays details of a publisher by ID or name."
)
public class PublisherShowCommand implements Callable<Integer> {

    @ParentCommand
    PublisherCommand parent; // Injects the parent command (PublisherCommand)

    private final PublisherService publisherService;

    /**
     * Constructor for PublisherShowCommand.
     * @param publisherService The service responsible for publisher operations.
     */
    public PublisherShowCommand(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the publisher to show")
    private Long id;

    @Option(names = {"-n", "--name"}, description = "Name of the publisher to show")
    private String name;

    /**
     * Executes the publisher show command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            Optional<Publisher> publisherOptional = Optional.empty();

            // Manual exclusivity check
            if (id != null && name != null) {
                System.err.println("Error: Cannot provide both --id and --name. Please choose one.");
                return 1;
            }
            if (id == null && name == null) {
                System.err.println("Error: Either --id or --name must be provided.");
                return 1;
            }

            if (id != null) {
                publisherOptional = publisherService.findById(id);
            } else { // name must be not null here due to checks above
                publisherOptional = publisherService.findPublisherByName(name);
            }

            if (publisherOptional.isPresent()) {
                Publisher publisher = publisherOptional.get();
                System.out.println("Publisher Details:");
                System.out.println("ID: " + publisher.getId());
                System.out.println("Name: " + publisher.getName());
            } else {
                if (id != null) {
                    System.out.println("Publisher with ID " + id + " not found.");
                } else {
                    System.out.println("Publisher with name '" + name + "' not found.");
                }
                return 1;
            }
            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error retrieving publisher: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}