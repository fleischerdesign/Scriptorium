package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.Author;
import java.util.List;
import java.util.Optional;

/**
 * A repository interface for Author entities.
 * It defines the standard operations to be performed on Author objects.
 */
public interface AuthorRepository {

    /**
     * Retrieves an author by its ID.
     *
     * @param id The ID of the author to retrieve.
     * @return An Optional containing the author if found, or an empty Optional otherwise.
     */
    Optional<Author> findById(Long id);

    /**
     * Retrieves an author by its name.
     *
     * @param name The name of the author to retrieve.
     * @return An Optional containing the author if found, or an empty Optional otherwise.
     */
    Optional<Author> findByName(String name);

    /**
     * Retrieves all authors.
     *
     * @return A list of all authors.
     */
    List<Author> findAll();

    /**
     * Saves a given author. Use the returned instance for further operations
     * as the save operation might have changed the author instance (e.g., set an ID).
     *
     * @param author The author to save.
     * @return The saved author.
     */
    Author save(Author author);

    /**
     * Deletes an author by its ID.
     *
     * @param id The ID of the author to delete.
     */
    void deleteById(Long id);
}
