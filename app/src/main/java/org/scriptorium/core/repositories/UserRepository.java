package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.User;
import java.util.List;
import java.util.Optional;

/**
 * A repository interface for User entities.
 * It defines the standard operations to be performed on User objects.
 */
public interface UserRepository extends BaseRepository<User, Long> {

    /**
     * Retrieves a user by its email address.
     *
     * @param email The email address of the user to retrieve.
     * @return An Optional containing the user if found, or an empty Optional otherwise.
     */
    Optional<User> findByEmail(String email);
}
