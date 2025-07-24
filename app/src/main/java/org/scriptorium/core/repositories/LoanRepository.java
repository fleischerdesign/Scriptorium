package org.scriptorium.core.repositories;

import org.scriptorium.core.domain.Loan;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Loan entities.
 * Defines the standard CRUD operations for loans, and additional methods
 * for finding loans by user, book, or status.
 */
public interface LoanRepository extends BaseRepository<Loan, Long> {

    /**
     * Finds all loans associated with a specific user ID.
     *
     * @param userId The ID of the user.
     * @return A list of loans for the given user.
     */
    List<Loan> findByUserId(Long userId);

    /**
     * Finds all loans associated with a specific copy ID.
     *
     * @param copyId The ID of the copy.
     * @return A list of loans for the given copy.
     */
    List<Loan> findByCopyId(Long copyId);
}
