package org.scriptorium.application;

import org.scriptorium.cli.ScriptoriumCommand;
import org.scriptorium.config.DependencyFactory;
import picocli.CommandLine;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.EndOfFileException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

/**
 * The main entry point for the Scriptorium CLI application.
 *
 * This class is responsible for initializing Picocli and determining whether to run
 * in interactive shell mode or to execute a single command based on the provided arguments.
 */
public class Main {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments. If arguments are provided, the application
     *             runs in single-command mode. If no arguments are present, it launches
     *             the interactive JLine3 shell.
     */
    public static void main(String[] args) {
        CommandLine cmd = new CommandLine(new ScriptoriumCommand(), new DependencyFactory());

        if (args.length > 0) {
            cmd.execute(args);
            return;
        }

        // Interactive Shell Mode
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
            LineReader lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();

            String prompt = "scriptorium> ";
            System.out.println("Welcome to Scriptorium! Type 'help' for commands, 'exit' to quit.");

            while (true) {
                String line;
                try {
                    line = lineReader.readLine(prompt);
                    String[] arguments = line.trim().split("\\s+");
                    if (arguments.length == 1 && arguments[0].equalsIgnoreCase("exit")) {
                        break;
                    }
                    cmd.execute(arguments);
                } catch (UserInterruptException e) {
                    // Ignore Ctrl+C
                } catch (EndOfFileException e) {
                    System.out.println("\nExiting...");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
