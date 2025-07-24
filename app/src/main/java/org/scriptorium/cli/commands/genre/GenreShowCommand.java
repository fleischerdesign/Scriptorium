package org.scriptorium.cli.commands.genre;

import org.scriptorium.core.domain.Genre;
import org.scriptorium.core.services.GenreService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Command to display details of a specific genre by ID or name.
 * This command allows users to retrieve and view information about an genre
 * stored in the system.
 */
@Command(
    name = "show",
    description = "Displays details of an genre by ID or name."
)
public class GenreShowCommand implements Callable<Integer> {

    @ParentCommand
    GenreCommand parent; // Injects the parent command (GenreCommand)

    private final GenreService genreService;

    /**
     * Constructor for GenreShowCommand.
     * @param genreService The service responsible for genre operations.
     */
    public GenreShowCommand(GenreService genreService) {
        this.genreService = genreService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the genre to show")
    private Long id;

    @Option(names = {"-n", "--name"}, description = "Name of the genre to show")
    private String name;

    /**
     * Executes the genre show command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            Optional<Genre> genreOptional = Optional.empty();

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
                genreOptional = genreService.findById(id);
            } else { // name must be not null here due to checks above
                genreOptional = genreService.findGenreByName(name);
            }

            if (genreOptional.isPresent()) {
                Genre genre = genreOptional.get();
                System.out.println("Genre Details:");
                System.out.println("ID: " + genre.getId());
                System.out.println("Name: " + genre.getName());
            } else {
                if (id != null) {
                    System.out.println("Genre with ID " + id + " not found.");
                } else {
                    System.out.println("Genre with name '" + name + "' not found.");
                }
                return 1;
            }
            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error retrieving genre: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
