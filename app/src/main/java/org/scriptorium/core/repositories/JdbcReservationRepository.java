package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.Book;
import org.scriptorium.core.domain.Reservation;
import org.scriptorium.core.domain.User;
import org.scriptorium.core.exceptions.DataAccessException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of the {@link ReservationRepository} interface.
 * Manages persistence operations for {@link Reservation} entities in a SQLite database.
 */
public class JdbcReservationRepository implements ReservationRepository {

    private final String dbUrl;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    /**
     * Constructs a new JdbcReservationRepository with the specified database URL and dependent repositories.
     * @param dbUrl The JDBC URL for the SQLite database.
     * @param bookRepository The repository for Book entities.
     * @param userRepository The repository for User entities.
     */
    public JdbcReservationRepository(String dbUrl, BookRepository bookRepository, UserRepository userRepository) {
        this.dbUrl = dbUrl;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    /**
     * Initializes the database table for reservations. Creates the 'reservations' table if it does not already exist.
     * Throws a DataAccessException if the table creation fails.
     */
    public void init() {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS reservations (\n"
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + " book_id INTEGER NOT NULL,\n"
                    + " user_id INTEGER NOT NULL,\n"
                    + " reservationDate TEXT NOT NULL,\n" // Stored as YYYY-MM-DD
                    + " status TEXT NOT NULL,\n" // e.g., PENDING, FULFILLED, CANCELLED
                    + " FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,\n"
                    + " FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE\n"
                    + ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new DataAccessException("Error initializing reservations table: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT id, book_id, user_id, reservationDate, status FROM reservations WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRowToReservation(rs));
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Error finding reservation by ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT id, book_id, user_id, reservationDate, status FROM reservations";
        List<Reservation> reservations = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reservations.add(mapRowToReservation(rs));
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Error retrieving all reservations.", e);
        }
        return reservations;
    }

    @Override
    public Reservation save(Reservation reservation) {
        String sql;
        boolean isNew = (reservation.getId() == null);
        if (isNew) {
            sql = "INSERT INTO reservations(book_id, user_id, reservationDate, status) VALUES(?,?,?,?)";
        } else {
            sql = "UPDATE reservations SET book_id = ?, user_id = ?, reservationDate = ?, status = ? WHERE id = ?";
        }

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql, isNew ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS)) {

            if (reservation.getBook() == null || reservation.getBook().getId() == null) {
                throw new IllegalArgumentException("Book must not be null and must have an ID for reservation operation.");
            }
            if (reservation.getUser() == null || reservation.getUser().getId() == null) {
                throw new IllegalArgumentException("User must not be null and must have an ID for reservation operation.");
            }

            pstmt.setLong(1, reservation.getBook().getId());
            pstmt.setLong(2, reservation.getUser().getId());
            pstmt.setString(3, reservation.getReservationDate().toString());
            pstmt.setString(4, reservation.getStatus().name());

            if (!isNew) {
                pstmt.setLong(5, reservation.getId());
            }

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Saving reservation failed, no rows affected.");
            }

            if (isNew) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reservation.setId(generatedKeys.getLong(1));
                        return reservation;
                    } else {
                        throw new SQLException("Creating reservation failed, no ID obtained.");
                    }
                }
            }
            return reservation;
        }
        catch (SQLException e) {
            throw new DataAccessException("Error saving reservation: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservations WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error deleting reservation with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to map a ResultSet row to a Reservation object.
     * This method fetches associated Book and User objects using their respective repositories.
     * @param rs The ResultSet containing reservation data.
     * @return A fully populated Reservation object.
     * @throws SQLException if a database access error occurs.
     */
    private Reservation mapRowToReservation(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        Long bookId = rs.getLong("book_id");
        Long userId = rs.getLong("user_id");
        LocalDate reservationDate = LocalDate.parse(rs.getString("reservationDate"));
        Reservation.ReservationStatus status = Reservation.ReservationStatus.valueOf(rs.getString("status"));

        // Fetch associated Book and User objects using their repositories
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new DataAccessException("Associated book with ID " + bookId + " not found for reservation " + id));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataAccessException("Associated user with ID " + userId + " not found for reservation " + id));

        return new Reservation(id, book, user, reservationDate, status);
    }
}
