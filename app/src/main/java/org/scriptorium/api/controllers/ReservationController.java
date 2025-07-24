package org.scriptorium.api.controllers;

import io.javalin.http.Context;
import org.scriptorium.core.services.ReservationService;

/**
 * Controller for handling reservation-related API requests.
 * This class exposes endpoints for retrieving reservation data.
 */
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * Constructs a ReservationController with the necessary ReservationService.
     * I'm using dependency injection here to provide the service.
     * @param reservationService The service for reservation operations.
     */
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Handles the GET /api/reservations request.
     * Retrieves all reservations using the ReservationService and returns them as JSON.
     * @param ctx The Javalin Context object for handling the request and response.
     */
    public void getAll(Context ctx) {
        ctx.json(reservationService.findAllReservations());
    }

    /**
     * Handles the GET /api/reservations/{id} request.
     * Retrieves a single reservation by ID using the ReservationService and returns it as JSON.
     * If the reservation is not found, it returns a 404 status.
     * @param ctx The Javalin Context object for handling the request and response.
     */
    public void getOne(Context ctx) {
        Long reservationId = Long.parseLong(ctx.pathParam("id"));
        reservationService.findReservationById(reservationId)
                .ifPresentOrElse(
                        ctx::json,
                        () -> ctx.status(404).result("Reservation not found")
                );
    }
}
