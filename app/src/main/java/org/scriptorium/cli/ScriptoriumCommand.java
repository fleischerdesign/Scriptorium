package org.scriptorium.cli;

import org.scriptorium.cli.commands.ExitCommand;
import org.scriptorium.cli.commands.ServerCommand;
import org.scriptorium.cli.commands.author.AuthorCommand;
import org.scriptorium.cli.commands.book.BookCommand;
import org.scriptorium.cli.commands.genre.GenreCommand;
import org.scriptorium.cli.commands.loan.LoanCommand;
import org.scriptorium.cli.commands.publisher.PublisherCommand;
import org.scriptorium.cli.commands.user.UserCommand;
import org.scriptorium.cli.commands.reservation.ReservationCommand;
import org.scriptorium.cli.commands.copy.CopyCommand; // New
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

/**
 * The main command container for the Scriptorium CLI.
 *
 * This class acts as the entry point for Picocli, defining the main command
 * and listing all available subcommands. It also enables standard help options.
 */
@Command(name = "scriptorium", mixinStandardHelpOptions = true, version = "Scriptorium 1.0", description = "A book management CLI.", subcommands = {
        BookCommand.class,
        UserCommand.class,
        AuthorCommand.class,
        PublisherCommand.class,
        GenreCommand.class,
        ExitCommand.class,
        LoanCommand.class,
        ReservationCommand.class,
        CopyCommand.class, // New
        ServerCommand.class,
        HelpCommand.class
})
public class ScriptoriumCommand {
}
