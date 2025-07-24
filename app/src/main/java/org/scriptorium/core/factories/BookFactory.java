package org.scriptorium.core.factories;

import org.scriptorium.core.domain.Author;
import org.scriptorium.core.domain.Book; // Updated Book entity
import org.scriptorium.core.domain.Genre;
import org.scriptorium.core.services.GenreService;
import org.scriptorium.core.domain.Publisher;
import org.scriptorium.core.dto.OpenLibraryBook;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A factory class for creating domain model objects, such as {@link Book},
 * from data transfer objects (DTOs) or other raw data sources.
 */
public class BookFactory {

    private final GenreService genreService;

    public BookFactory(GenreService genreService) {
        this.genreService = genreService;
    }

    /**
     * Creates a domain {@link Book} entity from an {@link OpenLibraryBook} DTO.
     *
     * This method is responsible for the complex logic of mapping API-specific data
     * to the application's rich domain model. It handles missing or malformed data
     * by providing sensible defaults, ensuring that the created {@code Book} object
     * is always in a valid state.
     *
     * @param apiBook The DTO received from the Open Library API.
     * @return A new, fully-populated {@code Book} domain entity.
     */
    public Book fromOpenLibraryBook(OpenLibraryBook apiBook) {
        // --- 1. Handle Title ---
        // Ensure title is never null or blank for the Book entity
        String title = Optional.ofNullable(apiBook.getTitle())
                               .filter(s -> !s.isBlank())
                               .orElse("Untitled Book"); // Provide a meaningful default

        // --- 2. Handle Authors ---
        // Create a list of Author domain objects from authorNames
        List<Author> authors = Optional.ofNullable(apiBook.getAuthorNames())
                                       .filter(list -> !list.isEmpty())
                                       .orElse(Collections.singletonList("Unknown Author")) // Fallback for no authors
                                       .stream()
                                       .map(Author::new) // Assuming Author constructor takes a name
                                       .collect(Collectors.toList());

        // --- 3. Handle Publishers ---
        // Get the list of publishers and create a Publisher object from the first one
        Publisher mainPublisher = Optional.ofNullable(apiBook.getPublishers())
                                          .filter(list -> !list.isEmpty())
                                          .map(list -> new Publisher(list.get(0))) // Assuming Publisher constructor takes a name
                                          .orElse(new Publisher("Unknown Publisher")); // Default Publisher

        // --- 4. Handle Publication Year ---
        // Use publication year from API or a default if not available
        int firstPublishYear = Optional.ofNullable(apiBook.getFirstPublishYear())
                                      .orElse(0); // Use 0 or another sentinel value if unknown, as per Book entity's constructor

        // --- 5. Handle ISBNs ---
        // Collect all available ISBNs from the API DTO
        List<String> isbns = new ArrayList<>();
        Optional.ofNullable(apiBook.getIsbns()).ifPresent(isbns::addAll); // Generic ISBNs
        Optional.ofNullable(apiBook.getIsbn10()).ifPresent(isbns::addAll); // ISBN-10 specific
        Optional.ofNullable(apiBook.getIsbn13()).ifPresent(isbns::addAll); // ISBN-13 specific
        // Ensure unique ISBNs (an ISBN might appear in multiple lists)
        isbns = isbns.stream().distinct().collect(Collectors.toList());

        // --- 6. Handle Genre ---
        // For now, always use UNKNOWN, as OpenLibraryBook DTO does not provide a direct genre field.
        // Check if genre exists, otherwise create it.
        Genre genre = genreService.findGenreByName("UNKNOWN")
                                  .orElseGet(() -> genreService.save(new Genre("UNKNOWN")));

        // --- 7. Handle Description (if OpenLibraryBook has one, not in the provided DTO) ---
        // Assuming OpenLibraryBook might have a description field (e.g., apiBook.getDescription())
        String description = null; // Default to null if not available or mapped

        // --- Construct the Book entity ---
        // The Book entity now auto-generates its ID
        return new Book(
            title,
            authors,
            genre,
            firstPublishYear,
            mainPublisher,
            description,
            isbns
        );
    }
}