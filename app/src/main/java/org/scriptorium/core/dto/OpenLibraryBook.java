package org.scriptorium.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenLibraryBook {
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("author_name")
    private List<String> authorNames;
    
    @JsonProperty("author_key")
    private List<String> authorKeys;
    
    @JsonProperty("publisher")
    private List<String> publishers;
    
    @JsonProperty("isbn")
    private List<String> isbns;
    
    @JsonProperty("isbn_10")
    private List<String> isbn10;
    
    @JsonProperty("isbn_13")
    private List<String> isbn13;
    
    @JsonProperty("first_publish_year")
    private Integer firstPublishYear;
    
    @JsonProperty("language")
    private List<String> languages;
    
    @JsonProperty("cover_i")
    private Integer coverId;
    
    @JsonProperty("ia")
    private List<String> ia;

    // Getters
    public String getTitle() {
        return title;
    }

    public List<String> getAuthorNames() {
        return authorNames;
    }

    public List<String> getPublishers() {
        return publishers;
    }

    public List<String> getIsbns() {
        return isbns;
    }

    public List<String> getIa() {
        return ia;
    }

    public Integer getFirstPublishYear() {
        return firstPublishYear;
    }

    public List<String> getLanguages() {
        return languages;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }

    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }

    public void setIsbns(List<String> isbns) {
        this.isbns = isbns;
    }

    public void setFirstPublishYear(Integer firstPublishYear) {
        this.firstPublishYear = firstPublishYear;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getAuthorKeys() {
        return authorKeys;
    }

    public void setAuthorKeys(List<String> authorKeys) {
        this.authorKeys = authorKeys;
    }

    public Integer getCoverId() {
        return coverId;
    }

    public void setCoverId(Integer coverId) {
        this.coverId = coverId;
    }

    public List<String> getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(List<String> isbn10) {
        this.isbn10 = isbn10;
    }

    public List<String> getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(List<String> isbn13) {
        this.isbn13 = isbn13;
    }
}