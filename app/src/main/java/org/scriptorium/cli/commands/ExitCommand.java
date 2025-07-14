package org.scriptorium.cli.commands;

import picocli.CommandLine.Command;

/**
 * Picocli command for exiting the interactive CLI shell.
 *
 * This command provides a graceful way for users to terminate the application
 * when running in interactive mode.
 */
@Command(name = "exit", description = "Exits the interactive shell.")
public class ExitCommand implements Runnable {

    /**
     * Executes the exit command.
     * Prints an exit message. The actual application termination is handled by the calling shell (JLine).
     */
    @Override
    public void run() {
        System.out.println("Exiting...");
        // JLine will handle the actual exit
    }
}
