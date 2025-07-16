package org.scriptorium.core.domain;

import java.util.Objects;

/**
 * Represents a publisher in the Scriptorium domain model.
 * This class holds basic information about a publisher, primarily their name and a database ID.
 */
public class Publisher {
    private Long id;
    private String name;

    /**
     * Default constructor for Publisher. Required for JDBC to instantiate objects from ResultSet.
     */
    public Publisher() {}

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
     * Constructs a new Publisher with the specified ID and name.
     *
     * @param id The unique identifier of the publisher.
     * @param name The name of the publisher.
     */
    public Publisher(Long id, String name) {
        this.id = id;
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Publisher name cannot be null or blank");
        }
        this.name = name;
    }

    /**
     * Returns the unique identifier of the publisher.
     * @return The publisher's ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the publisher.
     * @param id The new ID for the publisher.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the name of the publisher.
     *
     * @return The publisher's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the publisher.
     *
     * @param name The new name for the publisher.
     */
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Publisher name cannot be null or blank");
        }
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publisher publisher = (Publisher) o;
        // Equality based on ID if available, otherwise on name
        if (id != null && publisher.id != null) {
            return Objects.equals(id, publisher.id);
        }
        return Objects.equals(name, publisher.name);
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
        return "Publisher{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }
}