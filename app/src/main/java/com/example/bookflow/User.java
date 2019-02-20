package com.example.bookflow;

import java.util.ArrayList;

public class User implements Owner,Borrower{
    private String username;
    private String password;
    private String email;
    private int phoneNumber;
    private ArrayList<Book> ownedBooks;
    private ArrayList<Book> borrowedBooks;
    private ArrayList<Request>  requests;
    private ArrayList<Review> reviews;


    @Override
    public void addBook(String title, String author, int ISBN) {
        Book book = new Book(title,author,ISBN);
        ownedBooks.add(book);
    }

    @Override
    public void getBookDescription(int ISBN) {

    }

    @Override
    public void setStatus(Book book) {

    }

    @Override
    public void viewOwnedBooks() {

    }

    @Override
    public void editBookDescription() {

    }

    @Override
    public void deleteBook() {

    }


}