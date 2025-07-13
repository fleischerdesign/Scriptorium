package org.scriptorium.cli;

import org.scriptorium.cli.commands.ExitCommand;
import org.scriptorium.cli.commands.HelpCommand;
import org.scriptorium.cli.commands.ImportBookCommand;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandExecutor {
    private final Map<String, Command> commands = new HashMap<>();
    
    public CommandExecutor() {
        registerCommand(new HelpCommand(commands));
        registerCommand(new ExitCommand());
        registerCommand(new ImportBookCommand());
    }

    public void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                try {
                    System.out.print("\nEnter command (type 'help' for list): ");
                    if (!scanner.hasNextLine()) {
                        break; // EOF reached
                    }
                    
                    String input = scanner.nextLine().trim();
                    if (input.isEmpty()) {
                        continue;
                    }

                    Command command = commands.get(input);
                    if (command != null) {
                        command.execute();
                        if (input.equals("exit")) {
                            break;
                        }
                    } else {
                        System.out.println("Unknown command. Type 'help' for available commands.");
                    }
                } catch (Exception e) {
                    System.err.println("Error processing command: " + e.getMessage());
                }
            }
        }
    }
}
