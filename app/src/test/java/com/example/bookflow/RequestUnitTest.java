package com.example.bookflow;

import com.example.bookflow.Model.Book;
import com.example.bookflow.Model.Borrower;
import com.example.bookflow.Model.Owner;
import com.example.bookflow.Model.Request;
import com.example.bookflow.Model.User;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RequestUnitTest {
    @Test
    public void getBorrowerId() {
        User owner = new User();
        User borrower = new User();
        owner.setUid("123");
        borrower.setUid("456");
        Book book = new Book("frankenstein", "Mary Shelley", "9780440927174");
        book.setBookId("789");
        String ownerId = owner.getUid();
        String borrowerId = borrower.getUid();
        String bookId = book.getBookId();
        Request req = new Request(ownerId, borrowerId, bookId);
        assertEquals(req.getBorrowerId(), "456");
    }

    @Test
    public void setBorrowerId() {
        User borrower = new User();
        borrower.setUid("456");
        String borrowerId = borrower.getUid();
        Request req = new Request();
        req.setBorrowerId(borrowerId);
        assertEquals(req.getBorrowerId(), "456");
    }

    @Test
    public void getOwnerId() {
        User owner = new User();
        User borrower = new User();
        owner.setUid("123");
        borrower.setUid("456");
        Book book = new Book("frankenstein", "Mary Shelley", "9780440927174");
        book.setBookId("789");
        String ownerId = owner.getUid();
        String borrowerId = borrower.getUid();
        String bookId = book.getBookId();
        Request req = new Request(ownerId, borrowerId, bookId);
        assertEquals(req.getOwnerId(), "123");
    }

    @Test
    public void setOwner() {
        User owner = new User();
        owner.setUid("123");
        String ownerId = owner.getUid();
        Request req = new Request();
        req.setOwnerId(ownerId);
        assertEquals(req.getOwnerId(), "123");
    }

    @Test
    public void getBookId() {
        User owner = new User();
        User borrower = new User();
        owner.setUid("123");
        borrower.setUid("456");
        Book book = new Book("frankenstein", "Mary Shelley", "9780440927174");
        book.setBookId("789");
        String ownerId = owner.getUid();
        String borrowerId = borrower.getUid();
        String bookId = book.getBookId();
        Request req = new Request(ownerId, borrowerId, bookId);
        assertEquals(req.getBookId(), "789");
    }

    @Test
    public void setBookId() {
        Book book = new Book("frankenstein", "Mary Shelley", "9780440927174");
        book.setBookId("789");
        String bookId = book.getBookId();
        Request req = new Request();
        req.setBookId(bookId);
        assertEquals(req.getBookId(), "789");
    }
}
