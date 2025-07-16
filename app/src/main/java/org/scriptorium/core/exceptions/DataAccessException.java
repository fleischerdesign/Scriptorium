package org.scriptorium.core.exceptions;

/**
 * Generic exception for data access layer errors.
 * This wraps lower-level exceptions (like SQLException) to provide a more
 * consistent error handling mechanism in the application.
 */
public class DataAccessException extends RuntimeException {

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
