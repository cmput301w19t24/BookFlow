package com.example.bookflow;

import com.example.bookflow.Model.Book;
import com.example.bookflow.Model.Borrower;
import com.example.bookflow.Model.Owner;
import com.example.bookflow.Model.Request;
import com.example.bookflow.Model.User;

import org.junit.Test;

import static org.junit.Assert.*;

public class RequestTest {


    @Test
    public void getBorrower() {
        Borrower b = new User();
        Owner o = new User();
        Book book = new Book("frankenstein", "Mary Shelley", "9780440927174");
        Request req = new Request(b, o, book);
        assertEquals(req.getBorrower(), b);
    }

    @Test
    public void setBorrower() {
        Borrower b = new User();
        Owner o = new User();
        Book book = new Book("frankenstein", "Mary Shelley", "9780440927174");
        Request req = new Request(b, o, book);
        Borrower newb = new User();
        req.setBorrower(newb);
        assertEquals(req.getBorrower(), newb);
    }

    @Test
    public void getOwner() {
        Borrower b = new User();
        Owner o = new User();
        Book book = new Book("frankenstein", "Mary Shelley", "9780440927174");
        Request req = new Request(b, o, book);
        assertEquals(req.getOwner(), o);
    }

    @Test
    public void setOwner() {
        Borrower b = new User();
        Owner o = new User();
        Book book = new Book("frankenstein", "Mary Shelley", "9780440927174");
        Request req = new Request(b, o, book);
        Owner newo = new User();
        req.setOwner(newo);
        assertEquals(req.getBorrower(), newo);
    }

    @Test
    public void getBook() {
        Borrower b = new User();
        Owner o = new User();
        Book book = new Book("frankenstein", "Mary Shelley", "9780440927174");
        Request req = new Request(b, o, book);
        assertEquals(req.getBook(), book);
    }

    @Test
    public void setBook() {
        Borrower b = new User();
        Owner o = new User();
        Book book = new Book("frankenstein", "Mary Shelley", "9780440927174");
        Request req = new Request(b, o, book);
        Book newBook = new Book("2001: A Space Odyssey", "C. Clarke", "9780451066251");
        req.setBook(newBook);
        assertEquals(req.getBook(), newBook);
    }
}