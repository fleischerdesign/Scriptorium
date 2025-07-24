package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.Author;
import java.util.List;
import java.util.Optional;

/**
 * A repository interface for Author entities.
 * It defines the standard operations to be performed on Author objects.
 */
public interface AuthorRepository extends BaseRepository<Author, Long> {

    /**
     * Retrieves an author by its name.
     *
     * @param name The name of the author to retrieve.
     * @return An Optional containing the author if found, or an empty Optional otherwise.
     */
    Optional<Author> findByName(String name);
}
