package com.example.bookflow.Model;

import android.location.Location;
/**
 * Request
 * This class models a request between the
 * owner of a book and a potential borrower
 * of a book
 */
public class Request {
    private String ownerId;
    private String borrowerId;
    private String bookId;
    private Location location;
    private String status;

    public Request(){
    }

    public Request(String ownerId, String borrowerId, String bookId) {
        this.ownerId = ownerId;
        this.borrowerId = borrowerId;
        this.bookId = bookId;
        this.status = "pending";
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
