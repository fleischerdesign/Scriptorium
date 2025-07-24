package org.scriptorium.core.services;

import org.scriptorium.core.domain.Genre;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.exceptions.DuplicateEmailException;
import org.scriptorium.core.repositories.GenreRepository;

import java.util.Optional;

/**
 * Service layer for managing genres.
 * This class encapsulates the business logic for genre operations,
 * using the GenreRepository for data access.
 */
public class GenreService extends BaseService<Genre, Long> {

    private final GenreRepository genreRepository;

    /**
     * Constructs an GenreService with its required repository.
     * @param genreRepository The repository for Genre entities.
     */
    public GenreService(GenreRepository genreRepository) {
        super(genreRepository);
        this.genreRepository = genreRepository;
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
}
