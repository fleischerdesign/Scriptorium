package org.scriptorium.core.domain;

public class Publisher {
    private final String name;

    public Publisher(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Publisher name cannot be null or blank");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }
}