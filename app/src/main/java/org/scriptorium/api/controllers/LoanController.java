package org.scriptorium.api.controllers;

import io.javalin.http.Context;
import org.scriptorium.core.services.LoanService;

/**
 * Controller for handling loan-related API requests.
 * This class exposes endpoints for retrieving loan data.
 */
public class LoanController {

    private final LoanService loanService;

    /**
     * Constructs a LoanController with the necessary LoanService.
     * I'm using dependency injection here to provide the service.
     * @param loanService The service for loan operations.
     */
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    /**
     * Handles the GET /api/loans request.
     * Retrieves all loans using the LoanService and returns them as JSON.
     * @param ctx The Javalin Context object for handling the request and response.
     */
    public void getAll(Context ctx) {
        ctx.json(loanService.findAllLoans());
    }

    /**
     * Handles the GET /api/loans/{id} request.
     * Retrieves a single loan by ID using the LoanService and returns it as JSON.
     * If the loan is not found, it returns a 404 status.
     * @param ctx The Javalin Context object for handling the request and response.
     */
    public void getOne(Context ctx) {
        Long loanId = Long.parseLong(ctx.pathParam("id"));
        loanService.findLoanById(loanId)
                .ifPresentOrElse(
                        ctx::json,
                        () -> ctx.status(404).result("Loan not found")
                );
    }
}
