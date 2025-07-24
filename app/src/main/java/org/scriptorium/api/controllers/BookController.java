package org.scriptorium.api.controllers;

import io.javalin.http.Context;
import org.scriptorium.core.services.BookService;

/**
 * Controller for handling book-related API requests.
 * This class exposes endpoints for retrieving book data.
 */
public class BookController {

    private final BookService bookService;

    /**
     * Constructs a BookController with the necessary BookService.
     * I'm using dependency injection here to provide the service.
     * @param bookService The service for book operations.
     */
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Handles the GET /api/books request.
     * Retrieves all books using the BookService and returns them as JSON.
     * @param ctx The Javalin Context object for handling the request and response.
     */
    public void getAll(Context ctx) {
        ctx.json(bookService.findAllBooks());
    }
}
