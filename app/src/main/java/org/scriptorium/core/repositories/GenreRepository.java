package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.Genre;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Genre entities.
 * Defines the standard CRUD operations for genres.
 */
public interface GenreRepository {

    /**
     * Saves a new genre or updates an existing one.
     *
     * @param genre The genre to save.
     * @return The saved genre, typically with the generated ID.
     */
    Genre save(Genre genre);

    /**
     * Finds a genre by its ID.
     *
     * @param id The ID of the genre to find.
     * @return An Optional containing the genre if found, or empty if not.
     */
    Optional<Genre> findById(Long id);

    /**
     * Finds a genre by its name.
     *
     * @param name The name of the genre to find.
     * @return An Optional containing the genre if found, or empty if not.
     */
    Optional<Genre> findByName(String name);

    /**
     * Retrieves all genres.
     *
     * @return A list of all genres.
     */
    List<Genre> findAll();

    /**
     * Deletes a genre by its ID.
     *
     * @param id The ID of the genre to delete.
     */
    void deleteById(Long id);
}
