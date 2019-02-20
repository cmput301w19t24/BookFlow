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
    private String phoneNumber;
    private ArrayList<BookCopy> ownedBooks;
    private ArrayList<BookCopy> borrowedBooks;
    private ArrayList<Request>  requests;
    private ArrayList<Review> reviews;

    @Override
    public void addBook(BookCopy bookCopy) {
        ownedBooks.add(bookCopy);
    }

    @Override
    public void getBookDescription(String ISBN) {

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
    public void viewRequest(BookCopy bookCopy) {

    }

    @Override
    public void acceptRequest(BookCopy bookCopy) {

    }

    @Override
    public void declineRequest(BookCopy bookCopy) {

    }

    @Override
    public void handOverBook(BookCopy bookCopy, String ISBN) {

    }

    @Override
    public void receiveReturnedBook(BookCopy bookCopy, String ISBN) {

    }

    @Override
    public void attachPhoto(BookCopy bookCopy) {

    }

    @Override
    public void deletePhoto(BookCopy bookCopy) {

    }

    @Override
    public void specifyGeolocation() {

    }

    @Override
    public void searchBook(ArrayList<String> keywords) {

    }

    @Override
    public void requestBook(BookCopy bookCopy) {

    }

    @Override
    public ArrayList<BookCopy> listRequestedBooks() {
        return null;
    }

    @Override
    public ArrayList<BookCopy> listAcceptedBooks() {
        return null;
    }

    @Override
    public void receiveAcceptedBook() {

    }

    @Override
    public void listBorrowingBooks() {

    }

    @Override
    public void handOverBook() {

    }



    public void editContactInfo(String e, String p) {
        email = e;
        phoneNumber = p;
    }
}