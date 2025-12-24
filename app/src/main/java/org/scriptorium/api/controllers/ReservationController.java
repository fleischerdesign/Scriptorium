package org.scriptorium.api.controllers;

import org.scriptorium.core.domain.Reservation;
import org.scriptorium.core.services.ReservationService;

/**
 * Controller for handling reservation-related API requests.
 * This class exposes endpoints for retrieving reservation data.
 */
public class ReservationController extends CrudController<Reservation, Long, ReservationService> {

    /**
     * Constructs a ReservationController.
     *
     * @param reservationService The service for reservation operations, injected via dependency injection.
     */
    public ReservationController(ReservationService reservationService) {
        super(reservationService);
    }

    @Override
    protected String getPathPrefix() {
        return "/api/reservations";
    }

    @Override
    protected String getEntityName() {
        return "Reservation";
    }
}
