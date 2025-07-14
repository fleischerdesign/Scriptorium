package org.scriptorium.core.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object (DTO) representing the top-level response structure from the OpenLibrary API search endpoint.
 * This class encapsulates the total number of found documents and a list of {@link OpenLibraryBook} DTOs.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenLibraryBookResponse {
    /** The total number of documents found for the search query. */
    @JsonProperty("numFound")
    private int numFound;
    
    /** A list of book documents (DTOs) returned by the search. */
    @JsonProperty("docs")
    private List<OpenLibraryBook> docs;

    /**
     * Returns the total number of documents found.
     * @return The number of found documents.
     */
    public int getNumFound() {
        return numFound;
    }

    /**
     * Sets the total number of documents found.
     * @param numFound The number of found documents to set.
     */
    public void setNumFound(int numFound) {
        this.numFound = numFound;
    }

    /**
     * Returns the list of book documents.
     * @return A list of {@link OpenLibraryBook} DTOs.
     */
    public List<OpenLibraryBook> getDocs() {
        return docs;
    }

    /**
     * Sets the list of book documents.
     * @param docs The list of {@link OpenLibraryBook} DTOs to set.
     */
    public void setDocs(List<OpenLibraryBook> docs) {
        this.docs = docs;
    }
}