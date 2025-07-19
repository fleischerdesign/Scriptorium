package org.scriptorium.cli.commands.reservation;

import org.scriptorium.core.domain.Reservation;
import org.scriptorium.core.services.ReservationService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.concurrent.Callable;

/**
 * Command to cancel a reservation in the system.
 * This command allows users to change the status of a reservation to CANCELLED.
 */
@Command(
    name = "cancel",
    description = "Cancels an existing book reservation."
)
public class ReservationCancelCommand implements Callable<Integer> {

    @ParentCommand
    ReservationCommand parent; // Injects the parent command (ReservationCommand)

    private final ReservationService reservationService;

    /**
     * Constructor for ReservationCancelCommand.
     * @param reservationService The service responsible for reservation operations.
     */
    public ReservationCancelCommand(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the reservation to cancel", required = true)
    private Long id;

    /**
     * Executes the reservation cancellation command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            Reservation cancelledReservation = reservationService.cancelReservation(id);

            System.out.println("Reservation with ID " + id + " cancelled successfully.");
            System.out.println("New Status: " + cancelledReservation.getStatus());

            return 0;
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            return 1;
        } catch (DataAccessException e) {
            System.err.println("Error cancelling reservation: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
