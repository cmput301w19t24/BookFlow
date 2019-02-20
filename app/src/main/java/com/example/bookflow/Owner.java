package com.example.bookflow;

public interface Owner {
    void addBook(BookCopy bookCopy);
    void getBookDescription(int ISBN);
    void setStatus(BookCopy bookCopy);
    void viewOwnedBooks();
    void editBookDescription();
    void deleteBook();
    void viewRequest(Book book);
    void acceptRequest(Book book);
    void declineRequest(Book book);

}

