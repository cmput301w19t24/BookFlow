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
//    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    public Review() {

    }

    public Review(String comments, int rating, String reviewee, String reviewer, String uuid) {
        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.comments = comments;
        this.rating = rating;
        this.uuid = uuid;
    }

//    public Review(User reviewer, User reviewee, String comments, int rating, String uuid) {
//        this.reviewer = reviewer.getUid();
//        this.reviewee = reviewee.getUid();
//        this.comments = comments;
//        this.rating = rating;
//        this.uuid = uuid;
//    }

    public String getReviewerID() {
//        db.getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = (User)dataSnapshot.child(reviewer).getValue(User.class);
//                re_er.setUser(user);
//                return;
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        return re_er;
        return reviewer;
    }

    public String getReviewee() {
//        db.getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = (User)dataSnapshot.child(reviewee).getValue(User.class);
//                re_ee.setUser(user);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        return re_ee;
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
