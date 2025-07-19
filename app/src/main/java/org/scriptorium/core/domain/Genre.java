package org.scriptorium.core.domain;

import java.util.Objects;

/**
 * Represents a genre for books.
 * Each genre has a unique ID and a name.
 */
public class Genre {

    private Long id;
    private String name;

    /**
     * Default constructor.
     */
    public Genre() {
    }

    /**
     * Constructs a new Genre with the specified name.
     *
     * @param name The name of the genre.
     */
    public Genre(String name) {
        this.name = name;
    }

    /**
     * Constructs a new Genre with the specified ID and name.
     *
     * @param id   The unique identifier of the genre.
     * @param name The name of the genre.
     */
    public Genre(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return Objects.equals(id, genre.id) &&
               Objects.equals(name, genre.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Genre{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }
}
