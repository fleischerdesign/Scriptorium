package org.scriptorium.cli.commands.author;

import org.scriptorium.core.domain.Author;
import org.scriptorium.core.services.AuthorService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Command to list all authors in the system.
 * This command retrieves and displays a summary of all authors currently
 * stored in the application's database.
 */
@Command(
    name = "list",
    description = "Lists all authors in the system."
)
public class AuthorListCommand implements Callable<Integer> {

    @ParentCommand
    AuthorCommand parent; // Injects the parent command (AuthorCommand)

    private final AuthorService authorService;

    /**
     * Constructor for AuthorListCommand.
     * @param authorService The service responsible for author operations.
     */
    public AuthorListCommand(AuthorService authorService) {
        this.authorService = authorService;
    }

    /**
     * Executes the author list command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            List<Author> authors = authorService.findAllAuthors();

            if (authors.isEmpty()) {
                System.out.println("No authors found in the system.");
            } else {
                System.out.println("Listing all authors:");
                System.out.printf("%-5s %-30s%n", "ID", "Name");
                System.out.println("---------------------------------------");
                for (Author author : authors) {
                    System.out.printf("%-5d %-30s%n",
                        author.getId(),
                        author.getName()
                    );
                }
            }
            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error retrieving authors: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
