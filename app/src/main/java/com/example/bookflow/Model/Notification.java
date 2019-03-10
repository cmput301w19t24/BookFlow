package com.example.bookflow.Model;

public class Notification {
    private String senderId;
    private String bookId;
    private String type;
    private String senderName;
    private String bookTitle;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    private String transactionId;
    public Notification(){}
    public Notification(String senderId, String bookId, String type, String transactionId, String bookTitle, String senderName) {
        this.senderId = senderId;
        this.bookId = bookId;
        this.type = type;
        this.transactionId = transactionId;
        this.bookTitle = bookTitle;
        this.senderName = senderName;
    }
}
