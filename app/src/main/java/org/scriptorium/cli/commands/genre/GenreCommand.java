package org.scriptorium.cli.commands.genre;

import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

/**
 * Represents the top-level command for all genre-related operations.
 * This command acts as a container for subcommands like `create`, `list`, etc.
 * When executed without a subcommand, it provides a help message.
 */
@Command(
    name = "genre",
    description = "Manages genre-related operations (create, list, etc.).",
    mixinStandardHelpOptions = true,
    subcommands = {
        GenreCreateCommand.class,
        GenreDeleteCommand.class,
        GenreListCommand.class,
        GenreShowCommand.class,
        GenreUpdateCommand.class,
        HelpCommand.class // Picocli's built-in help command for subcommands
    }
)
public class GenreCommand implements Runnable {

    /**
     * This method is executed when the 'genre' command is called without any subcommands.
     * It typically prints a usage message.
     */
    @Override
    public void run() {
        System.out.println("Use 'scriptorium genre --help' to see available genre commands.");
    }
}
