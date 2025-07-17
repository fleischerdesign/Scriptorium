package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.User;

import java.sql.*;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.exceptions.DuplicateEmailException;

import java.util.List;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {

    private final String dbUrl;

    public JdbcUserRepository(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    // This method should be called once to initialize the database schema
    public void init() {
        try (Connection conn = DriverManager.getConnection(this.dbUrl);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS users ("
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " firstName TEXT NOT NULL,"
                    + " lastName TEXT NOT NULL,"
                    + " email TEXT NOT NULL UNIQUE,"
                    + " passwordHash TEXT NOT NULL,"
                    + " street TEXT,"
                    + " postalCode TEXT,"
                    + " city TEXT,"
                    + " country TEXT"
                    + ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new DataAccessException("Error initializing database: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(this.dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("passwordHash"));
                user.setStreet(rs.getString("street"));
                user.setPostalCode(rs.getString("postalCode"));
                user.setCity(rs.getString("city"));
                user.setCountry(rs.getString("country"));
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding user by ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        List<User> users = new java.util.ArrayList<>();
        try (Connection conn = DriverManager.getConnection(this.dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("passwordHash"));
                user.setStreet(rs.getString("street"));
                user.setPostalCode(rs.getString("postalCode"));
                user.setCity(rs.getString("city"));
                user.setCountry(rs.getString("country"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding all users.", e);
        }
        return users;
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            return insert(user);
        } else {
            return update(user);
        }
    }

    private User insert(User user) {
        String sql = "INSERT INTO users(firstName, lastName, email, passwordHash, street, postalCode, city, country) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(this.dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPasswordHash());
            pstmt.setString(5, user.getStreet());
            pstmt.setString(6, user.getPostalCode());
            pstmt.setString(7, user.getCity());
            pstmt.setString(8, user.getCountry());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                    return user;
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            // SQLite unique constraint violation error code is typically 19
            if (e.getErrorCode() == 19 && e.getMessage().contains("UNIQUE constraint failed: users.email")) {
                throw new DuplicateEmailException("User with email " + user.getEmail() + " already exists.", e);
            } else {
                throw new DataAccessException("Error saving new user: " + e.getMessage(), e);
            }
        }
    }

    private User update(User user) {
        String sql = "UPDATE users SET firstName = ?, lastName = ?, email = ?, passwordHash = ?, street = ?, postalCode = ?, city = ?, country = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(this.dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPasswordHash());
            pstmt.setString(5, user.getStreet());
            pstmt.setString(6, user.getPostalCode());
            pstmt.setString(7, user.getCity());
            pstmt.setString(8, user.getCountry());
            pstmt.setLong(9, user.getId());
            pstmt.executeUpdate();
            return user;
        } catch (SQLException e) {
            if (e.getErrorCode() == 19 && e.getMessage().contains("UNIQUE constraint failed: users.email")) {
                throw new DuplicateEmailException("User with email " + user.getEmail() + " already exists.", e);
            } else {
                throw new DataAccessException("Error updating user: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(this.dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error deleting user with ID " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(this.dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("passwordHash"));
                user.setStreet(rs.getString("street"));
                user.setPostalCode(rs.getString("postalCode"));
                user.setCity(rs.getString("city"));
                user.setCountry(rs.getString("country"));
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding user by email: " + email, e);
        }
        return Optional.empty();
    }
}