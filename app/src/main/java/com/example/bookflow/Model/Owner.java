package com.example.bookflow.Model;

import java.util.ArrayList;

public interface Owner {
    public void addBook(Book book);

    public String getBookDescription(String ISBN);

    public ArrayList<Book> viewOwnedBooks();

    public void editBookDescription(Book book,String title,String author, String ISBN);

    public void deleteBook(Book book);

    public void viewRequest(Book book);

    public void acceptRequest(Request request);

    public void declineRequest(Request request);

    public void ownerHandOverBook(Book book);

    public void receiveReturnedBook(Book book);
}

