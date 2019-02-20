package com.example.bookflow;

class Book {
    String title;
    String author;
    String isbn;
    
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

}
