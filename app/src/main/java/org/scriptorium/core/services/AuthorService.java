package org.scriptorium.core.services;

import org.scriptorium.core.domain.Author;
import org.scriptorium.core.repositories.AuthorRepository;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.exceptions.DuplicateEmailException;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing authors.
 * This class encapsulates the business logic for author operations,
 * using the AuthorRepository for data access.
 */
public class AuthorService {

    private final AuthorRepository authorRepository;

    /**
     * Constructs an AuthorService with its required repository.
     * @param authorRepository The repository for Author entities.
     */
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    /**
     * Creates a new author.
     * @param author The author object to create.
     * @return The created author, typically with a generated ID.
     * @throws DataAccessException if a data access error occurs.
     * @throws DuplicateEmailException if an author with the same name already exists.
     */
    public Author createAuthor(Author author) {
        try {
            return authorRepository.save(author);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to create author: " + e.getMessage(), e);
        }
    }

    /**
     * Finds an author by its ID.
     * @param id The ID of the author to find.
     * @return An Optional containing the author if found.
     * @throws DataAccessException if a data access error occurs.
     */
    public Optional<Author> findAuthorById(Long id) {
        try {
            return authorRepository.findById(id);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to find author by ID: " + id + ": " + e.getMessage(), e);
        }
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

    /**
     * Retrieves all authors.
     * @return A list of all authors.
     * @throws DataAccessException if a data access error occurs.
     */
    public List<Author> findAllAuthors() {
        try {
            return authorRepository.findAll();
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to retrieve all authors: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing author.
     * @param author The author with updated information to save.
     * @return The updated author object.
     * @throws DataAccessException if a data access error occurs.
     * @throws DuplicateEmailException if an author with the same name already exists.
     */
    public Author updateAuthor(Author author) {
        if (author.getId() == null) {
            throw new IllegalArgumentException("Author must have an ID to be updated.");
        }
        try {
            return authorRepository.save(author);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to update author: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes an author by its ID.
     * @param id The ID of the author to delete.
     * @throws DataAccessException if a data access error occurs.
     */
    public void deleteAuthor(Long id) {
        try {
            authorRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to delete author with ID " + id + ": " + e.getMessage(), e);
        }
    }
}
