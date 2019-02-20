package com.example.bookflow;

import java.util.ArrayList;

public interface Borrower {
    void searchBook(ArrayList<String> keywords);
    void requestBook(BookCopy bookCopy);
    ArrayList<BookCopy> listRequestedBooks();
    ArrayList<BookCopy> listAcceptedBooks();
    void receiveAcceptedBook();
    void listBorrowingBooks();
    void handOverBook();
    void viewGeoLocation(BookCopy bookCopy);

}
