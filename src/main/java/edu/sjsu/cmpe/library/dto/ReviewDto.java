package edu.sjsu.cmpe.library.dto;


import edu.sjsu.cmpe.library.domain.Review;

public class ReviewDto extends LinksDto{
	
	private Review review;

    /**
     * @param book
     */
    public ReviewDto(Review review) {
	super();
	this.review = review;
    }

    /**
     * @return the book
     */
    public Review getReview() {
	return review;
    }

    /**
     * @param book
     *            the book to set
     */
    public void setReview(Review review) {
	this.review = review;
    }

}

