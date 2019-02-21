package com.example.bookflow;

import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class BookUnitTest {
    Book book1 = new Book("ABC", "jinming", "0123456789123");
    
    @Test
    public void testCountIncrease() {
        book1.countIncrease();
        book1.countIncrease();
        assertEquals(2, book1.getCount());
    }
    
    @Test
    public void testAddAndGetPhoto() {
        URL url = null;
        try {
            url = new URL("https://en.wikipedia.org/wiki/Java_(programming_language)#/media/File:Java_programming_language_logo.svg");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BookPhoto photo1 = new BookPhoto(url);
        book1.addPhoto(photo1);
        assertEquals(photo1, book1.getPhoto(0));
    }
    
    @Test
    public void testDeletePhoto() {
        book1.deletePhoto(0);
        assertEquals(0, book1.getAllPhoto().size());
    }
}
