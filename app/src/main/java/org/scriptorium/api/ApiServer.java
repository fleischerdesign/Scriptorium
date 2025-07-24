
package org.scriptorium.api;

import io.javalin.Javalin;
import org.scriptorium.api.config.JacksonJsonMapper;
import org.scriptorium.api.controllers.BookController;
import org.scriptorium.api.controllers.AuthorController;
import org.scriptorium.api.controllers.UserController;
import org.scriptorium.api.controllers.PublisherController;
import org.scriptorium.api.controllers.GenreController;
import org.scriptorium.api.controllers.CopyController;
import org.scriptorium.api.controllers.LoanController;
import org.scriptorium.api.controllers.ReservationController;

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
     * I'm passing in the controllers as arguments to keep the route definition
     * clean and allow for easy testing and dependency injection.
     *
     * @param bookController The controller for book-related operations.
     * @param authorController The controller for author-related operations.
     * @param userController The controller for user-related operations.
     * @param publisherController The controller for publisher-related operations.
     * @param genreController The controller for genre-related operations.
     * @param copyController The controller for copy-related operations.
     * @param loanController The controller for loan-related operations.
     * @param reservationController The controller for reservation-related operations.
     */
    public void defineRoutes(BookController bookController, AuthorController authorController, UserController userController, PublisherController publisherController, GenreController genreController, CopyController copyController, LoanController loanController, ReservationController reservationController) {
        app.get("/api/books", bookController::getAll);
        app.get("/api/authors", authorController::getAll);
        app.get("/api/authors/{id}", authorController::getOne);
        app.get("/api/users", userController::getAll);
        app.get("/api/users/{id}", userController::getOne);
        app.get("/api/publishers", publisherController::getAll);
        app.get("/api/publishers/{id}", publisherController::getOne);
        app.get("/api/genres", genreController::getAll);
        app.get("/api/genres/{id}", genreController::getOne);
        app.get("/api/copies", copyController::getAll);
        app.get("/api/copies/{id}", copyController::getOne);
        app.get("/api/loans", loanController::getAll);
        app.get("/api/loans/{id}", loanController::getOne);
        app.get("/api/reservations", reservationController::getAll);
        app.get("/api/reservations/{id}", reservationController::getOne);
    }
}
