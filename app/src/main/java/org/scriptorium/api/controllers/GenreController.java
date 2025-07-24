package org.scriptorium.api.controllers;

import io.javalin.http.Context;
import org.scriptorium.core.services.GenreService;

/**
 * Controller for handling genre-related API requests.
 * This class exposes endpoints for retrieving genre data.
 */
public class GenreController {

    private final GenreService genreService;

    /**
     * Constructs a GenreController with the necessary GenreService.
     * I'm using dependency injection here to provide the service.
     * @param genreService The service for genre operations.
     */
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    /**
     * Handles the GET /api/genres request.
     * Retrieves all genres using the GenreService and returns them as JSON.
     * @param ctx The Javalin Context object for handling the request and response.
     */
    public void getAll(Context ctx) {
        ctx.json(genreService.findAllGenres());
    }

    /**
     * Handles the GET /api/genres/{id} request.
     * Retrieves a single genre by ID using the GenreService and returns it as JSON.
     * If the genre is not found, it returns a 404 status.
     * @param ctx The Javalin Context object for handling the request and response.
     */
    public void getOne(Context ctx) {
        Long genreId = Long.parseLong(ctx.pathParam("id"));
        genreService.findGenreById(genreId)
                .ifPresentOrElse(
                        ctx::json,
                        () -> ctx.status(404).result("Genre not found")
                );
    }
}
