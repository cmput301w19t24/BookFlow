package com.example.bookflow;

/**
 * This class models the relationship between two users with book
 */
public class Request {
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

    public BookCopy getBook() {
        return book;
    }

    public void setBook(BookCopy book) {
        this.book = book;
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
    private BookCopy book;

}
