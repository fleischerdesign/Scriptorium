package org.scriptorium.api.controllers;

import io.javalin.http.Context;
import org.scriptorium.core.services.PublisherService;

/**
 * Controller for handling publisher-related API requests.
 * This class exposes endpoints for retrieving publisher data.
 */
public class PublisherController {

    private final PublisherService publisherService;

    /**
     * Constructs a PublisherController with the necessary PublisherService.
     * I'm using dependency injection here to provide the service.
     * @param publisherService The service for publisher operations.
     */
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    /**
     * Handles the GET /api/publishers request.
     * Retrieves all publishers using the PublisherService and returns them as JSON.
     * @param ctx The Javalin Context object for handling the request and response.
     */
    public void getAll(Context ctx) {
        ctx.json(publisherService.findAllPublishers());
    }

    /**
     * Handles the GET /api/publishers/{id} request.
     * Retrieves a single publisher by ID using the PublisherService and returns it as JSON.
     * If the publisher is not found, it returns a 404 status.
     * @param ctx The Javalin Context object for handling the request and response.
     */
    public void getOne(Context ctx) {
        Long publisherId = Long.parseLong(ctx.pathParam("id"));
        publisherService.findPublisherById(publisherId)
                .ifPresentOrElse(
                        ctx::json,
                        () -> ctx.status(404).result("Publisher not found")
                );
    }
}
