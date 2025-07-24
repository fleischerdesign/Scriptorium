package org.scriptorium.core.services;

import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.repositories.BaseRepository;

import java.util.List;
import java.util.Optional;

/**
 * Abstract base class for all services, providing common CRUD operations.
 * I'm using generics here to make it reusable for any entity type (T) and ID type (ID).
 * It depends on a {@link BaseRepository} to perform data access operations.
 * @param <T> The type of the entity.
 * @param <ID> The type of the entity's ID.
 */
public abstract class BaseService<T, ID> {

    protected final BaseRepository<T, ID> repository;

    /**
     * Constructs a BaseService with its required repository.
     * @param repository The repository for the entity type.
     */
    public BaseService(BaseRepository<T, ID> repository) {
        this.repository = repository;
    }

    /**
     * Saves an entity. This method handles both creation and update based on whether the entity has an ID.
     * @param entity The entity object to save.
     * @return The saved entity, typically with a generated ID if it was a new creation.
     */
    public T save(T entity) {
        try {
            return repository.save(entity);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to save entity: " + e.getMessage(), e);
        }
    }

    /**
     * Finds an entity by its ID.
     * @param id The ID of the entity to find.
     * @return An Optional containing the entity if found.
     */
    public Optional<T> findById(ID id) {
        try {
            return repository.findById(id);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to find entity by ID: " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all entities.
     * @return A list of all entities.
     */
    public List<T> findAll() {
        try {
            return repository.findAll();
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to retrieve all entities: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes an entity by its ID.
     * @param id The ID of the entity to delete.
     */
    public void deleteById(ID id) {
        try {
            repository.deleteById(id);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to delete entity with ID " + id + ": " + e.getMessage(), e);
        }
    }
}
