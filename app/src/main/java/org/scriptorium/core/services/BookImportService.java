package org.scriptorium.core.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.scriptorium.core.domain.Book;
import org.scriptorium.core.dto.OpenLibraryBook;
import org.scriptorium.core.dto.OpenLibraryBookResponse;
import org.scriptorium.core.exceptions.BookImportException;
import org.scriptorium.core.factories.BookFactory;
import org.scriptorium.core.http.SimpleHttpClient;
import java.util.stream.Collectors;
import java.net.URLEncoder;
import java.util.List;

/**
 * Service class responsible for handling the logic of importing books from the OpenLibrary API.
 */
public class BookImportService {
    private final SimpleHttpClient httpClient;
    private final BookFactory bookFactory;
    private final String apiBaseUrl = "https://openlibrary.org";

    /**
     * Constructs a new BookImportService with its required dependencies.
     *
     * @param httpClient  The client for making HTTP requests to the OpenLibrary API.
     * @param bookFactory The factory for converting API DTOs into domain objects.
     * @param genreService The service for managing genre entities.
     */
    public BookImportService(SimpleHttpClient httpClient, BookFactory bookFactory, GenreService genreService) {
        this.httpClient = httpClient;
        this.bookFactory = new BookFactory(genreService);
    }

    /**
     * Searches for books by title using the OpenLibrary API and returns them as a list of domain objects.
     *
     * @param title The title of the book to search for.
     * @return A list of {@link Book} objects matching the search query.
     * @throws BookImportException if the API request fails or the response cannot be parsed.
     */
    public List<Book> importBooksByTitle(String title) throws BookImportException {
        try {
            String url = apiBaseUrl + "/search.json?title=" + URLEncoder.encode(title, "UTF-8");
            System.out.println("Fetching books from: " + url);
            String response = httpClient.get(url);
            
            if (response == null || response.isEmpty()) {
                throw new BookImportException("Empty response from API");
            }
            
            System.out.println("API Response:\n" + response);
            
            List<Book> books;
            try {
                books = parseResponse(response);
                if (books.isEmpty()) {
                    System.out.println("No books found for title: " + title);
                }
                return books;
            } catch (Exception e) {
                System.err.println("Failed to parse API response: " + e.getMessage());
                throw new BookImportException("Failed to parse API response", e);
            }
        } catch (Exception e) {
            System.err.println("API request failed: " + e.getMessage());
            throw new BookImportException("Failed to import books by title '" + title + "'", e);
        }
    }

    private List<Book> parseResponse(String json) throws BookImportException {
        try {
            System.out.println("Raw JSON response (first 1000 chars):\n" +
                (json != null ? json.substring(0, Math.min(1000, json.length())) : "null"));
            
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
            mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            // Disabled UNWRAP_ROOT_VALUE since API response isn't wrapped
            
            try {
                // First try parsing the full response
                OpenLibraryBookResponse response = mapper.readValue(json, OpenLibraryBookResponse.class);
                System.out.println("Found " + response.getDocs().size() + " books in response");
                
                return response.getDocs().stream()
                    .map(bookDto -> {
                        System.out.println("Processing book: " + bookDto.getTitle());
                        return bookFactory.fromOpenLibraryBook(bookDto);
                    })
                    .collect(Collectors.toList());
            } catch (JsonProcessingException e) {
                // If full parsing fails, try parsing just the docs array directly
                System.err.println("Full response parsing failed, trying docs array directly");
                JsonNode root = mapper.readTree(json);
                JsonNode docsNode = root.path("docs");
                List<OpenLibraryBook> books = mapper.convertValue(docsNode,
                    new TypeReference<List<OpenLibraryBook>>() {});
                return books.stream()
                    .map(bookFactory::fromOpenLibraryBook)
                    .collect(Collectors.toList());
            }
        } catch (Exception e) {
            System.err.println("Detailed parsing error:");
            e.printStackTrace();
            throw new BookImportException("Failed to parse API response: " + e.getMessage(), e);
        }
    }
}