package com.example.bookflow.Model;

import java.util.ArrayList;
import java.util.UUID;

//based on how lonelyTwitter, this probably has to implemented as a global ReviewList on the database,
//accessible by a storage reference, with unique IDs for each review.

public class ReviewList {
    private ArrayList<Review> rList;

    public ReviewList() {
        rList = new ArrayList<Review>();
    }
    public ArrayList<Review> getReviews() {
        return rList;
    }

    public ArrayList<String> toStringArray() {
        ArrayList<String> strList = new ArrayList<String>();
        for (Review eachReview: rList) {
            strList.add(eachReview.toString());
        }
        return strList;
    }

    public void addReview(Review review) {
        rList.add(review);
    }

    public void deleteReview(Review review) {
        rList.remove(review);
    }

    // edit review, should use Review getters and setters
    public void editReview(Review review, int rating, String comments) {
    }

    public Review getReviewByUUID(UUID uuid){return null;}

    public int count() {
        return rList.size();
    }

    // calculate average of all reviews for reviewee
    public int getReviewAverage(User reviewee) {return 0;}

    // get arrayList of all reviews by specified user, user uses this to get list of all their reviews
    // from which they can edit or delete a review (using edit/deleteReview)
    public ArrayList<Review> getReviewedByUser(User user) {
        return null;
    }


}
