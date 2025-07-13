package org.scriptorium.core.factories;

import org.scriptorium.core.domain.Author;
import org.scriptorium.core.domain.Book;
import org.scriptorium.core.domain.Genre;
import org.scriptorium.core.domain.Publisher;
import org.scriptorium.core.dto.OpenLibraryBook;
import java.time.Year;
import java.util.*;
import java.util.Objects;

public class BookFactory {
    public static Book fromOpenLibraryBook(OpenLibraryBook apiBook) {
        // Handle publisher - use first publisher or "Unknown"
        String publisherName = Optional.ofNullable(apiBook.getPublishers())
            .filter(list -> !list.isEmpty())
            .map(list -> list.get(0))
            .orElse("Unknown");
        Publisher publisher = new Publisher(publisherName);

        // Handle author - use first author or "Unknown"
        String authorName = Optional.ofNullable(apiBook.getAuthorNames())
            .filter(list -> !list.isEmpty())
            .map(list -> list.get(0))
            .orElse("Unknown");
        Author author = new Author(authorName);

        // Handle ISBN - extract from ia field
        String isbn = Optional.ofNullable(apiBook.getIa())
            .map(iaList -> iaList.stream()
                .filter(ia -> ia.startsWith("isbn_"))
                .map(ia -> ia.substring(5)) // Remove "isbn_" prefix
                .findFirst()
                .orElse(null))
            .orElse("0000000000");

        // Handle title
        String title = Optional.ofNullable(apiBook.getTitle())
            .orElse("No Title");

        // Use publication year from API or current year as fallback
        int publishYear = Optional.ofNullable(apiBook.getFirstPublishYear())
            .orElse(Year.now().getValue());

        return new Book(
            isbn,
            title,
            author,
            publishYear,
            publisher,
            Genre.UNKNOWN
        );
    }
}