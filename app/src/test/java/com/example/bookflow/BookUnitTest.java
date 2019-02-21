package com.example.bookflow;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class BookUnitTest {
    Book book1 = new Book("ABC", "jinming", "0123456789123");
    
    @Test
    public void testCountIncrease() {
        book1.countIncrease();
        book1.countIncrease();
        assertEquals(1, book1.getCount());
    }
    
    @Test
    public void testAddAndGetPhoto() {
        Photo photo1 = new Photo("https://en.wikipedia.org/wiki/Java_(programming_language)#/media/File:Java_programming_language_logo.svg");
        book1.addPhoto(photo1);
        assertEquals(photo1, book1.getPhoto(0));
    }
    
    @Test
    public void testDeletePhoto() {
        book1.deletePhoto(0);
        assertEquals(0, book1.getAllPhotos().size());
    }
}
