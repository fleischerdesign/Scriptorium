package org.scriptorium.core.services;

import org.scriptorium.core.domain.Book;
import org.scriptorium.core.domain.Reservation;
import org.scriptorium.core.domain.User;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.repositories.BookRepository;
import org.scriptorium.core.repositories.ReservationRepository;
import org.scriptorium.core.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing book reservations.
 * This class encapsulates the business logic for reservation operations,
 * using the ReservationRepository for data access and interacting with Book and User repositories.
 */
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    /**
     * Constructs a new ReservationService with its required repositories.
     *
     * @param reservationRepository The repository for Reservation entities.
     * @param bookRepository The repository for Book entities.
     * @param userRepository The repository for User entities.
     */
    public ReservationService(ReservationRepository reservationRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new reservation for a book by a user.
     *
     * @param bookId The ID of the book to reserve.
     * @param userId The ID of the user making the reservation.
     * @return The created Reservation object.
     * @throws IllegalArgumentException if the book or user is not found.
     * @throws DataAccessException if a data access error occurs.
     */
    public Reservation createReservation(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with ID " + bookId + " not found."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found."));

        Reservation newReservation = new Reservation(book, user, LocalDate.now(), Reservation.ReservationStatus.PENDING);
        try {
            return reservationRepository.save(newReservation);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to create reservation: " + e.getMessage(), e);
        }
    }

    /**
     * Cancels an existing reservation.
     *
     * @param reservationId The ID of the reservation to cancel.
     * @return The updated Reservation object.
     * @throws IllegalArgumentException if the reservation is not found or cannot be cancelled.
     * @throws DataAccessException if a data access error occurs.
     */
    public Reservation cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation with ID " + reservationId + " not found."));

        if (reservation.getStatus() == Reservation.ReservationStatus.FULFILLED) {
            throw new IllegalArgumentException("Cannot cancel a fulfilled reservation.");
        }

        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        try {
            return reservationRepository.save(reservation);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to cancel reservation with ID " + reservationId + ": " + e.getMessage(), e);
        }
    }

    /**
     * Marks a reservation as fulfilled.
     *
     * @param reservationId The ID of the reservation to fulfill.
     * @return The updated Reservation object.
     * @throws IllegalArgumentException if the reservation is not found or already fulfilled.
     * @throws DataAccessException if a data access error occurs.
     */
    public Reservation fulfillReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation with ID " + reservationId + " not found."));

        if (reservation.getStatus() == Reservation.ReservationStatus.FULFILLED) {
            throw new IllegalArgumentException("Reservation with ID " + reservationId + " is already fulfilled.");
        }

        reservation.setStatus(Reservation.ReservationStatus.FULFILLED);
        try {
            return reservationRepository.save(reservation);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to fulfill reservation with ID " + reservationId + ": " + e.getMessage(), e);
        }
    }

    /**
     * Finds a reservation by its ID.
     *
     * @param id The ID of the reservation to find.
     * @return An Optional containing the reservation if found.
     * @throws DataAccessException if a data access error occurs.
     */
    public Optional<Reservation> findReservationById(Long id) {
        try {
            return reservationRepository.findById(id);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to find reservation by ID: " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all reservations.
     *
     * @return A list of all reservations.
     * @throws DataAccessException if a data access error occurs.
     */
    public List<Reservation> findAllReservations() {
        try {
            return reservationRepository.findAll();
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to retrieve all reservations: " + e.getMessage(), e);
        }
    }

    /**
     * Finds all reservations for a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of reservations for the given user.
     * @throws DataAccessException if a data access error occurs.
     */
    public List<Reservation> findReservationsByUserId(Long userId) {
        try {
            // Assuming ReservationRepository will have a findByUserId method or similar
            // For now, we'll filter from findAll, but a dedicated repository method is better for performance
            return reservationRepository.findAll().stream()
                    .filter(r -> r.getUser().getId().equals(userId))
                    .toList();
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to find reservations by user ID " + userId + ": " + e.getMessage(), e);
        }
    }

    /**
     * Finds all reservations for a specific book.
     *
     * @param bookId The ID of the book.
     * @return A list of reservations for the given book.
     * @throws DataAccessException if a data access error occurs.
     */
    public List<Reservation> findReservationsByBookId(Long bookId) {
        try {
            // Assuming ReservationRepository will have a findByBookId method or similar
            // For now, we'll filter from findAll, but a dedicated repository method is better for performance
            return reservationRepository.findAll().stream()
                    .filter(r -> r.getBook().getId().equals(bookId))
                    .toList();
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to find reservations by book ID " + bookId + ": " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a reservation by its ID.
     *
     * @param id The ID of the reservation to delete.
     * @throws DataAccessException if a data access error occurs.
     */
    public void deleteReservation(Long id) {
        try {
            reservationRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to delete reservation with ID " + id + ": " + e.getMessage(), e);
        }
    }
}
