package org.scriptorium.core.repositories;

import org.scriptorium.core.exceptions.DataAccessException;

import java.util.List;
import java.util.Optional;

/**
 * Base interface for all repositories, defining common CRUD operations.
 * I'm using generics here to make it reusable for any entity type (T) and ID type (ID).
 * @param <T> The type of the entity.
 * @param <ID> The type of the entity's ID.
 */
public interface BaseRepository<T, ID> {

    /**
     * Saves an entity to the database. If the entity has no ID, it's inserted as a new record.
     * Otherwise, the existing record is updated.
     * @param entity The entity to save.
     * @return The saved entity, with its ID set if it was a new insertion.
     * @throws DataAccessException if a database access error occurs.
     */
    T save(T entity) throws DataAccessException;

    /**
     * Finds an entity by its ID.
     * @param id The ID of the entity to find.
     * @return An Optional containing the found entity, or an empty Optional if no entity is found.
     * @throws DataAccessException if a database access error occurs.
     */
    Optional<T> findById(ID id) throws DataAccessException;

    /**
     * Retrieves all entities.
     * @return A list of all entities.
     * @throws DataAccessException if a database access error occurs.
     */
    List<T> findAll() throws DataAccessException;

    /**
     * Deletes an entity by its ID.
     * @param id The ID of the entity to delete.
     * @throws DataAccessException if a database access error occurs.
     */
    void deleteById(ID id) throws DataAccessException;
}
