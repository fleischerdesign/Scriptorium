package org.scriptorium.core.services;

import org.scriptorium.core.domain.Copy;
import org.scriptorium.core.domain.Loan;
import org.scriptorium.core.domain.User;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.repositories.CopyRepository;
import org.scriptorium.core.repositories.LoanRepository;
import org.scriptorium.core.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for managing book loans.
 * This class encapsulates the business logic for loan operations,
 * using the LoanRepository for data access and interacting with Copy and User repositories.
 */
public class LoanService extends BaseService<Loan, Long> {

    private final LoanRepository loanRepository;
    private final CopyRepository copyRepository;
    private final UserRepository userRepository;

    /**
     * Constructs a new LoanService with its required repositories.
     *
     * @param loanRepository The repository for Loan entities.
     * @param copyRepository The repository for Copy entities.
     * @param userRepository The repository for User entities.
     */
    public LoanService(LoanRepository loanRepository, CopyRepository copyRepository, UserRepository userRepository) {
        super(loanRepository);
        this.loanRepository = loanRepository;
        this.copyRepository = copyRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new loan for a book copy to a user.
     *
     * @param copyId The ID of the copy to loan.
     * @param userId The ID of the user borrowing the copy.
     * @param dueDate The date the copy is due to be returned.
     * @return The created Loan object.
     * @throws IllegalArgumentException if the copy or user is not found, or if the copy is not available.
     * @throws DataAccessException if a data access error occurs.
     */
    public Loan createLoan(Long copyId, Long userId, LocalDate dueDate) {
        Copy copy = copyRepository.findById(copyId)
                .orElseThrow(() -> new IllegalArgumentException("Copy with ID " + copyId + " not found."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found."));

        if (copy.getStatus() != Copy.CopyStatus.AVAILABLE) {
            throw new IllegalArgumentException("Copy with ID " + copyId + " is not available for loan. Current status: " + copy.getStatus());
        }

        Loan newLoan = new Loan(copy, user, LocalDate.now(), dueDate);
        try {
            copy.setStatus(Copy.CopyStatus.ON_LOAN);
            copyRepository.save(copy); // Update copy status
            return repository.save(newLoan); // Use inherited save method
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
        Loan loan = repository.findById(loanId) // Use inherited findById method
                .orElseThrow(() -> new IllegalArgumentException("Loan with ID " + loanId + " not found."));

        if (loan.isReturned()) {
            throw new IllegalArgumentException("Copy for loan ID " + loanId + " has already been returned.");
        }

        loan.setReturnDate(LocalDate.now());
        try {
            Copy returnedCopy = loan.getCopy();
            returnedCopy.setStatus(Copy.CopyStatus.AVAILABLE);
            copyRepository.save(returnedCopy); // Update copy status
            return repository.save(loan); // Use inherited save method
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to return copy for loan ID " + loanId + ": " + e);
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
     * Finds all loans for a specific copy.
     *
     * @param copyId The ID of the copy.
     * @return A list of loans for the given copy.
     * @throws DataAccessException if a data access error occurs.
     */
    public List<Loan> findLoansByCopyId(Long copyId) {
        try {
            return loanRepository.findByCopyId(copyId);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to find loans by copy ID " + copyId + ": " + e.getMessage(), e);
        }
    }
}