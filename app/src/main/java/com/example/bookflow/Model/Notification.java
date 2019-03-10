package com.example.bookflow.Model;

public class Notification {
    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String sender_id;
    private String book_id;
    private String type;
    private String sender_name;
    private String book_title;

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    private String transaction_id;
    public Notification(){}
    public Notification(String sender_id, String book_id, String type, String transaction_id, String book_title, String sender_name) {
        this.sender_id = sender_id;
        this.book_id = book_id;
        this.type = type;
        this.transaction_id = transaction_id;
        this.book_title = book_title;
        this.sender_name = sender_name;
    }
}
