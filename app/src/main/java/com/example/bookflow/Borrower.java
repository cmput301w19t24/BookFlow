package com.example.bookflow;

import java.util.ArrayList;

public interface Borrower {

    /**
     * search for books, given a list of keywords
     * @param keywords an ArrayList of keywords
     */
    void searchBook(ArrayList<String> keywords);

    /**
     * request for a given book
     * @param book the book to be requested
     */
    void requestBook(Book book);

    /**
     * list all requested books
     * @return an ArrayList of requested books
     */
    ArrayList<Book> listRequestedBooks();

    /**
     * list all accepted books
     * @return an ArrayList of accepted books
     */
    ArrayList<Book> listAcceptedBooks();

    /**
     * list all borrowing books
     * @return an ArrayList of borrowing books
     */
    ArrayList<Book> listBorrowingBooks();

    /**
     * receive an accepted book with ISBN scanner
     */
    void receiveAcceptedBook(Book book);

    /**
     * hand over a book by scanning ISBN
     */
    void borrowerHandOverBook();

}
