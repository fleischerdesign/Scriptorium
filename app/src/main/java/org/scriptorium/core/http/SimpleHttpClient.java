package org.scriptorium.core.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 * A simple HTTP client for making GET requests.
 * This class provides basic functionality to fetch content from a given URL.
 */
public class SimpleHttpClient {

    /**
     * Executes an HTTP GET request to the specified URL and returns the response body as a String.
     *
     * @param urlString The URL to which the GET request will be sent.
     * @return The response body as a String.
     * @throws Exception if an I/O error occurs or the connection fails.
     */
    public String get(String urlString) throws Exception {
        URL url = new URI(urlString).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(conn.getInputStream())
        )) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } finally {
            conn.disconnect();
        }
    }
}