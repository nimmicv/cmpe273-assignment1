package edu.sjsu.cmpe.library.dto;

import edu.sjsu.cmpe.library.domain.Author;

public class AuthorDto extends LinksDto{
	
	private Author author;

    /**
     * @param book
     */
    public AuthorDto(Author author) {
	super();
	this.author = author;
    }

    /**
     * @return the book
     */
    public Author getAuthor() {
	return author;
    }

    /**
     * @param book
     *            the book to set
     */
    public void setAuthor(Author author) {
	this.author = author;
    }

}
