package org.scriptorium.api.controllers;

import org.scriptorium.core.domain.Author;
import org.scriptorium.core.services.AuthorService;

/**
 * Controller for handling author-related API requests.
 * This class exposes endpoints for retrieving author data.
 */
public class AuthorController extends CrudController<Author, Long, AuthorService> {

    /**
     * Constructs an AuthorController with the necessary AuthorService.
     * I'm using dependency injection here to provide the service.
     * @param authorService The service for author operations.
     */
    public AuthorController(AuthorService authorService) {
        super(authorService);
    }

    @Override
    protected String getPathPrefix() {
        return "/api/authors";
    }

    @Override
    protected String getEntityName() {
        return "Author";
    }
}
