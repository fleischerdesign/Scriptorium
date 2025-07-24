package org.scriptorium.core.services;

import org.scriptorium.core.domain.Book;
import org.scriptorium.core.domain.Copy;
import org.scriptorium.core.domain.Copy.CopyStatus;
import org.scriptorium.core.domain.Copy.MediaType;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.repositories.BookRepository;
import org.scriptorium.core.repositories.CopyRepository;

import java.util.List;

/**
 * Service class for managing book copies.
 * This class encapsulates the business logic for copy operations,
 * using the CopyRepository for data access and interacting with BookRepository.
 */
public class CopyService extends BaseService<Copy, Long> {

    private final CopyRepository copyRepository;
    private final BookRepository bookRepository;

    /**
     * Constructs a new CopyService with its required repositories.
     *
     * @param copyRepository The repository for Copy entities.
     * @param bookRepository The repository for Book entities.
     */
    public CopyService(CopyRepository copyRepository, BookRepository bookRepository) {
        super(copyRepository);
        this.copyRepository = copyRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * Creates a new copy for a given book.
     *
     * @param bookId The ID of the book for which to create a copy.
     * @param barcode The barcode of the copy (can be null).
     * @return The created Copy object.
     * @throws IllegalArgumentException if the book is not found.
     * @throws DataAccessException if a data access error occurs.
     */
    public Copy createCopy(Long bookId, String barcode) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with ID " + bookId + " not found."));

        Copy newCopy = new Copy(book.getId(), barcode, CopyStatus.AVAILABLE, MediaType.BOOK);
        try {
            return repository.save(newCopy);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to create copy: " + e.getMessage(), e);
        }
    }

    /**
     * Updates the status of a copy.
     *
     * @param copyId The ID of the copy to update.
     * @param newStatus The new status for the copy.
     * @return The updated Copy object.
     * @throws IllegalArgumentException if the copy is not found.
     * @throws DataAccessException if a data access error occurs.
     */
    public Copy updateCopyStatus(Long copyId, CopyStatus newStatus) {
        Copy copy = repository.findById(copyId)
                .orElseThrow(() -> new IllegalArgumentException("Copy with ID " + copyId + " not found."));

        copy.setStatus(newStatus);
        try {
            return repository.save(copy);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to update copy status: " + e.getMessage(), e);
        }
    }

    /**
     * Finds all copies for a specific book.
     *
     * @param bookId The ID of the book.
     * @return A list of copies for the given book.
     * @throws DataAccessException if a data access error occurs.
     */
    public List<Copy> findCopiesByBookId(Long bookId) {
        try {
            return copyRepository.findByItemIdAndMediaType(bookId, MediaType.BOOK);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to find copies by book ID " + bookId + ": " + e.getMessage(), e);
        }
    }

    /**
     * Finds all available copies for a specific book.
     *
     * @param bookId The ID of the book.
     * @return A list of available copies for the given book.
     * @throws DataAccessException if a data access error occurs.
     */
    public List<Copy> findAvailableCopiesByBookId(Long bookId) {
        try {
            return copyRepository.findByItemIdAndMediaTypeAndStatus(bookId, MediaType.BOOK, CopyStatus.AVAILABLE);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to find available copies by book ID " + bookId + ": " + e.getMessage(), e);
        }
    }
}
