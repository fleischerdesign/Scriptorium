package org.scriptorium.cli.commands.copy;

import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

/**
 * Represents the top-level command for all copy-related operations.
 * This command acts as a container for subcommands like `add`, `list`, `show`, `update`, `delete`.
 * When executed without a subcommand, it provides a help message.
 */
@Command(
    name = "copy",
    description = "Manages book copy-related operations (add, list, show, update, delete).",
    mixinStandardHelpOptions = true,
    subcommands = {
        CopyCreateCommand.class,
        CopyListCommand.class,
        CopyShowCommand.class,
        CopyUpdateCommand.class,
        CopyDeleteCommand.class,
        HelpCommand.class // Picocli's built-in help command for subcommands
    }
)
public class CopyCommand implements Runnable {

    /**
     * This method is executed when the 'copy' command is called without any subcommands.
     * It typically prints a usage message.
     */
    @Override
    public void run() {
        System.out.println("Use 'scriptorium copy --help' to see available copy commands.");
    }
}
