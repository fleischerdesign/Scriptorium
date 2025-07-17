package org.scriptorium.cli.commands.author;

import org.scriptorium.cli.AuthorCommand;
import org.scriptorium.core.domain.Author;
import org.scriptorium.core.services.AuthorService;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.exceptions.DuplicateEmailException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Command to update an existing author in the system.
 * This command allows users to modify the name of an author identified by their ID.
 */
@Command(
    name = "update",
    description = "Updates an existing author in the system."
)
public class AuthorUpdateCommand implements Callable<Integer> {

    @ParentCommand
    AuthorCommand parent; // Injects the parent command (AuthorCommand)

    private final AuthorService authorService;

    /**
     * Constructor for AuthorUpdateCommand.
     * @param authorService The service responsible for author operations.
     */
    public AuthorUpdateCommand(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the author to update", required = true)
    private Long id;

    @Option(names = {"-n", "--name"}, description = "New name of the author", required = true)
    private String newName;

    /**
     * Executes the author update command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            Optional<Author> existingAuthorOptional = authorService.findAuthorById(id);

            if (existingAuthorOptional.isEmpty()) {
                System.out.println("Author with ID " + id + " not found. Cannot update.");
                return 1;
            }

            Author existingAuthor = existingAuthorOptional.get();
            existingAuthor.setName(newName);

            Author updatedAuthor = authorService.updateAuthor(existingAuthor);

            System.out.println("Author updated successfully:");
            System.out.println("ID: " + updatedAuthor.getId());
            System.out.println("Name: " + updatedAuthor.getName());

            return 0;
        } catch (DuplicateEmailException e) {
            System.err.println("Error: " + e.getMessage());
            return 1;
        } catch (DataAccessException e) {
            System.err.println("Error updating author: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
