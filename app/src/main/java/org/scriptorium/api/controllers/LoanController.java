package org.scriptorium.api.controllers;

import org.scriptorium.core.domain.Loan;
import org.scriptorium.core.services.LoanService;

/**
 * Controller for handling loan-related API requests.
 * This class exposes endpoints for retrieving loan data.
 */
public class LoanController extends CrudController<Loan, Long, LoanService> {

    /**
     * Constructs a LoanController.
     *
     * @param loanService The service for loan operations, injected via dependency injection.
     */
    public LoanController(LoanService loanService) {
        super(loanService);
    }

    @Override
    protected String getPathPrefix() {
        return "/api/loans";
    }

    @Override
    protected String getEntityName() {
        return "Loan";
    }
}
