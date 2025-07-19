package org.scriptorium.core.services;

import org.scriptorium.core.domain.Book;
import org.scriptorium.core.domain.Loan;
import org.scriptorium.core.domain.User;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.repositories.BookRepository;
import org.scriptorium.core.repositories.LoanRepository;
import org.scriptorium.core.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing book loans.
 * This class encapsulates the business logic for loan operations,
 * using the LoanRepository for data access and interacting with Book and User repositories.
 */
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    /**
     * Constructs a new LoanService with its required repositories.
     *
     * @param loanRepository The repository for Loan entities.
     * @param bookRepository The repository for Book entities.
     * @param userRepository The repository for User entities.
     */
    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new loan for a book to a user.
     *
     * @param bookId The ID of the book to loan.
     * @param userId The ID of the user borrowing the book.
     * @param dueDate The date the book is due to be returned.
     * @return The created Loan object.
     * @throws IllegalArgumentException if the book or user is not found.
     * @throws DataAccessException if a data access error occurs.
     */
    public Loan createLoan(Long bookId, Long userId, LocalDate dueDate) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with ID " + bookId + " not found."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found."));

        Loan newLoan = new Loan(book, user, LocalDate.now(), dueDate);
        try {
            return loanRepository.save(newLoan);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to create loan: " + e.getMessage(), e);
        }
    }

    /**
     * Marks a loan as returned.
     *
     * @param loanId The ID of the loan to mark as returned.
     * @return The updated Loan object.
     * @throws IllegalArgumentException if the loan is not found or already returned.
     * @throws DataAccessException if a data access error occurs.
     */
    public Loan returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan with ID " + loanId + " not found."));

        if (loan.isReturned()) {
            throw new IllegalArgumentException("Book for loan ID " + loanId + " has already been returned.");
        }

        loan.setReturnDate(LocalDate.now());
        try {
            return loanRepository.save(loan);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to return book for loan ID " + loanId + ": " + e.getMessage(), e);
        }
    }

    /**
     * Saves an existing loan. This method is used for updating loan details.
     *
     * @param loan The loan object to save (update).
     * @return The saved (updated) loan object.
     * @throws DataAccessException if a data access error occurs.
     */
    public Loan save(Loan loan) {
        try {
            return loanRepository.save(loan);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to save loan: " + e.getMessage(), e);
        }
    }

    /**
     * Finds a loan by its ID.
     *
     * @param id The ID of the loan to find.
     * @return An Optional containing the loan if found.
     * @throws DataAccessException if a data access error occurs.
     */
    public Optional<Loan> findLoanById(Long id) {
        try {
            return loanRepository.findById(id);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to find loan by ID: " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all loans.
     *
     * @return A list of all loans.
     * @throws DataAccessException if a data access error occurs.
     */
    public List<Loan> findAllLoans() {
        try {
            return loanRepository.findAll();
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to retrieve all loans: " + e.getMessage(), e);
        }
    }

    /**
     * Finds all loans for a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of loans for the given user.
     * @throws DataAccessException if a data access error occurs.
     */
    public List<Loan> findLoansByUserId(Long userId) {
        try {
            return loanRepository.findByUserId(userId);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to find loans by user ID " + userId + ": " + e.getMessage(), e);
        }
    }

    /**
     * Finds all loans for a specific book.
     *
     * @param bookId The ID of the book.
     * @return A list of loans for the given book.
     * @throws DataAccessException if a data access error occurs.
     */
    public List<Loan> findLoansByBookId(Long bookId) {
        try {
            return loanRepository.findByBookId(bookId);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to find loans by book ID " + bookId + ": " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a loan by its ID.
     *
     * @param id The ID of the loan to delete.
     * @throws DataAccessException if a data access error occurs.
     */
    public void deleteLoan(Long id) {
        try {
            loanRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to delete loan with ID " + id + ": " + e.getMessage(), e);
        }
    }
}