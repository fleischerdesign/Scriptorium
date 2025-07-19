package org.scriptorium.cli.commands.publisher;

import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

/**
 * Represents the top-level command for all publisher-related operations.
 * This command acts as a container for subcommands like `create`, `list`, etc.
 * When executed without a subcommand, it provides a help message.
 */
@Command(
    name = "publisher",
    description = "Manages publisher-related operations (create, list, etc.).",
    mixinStandardHelpOptions = true,
    subcommands = {
        PublisherCreateCommand.class,
        PublisherDeleteCommand.class,
        PublisherListCommand.class,
        HelpCommand.class // Picocli's built-in help command for subcommands
    }
)
public class PublisherCommand implements Runnable {

    /**
     * This method is executed when the 'publisher' command is called without any subcommands.
     * It typically prints a usage message.
     */
    @Override
    public void run() {
        System.out.println("Use 'scriptorium publisher --help' to see available publisher commands.");
    }
}
