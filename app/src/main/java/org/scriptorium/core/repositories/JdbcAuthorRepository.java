package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.Author;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.exceptions.DuplicateEmailException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of the {@link AuthorRepository} interface.
 * Manages persistence operations for {@link Author} entities in a SQLite database.
 */
public class JdbcAuthorRepository implements AuthorRepository {

    private final String dbUrl;

    /**
     * Constructs a new JdbcAuthorRepository with the specified database URL.
     * @param dbUrl The JDBC URL for the SQLite database.
     */
    public JdbcAuthorRepository(String dbUrl) {
        this.dbUrl = dbUrl;
        init();
    }

    /**
     * Initializes the database table for authors. Creates the 'authors' table if it does not already exist.
     * Throws a DataAccessException if the table creation fails.
     */
    private void init() {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS authors (\n"
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + " name TEXT NOT NULL UNIQUE\n"
                    + ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new DataAccessException("Error initializing authors table: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Author> findById(Long id) {
        String sql = "SELECT id, name FROM authors WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Author(rs.getLong("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding author by ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Author> findByName(String name) {
        String sql = "SELECT id, name FROM authors WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Author(rs.getLong("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding author by name: " + name, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Author> findAll() {
        String sql = "SELECT id, name FROM authors";
        List<Author> authors = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                authors.add(new Author(rs.getLong("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving all authors.", e);
        }
        return authors;
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == null) {
            return insert(author);
        } else {
            return update(author);
        }
    }

    private Author insert(Author author) {
        String sql = "INSERT INTO authors(name) VALUES(?)";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, author.getName());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating author failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    author.setId(generatedKeys.getLong(1));
                    return author;
                } else {
                    throw new SQLException("Creating author failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            // SQLite unique constraint violation error code is typically 19
            if (e.getErrorCode() == 19 && e.getMessage().contains("UNIQUE constraint failed: authors.name")) {
                throw new DuplicateEmailException("Author with name '" + author.getName() + "' already exists.", e);
            } else {
                throw new DataAccessException("Error saving new author: " + e.getMessage(), e);
            }
        }
    }

    private Author update(Author author) {
        String sql = "UPDATE authors SET name = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, author.getName());
            pstmt.setLong(2, author.getId());
            pstmt.executeUpdate();
            return author;
        } catch (SQLException e) {
            if (e.getErrorCode() == 19 && e.getMessage().contains("UNIQUE constraint failed: authors.name")) {
                throw new DuplicateEmailException("Author with name '" + author.getName() + "' already exists.", e);
            } else {
                throw new DataAccessException("Error updating author: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM authors WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting author with ID " + id + ": " + e.getMessage(), e);
        }
    }
}