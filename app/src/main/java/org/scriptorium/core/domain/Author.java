package org.scriptorium.core.domain;

import java.util.Objects;

/**
 * Represents an author in the Scriptorium domain model.
 * This class holds basic information about an author, primarily their name and a database ID.
 */
public class Author {
    private Long id;
    private String name;

    /**
     * Default constructor for Author. Required for JDBC to instantiate objects from ResultSet.
     */
    public Author() {}

    /**
     * Constructs a new Author with the specified name.
     *
     * @param name The name of the author.
     */
    public Author(String name) {
        this.name = name;
    }

    /**
     * Constructs a new Author with the specified ID and name.
     *
     * @param id The unique identifier of the author.
     * @param name The name of the author.
     */
    public Author(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the unique identifier of the author.
     * @return The author's ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the author.
     * @param id The new ID for the author.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the name of the author.
     *
     * @return The author's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the author.
     *
     * @param name The new name for the author.
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        // Equality based on ID if available, otherwise on name
        if (id != null && author.id != null) {
            return Objects.equals(id, author.id);
        }
        return Objects.equals(name, author.name);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return Objects.hash(id);
        }
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Author{" +
               "id=" + id +
               ", name='" + name + "\'" +
               '}';
    }
}