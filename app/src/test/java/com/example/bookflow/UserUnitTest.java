package com.example.bookflow;

import com.example.bookflow.Model.Book;
import com.example.bookflow.Model.Borrower;
import com.example.bookflow.Model.Owner;
import com.example.bookflow.Model.Request;
import com.example.bookflow.Model.User;

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
    public void testViewOwnedBooks(){
        User user = new User();
        Book book = new Book("frankenstein", "Mary Shelley", "9780440927174");
        user.addBook(book);
        Book book2 = new Book("George's Marvelous Medicine", "Ronald Dahl", "9788711222102");
        user.addBook(book2);
        assertEquals(user.viewOwnedBooks().size(),2);
        assertEquals(user.viewOwnedBooks().get(0),book);
        assertEquals(user.viewOwnedBooks().get(1),book2);
    }


    @Test
    public void testDeleteBook(){
        User user = new User();
        Book book = new Book("frankenstein", "Mary Shelley", "9780440927174");
        user.addBook(book);
        assertEquals(user.getOwnedBooks().get(0),book);
        user.deleteBook(book);
        assertEquals(0, user.getOwnedBooks().size());
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
        assertEquals(book.getStatus(), Book.BookStatus.AVAILABLE);
    }


//    @Test
//    public void testAcceptRequest() {
//        Owner owner = new User();
//        Borrower borrower = new User();
//        Book book = new Book("frankenstein", "Mary Shelley", "9780440927174");
//        Request request = new Request(borrower, owner, book);
//        owner.acceptRequest(request);
//        assertEquals(request.getBook().getStatus(), "ACCEPTED");
//    }
//
//    @Test
//    public void testDeclineRequest() {
//        Owner owner = new User();
//        Borrower borrower = new User();
//        Book book = new Book("frankenstein", "Mary Shelley", "9780440927174");
//        Request request = new Request(borrower, owner, book);
//        owner.declineRequest(request);
//        assertEquals(request.getBook().getStatus(), "AVAILABLE");
//    }

    //todo lpe
//    @Test
//    public void testRequestBook() {
//        Borrower user = new User();
//        Book book = new Book("frankenstein", "Mary Shelley", "9780440927174");
//        user.requestBook(book);
//
//        assertEquals(book.getStatus(), "requested");
//    }
//
//    @Test
//    public void testListRequestedBooks() {
//        Borrower user = new User();
//        Book book1 = new Book("frankenstein", "Mary Shelley", "9789176053461");
//        user.requestBook(book1);
//
//        Book book2 = new Book("frankenstein", "Mary Shelley", "9782844002693");
//        user.requestBook(book2);
//
//        Book book3 = new Book("frankenstein", "Mary Shelley", "9782844002693");
//        user.requestBook(book3);
//
//        ArrayList<Book> books = user.listRequestedBooks();
//
//
//        assertEquals(books.get(0).getStatus(), "requested");
//        assertEquals(books.get(1).getStatus(), "requested");
//        assertEquals(books.get(2).getStatus(), "requested");
//    }

//    @Test
//    public void testListAcceptedBooks() {
//        Borrower b = new User();
//        Owner o = new User();
//
//        Book book1 = new Book("frankenstein", "Mary Shelley", "9789176053461");
//        Request request1 = new Request(b, o, book1);
//        o.addBook(book1);
//        b.requestBook(book1);
//        o.acceptRequest(request1);
//
//        Book book2 = new Book("frankenstein", "Mary Shelley", "9782844002693");
//        Request request2 = new Request(b, o, book2);
//        o.addBook(book2);
//        b.requestBook(book2);
//        o.acceptRequest(request2);
//
//        ArrayList<Book> books = b.listAcceptedBooks();
//
//        assertEquals(books.get(0).getStatus(), "accepted");
//        assertEquals(books.get(1).getStatus(), "accepted");
//    }

//    @Test
//    public void testReceiveAcceptedBook() {
//        Borrower b = new User();
//        Owner o = new User();
//
//        Book book1 = new Book("frankenstein", "Mary Shelley", "9789176053461");
//        Request request1 = new Request(b, o, book1);
//        o.addBook(book1);
//        b.requestBook(book1);
//        o.acceptRequest(request1);
//        o.ownerHandOverBook(book1);
//        b.receiveAcceptedBook(book1);
//
//        Book book2 = new Book("frankenstein", "Mary Shelley", "9782844002693");
//        Request request2 = new Request(b, o, book1);
//        o.addBook(book2);
//        b.requestBook(book2);
//        o.acceptRequest(request2);
//        o.ownerHandOverBook(book2);
//        b.receiveAcceptedBook(book2);
//
//        ArrayList<Book> books = b.listAcceptedBooks();
//
//        assertEquals(books.get(0).getStatus(), "accepted");
//        assertEquals(books.get(1).getStatus(), "accepted");
//
//    }
//
//    @Test
//    public void testBorrowerHandOverBook() {
//        Borrower b = new User();
//        Owner o = new User();
//
//        Book book1 = new Book("frankenstein", "Mary Shelley", "9789176053461");
//        Request request1 = new Request(b, o, book1);
//        o.addBook(book1);
//        b.requestBook(book1);
//        o.acceptRequest(request1);
//        o.ownerHandOverBook(book1);
//
//        Book book2 = new Book("frankenstein", "Mary Shelley", "9782844002693");
//        Request request2 = new Request(b, o, book1);
//        o.addBook(book2);
//        b.requestBook(book2);
//        o.acceptRequest(request2);
//        o.ownerHandOverBook(book2);
//
//        ArrayList<Book> books = b.listAcceptedBooks();
//
//        assertEquals(books.get(0).getStatus(), "borrowed");
//        assertEquals(books.get(1).getStatus(), "borrowed");
//
//    }
//
//    @Test
//    public void testListBorrowingBooks() {
//        Borrower b = new User();
//        Owner o = new User();
//
//        Book book1 = new Book("frankenstein", "Mary Shelley", "9789176053461");
//        Request request1 = new Request(b, o, book1);
//        o.addBook(book1);
//        b.requestBook(book1);
//        o.acceptRequest(request1);
//        o.ownerHandOverBook(book1);
//
//        Book book2 = new Book("frankenstein", "Mary Shelley", "9782844002693");
//        Request request2 = new Request(b, o, book1);
//        o.addBook(book2);
//        b.requestBook(book2);
//        o.acceptRequest(request2);
//        o.ownerHandOverBook(book2);
//
//        ArrayList<Book> books = b.listAcceptedBooks();
//
//        assertEquals(books.get(0).getStatus(), "borrowed");
//        assertEquals(books.get(1).getStatus(), "borrowed");
//
//    }

    @Test
    public void testEditContactInfo() {
        User u = new User();
        String email = "example@example.com";
        String phone = "1234567890";
        u.editContactInfo(email, phone);

        assertEquals(u.getEmail(), email);
        assertEquals(u.getPhoneNumber(), phone);

    }

}
