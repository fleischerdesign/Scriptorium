package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.Book;
import java.util.List;
import java.util.Optional;

/**
 * A repository interface for Book entities.
 * It defines the standard operations to be performed on Book objects.
 */
public interface BookRepository extends BaseRepository<Book, Long> {
}
