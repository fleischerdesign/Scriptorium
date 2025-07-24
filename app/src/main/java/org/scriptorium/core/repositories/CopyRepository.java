package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.Copy;
import org.scriptorium.core.domain.Copy.MediaType;
import org.scriptorium.core.domain.Copy.CopyStatus;

import java.util.List;
import java.util.Optional;

/**
 * A repository interface for Copy entities.
 * It defines the standard operations to be performed on Copy objects.
 */
public interface CopyRepository extends BaseRepository<Copy, Long> {

    /**
     * Finds copies by the ID of the associated media item and its type.
     *
     * @param itemId The ID of the media item (e.g., book ID).
     * @param mediaType The type of the media item (e.g., BOOK).
     * @return A list of copies associated with the given item ID and media type.
     */
    List<Copy> findByItemIdAndMediaType(Long itemId, MediaType mediaType);

    /**
     * Finds copies by the ID of the associated media item and its type and status.
     *
     * @param itemId The ID of the media item (e.g., book ID).
     * @param mediaType The type of the media item (e.g., BOOK).
     * @param status The status of the copy (e.g., AVAILABLE).
     * @return A list of copies associated with the given item ID, media type, and status.
     */
    List<Copy> findByItemIdAndMediaTypeAndStatus(Long itemId, MediaType mediaType, CopyStatus status);
}
