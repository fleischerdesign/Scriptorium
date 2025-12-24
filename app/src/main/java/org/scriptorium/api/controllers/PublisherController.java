package org.scriptorium.api.controllers;

import org.scriptorium.core.domain.Publisher;
import org.scriptorium.core.services.PublisherService;

/**
 * Controller for handling publisher-related API requests.
 * This class exposes endpoints for retrieving publisher data.
 */
public class PublisherController extends CrudController<Publisher, Long, PublisherService> {

    /**
     * Constructs a PublisherController.
     *
     * @param publisherService The service for publisher operations, injected via dependency injection.
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
