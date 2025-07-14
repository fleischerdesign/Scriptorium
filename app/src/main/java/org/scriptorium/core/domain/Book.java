package org.scriptorium.core.domain;

import java.time.Year;
import java.util.List;
import java.util.Objects;
import java.util.UUID; // Für eine interne, eindeutige ID

/**
 * Repräsentiert ein literarisches Werk in der Bibliothek.
 * Ein Werk ist die abstrakte Einheit (z.B. "Der Herr der Ringe"),
 * unabhängig von einer spezifischen Ausgabe oder einem physischen Exemplar.
 */
public class Book { // Besser wäre hier vielleicht "Work" oder "LiteraryWork"
    // Interne, eindeutige ID für das Werk
    private final String id; // UUID als String

    // Kerninformationen über das Werk
    private String title;
    private List<Author> authors; // Liste von Autoren
    private Genre genre;
    private String description; // Optionale Beschreibung

    // Optional: Primäre ISBN(s), falls bekannt und für das Werk relevant
    // Kann leere Liste sein, wenn keine ISBNs bekannt sind oder das Medium keine hat.
    private List<String> isbns; // Kann mehrere Ausgaben eines Werkes repräsentieren

    // Constructors
    // Hinweis: Für eine Werk-Klasse sind publicationYear und Publisher eher Ausgaben-Details.
    // Ich lasse sie hier der Einfachheit halber noch drin, aber ideal wäre eine Trennung.
    private int publicationYear; // Kann das Jahr der ersten Veröffentlichung des Werks sein
    private Publisher mainPublisher; // Haupt- oder Erstverlag des Werks

    /**
     * Konstruktor für ein neues Werk. Generiert automatisch eine UUID.
     *
     * @param title Buchtitel (mindestens 2 Zeichen)
     * @param authors Liste der Autor-Objekte (darf nicht leer sein)
     * @param genre Genre/Kategorie (darf nicht null sein)
     * @param publicationYear Erscheinungsjahr der Erstveröffentlichung des Werks (optional, kann 0 sein)
     * @param mainPublisher Hauptverlag des Werks (optional, kann null sein)
     * @param description Optionale Beschreibung des Werks
     * @param isbns Optionale Liste von ISBNs, die zu diesem Werk gehören
     */
    public Book(String title,
                List<Author> authors,
                Genre genre,
                int publicationYear,
                Publisher mainPublisher,
                String description,
                List<String> isbns) {
        this.id = UUID.randomUUID().toString(); // Generiert eine eindeutige ID

        setTitle(title); // Nutzt Setter für Validierung
        setAuthors(authors); // Nutzt Setter für Validierung
        setGenre(genre); // Nutzt Setter für Validierung

        // Optional: Validierung für publicationYear
        if (publicationYear != 0) { // Wenn 0 erlaubt ist für unbekannt
             int currentYear = Year.now().getValue();
             if (publicationYear < 1450 || publicationYear > currentYear) {
                 throw new IllegalArgumentException("Ungültiges Erscheinungsjahr für das Werk");
             }
        }
        this.publicationYear = publicationYear;
        this.mainPublisher = mainPublisher; // Publisher kann hier null sein
        this.description = description; // Beschreibung kann null sein
        this.isbns = isbns != null ? isbns : List.of(); // Stellen Sie sicher, dass es nie null ist
    }

    // --- GETTER ---
    public String getId() { return id; }
    public String getTitle() { return title; }
    public List<Author> getAuthors() { return authors; }
    public Genre getGenre() { return genre; }
    public int getPublicationYear() { return publicationYear; }
    public Publisher getMainPublisher() { return mainPublisher; }
    public String getDescription() { return description; }
    public List<String> getIsbns() { return isbns; }


    // --- SETTER (für veränderliche Attribute, wenn die Entität nicht komplett unveränderlich sein muss) ---
    // Wenn die Entität komplett unveränderlich sein soll, dann nur über den Konstruktor und ggf. eine "with..." Methode

    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Titel darf nicht leer sein");
        }
        if (title.length() < 2) {
            throw new IllegalArgumentException("Titel muss mindestens 2 Zeichen haben");
        }
        this.title = title;
    }

    public void setAuthors(List<Author> authors) {
        if (authors == null || authors.isEmpty()) {
            throw new IllegalArgumentException("Ein Werk muss mindestens einen Autor haben");
        }
        if (authors.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Autor-Liste darf keine Null-Einträge enthalten");
        }
        this.authors = authors;
    }

    public void setGenre(Genre genre) {
        this.genre = Objects.requireNonNull(genre, "Genre darf nicht null sein");
    }

    // Optional Setter für publicationYear, mainPublisher, description, isbns
    public void setPublicationYear(int publicationYear) {
        // Hier ggf. wieder Validierung einfügen, wenn es über Setter geändert werden darf
        this.publicationYear = publicationYear;
    }

    public void setMainPublisher(Publisher mainPublisher) {
        this.mainPublisher = mainPublisher;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsbns(List<String> isbns) {
        // Hier könnten Sie auch Validierung für ISBN-Formate hinzufügen
        this.isbns = isbns != null ? isbns : List.of();
    }


    /**
     * Equals basiert auf der internen ID, da diese das Werk eindeutig identifiziert.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id.equals(book.id);
    }

    /**
     * HashCode basiert auf der internen ID.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Informative String-Darstellung.
     */
    @Override
    public String toString() {
        return "Book{" +
               "id='" + id + '\'' +
               ", title='" + title + '\'' +
               ", authors=" + authors +
               ", genre=" + genre +
               ", publicationYear=" + publicationYear +
               ", mainPublisher=" + mainPublisher +
               '}';
    }
}