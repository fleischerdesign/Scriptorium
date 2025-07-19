package org.scriptorium.cli.commands.author;

import org.scriptorium.core.services.AuthorService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.concurrent.Callable;

/**
 * Command to delete an author from the system by its ID.
 * This command allows users to remove an author entry from the database.
 */
@Command(
    name = "delete",
    description = "Deletes an author from the system by its ID."
)
public class AuthorDeleteCommand implements Callable<Integer> {

    @ParentCommand
    AuthorCommand parent; // Injects the parent command (AuthorCommand)

    private final AuthorService authorService;

    /**
     * Constructor for AuthorDeleteCommand.
     * @param authorService The service responsible for author operations.
     */
    public AuthorDeleteCommand(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the author to delete", required = true)
    private Long id;

    /**
     * Executes the author deletion command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            // Check if the author exists before attempting to delete
            if (authorService.findAuthorById(id).isEmpty()) {
                System.out.println("Author with ID " + id + " not found. No deletion performed.");
                return 1;
            }

            authorService.deleteAuthor(id);
            System.out.println("Author with ID " + id + " deleted successfully.");
            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error deleting author: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
