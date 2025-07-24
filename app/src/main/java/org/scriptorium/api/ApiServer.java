
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
     * Initializes the Javalin application. I've configured it to use my custom
     * JacksonJsonMapper for JSON serialization/deserialization and enabled
     * CORS for all hosts to allow frontend applications to access the API.
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
     * I'm now passing in a list of CrudController instances, which makes the route
     * definition much cleaner and more scalable. Each controller is responsible
     * for registering its own routes.
     *
     * @param controllers A list of CrudController instances to register routes from.
     */
    public void defineRoutes(List<? extends CrudController<?, ?, ?>> controllers) {
        for (CrudController<?, ?, ?> controller : controllers) {
            controller.registerRoutes(app);
        }
    }
}
