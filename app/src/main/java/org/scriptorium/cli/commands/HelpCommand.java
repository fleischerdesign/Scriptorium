package org.scriptorium.cli.commands;

import org.scriptorium.cli.Command;
import java.util.Map;

public class HelpCommand implements Command {
    private final Map<String, Command> commands;

    public HelpCommand(Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void execute() {
        System.out.println("\nAvailable commands:");
        commands.keySet().forEach(cmd -> System.out.println("- " + cmd));
        System.out.println();
    }
}