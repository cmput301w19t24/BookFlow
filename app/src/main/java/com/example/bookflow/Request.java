package com.example.bookflow;

import android.location.Location;

/**
 * This class models the relationship between two users with book
 */
public class Request {

    public Request(Borrower borrower, Owner owner, Book book) {
        this.borrower = borrower;
        this.owner = owner;
        this.book = book;
        this.location = null;
    }

    public Borrower getBorrower() {
        return borrower;
    }

    public void setBorrower(Borrower borrower) {
        this.borrower = borrower;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * The borrower-side of the request
     */
    private Borrower borrower;

    /**
     * The owner-side of the request
     */
    private Owner owner;

    /**
     * The book
     */
    private Book book;

    /**
     * location/meetup point of the exchange
     */
    private Location location;

}
