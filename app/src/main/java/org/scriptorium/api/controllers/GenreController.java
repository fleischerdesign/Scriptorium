package org.scriptorium.api.controllers;

import org.scriptorium.core.domain.Genre;
import org.scriptorium.core.services.GenreService;

/**
 * Controller for handling genre-related API requests.
 * This class exposes endpoints for retrieving genre data.
 */
public class GenreController extends CrudController<Genre, Long, GenreService> {

    /**
     * Constructs a GenreController with the necessary GenreService.
     * I'm using dependency injection here to provide the service.
     * @param genreService The service for genre operations.
     */
    public GenreController(GenreService genreService) {
        super(genreService);
    }

    @Override
    protected String getPathPrefix() {
        return "/api/genres";
    }

    @Override
    protected String getEntityName() {
        return "Genre";
    }
}
