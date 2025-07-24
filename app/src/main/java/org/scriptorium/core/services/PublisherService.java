package org.scriptorium.core.services;

import org.scriptorium.core.domain.Publisher;
import org.scriptorium.core.repositories.PublisherRepository;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.exceptions.DuplicateEmailException;

import java.util.Optional;

/**
 * Service layer for managing publishers.
 * This class encapsulates the business logic for publisher operations,
 * using the PublisherRepository for data access.
 */
public class PublisherService extends BaseService<Publisher, Long> {

    private final PublisherRepository publisherRepository;

    /**
     * Constructs an PublisherService with its required repository.
     * @param publisherRepository The repository for Publisher entities.
     */
    public PublisherService(PublisherRepository publisherRepository) {
        super(publisherRepository);
        this.publisherRepository = publisherRepository;
    }

    /**
     * Finds an publisher by its name.
     * @param name The name of the publisher to find.
     * @return An Optional containing the publisher if found.
     * @throws DataAccessException if a data access error occurs.
     */
    public Optional<Publisher> findPublisherByName(String name) {
        try {
            return publisherRepository.findByName(name);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to find publisher by name: " + name + ": " + e.getMessage(), e);
        }
    }
}
