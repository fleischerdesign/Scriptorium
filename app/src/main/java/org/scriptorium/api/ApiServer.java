
package org.scriptorium.api;

import io.javalin.Javalin;
import org.scriptorium.api.config.JacksonJsonMapper;
import org.scriptorium.api.controllers.CrudController;

import java.util.List;

/**
 * This class sets up and manages the Javalin web server for the Scriptorium API.
 * It configures the JSON mapper, CORS, and defines all API routes.
 */
public class ApiServer {
    private final Javalin app;

    /**
     * Initializes the Javalin application, configuring it to use a custom
     * JacksonJsonMapper for JSON processing and enabling CORS for all hosts.
     */
    public ApiServer() {
        this.app = Javalin.create(config -> {
            config.jsonMapper(new JacksonJsonMapper());
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> it.anyHost());
            });
        });
    }

    /**
     * Starts the Javalin web server on the specified port.
     * @param port The port number to listen on.
     */
    public void start(int port) {
        app.start(port);
        System.out.println("API server started on port " + port);
    }

    /**
     * Stops the Javalin web server.
     */
    public void stop() {
        app.stop();
    }

    /**
     * Defines and registers all API routes with the Javalin application.
     * Routes are defined by iterating through the provided list of controllers,
     * allowing each controller to register its own endpoints. This approach
     * makes route definition cleaner and more scalable.
     *
     * @param controllers A list of CrudController instances to register routes from.
     */
    public void defineRoutes(List<? extends CrudController<?, ?, ?>> controllers) {
        for (CrudController<?, ?, ?> controller : controllers) {
            controller.registerRoutes(app);
        }
    }
}
