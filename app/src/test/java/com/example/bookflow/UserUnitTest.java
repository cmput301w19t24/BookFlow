package com.example.bookflow;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserUnitTest {
    @Test
    public void testAddBook(){
        User user = new User();
        Book book = new Book("frankenstein", "Mary Shelley", "9780440927174");

        user.addBook(book);
        assertEquals(user.getOwnedBooks().get(0),book);
    }

    @Test
    public void testGetBookDescription(){
        User user = new User();
        Book book = new Book("frankenstein", "Mary Shelley", "9780440927174");

        user.addBook(book);
        assertEquals("frankenstein Mary Shelley 9780440927174",user.getBookDescription("9780440927174"));
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
    public void testDeclineRequest() {
        Owner user = new User();
        Request request = new Request();
        user.declineRequest(request);
        assertEquals(request.getBook().status, "AVAILABLE");
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

        ArrayList<Book> books = user.listRequestedBooks();


        assertEquals(books.get(0).getStatus(), "requested");
        assertEquals(books.get(1).getStatus(), "requested");
        assertEquals(books.get(2).getStatus(), "requested");
    }

    @Test
    public void testListAcceptedBooks() {
        Borrower b = new User();
        Owner o = new User();

        Book book1 = new Book("frankenstein", "Mary Shelley", "9789176053461");
        o.addBook(book1);
        b.requestBook(book1);
        o.acceptRequest(book1);

        Book book2 = new Book("frankenstein", "Mary Shelley", "9782844002693");
        o.addBook(book2);
        b.requestBook(book2);
        o.acceptRequest(book2);

        ArrayList<Book> books = b.listAcceptedBooks();

        assertEquals(books.get(0).getStatus(), "accepted");
        assertEquals(books.get(1).getStatus(), "accepted");
    }

    @Test
    public void testReceiveAcceptedBook() {
        Borrower b = new User();
        Owner o = new User();

        Book book1 = new Book("frankenstein", "Mary Shelley", "9789176053461");
        Request request1 = new Request()
        o.addBook(book1);
        b.requestBook(book1);
        o.acceptRequest(book1);
        o.ownerHandOverBook(book1);
        b.receiveAcceptedBook();

        Book book2 = new Book("frankenstein", "Mary Shelley", "9782844002693");
        o.addBook(book2);
        b.requestBook(book2);
        o.acceptRequest(book2);

        ArrayList<Book> books = b.listAcceptedBooks();

        assertEquals(books.get(0).getStatus(), "accepted");
        assertEquals(books.get(1).getStatus(), "accepted");

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
