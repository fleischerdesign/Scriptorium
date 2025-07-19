package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.Reservation;
import java.util.List;
import java.util.Optional;

/**
 * A repository interface for Reservation entities.
 * It defines the standard operations to be performed on Reservation objects.
 */
public interface ReservationRepository {

    /**
     * Retrieves a reservation by its ID.
     *
     * @param id The ID of the reservation to retrieve.
     * @return An Optional containing the reservation if found, or an empty Optional otherwise.
     */
    Optional<Reservation> findById(Long id);

    /**
     * Retrieves all reservations.
     *
     * @return A list of all reservations.
     */
    List<Reservation> findAll();

    /**
     * Saves a given reservation. Use the returned instance for further operations
     * as the save operation might have changed the reservation instance (e.g., set an ID).
     *
     * @param reservation The reservation to save.
     * @return The saved reservation.
     */
    Reservation save(Reservation reservation);

    /**
     * Deletes a reservation by its ID.
     *
     * @param id The ID of the reservation to delete.
     */
    void deleteById(Long id);
}
