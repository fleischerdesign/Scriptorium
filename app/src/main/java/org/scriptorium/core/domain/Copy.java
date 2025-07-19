package org.scriptorium.core.domain;

/**
 * Represents a physical copy of a media item (e.g., a book, DVD, magazine).
 * This class allows for managing individual items in the library's inventory.
 */
public class Copy {

    private Long id;
    private Long itemId; // The ID of the actual media item (e.g., book.id, dvd.id)
    private String barcode;
    private CopyStatus status;
    private MediaType mediaType; // To distinguish between different types of media items

    public enum CopyStatus {
        AVAILABLE,
        ON_LOAN,
        LOST,
        DAMAGED,
        RESERVED // A copy can be reserved, but not yet on loan
    }

    public enum MediaType {
        BOOK,
        DVD,
        MAGAZINE // Example for future expansion
    }

    public Copy(Long id, Long itemId, String barcode, CopyStatus status, MediaType mediaType) {
        this.id = id;
        this.itemId = itemId;
        this.barcode = barcode;
        this.status = status;
        this.mediaType = mediaType;
    }

    public Copy(Long itemId, String barcode, CopyStatus status, MediaType mediaType) {
        this(null, itemId, barcode, status, mediaType);
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getItemId() {
        return itemId;
    }

    public String getBarcode() {
        return barcode;
    }

    public CopyStatus getStatus() {
        return status;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setStatus(CopyStatus status) {
        this.status = status;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }
}
