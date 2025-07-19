package org.scriptorium.cli.commands.reservation;

import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

/**
 * Represents the top-level command for all reservation-related operations.
 * This command acts as a container for subcommands like `create`, `cancel`, `fulfill`, `list`, `show`, `delete`.
 * When executed without a subcommand, it provides a help message.
 */
@Command(
    name = "reservation",
    description = "Manages reservation-related operations (create, cancel, fulfill, list, show, delete).",
    mixinStandardHelpOptions = true,
    subcommands = {
        ReservationCreateCommand.class,
        ReservationCancelCommand.class,
        ReservationFulfillCommand.class,
        ReservationListCommand.class,
        ReservationShowCommand.class,
        ReservationDeleteCommand.class,
        HelpCommand.class // Picocli's built-in help command for subcommands
    }
)
public class ReservationCommand implements Runnable {

    /**
     * This method is executed when the 'reservation' command is called without any subcommands.
     * It typically prints a usage message.
     */
    @Override
    public void run() {
        System.out.println("Use 'scriptorium reservation --help' to see available reservation commands.");
    }
}
