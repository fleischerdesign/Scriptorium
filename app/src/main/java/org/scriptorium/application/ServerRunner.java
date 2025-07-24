package org.scriptorium.application;

import org.scriptorium.api.ApiServer;
import org.scriptorium.core.factories.DependencyFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class is the entry point for the API server when run as a separate process.
 * It initializes the ApiServer and handles PID file management.
 */
public class ServerRunner {

    private static final String PID_FILE = "scriptorium.pid";

    public static void main(String[] args) {
        int port = 7070; // Default port
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number: " + args[0] + ". Using default port " + port);
            }
        }

        // Check if the port is available before starting the server
        if (!isPortAvailable(port)) {
            System.err.println("Error: Port " + port + " is already in use. Please choose a different port or stop the process using it.");
            System.exit(1);
        }

        DependencyFactory dependencyFactory = new DependencyFactory();
        ApiServer server = dependencyFactory.createApiServer();

        try {
            // Get current process PID
            String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
            try (FileWriter writer = new FileWriter(PID_FILE)) {
                writer.write(pid);
            }
            System.out.println("API server PID written to " + PID_FILE + ": " + pid);
        } catch (IOException e) {
            System.err.println("Error writing PID file: " + e.getMessage());
            // Continue without PID file if writing fails, but log the error
        }

        // Register a shutdown hook to stop the server gracefully and delete the PID file
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down API server...");
            server.stop();
            try {
                Files.deleteIfExists(Paths.get(PID_FILE));
            } catch (IOException e) {
                System.err.println("Error deleting PID file: " + e.getMessage());
            }
            System.out.println("API server stopped.");
        }));

        server.start(port);
        System.out.println("API server started on port " + port);
    }

    /**
     * Checks if a given port is available.
     * @param port The port number to check.
     * @return true if the port is available, false otherwise.
     */
    private static boolean isPortAvailable(int port) {
        try (ServerSocket ignored = new ServerSocket(port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}

