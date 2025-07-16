package org.scriptorium.cli;

import org.scriptorium.cli.commands.user.UserCreateCommand;
import org.scriptorium.cli.commands.user.UserDeleteCommand;
import org.scriptorium.cli.commands.user.UserListCommand;
import org.scriptorium.cli.commands.user.UserShowCommand;
import org.scriptorium.cli.commands.user.UserUpdateCommand;

import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;

@Command(
    name = "user",
    description = "Manages user-related operations.",
    mixinStandardHelpOptions = true,
    subcommands = {
        UserListCommand.class,
        UserCreateCommand.class,
        UserShowCommand.class,
        UserDeleteCommand.class,
        UserUpdateCommand.class,
        HelpCommand.class
    }
)
public class UserCommand implements Runnable {

    @Override
    public void run() {
        System.out.println("Use 'scriptorium user --help' to see available user commands.");
    }
}
