package org.scriptorium.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenLibraryBookResponse {
    @JsonProperty("numFound")
    private int numFound;
    
    @JsonProperty("docs")
    private List<OpenLibraryBook> docs;

    // Getters and setters
    public int getNumFound() {
        return numFound;
    }

    public void setNumFound(int numFound) {
        this.numFound = numFound;
    }

    public List<OpenLibraryBook> getDocs() {
        return docs;
    }

    public void setDocs(List<OpenLibraryBook> docs) {
        this.docs = docs;
    }
}