package org.scriptorium.cli;

import org.scriptorium.cli.commands.ExitCommand;
import org.scriptorium.cli.commands.book.BookImportCommand;

import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

/**
 * The main command container for the Scriptorium CLI.
 *
 * This class acts as the entry point for Picocli, defining the main command
 * and listing all available subcommands. It also enables standard help options.
 */
@Command(
    name = "scriptorium",
    mixinStandardHelpOptions = true,
    version = "Scriptorium 1.0",
    description = "A book management CLI.",
    subcommands = {
        BookCommand.class, // All book-related commands are now nested under 'book'
        ExitCommand.class,
        HelpCommand.class // Picocli's built-in help command
    }
)
public class ScriptoriumCommand {
}
