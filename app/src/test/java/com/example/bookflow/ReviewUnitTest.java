package com.example.bookflow;

import com.example.bookflow.Model.Review;
import com.example.bookflow.Model.ReviewList;
import com.example.bookflow.Model.User;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReviewUnitTest {

    public User usr1 = new User("aaron", "password12345");
    public User usr2 = new User("joe", "hello");

    @Test
    public void testsetReviewer() {
        User usr3 = new User();
        usr3.setUid("12345");
        Review review = new Review("Nice Nice!", 5, usr1.getUid(), usr2.getUid(),String.valueOf(5));
        review.setReviewer(usr3.getUid());
        assertEquals(usr3.getUid(), review.getReviewerID());
    }

    @Test
    public void testsetReviewee() {
        User usr3 = new User();
        Review review = new Review(usr1, usr2, "Nice Nice!",5);
        review.setReviewee(usr3);
        assertEquals(usr3,review.getReviewee());
    }

    @Test
    public void testsetRating() {
        Review review = new Review(usr1, usr2, "Nice Nice!",5);
        review.setRating(4);
        assertEquals(4,review.getRating());
    }

    @Test
    public void testsetComments() {
        Review review = new Review(usr1, usr2, "Nice Nice!",5);
        review.setComments("BAD BAD!");
        assertEquals("BAD BAD!",review.getComments());
    }
}
