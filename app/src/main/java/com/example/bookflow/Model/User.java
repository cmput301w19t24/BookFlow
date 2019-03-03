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
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
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
    public String getBookDescription(String ISBN) {
        return null;
    }

    @Override
    public ArrayList<Book> viewOwnedBooks() {
        return ownedBooks;
    }

    @Override
    public void editBookDescription(Book book ,String title,String author, String ISBN) {
        book.setDescription(ISBN,title,author);
    }

    @Override
    public void deleteBook(Book book) {

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

    }

    @Override
    public void receiveReturnedBook(Book book) {

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
    public void receiveAcceptedBook(Book book) {

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

}