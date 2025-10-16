package org.scriptorium.cli.commands.server;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Picocli command to start the Scriptorium API server as a separate process.
 * This command allows me to launch the web API in the background,
 * optionally specifying a custom port, and immediately returns control to the CLI.
 */
@Command(name = "start", description = "Starts the API server.")
public class ServerStartCommand implements Callable<Integer> {

    @ParentCommand
    private ServerCommand parent;

    @Option(names = {"-p", "--port"}, description = "The port to run the server on.", defaultValue = "7070")
    private int port;

    public ServerStartCommand() {
    }

    /**
     * The main execution logic for the `start` command.
     * It constructs a new Java process to run the `ServerRunner` class,
     * passing the desired port as an argument. This allows the API server
     * to run in the background, freeing up the current CLI session.
     * @return 0 for success, 1 for failure.
     * @throws Exception if the server process fails to start.
     */
    @Override
    public Integer call() throws Exception {
        String pidFile = "scriptorium.pid";

        // Check if PID file exists and if the process is still running
        if (Files.exists(Paths.get(pidFile))) {
            try {
                String pidString = Files.readString(Paths.get(pidFile)).trim();
                long pid = Long.parseLong(pidString);
                Optional<ProcessHandle> processHandle = ProcessHandle.of(pid);

                if (processHandle.isPresent() && processHandle.get().isAlive()) {
                    System.err.println("Error: Server appears to be already running with PID " + pid + ".");
                    System.err.println("If you want to stop it, use 'server stop'.");
                    return 1;
                } else {
                    System.out.println("Stale PID file found. Deleting it.");
                    Files.deleteIfExists(Paths.get(pidFile));
                }
            } catch (IOException | NumberFormatException e) {
                System.err.println("Warning: Could not read or parse PID file. Attempting to start anyway. " + e.getMessage());
                Files.deleteIfExists(Paths.get(pidFile)); // Attempt to clean up corrupted PID file
            }
        }

        try {
            String javaHome = System.getProperty("java.home");
            String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
            String classpath = System.getProperty("java.class.path");
            String className = "org.scriptorium.application.ServerRunner";

            List<String> command = new ArrayList<>();
            command.add(javaBin);
            command.add("-classpath");
            command.add(classpath);
            command.add(className);
            command.add(String.valueOf(port));

            ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectOutput(new File("server.log"));
            builder.redirectError(new File("server.err"));
            builder.start();

            System.out.println("API server process initiated on port " + port + ". Output redirected to server.log and server.err.");
            return 0;
        } catch (IOException e) {
            System.err.println("Error starting API server process: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
