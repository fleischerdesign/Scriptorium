package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.Publisher;
import java.util.List;
import java.util.Optional;

/**
 * A repository interface for Publisher entities.
 * It defines the standard operations to be performed on Publisher objects.
 */
public interface PublisherRepository extends BaseRepository<Publisher, Long> {

    /**
     * Retrieves a publisher by its name.
     *
     * @param name The name of the publisher to retrieve.
     * @return An Optional containing the publisher if found, or an empty Optional otherwise.
     */
    Optional<Publisher> findByName(String name);
}
