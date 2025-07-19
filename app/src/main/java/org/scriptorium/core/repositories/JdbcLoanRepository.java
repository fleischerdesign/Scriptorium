package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.Copy;
import org.scriptorium.core.domain.Loan;
import org.scriptorium.core.domain.User;
import org.scriptorium.core.exceptions.DataAccessException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of the {@link LoanRepository} interface.
 * Manages persistence operations for {@link Loan} entities in a SQLite database.
 */
public class JdbcLoanRepository implements LoanRepository {

    private final String dbUrl;
    private final CopyRepository copyRepository;
    private final UserRepository userRepository;

    /**
     * Constructs a new JdbcLoanRepository with the specified database URL and dependent repositories.
     * @param dbUrl The JDBC URL for the SQLite database.
     * @param copyRepository The repository for Copy entities.
     * @param userRepository The repository for User entities.
     */
    public JdbcLoanRepository(String dbUrl, CopyRepository copyRepository, UserRepository userRepository) {
        this.dbUrl = dbUrl;
        this.copyRepository = copyRepository;
        this.userRepository = userRepository;
    }

    /**
     * Initializes the database table for loans. Creates the 'loans' table if it does not already exist.
     * Throws a DataAccessException if the table creation fails.
     */
    public void init() {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS loans ("
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " copy_id INTEGER NOT NULL,"
                    + " user_id INTEGER NOT NULL,"
                    + " loanDate TEXT NOT NULL," // Stored as YYYY-MM-DD
                    + " dueDate TEXT NOT NULL,"  // Stored as YYYY-MM-DD
                    + " returnDate TEXT," // Stored as YYYY-MM-DD, can be NULL
                    + " FOREIGN KEY (copy_id) REFERENCES copies(id) ON DELETE CASCADE,"
                    + " FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
                    + ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new DataAccessException("Error initializing loans table: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Loan> findById(Long id) {
        String sql = "SELECT id, copy_id, user_id, loanDate, dueDate, returnDate FROM loans WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRowToLoan(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding loan by ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Loan> findAll() {
        String sql = "SELECT id, copy_id, user_id, loanDate, dueDate, returnDate FROM loans";
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                loans.add(mapRowToLoan(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving all loans.", e);
        }
        return loans;
    }

    @Override
    public Loan save(Loan loan) {
        if (loan.getId() == null) {
            return insert(loan);
        } else {
            return update(loan);
        }
    }

    private Loan insert(Loan loan) {
        String sql = "INSERT INTO loans(copy_id, user_id, loanDate, dueDate, returnDate) VALUES(?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (loan.getCopy() == null || loan.getCopy().getId() == null) {
                throw new IllegalArgumentException("Copy must not be null and must have an ID for loan insertion.");
            }
            if (loan.getUser() == null || loan.getUser().getId() == null) {
                throw new IllegalArgumentException("User must not be null and must have an ID for loan insertion.");
            }

            pstmt.setLong(1, loan.getCopy().getId());
            pstmt.setLong(2, loan.getUser().getId());
            pstmt.setString(3, loan.getLoanDate().toString());
            pstmt.setString(4, loan.getDueDate().toString());
            pstmt.setString(5, loan.getReturnDate() != null ? loan.getReturnDate().toString() : null);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating loan failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    loan.setId(generatedKeys.getLong(1));
                    return loan;
                } else {
                    throw new SQLException("Creating loan failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error saving new loan: " + e.getMessage(), e);
        }
    }

    private Loan update(Loan loan) {
        String sql = "UPDATE loans SET copy_id = ?, user_id = ?, loanDate = ?, dueDate = ?, returnDate = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (loan.getCopy() == null || loan.getCopy().getId() == null) {
                throw new IllegalArgumentException("Copy must not be null and must have an ID for loan update.");
            }
            if (loan.getUser() == null || loan.getUser().getId() == null) {
                throw new IllegalArgumentException("User must not be null and must have an ID for loan update.");
            }
            if (loan.getId() == null) {
                throw new IllegalArgumentException("Loan ID must not be null for update operation.");
            }

            pstmt.setLong(1, loan.getCopy().getId());
            pstmt.setLong(2, loan.getUser().getId());
            pstmt.setString(3, loan.getLoanDate().toString());
            pstmt.setString(4, loan.getDueDate().toString());
            pstmt.setString(5, loan.getReturnDate() != null ? loan.getReturnDate().toString() : null);
            pstmt.setLong(6, loan.getId());
            pstmt.executeUpdate();
            return loan;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating loan: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Loan> findByUserId(Long userId) {
        String sql = "SELECT id, copy_id, user_id, loanDate, dueDate, returnDate FROM loans WHERE user_id = ?";
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                loans.add(mapRowToLoan(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding loans by user ID " + userId + ": " + e.getMessage(), e);
        }
        return loans;
    }

    @Override
    public List<Loan> findByCopyId(Long copyId) {
        String sql = "SELECT id, copy_id, user_id, loanDate, dueDate, returnDate FROM loans WHERE copy_id = ?";
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, copyId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                loans.add(mapRowToLoan(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding loans by copy ID " + copyId + ": " + e.getMessage(), e);
        }
        return loans;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM loans WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting loan with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to map a ResultSet row to a Loan object.
     * This method fetches associated Copy and User objects using their respective repositories.
     * @param rs The ResultSet containing loan data.
     * @return A fully populated Loan object.
     * @throws SQLException if a database access error occurs.
     */
    private Loan mapRowToLoan(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        Long copyId = rs.getLong("copy_id");
        Long userId = rs.getLong("user_id");
        LocalDate loanDate = LocalDate.parse(rs.getString("loanDate"));
        LocalDate dueDate = LocalDate.parse(rs.getString("dueDate"));
        String returnDateStr = rs.getString("returnDate");
        LocalDate returnDate = (returnDateStr != null) ? LocalDate.parse(returnDateStr) : null;

        // Fetch associated Copy and User objects using their repositories
        Copy copy = copyRepository.findById(copyId)
                .orElseThrow(() -> new DataAccessException("Associated copy with ID " + copyId + " not found for loan " + id));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataAccessException("Associated user with ID " + userId + " not found for loan " + id));

        return new Loan(id, copy, user, loanDate, dueDate, returnDate);
    }
}
