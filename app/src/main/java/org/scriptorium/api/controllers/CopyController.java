package org.scriptorium.api.controllers;

import org.scriptorium.core.domain.Copy;
import org.scriptorium.core.services.CopyService;

/**
 * Controller for handling copy-related API requests.
 * This class exposes endpoints for retrieving copy data.
 */
public class CopyController extends CrudController<Copy, Long, CopyService> {

    /**
     * Constructs a CopyController.
     *
     * @param copyService The service for copy operations, injected via dependency injection.
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
