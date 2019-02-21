package com.example.bookflow;

import org.junit.Test;

import static org.junit.Assert.*;

public class BookUnitTest {
    Book book1 = new Book("ABC", "jinming", "0123456789123");
    
    @Test
    public void testCountIncrease() {
        book1.countIncrease();
        book1.countIncrease();
        assertEquals(1, book1.getCount());
    }
    
    @Test
    public void testPhotoMethods() {
        Photo photo1 = new Photo();
        
        book1.addPhoto(photo1);
        
        assertEqual(photo1, book1.getPhoto(0));
        
        book1.deletePhoto(0)
        
        assertEqual(0, );
    }
}
