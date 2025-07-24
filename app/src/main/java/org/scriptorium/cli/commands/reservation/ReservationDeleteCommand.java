package org.scriptorium.cli.commands.reservation;

import org.scriptorium.core.services.ReservationService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.concurrent.Callable;

/**
 * Command to delete a reservation from the system by its ID.
 * This command allows users to remove a reservation entry from the database.
 */
@Command(
    name = "delete",
    description = "Deletes a reservation from the system by its ID."
)
public class ReservationDeleteCommand implements Callable<Integer> {

    @ParentCommand
    ReservationCommand parent; // Injects the parent command (ReservationCommand)

    private final ReservationService reservationService;

    /**
     * Constructor for ReservationDeleteCommand.
     * @param reservationService The service responsible for reservation operations.
     */
    public ReservationDeleteCommand(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the reservation to delete", required = true)
    private Long id;

    /**
     * Executes the reservation deletion command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            // Check if the reservation exists before attempting to delete
            if (reservationService.findById(id).isEmpty()) {
                System.out.println("Reservation with ID " + id + " not found. No deletion performed.");
                return 1;
            }

            reservationService.deleteById(id);
            System.out.println("Reservation with ID " + id + " deleted successfully.");
            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error deleting reservation: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
