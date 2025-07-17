package org.scriptorium.cli.commands.author;

import org.scriptorium.cli.AuthorCommand;
import org.scriptorium.core.domain.Author;
import org.scriptorium.core.services.AuthorService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Command to display details of a specific author by ID or name.
 * This command allows users to retrieve and view information about an author
 * stored in the system.
 */
@Command(
    name = "show",
    description = "Displays details of an author by ID or name."
)
public class AuthorShowCommand implements Callable<Integer> {

    @ParentCommand
    AuthorCommand parent; // Injects the parent command (AuthorCommand)

    private final AuthorService authorService;

    /**
     * Constructor for AuthorShowCommand.
     * @param authorService The service responsible for author operations.
     */
    public AuthorShowCommand(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the author to show")
    private Long id;

    @Option(names = {"-n", "--name"}, description = "Name of the author to show")
    private String name;

    /**
     * Executes the author show command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            Optional<Author> authorOptional = Optional.empty();

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
                authorOptional = authorService.findAuthorById(id);
            } else { // name must be not null here due to checks above
                authorOptional = authorService.findAuthorByName(name);
            }

            if (authorOptional.isPresent()) {
                Author author = authorOptional.get();
                System.out.println("Author Details:");
                System.out.println("ID: " + author.getId());
                System.out.println("Name: " + author.getName());
            } else {
                if (id != null) {
                    System.out.println("Author with ID " + id + " not found.");
                } else {
                    System.out.println("Author with name '" + name + "' not found.");
                }
                return 1;
            }
            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error retrieving author: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
