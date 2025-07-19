package org.scriptorium.core.services;

import org.scriptorium.core.domain.Book;
import org.scriptorium.core.repositories.BookRepository;
import org.scriptorium.core.exceptions.DataAccessException;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing books.
 * This class encapsulates the business logic for book operations,
 * using various repositories for data access.
 */
public class BookService {

    private final BookRepository bookRepository;

    /**
     * Constructs a BookService with its required repository.
     * @param bookRepository The repository for Book entities.
     */
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Creates a new book.
     * @param book The book object to create.
     * @return The created book, typically with a generated ID.
     */
    public Book createBook(Book book) {
        try {
            // In a real application, more complex validation might be added here
            // e.g., check if authors/publisher exist or create them if they don't
            return bookRepository.save(book);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to create book: " + e.getMessage(), e);
        }
    }

    /**
     * Finds a book by its ID.
     * @param id The ID of the book to find.
     * @return An Optional containing the book if found.
     */
    public Optional<Book> findBookById(Long id) {
        try {
            return bookRepository.findById(id);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to find book by ID: " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all books.
     * @return A list of all books.
     */
    public List<Book> findAllBooks() {
        try {
            return bookRepository.findAll();
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to retrieve all books: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing book.
     * @param book The book with updated information to save.
     * @return The updated book object.
     */
    public Book updateBook(Book book) {
        if (book.getId() == null) {
            throw new IllegalArgumentException("Book must have an ID to be updated.");
        }
        try {
            return bookRepository.save(book);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to update book: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a book by its ID.
     * @param id The ID of the book to delete.
     */
    public void deleteBook(Long id) {
        try {
            bookRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to delete book with ID " + id + ": " + e.getMessage(), e);
        }
    }
}
