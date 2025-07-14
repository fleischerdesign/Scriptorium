package org.scriptorium.core.domain;

/**
 * Represents an author in the Scriptorium domain model.
 * This class holds basic information about an author, primarily their name.
 */
public class Author {
    private String name;

    /**
     * Default constructor for Author. Required for some frameworks (e.g., JSON deserialization).
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
}
