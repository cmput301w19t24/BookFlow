/**
 * author: Shengyao Lu
 * date: 2019/2/20
 */
package com.example.bookflow.Model;

import java.util.UUID;

/**
 * This class models the relationship between two users with book
 */
public class Review {
    private User reviewer;
    private User reviewee;
    private String comments;
    private int rating;
    private UUID uuid;

    public Review() {

    }

    public Review(User reviewer, User reviewee, String comments, int rating) {
        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.comments = comments;
        this.rating = rating;
        this.uuid = UUID.randomUUID();
    }

    public User getReviewer() {
        return reviewer;
    }

    public User getReviewee() {
        return reviewee;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getComments() {
        return comments;
    }

    public int getRating() {
        return rating;
    }

    // setter for reviewer/reviewee probably not necessary as they should not be editable
    public void setRating(int rating) { this.rating = rating; }
    public void setReviewer(User reviewer) { this.reviewer = reviewer; }
    public void setReviewee(User reviewee) { this.reviewee = reviewee; }
    public void setComments(String comments) { this.comments = comments; }


}
