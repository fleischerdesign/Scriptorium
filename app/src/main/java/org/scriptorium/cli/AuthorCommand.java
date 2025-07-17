package org.scriptorium.cli;

import org.scriptorium.cli.commands.author.AuthorCreateCommand;
import org.scriptorium.cli.commands.author.AuthorDeleteCommand;
import org.scriptorium.cli.commands.author.AuthorListCommand;
import org.scriptorium.cli.commands.author.AuthorShowCommand;
import org.scriptorium.cli.commands.author.AuthorUpdateCommand;

import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

/**
 * Represents the top-level command for all author-related operations.
 * This command acts as a container for subcommands like `create`, `list`, etc.
 * When executed without a subcommand, it provides a help message.
 */
@Command(
    name = "author",
    description = "Manages author-related operations (create, list, etc.).",
    mixinStandardHelpOptions = true,
    subcommands = {
        AuthorCreateCommand.class,
        AuthorShowCommand.class,
        AuthorListCommand.class,
        AuthorUpdateCommand.class,
        AuthorDeleteCommand.class,
        HelpCommand.class // Picocli's built-in help command for subcommands
    }
)
public class AuthorCommand implements Runnable {

    /**
     * This method is executed when the 'author' command is called without any subcommands.
     * It typically prints a usage message.
     */
    @Override
    public void run() {
        System.out.println("Use 'scriptorium author --help' to see available author commands.");
    }
}
