package org.scriptorium.cli.commands;

import org.scriptorium.cli.Command;

public class ExitCommand implements Command {
    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public void execute() {
        System.out.println("Exiting...");
    }
}