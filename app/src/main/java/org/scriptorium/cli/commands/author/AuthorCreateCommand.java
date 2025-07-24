package org.scriptorium.cli.commands.author;

import org.scriptorium.core.domain.Author;
import org.scriptorium.core.services.AuthorService;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.exceptions.DuplicateEmailException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.concurrent.Callable;

/**
 * Command to create a new author in the system.
 * This command allows users to add a new author by name.
 */
@Command(
    name = "create",
    description = "Creates a new author in the system."
)
public class AuthorCreateCommand implements Callable<Integer> {

    @ParentCommand
    AuthorCommand parent; // Injects the parent command (AuthorCommand)

    private final AuthorService authorService;

    /**
     * Constructor for AuthorCreateCommand.
     * @param authorService The service responsible for author operations.
     */
    public AuthorCreateCommand(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Option(names = {"-n", "--name"}, description = "Name of the author", required = true)
    private String name;

    /**
     * Executes the author creation command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            Author newAuthor = new Author(name);
            Author createdAuthor = authorService.save(newAuthor);

            System.out.println("Author created successfully:");
            System.out.println("ID: " + createdAuthor.getId());
            System.out.println("Name: " + createdAuthor.getName());

            return 0;
        } catch (DuplicateEmailException e) {
            System.err.println("Error: " + e.getMessage());
            return 1;
        } catch (DataAccessException e) {
            System.err.println("Error creating author: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
