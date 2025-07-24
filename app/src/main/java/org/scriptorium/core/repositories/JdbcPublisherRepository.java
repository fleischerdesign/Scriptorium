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
    }

    public void init() {
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

    @Override
    public Publisher save(Publisher publisher) {
        String sql;
        boolean isNew = (publisher.getId() == null);
        if (isNew) {
            sql = "INSERT INTO publishers(name) VALUES(?)";
        } else {
            sql = "UPDATE publishers SET name = ? WHERE id = ?";
        }

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql, isNew ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS)) {

            pstmt.setString(1, publisher.getName());

            if (!isNew) {
                pstmt.setLong(2, publisher.getId());
            }

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Saving publisher failed, no rows affected.");
            }

            if (isNew) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        publisher.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating publisher failed, no ID obtained.");
                    }
                }
            }
            return publisher;
        } catch (SQLException e) {
            if (e.getErrorCode() == 19 && e.getMessage().contains("UNIQUE constraint failed: publishers.name")) {
                throw new DuplicateEmailException("Publisher with name '" + publisher.getName() + "' already exists.", e);
            } else {
                throw new DataAccessException("Error saving publisher: " + e.getMessage(), e);
            }
        }
    }

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
        }
        catch (SQLException e) {
            throw new DataAccessException("Error finding publisher by ID: " + id, e);
        }
        return Optional.empty();
    }

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
}