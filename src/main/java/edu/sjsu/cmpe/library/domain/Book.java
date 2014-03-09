package edu.sjsu.cmpe.library.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;




public class Book {
    private long isbn;
    @NotEmpty
    private String title;
    private String language;
    @JsonProperty("num-pages")
    private int num_pages; 
    @NotEmpty
    @JsonProperty("publication-date")
    private String publication_date; 
   
    private String status="available";
    @JsonIgnoreProperties
    private List<Author> authors= new ArrayList<Author>();
    @JsonIgnoreProperties
    private List<Review> reviews=new ArrayList<Review>();
    private Date lastupdated;
    
   public Date getLastupdated() {
		return lastupdated;
	}


	public void setLastupdated(Date lastupdated) {
		this.lastupdated = lastupdated;
	}




public boolean isvalidstatus(String status)
   {
	int flag =0;
	String[] values = {"available" , "lost","in-queue","checked-out"};
   	for(String s : values)
   	{
   		if(status.equals(s))
   		{
   			flag =1;
   			break;
   		}
   	}
   	if(flag == 0)
   	{
   		return false;
   	}
   	else
   	{
   		return true;
   	}
   }
    

    
    
    public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors =authors;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		
    
		this.status = status;
		
	}

	public String getPublication_date() {
		return publication_date;
	}
	
	public void setPublication_date(String publication_date) {
		this.publication_date = publication_date;
	}

	public int getNum_pages() {
		return num_pages;
	}

	public void setNum_pages(int num_pages) {
		this.num_pages = num_pages;
	}

	

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

    // add more fields here

    /**
     * @return the isbn
     */
    public long getIsbn() {
	return isbn;
    }

    /**
     * @param isbn
     *            the isbn to set
     */
    public void setIsbn(long isbn) {
	this.isbn = isbn;
    }

    /**
     * @return the title
     */

    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            the title to set
     */
    @NotEmpty
    public void setTitle(String title) {
	this.title = title;
    }
}
