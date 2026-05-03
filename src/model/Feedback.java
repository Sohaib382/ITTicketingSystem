package model;

public class Feedback {
    private int rating;
    private String comments;

    public Feedback(int rating, String comments) {
        if (rating < 1 || rating > 5)
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        this.rating = rating;
        this.comments = comments;
    }

    public int getRating()     { return rating; }
    public String getComments(){ return comments; }

    @Override
    public String toString() {
        return "Rating: " + rating + "/5 — " + comments;
    }
}