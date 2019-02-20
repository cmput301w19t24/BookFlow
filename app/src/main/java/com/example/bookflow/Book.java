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
    
    public getTitle(){
        return this.title;
    }
    
    public getAuthor(){
        return this.author;
    }
    
    public getisbn(){
        return this.isbn;
    }

}
