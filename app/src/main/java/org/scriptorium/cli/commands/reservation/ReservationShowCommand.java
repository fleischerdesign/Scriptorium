package org.scriptorium.cli.commands.reservation;

import org.scriptorium.core.domain.Reservation;
import org.scriptorium.core.services.ReservationService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Command to display details of a specific reservation by ID.
 * This command allows users to retrieve and view information about a reservation
 * stored in the system.
 */
@Command(
    name = "show",
    description = "Displays details of a reservation by ID."
)
public class ReservationShowCommand implements Callable<Integer> {

    @ParentCommand
    ReservationCommand parent; // Injects the parent command (ReservationCommand)

    private final ReservationService reservationService;

    /**
     * Constructor for ReservationShowCommand.
     * @param reservationService The service responsible for reservation operations.
     */
    public ReservationShowCommand(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the reservation to show", required = true)
    private Long id;

    /**
     * Executes the reservation show command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            Optional<Reservation> reservationOptional = reservationService.findReservationById(id);

            if (reservationOptional.isPresent()) {
                Reservation reservation = reservationOptional.get();
                System.out.println("Reservation Details:");
                System.out.println("ID: " + reservation.getId());
                System.out.println("Book: " + (reservation.getBook() != null ? reservation.getBook().getTitle() + " (ID: " + reservation.getBook().getId() + ")" : "N/A"));
                System.out.println("User: " + (reservation.getUser() != null ? reservation.getUser().getFirstName() + " " + reservation.getUser().getLastName() + " (ID: " + reservation.getUser().getId() + ")" : "N/A"));
                System.out.println("Reservation Date: " + reservation.getReservationDate());
                System.out.println("Status: " + reservation.getStatus());
            } else {
                System.out.println("Reservation with ID " + id + " not found.");
                return 1;
            }
            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error retrieving reservation: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
