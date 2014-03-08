package edu.sjsu.cmpe.library.repository;

import edu.sjsu.cmpe.library.domain.Author;
import edu.sjsu.cmpe.library.domain.Book;

public class AuthorRepository implements AuthorRepositoryInterface{
	
	private int authorid;
	
	public AuthorRepository()
	{
		authorid =0;
		
	}
	
	 private final int generateAuthorId() {
	    	// increment existing isbnKey and return the new value
	    	return (int) (++authorid);
	   }
	
	 @Override
	 public Author saveAuthor(Author author) {
		author.setId(generateAuthorId());
		return author;
		
	}

}
