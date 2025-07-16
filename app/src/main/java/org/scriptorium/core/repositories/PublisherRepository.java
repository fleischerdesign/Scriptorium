package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.Publisher;
import java.util.List;
import java.util.Optional;

/**
 * A repository interface for Publisher entities.
 * It defines the standard operations to be performed on Publisher objects.
 */
public interface PublisherRepository {

    /**
     * Retrieves a publisher by its ID.
     *
     * @param id The ID of the publisher to retrieve.
     * @return An Optional containing the publisher if found, or an empty Optional otherwise.
     */
    Optional<Publisher> findById(Long id);

    /**
     * Retrieves a publisher by its name.
     *
     * @param name The name of the publisher to retrieve.
     * @return An Optional containing the publisher if found, or an empty Optional otherwise.
     */
    Optional<Publisher> findByName(String name);

    /**
     * Retrieves all publishers.
     *
     * @return A list of all publishers.
     */
    List<Publisher> findAll();

    /**
     * Saves a given publisher. Use the returned instance for further operations
     * as the save operation might have changed the publisher instance (e.g., set an ID).
     *
     * @param publisher The publisher to save.
     * @return The saved publisher.
     */
    Publisher save(Publisher publisher);

    /**
     * Deletes a publisher by its ID.
     *
     * @param id The ID of the publisher to delete.
     */
    void deleteById(Long id);
}
