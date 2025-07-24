package org.scriptorium.cli.commands.reservation;

import org.scriptorium.core.domain.Reservation;
import org.scriptorium.core.services.BookService;
import org.scriptorium.core.services.ReservationService;
import org.scriptorium.core.services.UserService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.concurrent.Callable;

/**
 * Command to create a new reservation in the system.
 * This command allows users to create a reservation for a book by a user.
 */
@Command(
    name = "create",
    description = "Creates a new book reservation."
)
public class ReservationCreateCommand implements Callable<Integer> {

    @ParentCommand
    ReservationCommand parent; // Injects the parent command (ReservationCommand)

    private final ReservationService reservationService;
    private final BookService bookService;
    private final UserService userService;

    /**
     * Constructor for ReservationCreateCommand.
     * @param reservationService The service responsible for reservation operations.
     * @param bookService The service responsible for book operations.
     * @param userService The service responsible for user operations.
     */
    public ReservationCreateCommand(ReservationService reservationService, BookService bookService, UserService userService) {
        this.reservationService = reservationService;
        this.bookService = bookService;
        this.userService = userService;
    }

    @Option(names = {"-b", "--book-id"}, description = "ID of the book to reserve", required = true)
    private Long bookId;

    @Option(names = {"-u", "--user-id"}, description = "ID of the user making the reservation", required = true)
    private Long userId;

    /**
     * Executes the reservation creation command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            // Verify book and user existence before creating reservation
            bookService.findById(bookId)
                    .orElseThrow(() -> new IllegalArgumentException("Book with ID " + bookId + " not found."));
            userService.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found."));

            Reservation newReservation = reservationService.createReservation(bookId, userId);

            System.out.println("Reservation created successfully:");
            System.out.println("ID: " + newReservation.getId());
            System.out.println("Book ID: " + newReservation.getBook().getId());
            System.out.println("User ID: " + newReservation.getUser().getId());
            System.out.println("Reservation Date: " + newReservation.getReservationDate());
            System.out.println("Status: " + newReservation.getStatus());

            return 0;
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            return 1;
        } catch (DataAccessException e) {
            System.err.println("Error creating reservation: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
