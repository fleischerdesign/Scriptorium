package org.scriptorium.cli.commands.user;

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
