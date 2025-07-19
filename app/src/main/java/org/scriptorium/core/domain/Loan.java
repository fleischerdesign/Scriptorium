package org.scriptorium.core.domain;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a loan of a book copy to a user within the Scriptorium system.
 * A loan tracks which book copy was borrowed by which user, when it was loaned,
 * when it is due, and when it was actually returned.
 */
public class Loan {

    private Long id;
    private Copy copy; // The specific copy being loaned
    private User user; // The user who borrowed the copy
    private LocalDate loanDate; // Date the copy was loaned
    private LocalDate dueDate;  // Date the copy is due to be returned
    private LocalDate returnDate; // Date the copy was actually returned (null if not yet returned)

    /**
     * Default constructor.
     */
    public Loan() {
    }

    /**
     * Constructs a new Loan with essential details.
     *
     * @param copy     The copy being loaned.
     * @param user     The user borrowing the copy.
     * @param loanDate The date the loan was initiated.
     * @param dueDate  The date the copy is due to be returned.
     */
    public Loan(Copy copy, User user, LocalDate loanDate, LocalDate dueDate) {
        this.copy = copy;
        this.user = user;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = null; // Initially, the copy is not returned
    }

    /**
     * Constructs a new Loan with all details, including an existing ID and return date.
     *
     * @param id         The unique identifier of the loan.
     * @param copy       The copy being loaned.
     * @param user       The user borrowing the copy.
     * @param loanDate   The date the loan was initiated.
     * @param dueDate    The date the copy is due to be returned.
     * @param returnDate The date the copy was actually returned (can be null).
     */
    public Loan(Long id, Copy copy, User user, LocalDate loanDate, LocalDate dueDate, LocalDate returnDate) {
        this.id = id;
        this.copy = copy;
        this.user = user;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Copy getCopy() {
        return copy;
    }

    public void setCopy(Copy copy) {
        this.copy = copy;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * Checks if the copy has been returned.
     * @return true if returnDate is not null, false otherwise.
     */
    public boolean isReturned() {
        return returnDate != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return Objects.equals(id, loan.id) &&
               Objects.equals(copy, loan.copy) &&
               Objects.equals(user, loan.user) &&
               Objects.equals(loanDate, loan.loanDate) &&
               Objects.equals(dueDate, loan.dueDate) &&
               Objects.equals(returnDate, loan.returnDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, copy, user, loanDate, dueDate, returnDate);
    }

    @Override
    public String toString() {
        return "Loan{" +
               "id=" + id +
               ", copy=" + (copy != null ? copy.getBarcode() : "null") + // Use barcode for copy representation
               ", user=" + (user != null ? user.getFirstName() + " " + user.getLastName() : "null") +
               ", loanDate=" + loanDate +
               ", dueDate=" + dueDate +
               ", returnDate=" + returnDate +
               '}';
    }
}