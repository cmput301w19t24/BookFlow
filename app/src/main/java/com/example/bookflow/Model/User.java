/**
 * author: Yuhan Ye
 * date: 2019/2/20
 */
package com.example.bookflow.Model;

import java.util.ArrayList;

/**
 * User class
 */
public class User implements Owner,Borrower{
    private String uid;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String imageurl; // user icon
    private String selfIntro;

    public String getSelfintro() {
        return selfIntro;
    }
    public void setSelfintro(String selfIntro) {
        this.selfIntro = selfIntro;
    }

    private ArrayList<Book> ownedBooks = new ArrayList<Book>();
    private ArrayList<Book> borrowedBooks= new ArrayList<Book>();
    private ArrayList<Request>  requests= new ArrayList<Request>();
    private ReviewList reviews;

    public User(){

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


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
    public ArrayList<Book> viewOwnedBooks() {
        return ownedBooks;
    }

    @Override
    public void deleteBook(Book book) {
        ownedBooks.remove(book);
    }

    @Override
    public void viewRequest(Book book) {

    }

    public void acceptRequest(Request request) {

    }

    public void declineRequest(Request request) {

    }

    @Override
    public void ownerHandOverBook(Book book) {
        book.setStatus(Book.BookStatus.BORROWED);
        // TODO: update data to firebase
    }

    @Override
    public void receiveReturnedBook(Book book) {
        book.setStatus(Book.BookStatus.AVAILABLE);
        // TODO: update data to firebase
    }

    @Override
    public void searchBook(ArrayList<String> keywords) {

    }

    @Override
    public void requestBook(Book book) {
        book.setStatus(Book.BookStatus.REQUESTED);
        // TODO: send notification
        // TODO: update data to firebase
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
    public void receiveAcceptedBook(Book book) {
        book.setStatus(Book.BookStatus.BORROWED);
        // TODO: update data to firebase
    }

    @Override
    public void borrowerHandOverBook(Book book) {
        book.setStatus(Book.BookStatus.AVAILABLE);
        // TODO: update data to firebase

    }

    @Override
    public ArrayList<Book> listBorrowingBooks() {
        return null;
    }



    public void editContactInfo(String e, String p) {
        email = e;
        phoneNumber = p;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageurl() {
        return imageurl;
    }
    public void setImageurl(String url) {
        this.imageurl = url;
    }

    public void setUid(String uid) {
        this.uid =uid;
    }
    public String getUid() {
        return this.uid;
    }

}