package com.example.bookflow;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReviewUnitTest {

    public User usr1 = new User("aaron", "password12345");
    public User usr2 = new User("joe", "hello");
    @Test
    public void addAndDeleteReviewTest() {
        ReviewList rList = new ReviewList();
        assertEquals(0, rList.count());
        Review r = new Review(usr1, usr2, "nice book", 5);
        rList.addReview(r);
        assertEquals(1, rList.count());
        rList.deleteReview(r);
        assertEquals(0, rList.count());
    }
    @Test
    public void reviewAverageTest() {
        ReviewList rList = new ReviewList();
        Review r1 = new Review(usr1, usr2, "nice book", 3);
        Review r2 = new Review(usr1, usr2, "nice book", 4);
        Review r3 = new Review(usr1, usr2, "nice book", 5);
        rList.addReview(r1);
        rList.addReview(r2);
        rList.addReview(r3);
        assertEquals(4, rList.getReviewAverage(usr2));
    }
}
