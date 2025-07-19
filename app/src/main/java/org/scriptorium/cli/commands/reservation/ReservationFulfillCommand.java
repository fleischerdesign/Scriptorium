package org.scriptorium.cli.commands.reservation;

import org.scriptorium.core.domain.Reservation;
import org.scriptorium.core.services.ReservationService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.concurrent.Callable;

/**
 * Command to fulfill a reservation in the system.
 * This command allows users to change the status of a reservation to FULFILLED.
 */
@Command(
    name = "fulfill",
    description = "Fulfills an existing book reservation."
)
public class ReservationFulfillCommand implements Callable<Integer> {

    @ParentCommand
    ReservationCommand parent; // Injects the parent command (ReservationCommand)

    private final ReservationService reservationService;

    /**
     * Constructor for ReservationFulfillCommand.
     * @param reservationService The service responsible for reservation operations.
     */
    public ReservationFulfillCommand(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the reservation to fulfill", required = true)
    private Long id;

    /**
     * Executes the reservation fulfillment command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            Reservation fulfilledReservation = reservationService.fulfillReservation(id);

            System.out.println("Reservation with ID " + id + " fulfilled successfully.");
            System.out.println("New Status: " + fulfilledReservation.getStatus());

            return 0;
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            return 1;
        } catch (DataAccessException e) {
            System.err.println("Error fulfilling reservation: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
