package org.scriptorium.cli.commands.genre;

import org.scriptorium.core.domain.Genre;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.exceptions.DuplicateEmailException;
import org.scriptorium.core.services.GenreService;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.concurrent.Callable;

/**
 * Command to create a new genre in the system.
 * This command allows users to add a new genre by name.
 */
@Command(
    name = "create",
    description = "Creates a new genre in the system."
)
public class GenreCreateCommand implements Callable<Integer> {

    @ParentCommand
    GenreCommand parent; // Injects the parent command (GenreCommand)

    private final GenreService genreService;

    /**
     * Constructor for GenreCreateCommand.
     * @param genreService The service responsible for genre operations.
     */
    public GenreCreateCommand(GenreService genreService) {
        this.genreService = genreService;
    }

    @Option(names = {"-n", "--name"}, description = "Name of the genre", required = true)
    private String name;

    /**
     * Executes the genre creation command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            Genre newGenre = new Genre(name);
            Genre createdGenre = genreService.createGenre(newGenre);

            System.out.println("Genre created successfully:");
            System.out.println("ID: " + createdGenre.getId());
            System.out.println("Name: " + createdGenre.getName());

            return 0;
        } catch (DuplicateEmailException e) {
            System.err.println("Error: " + e.getMessage());
            return 1;
        } catch (DataAccessException e) {
            System.err.println("Error creating genre: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
