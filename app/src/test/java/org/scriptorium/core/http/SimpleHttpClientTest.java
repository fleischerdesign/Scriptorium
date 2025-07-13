package org.scriptorium.core.http;

import org.junit.jupiter.api.Test;
import org.scriptorium.core.http.SimpleHttpClient;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleHttpClientTest {

    @Test
    public void testGetRequest() throws Exception {
        SimpleHttpClient client = new SimpleHttpClient();
        String url = "https://httpbin.org/get";
        String response = client.get(url);
        
        assertNotNull(response, "Response should not be null");
        assertTrue(response.contains("\"url\": \"" + url + "\""), 
            "Response should contain the request URL");
    }
}