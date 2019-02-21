package com.example.bookflow;

public interface Owner {
    public void addBook(Book book);

    public void getBookDescription(String ISBN);

    public void viewOwnedBooks();

    public void editBookDescription();

    public void deleteBook(Book book);

    public void viewRequest(Book book);

    public void acceptRequest(Book book);

    public void deleteRequest(Book book);

    public void ownerHandOverBook(Book book, String ISBN);

    public void receiveReturnedBook(Book book, String ISBN);
}

