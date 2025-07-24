package org.scriptorium.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.json.JsonMapper;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

/**
 * Custom JSON mapper for Javalin that uses Jackson for serialization and deserialization.
 * I've implemented this to ensure proper handling of Java 8 Date and Time API types
 * (like `LocalDateTime`, `LocalDate`, etc.) when converting to/from JSON.
 */
public class JacksonJsonMapper implements JsonMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Initializes the Jackson ObjectMapper. I'm registering the `JavaTimeModule`
     * here to make sure Jackson knows how to serialize and deserialize Java 8
     * date and time objects.
     */
    public JacksonJsonMapper() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Converts a JSON string into a Java object of the specified type.
     * This is crucial for handling incoming request bodies.
     *
     * @param json The JSON string to convert.
     * @param targetType The target Java type.
     * @param <T> The generic type of the target object.
     * @return The deserialized Java object.
     * @throws RuntimeException if deserialization fails.
     */
    @NotNull
    @Override
    public <T> T fromJsonString(@NotNull String json, @NotNull Type targetType) {
        try {
            return objectMapper.readValue(json, objectMapper.constructType(targetType));
        } catch (Exception e) {
            throw new RuntimeException("Could not deserialize json", e);
        }
    }

    /**
     * Converts a Java object into a JSON string.
     * This is used for sending responses back to the client.
     *
     * @param obj The Java object to convert.
     * @param type The type of the object (can be ignored by Jackson).
     * @return The serialized JSON string.
     * @throws RuntimeException if serialization fails.
     */
    @NotNull
    @Override
    public String toJsonString(@NotNull Object obj, @NotNull Type type) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Could not serialize object", e);
        }
    }
}
