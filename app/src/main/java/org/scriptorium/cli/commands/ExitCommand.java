package org.scriptorium.cli.commands;

import picocli.CommandLine.Command;

@Command(name = "exit", description = "Exits the interactive shell.")
public class ExitCommand implements Runnable {

    @Override
    public void run() {
        System.out.println("Exiting...");
        // JLine will handle the actual exit
    }
}
