package com.example.bookflow;

import android.net.Uri;

import com.example.bookflow.Model.Book;
import com.example.bookflow.Model.User;

import org.junit.Test;
import static org.junit.Assert.*;

public class BookUnitTest {
    Book book1 = new Book("ABC", "jinming", "0123456789123");
    
    @Test
    public void testCountIncrease() {
        book1.countIncrease();
        book1.countIncrease();
        assertEquals(2, book1.getCount());
    }

    // TODO: to be implemented in next phase
    /*
    @Test
    public void testSetAndGetPhoto() {
        Uri uri = null;
        uri = Uri.parse("https://givecatsabetterlife.com/wp-content/uploads/2018/10/ybJsG7Zty1s_hqdefault.jpg");

        book1.setPhotoUri(uri.toString());
        assertEquals(uri, Uri.parse(book1.getPhotoUri()));
    }*/
    
    @Test
    public void testDeletePhoto() {
        book1.deletePhotoUri();
        assertEquals(null, book1.getPhotoUri());
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
        book1.setStatus(Book.BookStatus.ACCEPTED);
        assertEquals(Book.BookStatus.ACCEPTED, book1.getStatus());
    }

}
