package com.example.bookflow.Model;

import android.net.Uri;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private int rating;
    private String status;
    private User owner;
    private User borrower;
    private int requestCount;
    private Uri photo;
    
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
    
    public void setIsbn(String isbn){
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
    public void addPhoto(Uri photo){
        this.photo = photo;
    }
    
    public Uri getPhoto(){
        return photo;
    }
    
    public void deletePhoto(){
        photo = null;
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
}
