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
    
    /*setter and getter of title*/
    public String getTitle(){
        return this.title;
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    /*setter and getter of author*/
    public String getAuthor(){
        return this.author;
    }
    
    public void setAuthor(String author){
        this.author = author;
    }
    
    /*getter and setter of isbn*/
    public String getIsbn(){
        return this.isbn;
    }
    
    public void setIsbn(){
        this.isbn = isbn;
    }
    
    /*getter and setter of rating*/
    public void setRating(int rating){
        this.rating = rating;
    }
    
    public int getRating(){
        return this.rating;
    }
    
    /*getter and setter of status*/
    public void setStatus(String newStatus){
        this.status = newStatus;
    }
    
    public String getStatus(){
        return this.status;
    }
    
    /*add, get and delete photo*/
    public void addPhoto(BookPhoto photo){
        photos.add(photo);
    }
    
    public Photo getPhoto(int index){
        return photos.get(index);
    }
    
    public void deletePhoto(int index){
        photos.remove(index);
    }
    
    /*getter and setter of owner*/
    public User getOwner(){
        return this.owner;
    }
    
    public void setOwner(User owner){
        this.owner = owner;
    }
    
    /*getter and setter of borrower*/
    public User getBorrower(){
        return this.borrower;
    }
    
    public void setBorrower(User newBorrower){
        this.borrower = newBorrower;
    }
    
    /*increase count*/
    public void countIncrease(){
        this.requestCount ++;
    }
    
    /*getter of count*/
    public int getCount(){
        return this.requestCount;
    }
    
    /*set isbn, title and author at once*/
    public void setDescription(String isbn, String title, String author){
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }
    
    public ArrayList<Photo> getAllPhoto(){
        return this.photos;
    }
}
