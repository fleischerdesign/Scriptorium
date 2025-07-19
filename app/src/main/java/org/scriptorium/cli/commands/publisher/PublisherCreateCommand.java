package org.scriptorium.cli.commands.publisher;

import java.util.concurrent.Callable;

import org.scriptorium.core.domain.Publisher;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.exceptions.DuplicateEmailException;
import org.scriptorium.core.services.PublisherService;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(
    name = "create",
    description = "Creates a new publisher in the system."
)
public class PublisherCreateCommand implements Callable<Integer> {

    @ParentCommand 
    PublisherCommand parent;

    private final PublisherService publisherService;
    
    /**
     * Constructor for PublisherCreateCommand.
     * @param publisherService The service responsible for publisher operations.
     */
    public PublisherCreateCommand(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @Option(names = {"-n", "--name"}, description = "Name of the publisher", required = true)
    private String name;

    /**
     * Executes the publisher creation command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            Publisher newPublisher = new Publisher(name);
            Publisher createdPublisher = publisherService.createPublisher(newPublisher);

            System.out.println("Publisher created successfully:");
            System.out.println("ID: " + createdPublisher.getId());
            System.out.println("Name: " + createdPublisher.getName());

            return 0;
        } catch (DuplicateEmailException e) {
            System.err.println("Error: " + e.getMessage());
            return 1;
        } catch (DataAccessException e) {
            System.err.println("Error creating publisher: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
    
}
