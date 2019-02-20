package com.example.bookflow;

public interface Owner {
    public void addBook(BookCopy bookCopy);
    public void getBookDescription(int ISBN);
    public void setStatus(BookCopy bookCopy);
    public void viewOwnedBooks();
    public void editBookDescription();
    public void deleteBook();
    public void viewRequest(Book book);
    public void acceptRequest(Book book);
    public void declineRequest(Book book);

}

