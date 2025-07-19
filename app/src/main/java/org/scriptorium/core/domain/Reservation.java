package org.scriptorium.core.domain;

import java.time.LocalDate;

/**
 * Represents a reservation of a book in the Scriptorium domain model.
 */
public class Reservation {

    private Long id;
    private Book book;
    private User user;
    private LocalDate reservationDate;
    private ReservationStatus status;

    public enum ReservationStatus {
        PENDING,
        FULFILLED,
        CANCELLED
    }

    public Reservation(Long id, Book book, User user, LocalDate reservationDate, ReservationStatus status) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.reservationDate = reservationDate;
        this.status = status;
    }

    public Reservation(Book book, User user, LocalDate reservationDate, ReservationStatus status) {
        this(null, book, user, reservationDate, status);
    }

    // Getters
    public Long getId() {
        return id;
    }
    public Book getBook() {
        return book;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
