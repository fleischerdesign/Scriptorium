package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.Genre;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.exceptions.DuplicateEmailException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of the {@link GenreRepository} interface.
 * Manages persistence operations for {@link Genre} entities in a SQLite database.
 */
public class JdbcGenreRepository implements GenreRepository {

    private final String dbUrl;

    /**
     * Constructs a new JdbcGenreRepository with the specified database URL.
     * @param dbUrl The JDBC URL for the SQLite database.
     */
    public JdbcGenreRepository(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    /**
     * Initializes the database table for genres. Creates the 'genres' table if it does not already exist.
     * Throws a DataAccessException if the table creation fails.
     */
    public void init() {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS genres (\n"
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + " name TEXT NOT NULL UNIQUE\n"
                    + ");";
            stmt.execute(sql);

            // Ensure 'UNKNOWN' genre exists
            String checkUnknownGenreSql = "SELECT COUNT(*) FROM genres WHERE name = 'UNKNOWN'";
            try (ResultSet rs = stmt.executeQuery(checkUnknownGenreSql)) {
                if (rs.next() && rs.getInt(1) == 0) {
                    String insertUnknownGenreSql = "INSERT INTO genres(name) VALUES('UNKNOWN')";
                    stmt.executeUpdate(insertUnknownGenreSql);
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error initializing genres table: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Genre> findById(Long id) {
        String sql = "SELECT id, name FROM genres WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Genre(rs.getLong("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding genre by ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Genre> findByName(String name) {
        String sql = "SELECT id, name FROM genres WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Genre(rs.getLong("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding genre by name: " + name, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Genre> findAll() {
        String sql = "SELECT id, name FROM genres";
        List<Genre> genres = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                genres.add(new Genre(rs.getLong("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving all genres.", e);
        }
        return genres;
    }

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() == null) {
            return insert(genre);
        } else {
            return update(genre);
        }
    }

    private Genre insert(Genre genre) {
        String sql = "INSERT INTO genres(name) VALUES(?)";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, genre.getName());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating genre failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    genre.setId(generatedKeys.getLong(1));
                    return genre;
                } else {
                    throw new SQLException("Creating genre failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 19 && e.getMessage().contains("UNIQUE constraint failed: genres.name")) {
                throw new DuplicateEmailException("Genre with name '" + genre.getName() + "' already exists.", e);
            } else {
                throw new DataAccessException("Error saving new genre: " + e.getMessage(), e);
            }
        }
    }

    private Genre update(Genre genre) {
        String sql = "UPDATE genres SET name = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, genre.getName());
            pstmt.setLong(2, genre.getId());
            pstmt.executeUpdate();
            return genre;
        } catch (SQLException e) {
            if (e.getErrorCode() == 19 && e.getMessage().contains("UNIQUE constraint failed: genres.name")) {
                throw new DuplicateEmailException("Genre with name '" + genre.getName() + "' already exists.", e);
            } else {
                throw new DataAccessException("Error updating genre: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM genres WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting genre with ID " + id + ": " + e.getMessage(), e);
        }
    }
}