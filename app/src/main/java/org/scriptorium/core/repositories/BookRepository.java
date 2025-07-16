package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.Book;
import java.util.List;
import java.util.Optional;

/**
 * A repository interface for Book entities.
 * It defines the standard operations to be performed on Book objects.
 */
public interface BookRepository {

    /**
     * Retrieves a book by its ID.
     *
     * @param id The ID of the book to retrieve.
     * @return An Optional containing the book if found, or an empty Optional otherwise.
     */
    Optional<Book> findById(Long id);

    /**
     * Retrieves all books.
     *
     * @return A list of all books.
     */
    List<Book> findAll();

    /**
     * Saves a given book. Use the returned instance for further operations
     * as the save operation might have changed the book instance (e.g., set an ID).
     *
     * @param book The book to save.
     * @return The saved book.
     */
    Book save(Book book);

    /**
     * Deletes a book by its ID.
     *
     * @param id The ID of the book to delete.
     */
    void deleteById(Long id);
}
