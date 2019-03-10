package com.example.bookflow.Model;

import android.location.Location;

public class TestRequest {
    private String owner_id;
    private String borrower_id;
    private String book_id;
    private Location location;

    public TestRequest(String owner_id, String borrower_id, String book_id) {
        this.owner_id = owner_id;
        this.borrower_id = borrower_id;
        this.book_id = book_id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getBorrower_id() {
        return borrower_id;
    }

    public void setBorrower_id(String borrower_id) {
        this.borrower_id = borrower_id;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
