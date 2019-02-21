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

    public Review(User reviewer, User reviewee, String comments, Integer rating) {
        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.comments = comments;
        this.rating = rating;
    }

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

    public void setRating(Integer rating) { this.rating = rating; }
    public void setReviewer(User reviewer) { this.reviewer = reviewer; }
    public void setReviewee(User reviewee) { this.reviewee = reviewee; }
    public void setComments(String comments) { this.comments = comments; }


}
