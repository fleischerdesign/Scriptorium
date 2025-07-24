package org.scriptorium.api.controllers;

import org.scriptorium.core.domain.Book;
import org.scriptorium.core.services.BookService;

/**
 * Controller for handling book-related API requests.
 * This class exposes endpoints for retrieving book data.
 */
public class BookController extends CrudController<Book, Long, BookService> {

    /**
     * Constructs a BookController with the necessary BookService.
     * I'm using dependency injection here to provide the service.
     * @param bookService The service for book operations.
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
