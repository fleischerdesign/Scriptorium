package org.scriptorium.api.controllers;

import org.scriptorium.core.domain.Copy;
import org.scriptorium.core.services.CopyService;

/**
 * Controller for handling copy-related API requests.
 * This class exposes endpoints for retrieving copy data.
 */
public class CopyController extends CrudController<Copy, Long, CopyService> {

    /**
     * Constructs a CopyController with the necessary CopyService.
     * I'm using dependency injection here to provide the service.
     * @param copyService The service for copy operations.
     */
    public CopyController(CopyService copyService) {
        super(copyService);
    }

    @Override
    protected String getPathPrefix() {
        return "/api/copies";
    }

    @Override
    protected String getEntityName() {
        return "Copy";
    }
}
