package org.scriptorium.core.services;

import org.scriptorium.core.domain.Publisher;
import org.scriptorium.core.repositories.PublisherRepository;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.exceptions.DuplicateEmailException;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing publishers.
 * This class encapsulates the business logic for publisher operations,
 * using the PublisherRepository for data access.
 */
public class PublisherService {

    private final PublisherRepository publisherRepository;

    /**
     * Constructs an PublisherService with its required repository.
     * @param publisherRepository The repository for Publisher entities.
     */
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    /**
     * Creates a new publisher.
     * @param publisher The publisher object to create.
     * @return The created publisher, typically with a generated ID.
     * @throws DataAccessException if a data access error occurs.
     * @throws DuplicateEmailException if an publisher with the same name already exists.
     */
    public Publisher createPublisher(Publisher publisher) {
        try {
            return publisherRepository.save(publisher);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to create publisher: " + e.getMessage(), e);
        }
    }

    /**
     * Finds an publisher by its ID.
     * @param id The ID of the publisher to find.
     * @return An Optional containing the publisher if found.
     * @throws DataAccessException if a data access error occurs.
     */
    public Optional<Publisher> findPublisherById(Long id) {
        try {
            return publisherRepository.findById(id);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to find publisher by ID: " + id + ": " + e.getMessage(), e);
        }
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

    /**
     * Retrieves all publishers.
     * @return A list of all publishers.
     * @throws DataAccessException if a data access error occurs.
     */
    public List<Publisher> findAllPublishers() {
        try {
            return publisherRepository.findAll();
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to retrieve all publishers: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing publisher.
     * @param publisher The publisher with updated information to save.
     * @return The updated publisher object.
     * @throws DataAccessException if a data access error occurs.
     * @throws DuplicateEmailException if an Publisher with the same name already exists.
     */
    public Publisher updatePublisher(Publisher publisher) {
        if (publisher.getId() == null) {
            throw new IllegalArgumentException("Publisher must have an ID to be updated.");
        }
        try {
            return publisherRepository.save(publisher);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to update publisher: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes an publisher by its ID.
     * @param id The ID of the publisher to delete.
     * @throws DataAccessException if a data access error occurs.
     */
    public void deletePublisher(Long id) {
        try {
            publisherRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to delete publisher with ID " + id + ": " + e.getMessage(), e);
        }
    }
}
