package com.example.bookflow.Model;

import java.util.ArrayList;

/**
 * Owner interface that defines behaviors of an owner.
 */
public interface Owner {
    /**
     * user add book
     * @param book book to be added
     */
    void addBook(Book book);

    /**
     * user view owned books
     * @return list of owned books
     */
    ArrayList<Book> viewOwnedBooks();

    /**
     * user delete book
     * @param book book to be deleted
     */
    void deleteBook(Book book);

    /**
     * user view request for a certain book
     * @param book to be viewed for requests
     */
    void viewRequest(Book book);

    /**
     * user accept request
     * @param request request to be accepted
     */
    void acceptRequest(Request request);

    /**
     * user decline request
     * @param request request to be declined
     */
    void declineRequest(Request request);

    /**
     * user hand over a book as a owner
     * @param book book to be handed over
     */
    void ownerHandOverBook(Book book);

    /**
     * user check receive the returned book
     * @param book to be checked received
     */
    void receiveReturnedBook(Book book);
}

