package org.scriptorium.api.controllers;

import org.scriptorium.core.domain.Publisher;
import org.scriptorium.core.services.PublisherService;

/**
 * Controller for handling publisher-related API requests.
 * This class exposes endpoints for retrieving publisher data.
 */
public class PublisherController extends CrudController<Publisher, Long, PublisherService> {

    /**
     * Constructs a PublisherController with the necessary PublisherService.
     * I'm using dependency injection here to provide the service.
     * @param publisherService The service for publisher operations.
     */
    public PublisherController(PublisherService publisherService) {
        super(publisherService);
    }

    @Override
    protected String getPathPrefix() {
        return "/api/publishers";
    }

    @Override
    protected String getEntityName() {
        return "Publisher";
    }
}
