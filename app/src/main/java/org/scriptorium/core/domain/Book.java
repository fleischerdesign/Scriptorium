package org.scriptorium.core.domain;

import java.util.Objects;

/**
 * Repräsentiert ein Buch in der Bibliothek.
 * Enthält Kerninformationen über das Buch und Validierungslogik.
 */
public class Book {
    // Unveränderliche Felder für Datenintegrität
    private final String isbn;
    private final String title;
    private final Author author;
    private final int publicationYear;
    private final Publisher publisher;
    private final Genre genre;

    /**
     * Konstruktor mit Validierung
     * @param isbn Internationale Standardbuchnummer (muss 10 oder 13 Zeichen haben)
     * @param title Buchtitel (mindestens 2 Zeichen)
     * @param author Autor-Objekt (darf nicht null sein)
     * @param publicationYear Erscheinungsjahr (zwischen 1450 und aktuelles Jahr)
     * @param publisher Verlag (darf nicht null sein)
     * @param genre Genre/Kategorie (darf nicht null sein)
     */
    public Book(String isbn, 
                String title, 
                Author author, 
                int publicationYear, 
                Publisher publisher, 
                Genre genre) {
                
        // ISBN-Validierung
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN darf nicht leer sein");
        }
        if (isbn.length() != 10 && isbn.length() != 13) {
            throw new IllegalArgumentException("ISBN muss 10 oder 13 Zeichen haben");
        }
        
        // Titel-Validierung
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Titel darf nicht leer sein");
        }
        if (title.length() < 2) {
            throw new IllegalArgumentException("Titel muss mindestens 2 Zeichen haben");
        }
        
        // Jahres-Validierung
        int currentYear = java.time.Year.now().getValue();
        if (publicationYear < 1450 || publicationYear > currentYear) {
            throw new IllegalArgumentException("Ungültiges Erscheinungsjahr");
        }
        
        this.isbn = isbn;
        this.title = title;
        this.author = Objects.requireNonNull(author, "Autor darf nicht null sein");
        this.publicationYear = publicationYear;
        this.publisher = Objects.requireNonNull(publisher, "Verlag darf nicht null sein");
        this.genre = Objects.requireNonNull(genre, "Genre darf nicht null sein");
    }

    // Getter-Methoden (keine Setter für Unveränderlichkeit)
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public Author getAuthor() { return author; }
    public int getPublicationYear() { return publicationYear; }
    public Publisher getPublisher() { return publisher; }
    public Genre getGenre() { return genre; }

    /**
     * Equals basiert auf ISBN, da diese weltweit eindeutig ist
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return isbn.equals(book.isbn);
    }

    /**
     * HashCode basiert auf ISBN
     */
    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    /**
     * Informative String-Darstellung
     */
    @Override
    public String toString() {
        return title + " (" + publicationYear + ") - " + author;
    }
}
