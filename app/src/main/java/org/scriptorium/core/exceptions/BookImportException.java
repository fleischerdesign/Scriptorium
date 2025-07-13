package org.scriptorium.core.exceptions;

public class BookImportException extends Exception {
    public BookImportException(String message) {
        super(message);
    }

    public BookImportException(String message, Throwable cause) {
        super(message, cause);
    }
}