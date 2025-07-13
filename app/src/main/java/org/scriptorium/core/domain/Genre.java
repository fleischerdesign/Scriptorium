package org.scriptorium.core.domain;

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