package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.Copy;
import org.scriptorium.core.domain.Copy.CopyStatus;
import org.scriptorium.core.domain.Copy.MediaType;
import org.scriptorium.core.exceptions.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of the {@link CopyRepository} interface.
 * Manages persistence operations for {@link Copy} entities in a SQLite database.
 */
public class JdbcCopyRepository implements CopyRepository {

    private final String dbUrl;
    private final BookRepository bookRepository; // Dependency to fetch associated Book objects

    /**
     * Constructs a new JdbcCopyRepository with the specified database URL.
     * @param dbUrl The JDBC URL for the SQLite database.
     * @param bookRepository The repository for Book entities.
     */
    public JdbcCopyRepository(String dbUrl, BookRepository bookRepository) {
        this.dbUrl = dbUrl;
        this.bookRepository = bookRepository;
    }

    /**
     * Initializes the database table for copies. Creates the 'copies' table if it does not already exist.
     * Throws a DataAccessException if the table creation fails.
     */
    public void init() {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS copies (\n"
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + " item_id INTEGER NOT NULL,\n"
                    + " media_type TEXT NOT NULL,\n"
                    + " barcode TEXT UNIQUE,\n"
                    + " status TEXT NOT NULL\n"
                    + ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new DataAccessException("Error initializing copies table: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Copy> findById(Long id) {
        String sql = "SELECT id, item_id, media_type, barcode, status FROM copies WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRowToCopy(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding copy by ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Copy> findAll() {
        String sql = "SELECT id, item_id, media_type, barcode, status FROM copies";
        List<Copy> copies = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                copies.add(mapRowToCopy(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving all copies.", e);
        }
        return copies;
    }

    @Override
    public List<Copy> findByItemIdAndMediaType(Long itemId, MediaType mediaType) {
        String sql = "SELECT id, item_id, media_type, barcode, status FROM copies WHERE item_id = ? AND media_type = ?";
        List<Copy> copies = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, itemId);
            pstmt.setString(2, mediaType.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                copies.add(mapRowToCopy(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding copies by item ID " + itemId + " and media type " + mediaType + ": " + e.getMessage(), e);
        }
        return copies;
    }

    @Override
    public List<Copy> findByItemIdAndMediaTypeAndStatus(Long itemId, MediaType mediaType, CopyStatus status) {
        String sql = "SELECT id, item_id, media_type, barcode, status FROM copies WHERE item_id = ? AND media_type = ? AND status = ?";
        List<Copy> copies = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, itemId);
            pstmt.setString(2, mediaType.name());
            pstmt.setString(3, status.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                copies.add(mapRowToCopy(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding copies by item ID " + itemId + ", media type " + mediaType + " and status " + status + ": " + e.getMessage(), e);
        }
        return copies;
    }

    @Override
    public Copy save(Copy copy) {
        if (copy.getId() == null) {
            return insert(copy);
        } else {
            return update(copy);
        }
    }

    private Copy insert(Copy copy) {
        String sql = "INSERT INTO copies(item_id, media_type, barcode, status) VALUES(?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, copy.getItemId());
            pstmt.setString(2, copy.getMediaType().name());
            pstmt.setString(3, copy.getBarcode());
            pstmt.setString(4, copy.getStatus().name());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating copy failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    copy.setId(generatedKeys.getLong(1));
                    return copy;
                } else {
                    throw new SQLException("Creating copy failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error saving new copy: " + e.getMessage(), e);
        }
    }

    private Copy update(Copy copy) {
        String sql = "UPDATE copies SET item_id = ?, media_type = ?, barcode = ?, status = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, copy.getItemId());
            pstmt.setString(2, copy.getMediaType().name());
            pstmt.setString(3, copy.getBarcode());
            pstmt.setString(4, copy.getStatus().name());
            pstmt.setLong(5, copy.getId());
            pstmt.executeUpdate();
            return copy;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating copy: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM copies WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting copy with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to map a ResultSet row to a Copy object.
     * This method fetches associated Book objects using their respective repositories.
     * @param rs The ResultSet containing copy data.
     * @return A fully populated Copy object.
     * @throws SQLException if a database access error occurs.
     */
    private Copy mapRowToCopy(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        Long itemId = rs.getLong("item_id");
        MediaType mediaType = MediaType.valueOf(rs.getString("media_type"));
        String barcode = rs.getString("barcode");
        CopyStatus status = CopyStatus.valueOf(rs.getString("status"));

        // For now, we only handle BOOK media type. Extend this logic for other types.
        if (mediaType == MediaType.BOOK) {
            bookRepository.findById(itemId)
                    .orElseThrow(() -> new DataAccessException("Associated book with ID " + itemId + " not found for copy " + id));
        }

        // Note: The Copy object will hold the Book object directly for convenience
        // This might need to be refactored if we introduce a more generic 'Item' interface/class
        return new Copy(id, itemId, barcode, status, mediaType);
    }
}
