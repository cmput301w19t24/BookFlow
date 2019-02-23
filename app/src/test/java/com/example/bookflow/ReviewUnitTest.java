package com.example.bookflow;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReviewUnitTest {

    public User usr1 = new User("aaron", "password12345");
    public User usr2 = new User("joe", "hello");
    @Test
    public void ratingAndCommentsGetterSetterTest() {
        Review r = new Review(usr1, usr2, "great book", 5);
        assertEquals("great book", r.getComments());
        assertEquals(5, r.getRating());
        r.setComments("terrible book");
        assertEquals("terrible book", r.getComments());
        r.setRating(1);
        assertEquals(1, r.getRating());
    }
    @Test
    public void addDeleteReviewFromListTest() {
        ReviewList rList = new ReviewList();
        assertEquals(0, rList.count());
        Review r = new Review(usr1, usr2, "nice book", 5);
        rList.addReview(r);
        assertTrue(rList.getReviews().contains(r));
        assertEquals(1, rList.count());
        rList.deleteReview(r);
        assertFalse(rList.getReviews().contains(r));
        assertEquals(0, rList.count());
    }

    @Test
    public void editReviewOnListTest() {
        ReviewList rList = new ReviewList();
        Review r = new Review(usr1, usr2, "nice book", 5);
        rList.addReview(r);
        rList.editReview(r, 3, "okay book");
        Review test = rList.getReviewByUUID(r.getUUID());
        assertEquals(3, test.getRating());
        assertEquals("okay book", test.getComments());
    }

    @Test
    public void reviewAverageFromListTest() {
        ReviewList rList = new ReviewList();
        Review r1 = new Review(usr1, usr2, "nice book", 3);
        Review r2 = new Review(usr1, usr2, "nice book", 4);
        Review r3 = new Review(usr1, usr2, "nice book", 5);
        rList.addReview(r1);
        rList.addReview(r2);
        rList.addReview(r3);
        assertEquals(4, rList.getReviewAverage(usr2));
    }

    /*
    need to add signatures and test cases for user related review methods
    @Test
    public void userReviewUserTest() {
        // given reference to ReviewList, a user should be able to add a review for another user
        ReviewList rList = new ReviewList();
        usr1.reviewUser(rList, usr2, "loved the book", 5); // should return a unique ID for review
        assertEquals()
    }
    */

}
