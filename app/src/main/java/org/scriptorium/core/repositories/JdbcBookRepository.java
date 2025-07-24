package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.Author;
import org.scriptorium.core.domain.Book;
import org.scriptorium.core.domain.Publisher;
import org.scriptorium.core.exceptions.DataAccessException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of the {@link BookRepository} interface.
 * Manages persistence operations for {@link Book} entities, including their
 * relationships with {@link Author} and {@link Publisher} entities, and ISBNs.
 */
public class JdbcBookRepository implements BookRepository {

    private final String dbUrl;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final GenreRepository genreRepository;

    /**
     * Constructs a new JdbcBookRepository with the specified database URL and dependent repositories.
     * @param dbUrl The JDBC URL for the SQLite database.
     * @param authorRepository The repository for Author entities.
     * @param publisherRepository The repository for Publisher entities.
     * @param genreRepository The repository for Genre entities.
     */
    public JdbcBookRepository(String dbUrl, AuthorRepository authorRepository, PublisherRepository publisherRepository, GenreRepository genreRepository) {
        this.dbUrl = dbUrl;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.genreRepository = genreRepository;
    }

    public void init() {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            // Books table
            String createBooksTable = "CREATE TABLE IF NOT EXISTS books ("
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " title TEXT NOT NULL,"
                    + " genre_id INTEGER,"
                    + " publicationYear INTEGER,"
                    + " publisher_id INTEGER,"
                    + " description TEXT,"
                    + " FOREIGN KEY (publisher_id) REFERENCES publishers(id) ON DELETE SET NULL,"
                    + " FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE SET NULL"
                    + ");";
            stmt.execute(createBooksTable);

            // Book-Authors join table (Many-to-Many)
            String createBookAuthorsTable = "CREATE TABLE IF NOT EXISTS book_authors ("
                    + " book_id INTEGER NOT NULL,"
                    + " author_id INTEGER NOT NULL,"
                    + " PRIMARY KEY (book_id, author_id),"
                    + " FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,"
                    + " FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE CASCADE"
                    + ");";
            stmt.execute(createBookAuthorsTable);

            // Book-ISBNs table (One-to-Many)
            String createBookIsbnsTable = "CREATE TABLE IF NOT EXISTS book_isbns ("
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " book_id INTEGER NOT NULL,"
                    + " isbn_value TEXT NOT NULL UNIQUE,"
                    + " FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE"
                    + ");";
            stmt.execute(createBookIsbnsTable);

        } catch (SQLException e) {
            throw new DataAccessException("Error initializing book-related tables: " + e.getMessage(), e);
        }
    }

    /**
     * Finds a book by its ID and eagerly fetches its related authors, publisher, and ISBNs.
     *
     * @param id The ID of the book to find.
     * @return An {@link Optional} containing the found {@link Book}, or an empty Optional if no book is found.
     * @throws DataAccessException if a database access error occurs.
     */
    @Override
    public Optional<Book> findById(Long id) {
        // SQL queries to fetch book details, authors, and ISBNs
        String bookSql = "SELECT id, title, genre_id, publicationYear, publisher_id, description FROM books WHERE id = ?";
        String authorsSql = "SELECT a.id, a.name FROM authors a JOIN book_authors ba ON a.id = ba.author_id WHERE ba.book_id = ?";
        String isbnsSql = "SELECT isbn_value FROM book_isbns WHERE book_id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmtBook = conn.prepareStatement(bookSql);
             PreparedStatement pstmtAuthors = conn.prepareStatement(authorsSql);
             PreparedStatement pstmtIsbns = conn.prepareStatement(isbnsSql)) {

            // 1. Fetch core Book details
            pstmtBook.setLong(1, id);
            ResultSet rsBook = pstmtBook.executeQuery();

            if (!rsBook.next()) {
                return Optional.empty(); // Book not found, return empty Optional
            }

            // Map ResultSet to Book object
            Book book = new Book();
            book.setId(rsBook.getLong("id"));
            book.setTitle(rsBook.getString("title"));
            book.setGenre(genreRepository.findById(rsBook.getLong("genre_id")).orElse(null));
            book.setPublicationYear(rsBook.getInt("publicationYear"));
            book.setDescription(rsBook.getString("description"));

            // 2. Fetch and set Publisher details
            long publisherId = rsBook.getLong("publisher_id");
            if (!rsBook.wasNull()) { // Check if publisher_id was actually set (not NULL)
                publisherRepository.findById(publisherId).ifPresent(book::setMainPublisher);
            }

            // 3. Fetch and set Authors (Many-to-Many relationship)
            List<Author> authors = new ArrayList<>();
            pstmtAuthors.setLong(1, id);
            ResultSet rsAuthors = pstmtAuthors.executeQuery();
            while (rsAuthors.next()) {
                authors.add(new Author(rsAuthors.getLong("id"), rsAuthors.getString("name")));
            }
            book.setAuthors(authors);

            // 4. Fetch and set ISBNs (One-to-Many relationship)
            List<String> isbns = new ArrayList<>();
            pstmtIsbns.setLong(1, id);
            ResultSet rsIsbns = pstmtIsbns.executeQuery();
            while (rsIsbns.next()) {
                isbns.add(rsIsbns.getString("isbn_value"));
            }
            book.setIsbns(isbns);

            return Optional.of(book); // Return the fully constructed Book object

        } catch (SQLException e) {
            // Wrap SQLException in a DataAccessException for consistent error handling
            throw new DataAccessException("Error finding book by ID: " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all books from the database, eagerly fetching their related authors, publishers, and ISBNs.
     * Note: This method can be inefficient for large datasets due to the N+1 query problem
     * (one query for all books, then N additional queries for authors and ISBNs for each book).
     *
     * @return A list of all {@link Book} entities.
     * @throws DataAccessException if a database access error occurs.
     */
    @Override
    public List<Book> findAll() {
        String bookSql = "SELECT b.id, b.title, b.genre_id, b.publicationYear, b.publisher_id, b.description, " +
                         "p.name AS publisherName " +
                         "FROM books b LEFT JOIN publishers p ON b.publisher_id = p.id";
        String authorsSql = "SELECT a.id, a.name FROM authors a JOIN book_authors ba ON a.id = ba.author_id WHERE ba.book_id = ?";
        String isbnsSql = "SELECT isbn_value FROM book_isbns WHERE book_id = ?";

        List<Book> books = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmtAuthors = conn.prepareStatement(authorsSql);
             PreparedStatement pstmtIsbns = conn.prepareStatement(isbnsSql);
             Statement stmtBooks = conn.createStatement();
             ResultSet rsBooks = stmtBooks.executeQuery(bookSql)) {

            while (rsBooks.next()) {
                Book book = new Book();
                book.setId(rsBooks.getLong("id"));
                book.setTitle(rsBooks.getString("title"));
                book.setGenre(genreRepository.findById(rsBooks.getLong("genre_id")).orElse(null));
                book.setPublicationYear(rsBooks.getInt("publicationYear"));
                book.setDescription(rsBooks.getString("description"));

                // Fetch Publisher directly from the initial JOIN
                long publisherId = rsBooks.getLong("publisher_id");
                if (!rsBooks.wasNull()) {
                    String publisherName = rsBooks.getString("publisherName");
                    if (publisherName != null) {
                        book.setMainPublisher(new Publisher(publisherId, publisherName));
                    }
                }

                // Fetch Authors for the current book
                List<Author> authors = new ArrayList<>();
                pstmtAuthors.setLong(1, book.getId());
                ResultSet rsAuthors = pstmtAuthors.executeQuery();
                while (rsAuthors.next()) {
                    authors.add(new Author(rsAuthors.getLong("id"), rsAuthors.getString("name")));
                }
                book.setAuthors(authors);

                // Fetch ISBNs for the current book
                List<String> isbns = new ArrayList<>();
                pstmtIsbns.setLong(1, book.getId());
                ResultSet rsIsbns = pstmtIsbns.executeQuery();
                while (rsIsbns.next()) {
                    isbns.add(rsIsbns.getString("isbn_value"));
                }
                book.setIsbns(isbns);

                books.add(book);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving all books: " + e.getMessage(), e);
        }
        return books;
    }

    /**
     * Saves a book to the database. If the book has no ID, it's inserted as a new record.
     * Otherwise, the existing record is updated.
     *
     * @param book The {@link Book} to save.
     * @return The saved book, now with its ID set if it was a new insertion.
     * @throws DataAccessException if a database access error occurs.
     */
    @Override
    public Book save(Book book) {
        String sql;
        boolean isNew = (book.getId() == null);
        if (isNew) {
            sql = "INSERT INTO books(title, genre_id, publicationYear, publisher_id, description) VALUES(?,?,?,?,?)";
        } else {
            sql = "UPDATE books SET title = ?, genre_id = ?, publicationYear = ?, publisher_id = ?, description = ? WHERE id = ?";
        }

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql, isNew ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS)) {

            pstmt.setString(1, book.getTitle());
            if (book.getGenre() != null && book.getGenre().getId() != null) {
                pstmt.setLong(2, book.getGenre().getId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setInt(3, book.getPublicationYear());

            // Handle Publisher: Find existing or save new one
            Long publisherId = null;
            if (book.getMainPublisher() != null) {
                Publisher publisher = book.getMainPublisher();
                // Check if publisher already exists to avoid duplicates
                Optional<Publisher> existingPublisher = publisherRepository.findByName(publisher.getName());
                if (existingPublisher.isPresent()) {
                    publisherId = existingPublisher.get().getId();
                } else {
                    // If not, save the new publisher
                    Publisher savedPublisher = publisherRepository.save(publisher);
                    publisherId = savedPublisher.getId();
                }
            }
            if (publisherId != null) {
                pstmt.setLong(4, publisherId);
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }

            pstmt.setString(5, book.getDescription());

            if (!isNew) {
                pstmt.setLong(6, book.getId());
            }

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Saving book failed, no rows affected.");
            }

            if (isNew) {
                // Retrieve and set the generated book ID
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        book.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating book failed, no ID obtained.");
                    }
                }
            } else {
                // Simple strategy for relationship update: delete old and insert new.
                // This is less efficient but easier to implement than diffing collections.
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate("DELETE FROM book_authors WHERE book_id = " + book.getId());
                    stmt.executeUpdate("DELETE FROM book_isbns WHERE book_id = " + book.getId());
                }
            }

            // Handle Authors (Many-to-Many relationship)
            // For each author, find if they exist or create them, then link in the join table.
            String insertBookAuthorSql = "INSERT INTO book_authors(book_id, author_id) VALUES(?,?)";
            try (PreparedStatement pstmtAuthors = conn.prepareStatement(insertBookAuthorSql)) {
                for (Author author : book.getAuthors()) {
                    Long authorId;
                    Optional<Author> existingAuthor = authorRepository.findByName(author.getName());
                    if (existingAuthor.isPresent()) {
                        authorId = existingAuthor.get().getId();
                    } else {
                        Author savedAuthor = authorRepository.save(author);
                        authorId = savedAuthor.getId();
                    }
                    pstmtAuthors.setLong(1, book.getId());
                    pstmtAuthors.setLong(2, authorId);
                    pstmtAuthors.addBatch();
                }
                pstmtAuthors.executeBatch(); // Execute all batched inserts
            }

            // Handle ISBNs (One-to-Many relationship)
            String insertBookIsbnSql = "INSERT INTO book_isbns(book_id, isbn_value) VALUES(?,?)";
            try (PreparedStatement pstmtIsbns = conn.prepareStatement(insertBookIsbnSql)) {
                for (String isbn : book.getIsbns()) {
                    pstmtIsbns.setLong(1, book.getId());
                    pstmtIsbns.setString(2, isbn);
                    pstmtIsbns.addBatch();
                }
                pstmtIsbns.executeBatch(); // Execute all batched inserts
            }

            return book;

        } catch (SQLException e) {
            throw new DataAccessException("Error saving book: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a book from the database by its ID.
     * Associated author and ISBN links are deleted automatically due to `ON DELETE CASCADE` constraints.
     *
     * @param id The ID of the book to delete.
     * @throws DataAccessException if a database access error occurs.
     */
    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting book with ID " + id + ": " + e.getMessage(), e);
        }
    }
}
