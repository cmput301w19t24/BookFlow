package com.example.bookflow;

public interface Owner {
    public void addBook(String title, String author, int ISBN);
    public void getBookDescription(int ISBN);
    public void setStatus(Book book);
    public void viewOwnedBooks();
    public void editBookDescription();
    public void deleteBook();
}

