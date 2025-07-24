package org.scriptorium.core.services;

import org.scriptorium.core.domain.Author;
import org.scriptorium.core.repositories.AuthorRepository;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.exceptions.DuplicateEmailException;

import java.util.Optional;

/**
 * Service layer for managing authors.
 * This class encapsulates the business logic for author operations,
 * using the AuthorRepository for data access.
 */
public class AuthorService extends BaseService<Author, Long> {

    private final AuthorRepository authorRepository;

    /**
     * Constructs an AuthorService with its required repository.
     * @param authorRepository The repository for Author entities.
     */
    public AuthorService(AuthorRepository authorRepository) {
        super(authorRepository);
        this.authorRepository = authorRepository;
    }

    /**
     * Finds an author by its name.
     * @param name The name of the author to find.
     * @return An Optional containing the author if found.
     * @throws DataAccessException if a data access error occurs.
     */
    public Optional<Author> findAuthorByName(String name) {
        try {
            return authorRepository.findByName(name);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to find author by name: " + name + ": " + e.getMessage(), e);
        }
    }
}
