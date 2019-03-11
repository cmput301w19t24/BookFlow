package com.example.bookflow;

import com.example.bookflow.Model.Book;
import com.example.bookflow.Model.Notification;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NotificationUnitTest {
    Notification notification = new Notification();
    @Test
    public void testSetSenderId(){
        notification.setSenderId("123");
        assertEquals(notification.getSenderId(), "123");
    }

    @Test
    public void testSetBookId(){
        notification.setBookId("456");
        assertEquals(notification.getBookId(), "456");
    }

    @Test
    public void testSetType(){
        notification.setType("Request");
        assertEquals(notification.getType(), "Request");
    }

    @Test
    public void testSetSenderName(){
        notification.setSenderName("Jerry");
        assertEquals(notification.getSenderName(), "Jerry");
    }

    @Test
    public void testSetBookTitle(){
        notification.setBookTitle("Hello");
        assertEquals(notification.getBookTitle(), "Hello");
    }

    @Test
    public void testSetTransactionId(){
        notification.setTransactionId("111");
        assertEquals(notification.getTransactionId(), "111");
    }

}
