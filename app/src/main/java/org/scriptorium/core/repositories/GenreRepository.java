package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.Genre;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Genre entities.
 * Defines the standard CRUD operations for genres.
 */
public interface GenreRepository extends BaseRepository<Genre, Long> {

    /**
     * Finds a genre by its name.
     *
     * @param name The name of the genre to find.
     * @return An Optional containing the genre if found, or empty if not.
     */
    Optional<Genre> findByName(String name);
}
