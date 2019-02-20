package com.example.bookflow;

import java.util.ArrayList;

public interface Borrower {

    /**
     * set the status of a book
     * @param bookCopy the book whose status will be changed
     * @param status the status
     */
    void setStatus(BookCopy bookCopy, String status);

    /**
     * search for books, given a list of keywords
     * @param keywords an ArrayList of keywords
     */
    void searchBook(ArrayList<String> keywords);

    /**
     * request for a given book
     * @param bookCopy the book to be requested
     */
    void requestBook(BookCopy bookCopy);

    /**
     * list all requested books
     * @return an ArrayList of requested books
     */
    ArrayList<BookCopy> listRequestedBooks();

    /**
     * list all accepted books
     * @return an ArrayList of accepted books
     */
    ArrayList<BookCopy> listAcceptedBooks();

    /**
     * list all borrowing books
     * @return an ArrayList of borrowing books
     */
    ArrayList<BookCopy> listBorrowingBooks();

    /**
     * receive an accepted book with ISBN scanner
     */
    void receiveAcceptedBook();

    /**
     * hand over a book by scanning ISBN
     */
    void handOverBook();

    /**
     * view the geo location of the book
     * @param bookCopy
     */
    void viewGeoLocation(BookCopy bookCopy);

}
