package org.scriptorium.api.controllers;

import io.javalin.http.Context;
import org.scriptorium.core.services.CopyService;

/**
 * Controller for handling copy-related API requests.
 * This class exposes endpoints for retrieving copy data.
 */
public class CopyController {

    private final CopyService copyService;

    /**
     * Constructs a CopyController with the necessary CopyService.
     * I'm using dependency injection here to provide the service.
     * @param copyService The service for copy operations.
     */
    public CopyController(CopyService copyService) {
        this.copyService = copyService;
    }

    /**
     * Handles the GET /api/copies request.
     * Retrieves all copies using the CopyService and returns them as JSON.
     * @param ctx The Javalin Context object for handling the request and response.
     */
    public void getAll(Context ctx) {
        ctx.json(copyService.findAllCopies());
    }

    /**
     * Handles the GET /api/copies/{id} request.
     * Retrieves a single copy by ID using the CopyService and returns it as JSON.
     * If the copy is not found, it returns a 404 status.
     * @param ctx The Javalin Context object for handling the request and response.
     */
    public void getOne(Context ctx) {
        Long copyId = Long.parseLong(ctx.pathParam("id"));
        copyService.findCopyById(copyId)
                .ifPresentOrElse(
                        ctx::json,
                        () -> ctx.status(404).result("Copy not found")
                );
    }
}
