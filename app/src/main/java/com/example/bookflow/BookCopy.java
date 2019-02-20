package com.example.bookflow;

import java.util.ArrayList;

public class BookCopy{
    Book book;
    int isbn;
    String status;
    User owner;
    User borrower;
    ArrayList<Photo> photos = new ArrayList<>();
    
    public BookCopy(Book book, int isbn, User owner){
        this.book = book;
        this.isbn = isbn;
        this.owner = owner;
    }
    
    public setStatus(String newStatus){
        this.status = newStatus;
    }
    
    public addPhoto(BookPhoto photo){
        photos.add(photo);
    }
    
    public getPhoto(int index){
        return photos.get(index);
    }
    
    public deletePhoto(int index){
        photos.remove(index);
    }
    
    public getOwner(){
        return this.owner;
    }
    
    public getBorrower(){
        return this.borrower;
    }
    
    public setBorrower(User newborrower){
        this.borrower = newBorrower;
    }
}
