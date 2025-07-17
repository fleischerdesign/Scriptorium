package org.scriptorium.core.services;

import org.scriptorium.core.domain.User;
import org.scriptorium.core.repositories.UserRepository;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.exceptions.DuplicateEmailException;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing users.
 * This class encapsulates the business logic for user operations,
 * using a UserRepository for data access.
 */
public class UserService {

    private final UserRepository userRepository;

    /**
     * Constructs a UserService with a given UserRepository.
     * @param userRepository The repository to be used for user data operations.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean checkPassword(String candidate, String hashed) {
        return BCrypt.checkpw(candidate, hashed);
    }

    /**
     * Creates a new user.
     * In a real application, this is where you would add validation
     * or other business logic before saving.
     *
     * @param user The user object to create.
     * @return The created user, typically with a generated ID.
     */
    public User createUser(User user) {
        try {
            // Hash the password before saving
            if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
                user.setPasswordHash(hashPassword(user.getPasswordHash()));
            }
            return userRepository.save(user);
        } catch (DuplicateEmailException e) {
            throw new DuplicateEmailException("User with email " + user.getEmail() + " already exists.", e);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to create user: " + e.getMessage(), e);
        }
    }

    /**
     * Finds a user by their ID.
     *
     * @param id The ID of the user to find.
     * @return An Optional containing the user if found.
     */
    public Optional<User> findUserById(Long id) {
        try {
            return userRepository.findById(id);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to find user by ID: " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all users.
     *
     * @return A list of all users.
     */
    public List<User> findAllUsers() {
        try {
            return userRepository.findAll();
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to retrieve all users: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing user.
     *
     * @param user The user with updated information to save.
     * @return The updated user object.
     */
    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User must have an ID to be updated.");
        }
        try {
            // Hash the password if it's provided for update
            if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
                user.setPasswordHash(hashPassword(user.getPasswordHash()));
            }
            return userRepository.save(user);
        } catch (DuplicateEmailException e) {
            throw new DuplicateEmailException("User with email " + user.getEmail() + " already exists.", e);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to update user: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     */
    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to delete user with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Authenticates a user by email and password.
     *
     * @param email The email of the user.
     * @param password The plain-text password provided by the user.
     * @return An Optional containing the authenticated user if credentials are valid, otherwise empty.
     */
    public Optional<User> authenticateUser(String email, String password) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (checkPassword(password, user.getPasswordHash())) {
                    return Optional.of(user);
                }
            }
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new DataAccessException("Authentication failed: " + e.getMessage(), e);
        }
    }
}
