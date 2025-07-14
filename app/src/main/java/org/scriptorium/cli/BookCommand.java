package org.scriptorium.cli;

import org.scriptorium.cli.commands.book.BookImportCommand;

import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

/**
 * Represents the top-level command for all book-related operations.
 * This command acts as a container for subcommands like `import-book`, `add-book`, etc.
 * When executed without a subcommand, it provides a help message.
 */
@Command(
    name = "book",
    description = "Manages book-related operations (import, add, list, etc.).",
    mixinStandardHelpOptions = true,
    subcommands = {
        BookImportCommand.class, // Existing import command now nested under 'book'
        HelpCommand.class // Picocli's built-in help command for subcommands
    }
)
public class BookCommand implements Runnable {

    /**
     * This method is executed when the 'book' command is called without any subcommands.
     * It typically prints a usage message.
     */
    @Override
    public void run() {
        System.out.println("Use 'scriptorium book --help' to see available book commands.");
    }
}
