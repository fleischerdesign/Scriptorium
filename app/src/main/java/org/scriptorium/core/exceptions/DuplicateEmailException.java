package org.scriptorium.core.exceptions;

/**
 * Exception thrown when an attempt is made to create a user with an email
 * address that already exists in the system.
 */
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String message) {
        super(message);
    }

    public DuplicateEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
