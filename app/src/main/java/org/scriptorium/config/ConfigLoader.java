package org.scriptorium.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private final Properties properties = new Properties();

    public ConfigLoader(String resourceName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + resourceName);
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file: " + resourceName, e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
