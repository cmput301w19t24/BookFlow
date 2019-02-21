package com.example.bookflow;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserUnitTest {
    @Test
    public void testAddBook(){
        User user = new User();
        Book book = new Book("hi","shiki","123456789");
        user.addBook(book);
        assertEquals(user.getOwnedBooks().get(0),book);
    }

    @Test
    public void testGetBookDescription(){
        User user = new User();
        Book book = new Book("hi","shiki","123456789");
        user.addBook(book);
        assertEquals("hi shiki 123456789",user.getBookDescription("123456789"));
    }

    @Test
    public void testViewOwnedBooks(){

    }

    @Test
    public void testEditBookDescription(){

    }

    @Test
    public void testDeleteBook(){

    }

    @Test
    public void testViewRequest(){

    }

    //todo lsy

    @Test
    public void testReceiveReturnedBook() {
        Owner user = new User();
        Book book = new Book("frankenstein", "Mary Shelley", "9780440927174");
        user.receiveReturnedBook(book);
        assertEquals(book.status, "AVAILABLE");
    }

    @Test
    public void testAcceptRequest() {
        Owner user = new User();
        Request request = new Request();
        user.acceptRequest(request);
        assertEquals(request.getBook().status, "ACCEPTED");
    }

    @Test
    public void testSearchBook(ArrayList<String> keywords) {

    }

    //todo lpe
    @Test
    public void testRequestBook() {
        Borrower user = new User();
        Book book = new Book("frankenstein", "Mary Shelley", "9780440927174");
        user.requestBook(book);

        assertEquals(book.getStatus(), "requested");
    }

    @Test
    public void testListRequestedBooks() {
        Borrower user = new User();
        Book book1 = new Book("frankenstein", "Mary Shelley", "9789176053461");
        user.requestBook(book1);

        Book book2 = new Book("frankenstein", "Mary Shelley", "9782844002693");
        user.requestBook(book2);

        Book book3 = new Book("frankenstein", "Mary Shelley", "9782844002693");
        user.requestBook(book3);


        assertEquals(book1.getStatus(), "requested");
        assertEquals(book2.getStatus(), "requested");
        assertEquals(book3.getStatus(), "requested");
    }

    @Test
    public void testListAcceptedBooks() {

    }

    @Test
    public void testReceiveAcceptedBook() {

    }

    @Test
    public void testBorrowerHandOverBook() {

    }

    @Test
    public void testListBorrowingBooks() {

    }

    @Test
    public void testEditContactInfo() {

    }
}
