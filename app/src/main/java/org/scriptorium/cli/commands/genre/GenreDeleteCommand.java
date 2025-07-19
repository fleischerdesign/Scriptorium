package org.scriptorium.cli.commands.genre;

import org.scriptorium.core.services.GenreService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.concurrent.Callable;

/**
 * Command to delete an genre from the system by its ID.
 * This command allows users to remove an genre entry from the database.
 */
@Command(
    name = "delete",
    description = "Deletes an genre from the system by its ID."
)
public class GenreDeleteCommand implements Callable<Integer> {

    @ParentCommand
    GenreCommand parent; // Injects the parent command (GenreCommand)

    private final GenreService genreService;

    /**
     * Constructor for GenreDeleteCommand.
     * @param genreService The service responsible for genre operations.
     */
    public GenreDeleteCommand(GenreService genreService) {
        this.genreService = genreService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the genre to delete", required = true)
    private Long id;

    /**
     * Executes the genre deletion command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            // Check if the genre exists before attempting to delete
            if (genreService.findGenreById(id).isEmpty()) {
                System.out.println("Genre with ID " + id + " not found. No deletion performed.");
                return 1;
            }

            genreService.deleteGenre(id);
            System.out.println("Genre with ID " + id + " deleted successfully.");
            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error deleting genre: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
