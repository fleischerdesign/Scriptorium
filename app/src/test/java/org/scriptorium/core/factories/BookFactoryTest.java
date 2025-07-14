package org.scriptorium.core.factories;

import org.scriptorium.core.domain.Book;
import org.scriptorium.core.domain.Genre; // Import Genre Klasse
import org.scriptorium.core.dto.OpenLibraryBook;
import org.junit.jupiter.api.Test;
import java.util.Collections; // Für Collections.emptyList() oder Collections.singletonList()
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BookFactoryTest {

    @Test
    void fromOpenLibraryBook_createsValidBook() {
        OpenLibraryBook apiBook = new OpenLibraryBook();
        apiBook.setTitle("Test Book Title"); // Angepasster Titel für den Test
        apiBook.setAuthorNames(List.of("Test Author One", "Test Author Two")); // Mehrere Autoren
        apiBook.setPublishers(List.of("Test Publisher Name"));
        apiBook.setIsbns(List.of("978-1234567890")); // ISBN-13
        apiBook.setIsbn10(List.of("123456789X")); // ISBN-10
        apiBook.setIsbn13(List.of("978-0987654321")); // Eine weitere ISBN-13
        apiBook.setFirstPublishYear(2023); // Explizites Erscheinungsjahr

        Book book = BookFactory.fromOpenLibraryBook(apiBook);

        assertNotNull(book.getId(), "Book ID should be generated and not null"); // ID sollte generiert werden
        assertEquals("Test Book Title", book.getTitle(), "Title should match the API DTO");

        // Überprüfung der Autorenliste
        assertNotNull(book.getAuthors(), "Authors list should not be null");
        assertFalse(book.getAuthors().isEmpty(), "Authors list should not be empty");
        assertEquals(2, book.getAuthors().size(), "There should be two authors");
        assertEquals("Test Author One", book.getAuthors().get(0).getName(), "First author name should match");
        assertEquals("Test Author Two", book.getAuthors().get(1).getName(), "Second author name should match");

        // Überprüfung des Publishers
        assertNotNull(book.getMainPublisher(), "Main Publisher should not be null");
        assertEquals("Test Publisher Name", book.getMainPublisher().getName(), "Publisher name should match");

        // Überprüfung der ISBNs (alle gesammelten ISBNs sollten vorhanden sein)
        assertNotNull(book.getIsbns(), "ISBNs list should not be null");
        assertFalse(book.getIsbns().isEmpty(), "ISBNs list should not be empty");
        // Überprüfen, ob alle relevanten ISBNs vorhanden sind
        assertTrue(book.getIsbns().contains("978-1234567890"), "Should contain ISBN-13");
        assertTrue(book.getIsbns().contains("123456789X"), "Should contain ISBN-10");
        assertTrue(book.getIsbns().contains("978-0987654321"), "Should contain another ISBN-13");
        assertEquals(3, book.getIsbns().size(), "Should contain 3 unique ISBNs"); // Testet auch die Distinct-Logik in der Factory

        assertEquals(2023, book.getPublicationYear(), "Publication year should match");
        assertEquals(Genre.UNKNOWN, book.getGenre(), "Genre should be UNKNOWN by default");
    }

    @Test
    void fromOpenLibraryBook_handlesMissingDataAndProvidesDefaults() {
        OpenLibraryBook apiBook = new OpenLibraryBook();
        apiBook.setTitle(null); // Titel ist null
        apiBook.setAuthorNames(null); // Autorenliste ist null
        apiBook.setPublishers(null); // Publisherliste ist null
        apiBook.setIsbns(null); // ISBNs sind null
        apiBook.setIsbn10(null);
        apiBook.setIsbn13(null);
        apiBook.setFirstPublishYear(null); // Erscheinungsjahr ist null
        // Keine Beschreibung gesetzt, sollte null bleiben

        Book book = BookFactory.fromOpenLibraryBook(apiBook);

        assertNotNull(book.getId(), "Book ID should still be generated when data is missing");
        assertEquals("Untitled Book", book.getTitle(), "Title should default to 'Untitled Book'");

        // Überprüfung der Autorenliste bei fehlenden Daten
        assertNotNull(book.getAuthors(), "Authors list should not be null even if source is null");
        assertEquals(1, book.getAuthors().size(), "Authors list should contain one default author");
        assertEquals("Unknown Author", book.getAuthors().get(0).getName(), "Default author should be 'Unknown Author'");

        // Überprüfung des Publishers bei fehlenden Daten
        assertNotNull(book.getMainPublisher(), "Main Publisher should not be null even if source is null");
        assertEquals("Unknown Publisher", book.getMainPublisher().getName(), "Publisher should default to 'Unknown Publisher'");

        // Überprüfung der ISBNs bei fehlenden Daten
        assertNotNull(book.getIsbns(), "ISBNs list should not be null even if source is null");
        assertTrue(book.getIsbns().isEmpty(), "ISBNs list should be empty if no ISBNs are provided");

        assertEquals(0, book.getPublicationYear(), "Publication year should default to 0 if not provided");
        assertEquals(Genre.UNKNOWN, book.getGenre(), "Genre should be UNKNOWN by default");
        assertNull(book.getDescription(), "Description should be null if not provided in API DTO or factory");
    }

    @Test
    void fromOpenLibraryBook_handlesEmptyLists() {
        OpenLibraryBook apiBook = new OpenLibraryBook();
        apiBook.setTitle("Title from Empty Lists Test");
        apiBook.setAuthorNames(Collections.emptyList()); // Leere Autorenliste
        apiBook.setPublishers(Collections.emptyList()); // Leere Publisherliste
        apiBook.setIsbns(Collections.emptyList()); // Leere ISBNs Liste
        apiBook.setIsbn10(Collections.emptyList());
        apiBook.setIsbn13(Collections.emptyList());
        apiBook.setFirstPublishYear(null);

        Book book = BookFactory.fromOpenLibraryBook(apiBook);

        assertEquals("Title from Empty Lists Test", book.getTitle());

        // Überprüfung der Autorenliste bei leeren Listen
        assertNotNull(book.getAuthors(), "Authors list should not be null for empty list source");
        assertEquals(1, book.getAuthors().size(), "Authors list should contain one default author for empty list source");
        assertEquals("Unknown Author", book.getAuthors().get(0).getName(), "Default author should be 'Unknown Author'");

        // Überprüfung des Publishers bei leeren Listen
        assertNotNull(book.getMainPublisher(), "Main Publisher should not be null for empty list source");
        assertEquals("Unknown Publisher", book.getMainPublisher().getName(), "Publisher should default to 'Unknown Publisher' for empty list source");

        // Überprüfung der ISBNs bei leeren Listen
        assertNotNull(book.getIsbns(), "ISBNs list should not be null for empty list source");
        assertTrue(book.getIsbns().isEmpty(), "ISBNs list should be empty for empty list source");

        assertEquals(0, book.getPublicationYear(), "Publication year should default to 0 if not provided");
        assertEquals(Genre.UNKNOWN, book.getGenre(), "Genre should be UNKNOWN by default");
    }

    @Test
    void fromOpenLibraryBook_handlesMissingOptionalFields() {
        OpenLibraryBook apiBook = new OpenLibraryBook();
        apiBook.setTitle("Book with minimal data");
        apiBook.setAuthorNames(List.of("Single Author"));
        apiBook.setPublishers(List.of("Single Publisher"));
        // Keine ISBNs gesetzt
        apiBook.setFirstPublishYear(null); // Kein Publikationsjahr

        Book book = BookFactory.fromOpenLibraryBook(apiBook);

        assertEquals("Book with minimal data", book.getTitle());
        assertEquals("Single Author", book.getAuthors().get(0).getName());
        assertEquals("Single Publisher", book.getMainPublisher().getName());
        assertTrue(book.getIsbns().isEmpty(), "ISBNs list should be empty if not provided");
        assertEquals(0, book.getPublicationYear(), "Publication year should default to 0");
        assertEquals(Genre.UNKNOWN, book.getGenre(), "Genre should be UNKNOWN");
        assertNull(book.getDescription(), "Description should be null if not provided");
    }
}