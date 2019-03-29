/**
 * author: Shengyao Lu
 * date: 2019/2/20
 */
package com.example.bookflow.Model;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.UUID;

/**
 * This class models the relationship between two users with book
 */
public class Review {
    private String reviewer;
    private String reviewee;
    private String comments;
    private String date;
    private int rating;
    private String uuid;

    public Review() {

    }

    public Review(String comments, int rating, String reviewee, String reviewer, String uuid) {
        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.comments = comments;
        this.rating = rating;
        this.uuid = uuid;
    }

    public String getReviewerID() {
        return reviewer;
    }

    public String getReviewee() {
        return reviewee;
    }

    public void setDate(Date date) {
        this.date = date.toString();
    }

    public String getUUID() {
        return this.uuid;
    }

    public String getComments() {
        return comments;
    }

    public String getRating() {
        return Integer.toString(this.rating);
    }

    public String toString() {
        String result = getReviewerID() + "\n";
        result = result + comments + "\n";
        result = result + "Rating: " + String.valueOf(rating);
        return result;
    }

    // setter for reviewer/reviewee probably not necessary as they should not be editable
    public void setUUID(String uuid) { this.uuid = uuid; }
    public void setRating(int rating) { this.rating = rating; }
    public void setReviewer(String reviewer) { this.reviewer = reviewer; }
    public void setReviewee(String reviewee) { this.reviewee = reviewee; }
    public void setComments(String comments) { this.comments = comments; }


}
