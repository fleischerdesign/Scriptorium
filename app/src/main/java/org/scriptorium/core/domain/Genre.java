package org.scriptorium.core.domain;

/**
 * Represents the genre of a book within the Scriptorium domain model.
 * This enum provides a predefined set of genres and a utility method to convert
 * a string value into a {@code Genre} enum, defaulting to UNKNOWN for unrecognized values.
 */
public enum Genre {
    UNKNOWN,
    FICTION,
    NON_FICTION,
    SCIENCE,
    TECHNOLOGY,
    HISTORY,
    BIOGRAPHY,
    FANTASY,
    MYSTERY,
    ROMANCE;

    /**
     * Converts a string representation to a {@code Genre} enum.
     * The conversion is case-insensitive. If the string is null, blank, or does not
     * match any defined genre, it defaults to {@code UNKNOWN}.
     *
     * @param value The string to convert to a Genre.
     * @return The corresponding {@code Genre} enum, or {@code UNKNOWN} if not found or invalid.
     */
    public static Genre fromString(String value) {
        if (value == null || value.isBlank()) {
            return UNKNOWN;
        }
        try {
            return Genre.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}