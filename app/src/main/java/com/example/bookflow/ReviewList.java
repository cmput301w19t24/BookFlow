package com.example.bookflow;

import java.util.ArrayList;

// should there be a global ReviewList object held in firebase?
public class ReviewList {
    private ArrayList<Review> rList;
    public ReviewList() {
        rList = new ArrayList<Review>();
    }
    public void addReview(Review review) {
        rList.add(review);
    }
    public void deleteReview (Review review) {
        rList.remove(review);
    }
    public ArrayList<Review> getReviews() {
        return rList;
    }
    // global array count
    public int count() {
        return rList.size();
    }
    // returns count of reviews for specified reviewee
    public int getReviewCount(User reviewee) {return 0;}

    // calculate average of all reviews for reviewee
    public int getReviewAverage(User reviewee) {return 0;}

}
