package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.User;
import java.util.List;
import java.util.Optional;

/**
 * A repository interface for User entities.
 * It defines the standard operations to be performed on User objects.
 */
public interface UserRepository {

    /**
     * Retrieves a user by its ID.
     *
     * @param id The ID of the user to retrieve.
     * @return An Optional containing the user if found, or an empty Optional otherwise.
     */
    Optional<User> findById(Long id);

    /**
     * Retrieves all users.
     *
     * @return A list of all users.
     */
    List<User> findAll();

    /**
     * Saves a given user. Use the returned instance for further operations
     * as the save operation might have changed the user instance (e.g., set an ID).
     *
     * @param user The user to save.
     * @return The saved user.
     */
    User save(User user);

    /**
     * Deletes a user by its ID.
     *
     * @param id The ID of the user to delete.
     */
    void deleteById(Long id);
}
