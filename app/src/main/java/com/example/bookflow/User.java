/**
 * author: Yuhan Ye
 * date: 2019/2/20
 */
package com.example.bookflow;

import java.util.ArrayList;

/**
 * User class
 */
public class User implements Owner,Borrower{
    private String username;
    private String password;
    private String email;
    private int phoneNumber;
    private ArrayList<BookCopy> ownedBooks;
    private ArrayList<BookCopy> borrowedBooks;
    private ArrayList<Request>  requests;
    private ArrayList<Review> reviews;

    @Override
    public void addBook(BookCopy bookCopy) {
        ownedBooks.add(bookCopy);
    }

    /**
     *
     * @param ISBN
     */
    @Override
    public void getBookDescription(int ISBN) {

    }

    /**
     *
     * @param bookCopy
     */
    @Override
    public void setStatus(BookCopy bookCopy) {

    }

    @Override
    public void viewOwnedBooks() {

    }

    @Override
    public void editBookDescription() {

    }

    @Override
    public void deleteBook() {

    }

    @Override
    public void viewRequest(Book book) {

    }

    @Override
    public void acceptRequest(Book book) {

    }

    @Override
    public void declineRequest(Book book) {

    }


}