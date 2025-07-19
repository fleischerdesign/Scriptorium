package org.scriptorium.cli.commands.loan;

import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

/**
 * Represents the top-level command for all loan-related operations.
 * This command acts as a container for subcommands like `create`, `list`, etc.
 * When executed without a subcommand, it provides a help message.
 */
@Command(
    name = "loan",
    description = "Manages book loan operations (create, return, list, etc.).",
    mixinStandardHelpOptions = true,
    subcommands = {
        LoanCreateCommand.class,
        LoanShowCommand.class,
        LoanListCommand.class,
        LoanUpdateCommand.class,
        LoanDeleteCommand.class,
        LoanReturnCommand.class, // New: LoanReturnCommand
        HelpCommand.class // Picocli's built-in help command for subcommands
    }
)
public class LoanCommand implements Runnable {

    /**
     * This method is executed when the 'loan' command is called without any subcommands.
     * It typically prints a usage message.
     */
    @Override
    public void run() {
        System.out.println("Use 'scriptorium loan --help' to see available loan commands.");
    }
}