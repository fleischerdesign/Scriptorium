package org.scriptorium.cli.commands.server;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.Optional;
import java.lang.ProcessHandle;

/**
 * Picocli command to stop the Scriptorium API server.
 * This command reads the PID from a file and attempts to terminate the process.
 */
@Command(name = "stop", description = "Stops the API server.")
public class ServerStopCommand implements Callable<Integer> {

    @ParentCommand
    private ServerCommand parent;

    private static final String PID_FILE = "scriptorium.pid";

    @Override
    public Integer call() throws Exception {
        try {
            if (!Files.exists(Paths.get(PID_FILE))) {
                System.out.println("PID file not found. Is the server running?");
                return 1;
            }

            String pidString = Files.readString(Paths.get(PID_FILE)).trim();
            long pid = Long.parseLong(pidString);

            Optional<ProcessHandle> processHandle = ProcessHandle.of(pid);
            if (processHandle.isPresent()) {
                ProcessHandle p = processHandle.get();
                System.out.println("Attempting to stop process with PID: " + pid);
                if (p.destroy()) { // Attempt graceful termination
                    System.out.println("Process with PID " + pid + " terminated.");
                } else if (p.destroyForcibly()) { // Forceful termination if graceful fails
                    System.out.println("Process with PID " + pid + " forcibly terminated.");
                } else {
                    System.err.println("Failed to terminate process with PID: " + pid);
                    return 1;
                }
            } else {
                System.out.println("Process with PID " + pid + " not found. Perhaps it already stopped?");
            }

            Files.deleteIfExists(Paths.get(PID_FILE));
            System.out.println("PID file deleted.");
            return 0;

        } catch (IOException e) {
            System.err.println("Error reading or deleting PID file: " + e.getMessage());
            return 1;
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid PID found in file. " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
