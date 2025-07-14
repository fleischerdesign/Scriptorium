package org.scriptorium.core.factories;

import org.scriptorium.core.domain.Author;
import org.scriptorium.core.domain.Book; // Your updated Book entity
import org.scriptorium.core.domain.Genre;
import org.scriptorium.core.domain.Publisher;
import org.scriptorium.core.dto.OpenLibraryBook;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookFactory {

    /**
     * Creates a domain Book entity from an OpenLibraryBook DTO.
     * This factory maps API-specific data to your rich domain model,
     * handling multiple authors, optional fields, and ISBN collection.
     *
     * @param apiBook The DTO received from the Open Library API.
     * @return A new Book domain entity.
     */
    public static Book fromOpenLibraryBook(OpenLibraryBook apiBook) {
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
        // For now, always use UNKNOWN, or implement mapping logic if possible
        Genre genre = Genre.UNKNOWN; // Or implement mapping logic from apiBook.getSubjects() etc.

        // --- 7. Handle Description (if OpenLibraryBook has one, not in your provided DTO) ---
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
            description, // Pass description
            isbns        // Pass collected ISBNs
        );
    }
}