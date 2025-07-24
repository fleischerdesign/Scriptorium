package org.scriptorium.api.controllers;

import io.javalin.http.Context;
import org.scriptorium.core.services.AuthorService;

/**
 * Controller for handling author-related API requests.
 * This class exposes endpoints for retrieving author data.
 */
public class AuthorController {

    private final AuthorService authorService;

    /**
     * Constructs an AuthorController with the necessary AuthorService.
     * I'm using dependency injection here to provide the service.
     * @param authorService The service for author operations.
     */
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    /**
     * Handles the GET /api/authors request.
     * Retrieves all authors using the AuthorService and returns them as JSON.
     * @param ctx The Javalin Context object for handling the request and response.
     */
    public void getAll(Context ctx) {
        ctx.json(authorService.findAllAuthors());
    }

    /**
     * Handles the GET /api/authors/{id} request.
     * Retrieves a single author by ID using the AuthorService and returns it as JSON.
     * If the author is not found, it returns a 404 status.
     * @param ctx The Javalin Context object for handling the request and response.
     */
    public void getOne(Context ctx) {
        Long authorId = Long.parseLong(ctx.pathParam("id"));
        authorService.findAuthorById(authorId)
                .ifPresentOrElse(
                        ctx::json,
                        () -> ctx.status(404).result("Author not found")
                );
    }
}
