package com.example.bookflow.Model;
/**
 * Notification
 * This class models the notifications
 * sent between users whenever a book is
 * requested, or a book is accepted
 */
public class Notification {
    private String senderId;
    private String bookId;
    private String type;
    private String senderName;
    private String bookTitle;
    private String transactionId;
    private String timestamp;
    private String viewed;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


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

    public String getViewed(){
        return this.viewed;
    }
    public void setViewed(){
        this.viewed = "true";
    }

    public Notification(){}
    public Notification(String senderId, String bookId, String type, String transactionId, String bookTitle, String senderName, String timestamp) {
        this.senderId = senderId;
        this.bookId = bookId;
        this.type = type;
        this.transactionId = transactionId;
        this.bookTitle = bookTitle;
        this.senderName = senderName;
        this.timestamp = timestamp;
        this.viewed = "false";
    }
}
