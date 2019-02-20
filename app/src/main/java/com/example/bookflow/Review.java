/**
 * author: Shengyao Lu
 * date: 2019/2/20
 */
package com.example.bookflow;
/**
 * This class models the relationship between two users with book
 */
class Review {
    private User reviewer;
    private User reviewee;
    private String comments;
    private Integer rating;

    public User getReviewer() {
        return reviewer;
    }

    public User getReviewee() {
        return reviewee;
    }

    public String getComments() {
        return comments;
    }

    public Integer getRating() {
        return rating;
    }
}
