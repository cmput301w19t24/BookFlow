/**
 * author: Yuhan Ye
 * date: 2019/2/20
 */
package com.example.bookflow.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * User class records all user attributes
 * @attributs: uid, username, password, email, phoneNumber, imageurl, selfIntro
 */
public class User implements Owner,Borrower{
    private String uid;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String imageurl;
    private String selfIntro;

    private ArrayList<Book> ownedBooks = new ArrayList<Book>();
    private ArrayList<Book> borrowedBooks= new ArrayList<Book>();
    private ArrayList<Request>  requests= new ArrayList<Request>();
    private ArrayList<Review> reviews;

    public User() {

    }

    /**
     * constructor when username and password are given
     * @param username string
     * @param password string
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setUser(User user) {
        this.uid = user.getUid();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.imageurl = user.getImageurl();
    }

    /**
     * getter of image url
     * @return imageUrl
     */
    public String getImageurl() {
        return imageurl;
    }

    /**
     * setter of imageUrl
     * @param url string
     */
    public void setImageurl(String url) {
        this.imageurl = url;
    }

//    public String getNotificationToken() {
//        return notificationToken;
//    }
//
//    public void setNotificationToken(String notificationToken) {
//        this.notificationToken = notificationToken;
//    }

    /**
     * self intro getter
     * @return self introduction as a string
     */
    public String getSelfIntro() {
        return selfIntro;
    }

    /**
     * self intro setter
     * @param selfIntro self introduction as a string
     */
    public void setSelfIntro(String selfIntro) {
        this.selfIntro = selfIntro;
    }

    /**
     * get all books owned by the user
     * @return ownedBooks
     */
    public ArrayList<Book> getOwnedBooks(){
        return ownedBooks;
    }

    /**
     * getter of borrowed books
     * @return borrowedBooks
     */
    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    /**
     * add a book to ownedbook list
     * @param book a Book with book informations
     */
    @Override
    public void addBook(Book book) {
        ownedBooks.add(book);
    }

    /**
     * get all ownedBooks
     * @return ownedBooks
     */
    @Override
    public ArrayList<Book> viewOwnedBooks() {
        return ownedBooks;
    }

    /**
     * remove a book from owned book list
     * @param book the bok you want to delete
     */
    @Override
    public void deleteBook(Book book) {
        ownedBooks.remove(book);
    }

    /**
     * TODO
     * @param book Book
     */
    @Override
    public void viewRequest(Book book) {

    }

    /**
     * TODO
     * @param request Request
     */
    public void acceptRequest(Request request) {

    }

    /**
     * TODO
     * @param request Request
     */
    public void declineRequest(Request request) {

    }

    /**
     * change the status of a book to BORROWED
     * @param book the change target
     */
    @Override
    public void ownerHandOverBook(Book book) {
        book.setStatus(Book.BookStatus.BORROWED);
        // TODO: update data to firebase
    }

    /**
     * change the status of a book to AVAILABLE
     * @param book
     */
    @Override
    public void receiveReturnedBook(Book book) {
        book.setStatus(Book.BookStatus.AVAILABLE);
        // TODO: update data to firebase
    }

    /**
     * TODO
     * @param keywords an ArrayList of keywords
     */
    @Override
    public void searchBook(ArrayList<String> keywords) {

    }

    /**
     * change the status of a book to REQUESTED
     * @param book the book to be requested
     */
    @Override
    public void requestBook(Book book) {
        book.setStatus(Book.BookStatus.REQUESTED);
        // TODO: send notification
        // TODO: update data to firebase
    }

    /**
     * TODO
     * @return null
     */
    @Override
    public ArrayList<Book> listRequestedBooks() {
        return null;
    }

    /**
     * TODO
     * @return null
     */
    @Override
    public ArrayList<Book> listAcceptedBooks() {
        return null;
    }

    /**
     * change the status of a book to BORROWED(by borrower)
     * @param book the accepted book
     */
    @Override
    public void receiveAcceptedBook(Book book) {
        book.setStatus(Book.BookStatus.BORROWED);
        // TODO: update data to firebase
    }

    /**
     * change the book status to AVAILABLE
     * @param book
     */
    @Override
    public void borrowerHandOverBook(Book book) {
        book.setStatus(Book.BookStatus.AVAILABLE);
        // TODO: update data to firebase

    }

    /**
     * TODO
     * @return null
     */
    @Override
    public ArrayList<Book> listBorrowingBooks() {
        return null;
    }


    /**
     * edit contact information
     * @param e email information string
     * @param p phoneNumber information string
     */
    public void editContactInfo(String e, String p) {
        email = e;
        phoneNumber = p;
    }

    /**
     * getter of username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * setter of username
     * @param username string
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * getter of email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * setter of email
     * @param email string
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * getter of phone
     * @return phone
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * setter of phone
     * @param phoneNumber string
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * setter of uid
     * @param uid string
     */
    public void setUid(String uid) {
        this.uid =uid;
    }

    /**
     * getter of uid
     * @return uid
     */
    public String getUid() {
        return this.uid;
    }

}