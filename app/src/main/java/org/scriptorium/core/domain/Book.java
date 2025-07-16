package org.scriptorium.core.domain;

import java.time.Year;
import java.util.List;
import java.util.Objects;
/**
 * Represents a literary work in the Scriptorium domain model.
 * A work is an abstract unit (e.g., "The Lord of the Rings"),
 * independent of a specific edition or physical copy.
 */
public class Book {
    // Database-generated ID
    private Long id;

    // Core information about the work
    private String title;
    private List<Author> authors;
    private Genre genre;
    private String description;

    private int publicationYear;
    private Publisher mainPublisher;

    // Optional: Primary ISBN(s), if known and relevant for the work
    // Can be an empty list if no ISBNs are known or the medium does not have any.
    private List<String> isbns;

    // Constructors
    /**
     * Default constructor required for JDBC to instantiate objects from ResultSet.
     */
    public Book() {
    }

    /**
     * Constructs a new Book instance with core details.
     *
     * @param title The title of the book (must be at least 2 characters).
     * @param authors A list of {@link Author} objects (must not be empty).
     * @param genre The {@link Genre} of the book (must not be null).
     * @param publicationYear The year of the book's first publication (optional, can be 0 for unknown).
     * @param mainPublisher The main or primary publisher of the work (optional, can be null).
     * @param description An optional description of the work.
     * @param isbns An optional list of ISBNs associated with this work.
     * @throws IllegalArgumentException if title, authors, or genre are invalid, or if publication year is out of range.
     */
    public Book(String title,
                List<Author> authors,
                Genre genre,
                int publicationYear,
                Publisher mainPublisher,
                String description,
                List<String> isbns) {

        setTitle(title);
        setAuthors(authors);
        setGenre(genre);

        // Optional: Validation for publicationYear
        if (publicationYear != 0) {
             int currentYear = Year.now().getValue();
             if (publicationYear < 1450 || publicationYear > currentYear) {
                 throw new IllegalArgumentException("Invalid publication year for the work");
             }
        }
        this.publicationYear = publicationYear;
        this.mainPublisher = mainPublisher;
        this.description = description;
        this.isbns = isbns != null ? isbns : List.of();
    }

    /**
     * Returns the unique identifier of the book.
     * @return The book's ID.
     */
    public Long getId() { return id; }

    /**
     * Sets the unique identifier of the book.
     * @param id The new ID for the book.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the title of the book.
     * @return The book's title.
     */
    public String getTitle() { return title; }

    /**
     * Returns the list of authors for the book.
     * @return A list of {@link Author} objects.
     */
    public List<Author> getAuthors() { return authors; }

    /**
     * Returns the genre of the book.
     * @return The book's {@link Genre}.
     */
    public Genre getGenre() { return genre; }

    /**
     * Returns the first publication year of the book.
     * @return The publication year.
     */
    public int getPublicationYear() { return publicationYear; }

    /**
     * Returns the main publisher of the book.
     * @return The {@link Publisher} object.
     */
    public Publisher getMainPublisher() { return mainPublisher; }

    /**
     * Returns the description of the book.
     * @return The book's description.
     */
    public String getDescription() { return description; }

    /**
     * Returns the list of ISBNs associated with the book.
     * @return A list of ISBN strings.
     */
    public List<String> getIsbns() { return isbns; }


    /**
     * Sets the title of the book.
     * @param title The new title for the book.
     * @throws IllegalArgumentException if the title is null, blank, or too short.
     */
    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (title.length() < 2) {
            throw new IllegalArgumentException("Title must have at least 2 characters");
        }
        this.title = title;
    }

    /**
     * Sets the authors of the book.
     * @param authors The new list of {@link Author} objects.
     * @throws IllegalArgumentException if the list of authors is null, empty, or contains null entries.
     */
    public void setAuthors(List<Author> authors) {
        if (authors == null || authors.isEmpty()) {
            throw new IllegalArgumentException("A work must have at least one author");
        }
        if (authors.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Author list cannot contain null entries");
        }
        this.authors = authors;
    }

    /**
     * Sets the genre of the book.
     * @param genre The new {@link Genre} for the book.
     * @throws NullPointerException if the genre is null.
     */
    public void setGenre(Genre genre) {
        this.genre = Objects.requireNonNull(genre, "Genre cannot be null");
    }

    /**
     * Sets the publication year of the book.
     * @param publicationYear The new publication year.
     */
    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    /**
     * Sets the main publisher of the book.
     * @param mainPublisher The new {@link Publisher} for the book.
     */
    public void setMainPublisher(Publisher mainPublisher) {
        this.mainPublisher = mainPublisher;
    }

    /**
     * Sets the description of the book.
     * @param description The new description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the list of ISBNs for the book.
     * @param isbns The new list of ISBN strings.
     */
    public void setIsbns(List<String> isbns) {
        this.isbns = isbns != null ? isbns : List.of();
    }


    /**
     * Compares this Book object to another object for equality.
     * Equality is based on the internal unique ID.
     *
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        // Equality based on ID if available, otherwise on a business key (e.g., title + first author)
        if (id != null && book.id != null) {
            return Objects.equals(id, book.id);
        }
        return Objects.equals(title, book.title) &&
               Objects.equals(authors, book.authors) &&
               Objects.equals(genre, book.genre) &&
               publicationYear == book.publicationYear;
    }

    /**
     * Returns a hash code value for the Book object.
     * The hash code is based on the internal unique ID.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        if (id != null) {
            return Objects.hash(id);
        }
        return Objects.hash(title, authors, genre, publicationYear);
    }

    /**
     * Returns a string representation of the Book object.
     * Provides an informative string including ID, title, authors, genre, publication year, and main publisher.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "Book{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", authors=" + authors +
               ", genre=" + genre +
               ", publicationYear=" + publicationYear +
               ", mainPublisher=" + mainPublisher +
               ", isbns=" + isbns +
               '}';
    }
}