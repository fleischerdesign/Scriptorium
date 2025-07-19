package org.scriptorium.core.factories;

import org.scriptorium.core.domain.Book;
import org.scriptorium.core.domain.Genre;
import org.scriptorium.core.dto.OpenLibraryBook;
import org.scriptorium.core.services.GenreService;
import org.scriptorium.core.repositories.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookFactoryTest {

    private GenreService mockGenreService;
    private BookFactory factory;

    @BeforeEach
    void setUp() {
        // Create a simple mock for GenreRepository
        GenreRepository mockGenreRepository = new GenreRepository() {
            @Override
            public Genre save(Genre genre) {
                // Simulate saving by assigning a dummy ID if null
                if (genre.getId() == null) {
                    genre.setId(1L); // Assign a dummy ID for testing
                }
                return genre;
            }

            @Override
            public Optional<Genre> findById(Long id) {
                return Optional.empty(); // Not needed for these tests
            }

            @Override
            public Optional<Genre> findByName(String name) {
                // Simulate finding an existing genre if needed, otherwise empty
                if ("UNKNOWN".equals(name)) {
                    return Optional.of(new Genre(1L, "UNKNOWN"));
                }
                return Optional.empty();
            }

            @Override
            public List<Genre> findAll() {
                return Collections.emptyList(); // Not needed for these tests
            }

            @Override
            public void deleteById(Long id) {
                // Not needed for these tests
            }
        };
        mockGenreService = new GenreService(mockGenreRepository);
        factory = new BookFactory(mockGenreService);
    }

    @Test
    void fromOpenLibraryBook_createsValidBook() {
        OpenLibraryBook apiBook = new OpenLibraryBook();
        apiBook.setTitle("Test Book Title");

        Book book = factory.fromOpenLibraryBook(apiBook);

        assertEquals("Test Book Title", book.getTitle(), "Title should match the API DTO");
    }

    @Test
    void fromOpenLibraryBook_handlesMissingDataAndProvidesDefaults() {
        OpenLibraryBook apiBook = new OpenLibraryBook();
        apiBook.setTitle(null);

        Book book = factory.fromOpenLibraryBook(apiBook);

        assertEquals("Untitled Book", book.getTitle(), "Title should default to 'Untitled Book'");
    }

    @Test
    void fromOpenLibraryBook_handlesEmptyLists() {
        OpenLibraryBook apiBook = new OpenLibraryBook();
        apiBook.setTitle("Title from Empty Lists Test");
        apiBook.setAuthorNames(Collections.emptyList());
        apiBook.setPublishers(Collections.emptyList());
        apiBook.setIsbns(Collections.emptyList());
        apiBook.setIsbn10(Collections.emptyList());
        apiBook.setIsbn13(Collections.emptyList());
        apiBook.setFirstPublishYear(null);

        Book book = factory.fromOpenLibraryBook(apiBook);

        assertEquals("Title from Empty Lists Test", book.getTitle());

        assertNotNull(book.getAuthors(), "Authors list should not be null for empty list source");
        assertEquals(1, book.getAuthors().size(), "Authors list should contain one default author for empty list source");
        assertEquals("Unknown Author", book.getAuthors().get(0).getName(), "Default author should be 'Unknown Author'");

        assertNotNull(book.getMainPublisher(), "Main Publisher should not be null for empty list source");
        assertEquals("Unknown Publisher", book.getMainPublisher().getName(), "Publisher should default to 'Unknown Publisher' for empty list source");

        assertNotNull(book.getIsbns(), "ISBNs list should not be null for empty list source");
        assertTrue(book.getIsbns().isEmpty(), "ISBNs list should be empty for empty list source");

        assertEquals(0, book.getPublicationYear(), "Publication year should default to 0 if not provided");
        assertNotNull(book.getGenre(), "Genre should not be null");
        assertEquals("UNKNOWN", book.getGenre().getName(), "Genre should be UNKNOWN by default");
    }

    @Test
    void fromOpenLibraryBook_handlesMissingOptionalFields() {
        OpenLibraryBook apiBook = new OpenLibraryBook();
        apiBook.setTitle("Book with minimal data");
        apiBook.setAuthorNames(List.of("Single Author"));
        apiBook.setPublishers(List.of("Single Publisher"));

        Book book = factory.fromOpenLibraryBook(apiBook);

        assertEquals("Book with minimal data", book.getTitle());
        assertEquals("Single Author", book.getAuthors().get(0).getName());
        assertEquals("Single Publisher", book.getMainPublisher().getName());
        assertTrue(book.getIsbns().isEmpty(), "ISBNs list should be empty if not provided");
        assertEquals(0, book.getPublicationYear(), "Publication year should default to 0");
        assertNotNull(book.getGenre(), "Genre should not be null");
        assertEquals("UNKNOWN", book.getGenre().getName(), "Genre should be UNKNOWN");
        assertNull(book.getDescription(), "Description should be null if not provided");
    }
}