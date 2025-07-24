package org.scriptorium.core.services;

import org.scriptorium.core.domain.Book;
import org.scriptorium.core.repositories.BookRepository;
import org.scriptorium.core.exceptions.DataAccessException;

/**
 * Service layer for managing books.
 * This class encapsulates the business logic for book operations,
 * using various repositories for data access.
 */
public class BookService extends BaseService<Book, Long> {

    private final BookRepository bookRepository;

    /**
     * Constructs a BookService with its required repository.
     * @param bookRepository The repository for Book entities.
     */
    public BookService(BookRepository bookRepository) {
        super(bookRepository);
        this.bookRepository = bookRepository;
    }
}