package org.scriptorium.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing a book entry from the OpenLibrary API search results.
 * This class is used to deserialize the JSON response into a Java object.
 * Fields are mapped using {@code @JsonProperty} annotations.
 */
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore any JSON fields not defined in this DTO
public class OpenLibraryBook {
    /** The title of the book. */
    @JsonProperty("title")
    private String title;
    
    /** A list of author names associated with the book. */
    @JsonProperty("author_name")
    private List<String> authorNames;
    
    /** A list of author keys (IDs) associated with the book. */
    @JsonProperty("author_key")
    private List<String> authorKeys;
    
    /** A list of publisher names. */
    @JsonProperty("publisher")
    private List<String> publishers;
    
    /** A list of general ISBNs for the book. */
    @JsonProperty("isbn")
    private List<String> isbns;
    
    /** A list of ISBN-10 identifiers. */
    @JsonProperty("isbn_10")
    private List<String> isbn10;
    
    /** A list of ISBN-13 identifiers. */
    @JsonProperty("isbn_13")
    private List<String> isbn13;
    
    /** The year of the book's first publication. */
    @JsonProperty("first_publish_year")
    private Integer firstPublishYear;
    
    /** A list of languages the book is available in. */
    @JsonProperty("language")
    private List<String> languages;
    
    /** The cover ID, used to construct cover image URLs. */
    @JsonProperty("cover_i")
    private Integer coverId;
    
    /** A list of Internet Archive IDs. */
    @JsonProperty("ia")
    private List<String> ia;

    /**
     * Returns the title of the book.
     * @return The book title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the book.
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the list of author names.
     * @return A list of author names.
     */
    public List<String> getAuthorNames() {
        return authorNames;
    }

    /**
     * Sets the list of author names.
     * @param authorNames The list of author names to set.
     */
    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }

    /**
     * Returns the list of author keys (IDs).
     * @return A list of author keys.
     */
    public List<String> getAuthorKeys() {
        return authorKeys;
    }

    /**
     * Sets the list of author keys (IDs).
     * @param authorKeys The list of author keys to set.
     */
    public void setAuthorKeys(List<String> authorKeys) {
        this.authorKeys = authorKeys;
    }

    /**
     * Returns the list of publisher names.
     * @return A list of publisher names.
     */
    public List<String> getPublishers() {
        return publishers;
    }

    /**
     * Sets the list of publisher names.
     * @param publishers The list of publisher names to set.
     */
    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }

    /**
     * Returns the list of general ISBNs.
     * @return A list of ISBNs.
     */
    public List<String> getIsbns() {
        return isbns;
    }

    /**
     * Sets the list of general ISBNs.
     * @param isbns The list of ISBNs to set.
     */
    public void setIsbns(List<String> isbns) {
        this.isbns = isbns;
    }

    /**
     * Returns the list of ISBN-10 identifiers.
     * @return A list of ISBN-10s.
     */
    public List<String> getIsbn10() {
        return isbn10;
    }

    /**
     * Sets the list of ISBN-10 identifiers.
     * @param isbn10 The list of ISBN-10s to set.
     */
    public void setIsbn10(List<String> isbn10) {
        this.isbn10 = isbn10;
    }

    /**
     * Returns the list of ISBN-13 identifiers.
     * @return A list of ISBN-13s.
     */
    public List<String> getIsbn13() {
        return isbn13;
    }

    /**
     * Sets the list of ISBN-13 identifiers.
     * @param isbn13 The list of ISBN-13s to set.
     */
    public void setIsbn13(List<String> isbn13) {
        this.isbn13 = isbn13;
    }

    /**
     * Returns the year of the book's first publication.
     * @return The first publish year.
     */
    public Integer getFirstPublishYear() {
        return firstPublishYear;
    }

    /**
     * Sets the year of the book's first publication.
     * @param firstPublishYear The first publish year to set.
     */
    public void setFirstPublishYear(Integer firstPublishYear) {
        this.firstPublishYear = firstPublishYear;
    }

    /**
     * Returns the list of languages the book is available in.
     * @return A list of languages.
     */
    public List<String> getLanguages() {
        return languages;
    }

    /**
     * Sets the list of languages the book is available in.
     * @param languages The list of languages to set.
     */
    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    /**
     * Returns the cover ID.
     * @return The cover ID.
     */
    public Integer getCoverId() {
        return coverId;
    }

    /**
     * Sets the cover ID.
     * @param coverId The cover ID to set.
     */
    public void setCoverId(Integer coverId) {
        this.coverId = coverId;
    }

    /**
     * Returns the list of Internet Archive IDs.
     * @return A list of Internet Archive IDs.
     */
    public List<String> getIa() {
        return ia;
    }

    /**
     * Sets the list of Internet Archive IDs.
     * @param ia The list of Internet Archive IDs to set.
     */
    public void setIa(List<String> ia) {
        this.ia = ia;
    }
}