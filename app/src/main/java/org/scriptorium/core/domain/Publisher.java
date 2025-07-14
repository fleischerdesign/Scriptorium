package org.scriptorium.core.domain;

/**
 * Represents a publisher in the Scriptorium domain model.
 * This class holds basic information about a publisher, primarily their name.
 */
public class Publisher {
    private final String name;

    /**
     * Constructs a new Publisher with the specified name.
     *
     * @param name The name of the publisher (cannot be null or blank).
     * @throws IllegalArgumentException if the name is null or blank.
     */
    public Publisher(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Publisher name cannot be null or blank");
        }
        this.name = name;
    }

    /**
     * Returns the name of the publisher.
     *
     * @return The publisher's name.
     */
    public String getName() {
        return name;
    }
}