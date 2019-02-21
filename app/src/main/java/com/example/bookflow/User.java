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
    private ArrayList<Book> ownedBooks;
    private ArrayList<Book> borrowedBooks;
    private ArrayList<Request>  requests;
    private ArrayList<Review> reviews;

    public ArrayList<Book> getOwnedBooks(){
        return ownedBooks;
    }

    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }



    @Override
    public void addBook(Book book) {
        ownedBooks.add(book);
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
    public void deleteBook(Book book) {

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

    @Override
    public void ownerHandOverBook(Book book, String ISBN) {

    }

    @Override
    public void receiveReturnedBook(Book book, String ISBN) {

    }

    @Override
    public void attachPhoto(Book book) {

    }

    @Override
    public void deletePhoto(Book book) {

    }

    @Override
    public void specifyGeolocation() {

    }

    @Override
    public void searchBook(ArrayList<String> keywords) {

    }

    @Override
    public void requestBook(Book book) {

    }

    @Override
    public ArrayList<Book> listRequestedBooks() {
        return null;
    }

    @Override
    public ArrayList<Book> listAcceptedBooks() {
        return null;
    }

    @Override
    public void receiveAcceptedBook() {

    }

    @Override
    public void borrowerHandOverBook() {

    }

    @Override
    public ArrayList<Book> listBorrowingBooks() {
        return null;
    }



    public void editContactInfo(String e, String p) {
        email = e;
        phoneNumber = p;
    }
}