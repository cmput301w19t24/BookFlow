package com.example.bookflow;

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
    
}
