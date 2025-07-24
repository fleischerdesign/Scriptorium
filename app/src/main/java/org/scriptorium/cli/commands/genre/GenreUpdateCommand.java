package org.scriptorium.cli.commands.genre;

import org.scriptorium.core.domain.Genre;
import org.scriptorium.core.services.GenreService;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.exceptions.DuplicateEmailException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Command to update an existing genre in the system.
 * This command allows users to modify the name of an genre identified by their ID.
 */
@Command(
    name = "update",
    description = "Updates an existing genre in the system."
)
public class GenreUpdateCommand implements Callable<Integer> {

    @ParentCommand
    GenreCommand parent; // Injects the parent command (GenreCommand)

    private final GenreService genreService;

    /**
     * Constructor for GenreUpdateCommand.
     * @param genreService The service responsible for genre operations.
     */
    public GenreUpdateCommand(GenreService genreService) {
        this.genreService = genreService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the genre to update", required = true)
    private Long id;

    @Option(names = {"-n", "--name"}, description = "New name of the genre", required = true)
    private String newName;

    /**
     * Executes the genre update command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            Optional<Genre> existingGenreOptional = genreService.findById(id);

            if (existingGenreOptional.isEmpty()) {
                System.out.println("Genre with ID " + id + " not found. Cannot update.");
                return 1;
            }

            Genre existingGenre = existingGenreOptional.get();
            existingGenre.setName(newName);

            Genre updatedGenre = genreService.save(existingGenre);

            System.out.println("Genre updated successfully:");
            System.out.println("ID: " + updatedGenre.getId());
            System.out.println("Name: " + updatedGenre.getName());

            return 0;
        } catch (DuplicateEmailException e) {
            System.err.println("Error: " + e.getMessage());
            return 1;
        } catch (DataAccessException e) {
            System.err.println("Error updating genre: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
