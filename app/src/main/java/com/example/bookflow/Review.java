package com.example.bookflow;

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
