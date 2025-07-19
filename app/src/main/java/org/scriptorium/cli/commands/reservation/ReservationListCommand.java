package org.scriptorium.cli.commands.reservation;

import org.scriptorium.core.domain.Reservation;
import org.scriptorium.core.services.ReservationService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Command to list all reservations or reservations filtered by user/book ID.
 * This command retrieves and displays a summary of reservations currently
 * stored in the application's database.
 */
@Command(
    name = "list",
    description = "Lists all reservations or reservations filtered by user/book ID."
)
public class ReservationListCommand implements Callable<Integer> {

    @ParentCommand
    ReservationCommand parent; // Injects the parent command (ReservationCommand)

    private final ReservationService reservationService;

    /**
     * Constructor for ReservationListCommand.
     * @param reservationService The service responsible for reservation operations.
     */
    public ReservationListCommand(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Option(names = {"-u", "--user-id"}, description = "Filter reservations by user ID")
    private Long userId;

    @Option(names = {"-b", "--book-id"}, description = "Filter reservations by book ID")
    private Long bookId;

    /**
     * Executes the reservation list command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            List<Reservation> reservations;

            if (userId != null && bookId != null) {
                System.err.println("Error: Cannot provide both --user-id and --book-id. Please choose one.");
                return 1;
            } else if (userId != null) {
                reservations = reservationService.findReservationsByUserId(userId);
            } else if (bookId != null) {
                reservations = reservationService.findReservationsByBookId(bookId);
            } else {
                reservations = reservationService.findAllReservations();
            }

            if (reservations.isEmpty()) {
                System.out.println("No reservations found.");
            } else {
                System.out.println("Listing reservations:");
                System.out.printf("%-5s %-20s %-20s %-18s %-15s%n", "ID", "Book Title", "User Name", "Reservation Date", "Status");
                System.out.println("-----------------------------------------------------------------------------------------");
                for (Reservation reservation : reservations) {
                    String bookTitle = (reservation.getBook() != null) ? reservation.getBook().getTitle() : "N/A";
                    String userName = (reservation.getUser() != null) ? reservation.getUser().getFirstName() + " " + reservation.getUser().getLastName() : "N/A";
                    System.out.printf("%-5d %-20s %-20s %-18s %-15s%n",
                        reservation.getId(),
                        bookTitle,
                        userName,
                        reservation.getReservationDate(),
                        reservation.getStatus()
                    );
                }
            }
            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error retrieving reservations: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
