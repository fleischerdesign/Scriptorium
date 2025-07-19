package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.Loan;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Loan entities.
 * Defines the standard CRUD operations for loans, and additional methods
 * for finding loans by user, book, or status.
 */
public interface LoanRepository {

    /**
     * Saves a new loan or updates an existing one.
     *
     * @param loan The loan to save.
     * @return The saved loan, typically with the generated ID.
     */
    Loan save(Loan loan);

    /**
     * Finds a loan by its ID.
     *
     * @param id The ID of the loan to find.
     * @return An Optional containing the loan if found, or empty if not.
     */
    Optional<Loan> findById(Long id);

    /**
     * Retrieves all loans.
     *
     * @return A list of all loans.
     */
    List<Loan> findAll();

    /**
     * Finds all loans associated with a specific user ID.
     *
     * @param userId The ID of the user.
     * @return A list of loans for the given user.
     */
    List<Loan> findByUserId(Long userId);

    /**
     * Finds all loans associated with a specific book ID.
     *
     * @param bookId The ID of the book.
     * @return A list of loans for the given book.
     */
    List<Loan> findByBookId(Long bookId);

    /**
     * Deletes a loan by its ID.
     *
     * @param id The ID of the loan to delete.
     */
    void deleteById(Long id);
}
