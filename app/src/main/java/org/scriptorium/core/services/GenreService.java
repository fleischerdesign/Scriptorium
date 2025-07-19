package org.scriptorium.core.services;

import org.scriptorium.core.domain.Genre;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.exceptions.DuplicateEmailException;
import org.scriptorium.core.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing genres.
 * This class encapsulates the business logic for genre operations,
 * using the GenreRepository for data access.
 */
public class GenreService {

    private final GenreRepository genreRepository;

    /**
     * Constructs an GenreService with its required repository.
     * @param genreRepository The repository for Genre entities.
     */
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    /**
     * Creates a new genre.
     * @param genre The genre object to create.
     * @return The created genre, typically with a generated ID.
     * @throws DataAccessException if a data access error occurs.
     * @throws DuplicateEmailException if an genre with the same name already exists.
     */
    public Genre createGenre(Genre genre) {
        try {
            return genreRepository.save(genre);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to create genre: " + e.getMessage(), e);
        }
    }

    /**
     * Finds an genre by its ID.
     * @param id The ID of the genre to find.
     * @return An Optional containing the genre if found.
     * @throws DataAccessException if a data access error occurs.
     */
    public Optional<Genre> findGenreById(Long id) {
        try {
            return genreRepository.findById(id);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to find genre by ID: " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Finds an genre by its name.
     * @param name The name of the genre to find.
     * @return An Optional containing the genre if found.
     * @throws DataAccessException if a data access error occurs.
     */
    public Optional<Genre> findGenreByName(String name) {
        try {
            return genreRepository.findByName(name);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to find genre by name: " + name + ": " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all genres.
     * @return A list of all genre.
     * @throws DataAccessException if a data access error occurs.
     */
    public List<Genre> findAllGenres() {
        try {
            return genreRepository.findAll();
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to retrieve all genres: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing genre.
     * @param genre The genre with updated information to save.
     * @return The updated genre object.
     * @throws DataAccessException if a data access error occurs.
     * @throws DuplicateEmailException if an genre with the same name already exists.
     */
    public Genre updateGenre(Genre genre) {
        if (genre.getId() == null) {
            throw new IllegalArgumentException("Genre must have an ID to be updated.");
        }
        try {
            return genreRepository.save(genre);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to update genre: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes an genre by its ID.
     * @param id The ID of the genre to delete.
     * @throws DataAccessException if a data access error occurs.
     */
    public void deleteGenre(Long id) {
        try {
            genreRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to delete genre with ID " + id + ": " + e.getMessage(), e);
        }
    }
}
