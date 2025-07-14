package org.scriptorium.core.exceptions;

/**
 * Custom exception class for errors encountered during the book import process.
 * This exception is typically thrown when issues arise with API calls, data parsing,
 * or any other step involved in importing book information.
 */
public class BookImportException extends Exception {

    /**
     * Constructs a new BookImportException with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method).
     */
    public BookImportException(String message) {
        super(message);
    }

    /**
     * Constructs a new BookImportException with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause The cause (which is saved for later retrieval by the {@link Throwable#getCause()} method).
     *              (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public BookImportException(String message, Throwable cause) {
        super(message, cause);
    }
}