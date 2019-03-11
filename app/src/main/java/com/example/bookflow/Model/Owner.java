package com.example.bookflow.Model;

import java.util.ArrayList;

public interface Owner {
    void addBook(Book book);

    ArrayList<Book> viewOwnedBooks();

    void deleteBook(Book book);

    void viewRequest(Book book);

    void acceptRequest(Request request);

    void declineRequest(Request request);

    void ownerHandOverBook(Book book);

    void receiveReturnedBook(Book book);
}

