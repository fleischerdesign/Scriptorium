package org.scriptorium.cli.commands.genre;

import org.scriptorium.core.domain.Genre;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.services.GenreService;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Command to list all genres in the system.
 * This command retrieves and displays a summary of all genres currently
 * stored in the application's database.
 */
@Command(
    name = "list",
    description = "Lists all genres in the system."
)
public class GenreListCommand implements Callable<Integer> {

    @ParentCommand
    GenreCommand parent; // Injects the parent command (GenreCommand)

    private final GenreService genreService;

    /**
     * Constructor for GenreListCommand.
     * @param genreService The service responsible for genre operations.
     */
    public GenreListCommand(GenreService genreService) {
        this.genreService = genreService;
    }

    /**
     * Executes the genre list command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            List<Genre> genres = genreService.findAllGenres();

            if (genres.isEmpty()) {
                System.out.println("No genres found in the system.");
            } else {
                System.out.println("Listing all genres:");
                System.out.printf("%-5s %-30s%n", "ID", "Name");
                System.out.println("---------------------------------------");
                for (Genre genre : genres) {
                    System.out.printf("%-5d %-30s%n",
                        genre.getId(),
                        genre.getName()
                    );
                }
            }
            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error retrieving genres: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
