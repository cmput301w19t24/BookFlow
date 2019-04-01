package com.example.bookflow;

import com.example.bookflow.Model.Review;
import com.example.bookflow.Model.User;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReviewUnitTest {

    public User usr1 = new User("aaron", "password12345");
    public User usr2 = new User("joe", "hello");

    @Test
    public void testsetReviewer() {
        Review review = new Review("Nice Nice!", 5, usr1.getUid(), usr2.getUid(),String.valueOf(5));
        review.setReviewer("12345");
        assertEquals("12345", review.getReviewerID());
    }

    @Test
    public void testsetReviewee() {
        Review review = new Review("Nice Nice!", 5, usr1.getUid(), usr2.getUid(),String.valueOf(5));
        review.setReviewee("12345");
        assertEquals("12345",review.getReviewee());
    }

    @Test
    public void testsetRating() {
        Review review = new Review("Nice Nice!", 5, usr1.getUid(), usr2.getUid(),String.valueOf(5));
        review.setRating(4);
        assertEquals(String.valueOf(4),review.getRating());
    }

    @Test
    public void testsetComments() {
        Review review = new Review("Nice Nice!", 5, usr1.getUid(), usr2.getUid(),String.valueOf(5));
        review.setComments("BAD BAD!");
        assertEquals("BAD BAD!",review.getComments());
    }
}
