package org.scriptorium.cli.commands;

import org.scriptorium.api.ApiServer;
import org.scriptorium.cli.ScriptoriumCommand;
import org.scriptorium.core.factories.DependencyFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.concurrent.Callable;

/**
 * Picocli command to start the Scriptorium API server.
 * This command allows me to easily launch the web API from the command line,
 * optionally specifying a custom port.
 */
@Command(name = "serve", description = "Starts the API server.")
public class ServeCommand implements Callable<Integer> {

    @ParentCommand
    private ScriptoriumCommand parent;

    @Option(names = {"-p", "--port"}, description = "The port to run the server on.", defaultValue = "7070")
    private int port;

    private final DependencyFactory dependencyFactory;

    /**
     * Constructs the ServeCommand, injecting the DependencyFactory.
     * This factory is essential for creating and configuring the ApiServer.
     * @param dependencyFactory The application's dependency factory.
     */
    public ServeCommand(DependencyFactory dependencyFactory) {
        this.dependencyFactory = dependencyFactory;
    }

    /**
     * The main execution logic for the `serve` command.
     * It creates the ApiServer using the DependencyFactory, starts it on the configured port,
     * and then keeps the main thread alive to ensure the server continues running.
     * @return 0 for success, or a non-zero exit code if an error occurs.
     * @throws Exception if the server fails to start or an interruption occurs.
     */
    @Override
    public Integer call() throws Exception {
        ApiServer server = dependencyFactory.createApiServer();
        server.start(port);

        // Keep the main thread alive to prevent the server from stopping immediately.
        // This is important because Javalin runs on its own threads.
        Thread.currentThread().join();
        return 0;
    }
}
