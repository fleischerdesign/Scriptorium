package org.scriptorium.api.controllers;

import org.scriptorium.core.domain.Book;
import org.scriptorium.core.services.BookService;

/**
 * Controller for handling book-related API requests.
 * This class exposes endpoints for retrieving book data.
 */
public class BookController extends CrudController<Book, Long, BookService> {

    /**
     * Constructs a BookController.
     *
     * @param bookService The service for book operations, injected via dependency injection.
     */
    public BookController(BookService bookService) {
        super(bookService);
    }

    @Override
    protected String getPathPrefix() {
        return "/api/books";
    }

    @Override
    protected String getEntityName() {
        return "Book";
    }
}
