package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.Publisher;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.exceptions.DuplicateEmailException; // Reusing for unique name constraint

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of the {@link PublisherRepository} interface.
 * Manages persistence operations for {@link Publisher} entities in a SQLite database.
 */
public class JdbcPublisherRepository implements PublisherRepository {

    private final String dbUrl;

    /**
     * Constructs a new JdbcPublisherRepository with the specified database URL.
     * @param dbUrl The JDBC URL for the SQLite database.
     */
    public JdbcPublisherRepository(String dbUrl) {
        this.dbUrl = dbUrl;
        init();
    }

    /**
     * Initializes the database table for publishers. Creates the 'publishers' table if it does not already exist.
     * Throws a DataAccessException if the table creation fails.
     */
    private void init() {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS publishers (\n"
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + " name TEXT NOT NULL UNIQUE\n"
                    + ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new DataAccessException("Error initializing publishers table: " + e.getMessage(), e);
        }
    }
    /**
     * Finds a publisher by its ID.
     *
     * @param id The ID of the publisher to find.
     * @return An {@link Optional} containing the found {@link Publisher}, or an empty Optional if no publisher is found.
     * @throws DataAccessException if a database access error occurs.
     */
    @Override
    public Optional<Publisher> findById(Long id) {
        String sql = "SELECT id, name FROM publishers WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Publisher(rs.getLong("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding publisher by ID: " + id, e);
        }
        return Optional.empty();
    }
    /**
     * Finds a publisher by its name.
     *
     * @param name The name of the publisher to find.
     * @return An {@link Optional} containing the found {@link Publisher}, or an empty Optional if no publisher is found.
     * @throws DataAccessException if a database access error occurs.
     */
    @Override
    public Optional<Publisher> findByName(String name) {
        String sql = "SELECT id, name FROM publishers WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Publisher(rs.getLong("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding publisher by name: " + name, e);
        }
        return Optional.empty();
    }

    /**
     * Retrieves all publishers from the database.
     *
     * @return A list of all {@link Puvlisher} entities.
     * @throws DataAccessException if a database access error occurs.
     */
    @Override
    public List<Publisher> findAll() {
        String sql = "SELECT id, name FROM publishers";
        List<Publisher> publishers = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                publishers.add(new Publisher(rs.getLong("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving all publishers.", e);
        }
        return publishers;
    }

    /**
     * Saves a publisher to the database. If the publisher has no ID, it's inserted as a new record.
     * Otherwise, the existing record is updated.
     *
     * @param publisher The {@link Publisher} to save.
     * @return The saved publisher, now with its ID set if it was a new insertion.
     * @throws DataAccessException if a database access error occurs.
     */
    @Override
    public Publisher save(Publisher publisher) {
        if (publisher.getId() == null) {
            return insert(publisher);
        } else {
            return update(publisher);
        }
    }

    /**
     * Inserts a new publisher into the database.
     *
     * @param publisher The new publisher to insert.
     * @return The inserted publisher with its new database-generated ID.
     * @throws DataAccessException if the insertion fails.
     */
    private Publisher insert(Publisher publisher) {
        String sql = "INSERT INTO publishers(name) VALUES(?)";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, publisher.getName());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating publisher failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    publisher.setId(generatedKeys.getLong(1));
                    return publisher;
                } else {
                    throw new SQLException("Creating publisher failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            // SQLite unique constraint violation error code is typically 19
            if (e.getErrorCode() == 19 && e.getMessage().contains("UNIQUE constraint failed: publishers.name")) {
                throw new DuplicateEmailException("Publisher with name '" + publisher.getName() + "' already exists.", e);
            } else {
                throw new DataAccessException("Error saving new publisher: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Updates an existing publisher's details.
     *
     * @param publisher The Publisher with updated information.
     * @return The updated Publisher.
     * @throws DataAccessException if the update fails.
     */
    private Publisher update(Publisher publisher) {
        String sql = "UPDATE publishers SET name = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, publisher.getName());
            pstmt.setLong(2, publisher.getId());
            pstmt.executeUpdate();
            return publisher;
        } catch (SQLException e) {
            if (e.getErrorCode() == 19 && e.getMessage().contains("UNIQUE constraint failed: publishers.name")) {
                throw new DuplicateEmailException("Publisher with name '" + publisher.getName() + "' already exists.", e);
            } else {
                throw new DataAccessException("Error updating publisher: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Deletes a Publisher from the database by its ID.
     *
     * @param id The ID of the publisher to delete.
     * @throws DataAccessException if a database access error occurs.
     */
    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM publishers WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting publisher with ID " + id + ": " + e.getMessage(), e);
        }
    }
}