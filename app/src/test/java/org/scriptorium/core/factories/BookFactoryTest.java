package org.scriptorium.core.factories;

import org.scriptorium.core.domain.Author;
import org.scriptorium.core.domain.Book;
import org.scriptorium.core.domain.Publisher;
import org.scriptorium.core.dto.OpenLibraryBook;
import org.scriptorium.core.factories.BookFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class BookFactoryTest {
    @Test
    void fromOpenLibraryBook_createsValidBook() {
        OpenLibraryBook apiBook = new OpenLibraryBook();
        apiBook.setTitle("Test Book");
        apiBook.setAuthorNames(List.of("Test Author"));
        apiBook.setPublishers(List.of("Test Publisher"));
        apiBook.setIsbns(List.of("1234567890"));

        Book book = BookFactory.fromOpenLibraryBook(apiBook);

        assertEquals("Test Book", book.getTitle());
        assertEquals("Test Author", book.getAuthor().getName());
        assertEquals("Test Publisher", book.getPublisher().getName());
        assertEquals("1234567890", book.getIsbn());
    }

    @Test
    void fromOpenLibraryBook_handlesMissingData() {
        OpenLibraryBook apiBook = new OpenLibraryBook();
        apiBook.setTitle(null);
        apiBook.setAuthorNames(null);
        apiBook.setPublishers(null);
        apiBook.setIsbns(null);

        Book book = BookFactory.fromOpenLibraryBook(apiBook);

        assertEquals("No Title", book.getTitle());
        assertEquals("Unknown", book.getAuthor().getName());
        assertEquals("Unknown", book.getPublisher().getName());
        assertEquals("0000000000", book.getIsbn());
    }
}