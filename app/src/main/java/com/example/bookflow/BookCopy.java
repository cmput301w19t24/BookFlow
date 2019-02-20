package com.example.bookflow;

import java.util.ArrayList;

/**
 * Represent a single copy of a kind of book.
 * For example a book called "Frankenstein" may have multiple copies and each one may have different owner, borrower or status.
 */
public class BookCopy{
    Book book;
    String status;
    User owner;
    User borrower;
    ArrayList<Photo> photos = new ArrayList<Photo>();
    
    public void BookCopy(Book book, User owner){
        this.book = book;
        this.owner = owner;
    }
    
    public void setStatus(String newStatus){
        this.status = newStatus;
    }
    
    public void addPhoto(BookPhoto photo){
        photos.add(photo);
    }
    
    public Photo getPhoto(int index){
        return photos.get(index);
    }
    
    public void deletePhoto(int index){
        photos.remove(index);
    }
    
    public User getOwner(){
        return this.owner;
    }
    
    public User getBorrower(){
        return this.borrower;
    }
    
    public void setBorrower(User newBorrower){
        this.borrower = newBorrower;
    }
}
