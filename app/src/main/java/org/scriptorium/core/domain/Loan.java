package org.scriptorium.core.domain;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a loan of a book to a user within the Scriptorium system.
 * A loan tracks which book was borrowed by which user, when it was loaned,
 * when it is due, and when it was actually returned.
 */
public class Loan {

    private Long id;
    private Book book; // The book being loaned
    private User user; // The user who borrowed the book
    private LocalDate loanDate; // Date the book was loaned
    private LocalDate dueDate;  // Date the book is due to be returned
    private LocalDate returnDate; // Date the book was actually returned (null if not yet returned)

    /**
     * Default constructor.
     */
    public Loan() {
    }

    /**
     * Constructs a new Loan with essential details.
     *
     * @param book     The book being loaned.
     * @param user     The user borrowing the book.
     * @param loanDate The date the loan was initiated.
     * @param dueDate  The date the book is due to be returned.
     */
    public Loan(Book book, User user, LocalDate loanDate, LocalDate dueDate) {
        this.book = book;
        this.user = user;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = null; // Initially, the book is not returned
    }

    /**
     * Constructs a new Loan with all details, including an existing ID and return date.
     *
     * @param id         The unique identifier of the loan.
     * @param book       The book being loaned.
     * @param user       The user borrowing the book.
     * @param loanDate   The date the loan was initiated.
     * @param dueDate    The date the book is due to be returned.
     * @param returnDate The date the book was actually returned (can be null).
     */
    public Loan(Long id, Book book, User user, LocalDate loanDate, LocalDate dueDate, LocalDate returnDate) {
        this.id = id;
        this.book = book;
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

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
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
     * Checks if the book has been returned.
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
               Objects.equals(book, loan.book) &&
               Objects.equals(user, loan.user) &&
               Objects.equals(loanDate, loan.loanDate) &&
               Objects.equals(dueDate, loan.dueDate) &&
               Objects.equals(returnDate, loan.returnDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, user, loanDate, dueDate, returnDate);
    }

    @Override
    public String toString() {
        return "Loan{" +
               "id=" + id +
               ", book=" + (book != null ? book.getTitle() : "null") + // Avoid deep recursion
               ", user=" + (user != null ? user.getFirstName() + " " + user.getLastName() : "null") + // Avoid deep recursion
               ", loanDate=" + loanDate +
               ", dueDate=" + dueDate +
               ", returnDate=" + returnDate +
               '}';
    }
}
