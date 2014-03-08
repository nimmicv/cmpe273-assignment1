package edu.sjsu.cmpe.library.domain;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Range;

public class Review {
	
	private long id;
	@Range(min=1,max=5)
	private long rating;
	private String comment;
	
	public long getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	

}
