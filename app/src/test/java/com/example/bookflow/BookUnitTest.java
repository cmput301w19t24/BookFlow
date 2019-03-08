package com.example.bookflow;

import android.net.Uri;

import com.example.bookflow.Model.Book;
import com.example.bookflow.Model.Photo;
import com.example.bookflow.Model.User;

import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

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
        Uri uri = null;
        uri = Uri.parse("https://en.wikipedia.org/wiki/Java_(programming_language)#/media/File:Java_programming_language_logo.svg");

        book1.addPhoto(uri);
        assertEquals(uri, book1.getPhoto());
    }
    
    @Test
    public void testDeletePhoto() {
        book1.deletePhoto();
        assertEquals(null, book1.getPhoto());
    }
    
    @Test
    public void testSetTitle(){
        book1.setTitle("My Story");
        assertEquals("My Story", book1.getTitle());
    }
    
    @Test
    public void testSetAuthor(){
        book1.setAuthor("Jinming");
        assertEquals("Jinming", book1.getAuthor());
    }
    
    @Test
    public void testSetIsbn(){
        book1.setIsbn("0123456789");
        assertEquals("0123456789", book1.getIsbn());
    }
    
    @Test
    public void testSetRating(){
        book1.setRating(5);
        assertEquals(5, book1.getRating());
    }
    
    @Test
    public void testSetStatus(){
        book1.setStatus("Available");
        assertEquals("Available", book1.getStatus());
    }
    
    @Test
    public void testSetOwner(){
        User user1 = new User();
        book1.setOwner(user1);
        assertEquals(user1, book1.getOwner());
    }
    
    @Test
    public void testSetBorrower(){
        User user1 = new User();
        book1.setBorrower(user1);
        assertEquals(user1, book1.getBorrower());
    }
    
    @Test
    public void testSetDescription(){
        book1.setDescription("9876543210", "My New Story", "New Jinming");
        assertEquals("9876543210", book1.getIsbn());
        assertEquals("My New Story", book1.getTitle());
        assertEquals("New Jinming", book1.getAuthor());
    }
}
