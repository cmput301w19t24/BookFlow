package com.example.bookflow;

import java.util.ArrayList;

class Book {
    String title;
    String author;
    String isbn;
    int rating;
    String status;
    User owner;
    User borrower;
    int requestCount;
    ArrayList<Photo> photos = new ArrayList<Photo>();
    
    public Book(String title, String author, String isbn){
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.rating = 0;
        this.requestCount = 0;
    }
    
    public String getTitle(){
        return this.title;
    }
    
    public String getAuthor(){
        return this.author;
    }
    
    public String getisbn(){
        return this.isbn;
    }
    
    public void setRating(int rating){
        this.rating = rating;
    }
    
    public int getRating(){
        return this.rating;
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
    
    public void setOwner(User owner){
        this.owner = owner;
    }
    
    public User getBorrower(){
        return this.borrower;
    }
    
    public void setBorrower(User newBorrower){
        this.borrower = newBorrower;
    }
    
    public void countIncrease(){
        this.requestCount ++;
    }
    
    public int getCount(){
        return this.requestCount;
    }
    
    public void setDescription(String isbn, String title, String author){
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }
}
