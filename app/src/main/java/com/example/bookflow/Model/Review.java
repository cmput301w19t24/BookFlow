/**
 * author: Shengyao Lu
 * date: 2019/2/20
 */
package com.example.bookflow.Model;

import java.util.Date;
import java.util.UUID;

/**
 * This class models the relationship between two users with book
 */
public class Review {
    private User reviewer;
    private User reviewee;
    private String comments;
    private String date;
    private int rating;
    private UUID uuid;

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

    public void setDate(Date date) {
        this.date = date.toString();
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getComments() {
        return comments;
    }

    public String getRating() {
        return Integer.toString(this.rating);
    }

    public String toString() {
        String result = reviewer.getUsername() + "\n";
        result = result + comments + "\n";
        result = result + "Rating: " + String.valueOf(rating);
        return result;

    }

    // setter for reviewer/reviewee probably not necessary as they should not be editable
    public void setUUID(UUID uuid) { this.uuid = uuid; }
    public void setRating(int rating) { this.rating = rating; }
    public void setReviewer(User reviewer) { this.reviewer = reviewer; }
    public void setReviewee(User reviewee) { this.reviewee = reviewee; }
    public void setComments(String comments) { this.comments = comments; }


}
