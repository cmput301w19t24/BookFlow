package com.example.bookflow;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserUnitTest {
    @Test
    public void testAddBook(){

    }

    @Test
    public void testGetBookDescription(){

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
    public void testReceiveReturnedBook(Book book, String ISBN) {
        assertEquals(book.status, "AVAILABLE");
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
