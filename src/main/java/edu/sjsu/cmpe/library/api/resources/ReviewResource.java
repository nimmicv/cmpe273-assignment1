package edu.sjsu.cmpe.library.api.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.params.LongParam;

import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Review;
import edu.sjsu.cmpe.library.dto.AuthorDto;
import edu.sjsu.cmpe.library.dto.BookDto;
import edu.sjsu.cmpe.library.dto.LinkDto;
import edu.sjsu.cmpe.library.dto.ReviewDto;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/books/{isbn}/reviews")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReviewResource {
	
	private BookResource BookRes;
	
	public ReviewResource(BookResource brs)
	{
		BookRes = brs;
	}
	
	@GET
	public Response getReviews(@PathParam("isbn") LongParam isbn)
	{
		Book book = BookRes.getBookRepository().getBookByISBN(isbn.get());
		if(book==null)
		{
			return Response.status(404).entity("Book not found").build();
		}
		List<Review> reviews = book.getReviews();
		Map<Object,Object> map = new HashMap<Object,Object>();
		map.put("reviews", reviews);
		map.put("links", new ArrayList<LinkDto>());
		return Response.status(200).entity(map).build();
		
		
		//return null;
	}
	@GET
    @Path("/{id}")
	public Response getReview(@PathParam("isbn") LongParam isbn,@PathParam("id") LongParam id)
	{
		Book book = BookRes.getBookRepository().getBookByISBN(isbn.get());
		if(book==null)
		{
			return Response.status(404).entity("Book not found").build();
		}
		List<Review> reviews = book.getReviews();
		
		long thisid =id.get();
		int newid = (int) (thisid-1);
		Review review = reviews.get(newid);
		if(review==null)
		{
			return Response.status(404).entity("Review not found").build();
		}
		ReviewDto reviewdto =new ReviewDto(review);
		//Map<Object,Object> map = new HashMap<Object,Object>();
		reviewdto.addLink(new LinkDto("view-Review", "/books/" + isbn.get() +"/reviews/"+thisid,
			"GET"));
		//map.put("review", authordto);
		//map.put("links", authordto.getLinks());
		//return reviewdto;
		return Response.status(200).entity(reviewdto).build();
		//return Response.status(200).entity(map).build();
		
		
		//return null;
	}
    @POST
    public Response createReview(@PathParam("isbn") LongParam isbn,Review review)
	{
    	Book book = BookRes.getBookRepository().getBookByISBN(isbn.get());
    	if(book==null)
		{
			return Response.status(404).entity("Book not found").build();
		}
    	if(review.getRating()<1 && review.getRating()>5)
    	{
    		return Response.status(200).entity("Rating is invalid or null").build();
    	}
    	if(review.getComment()==null)
    	{
    		return Response.status(200).entity("Comment cannot be null").build();
    	}
    		
    	int id = (book.getReviews().size())+1; 
        review.setId(id);
        book.getReviews().add(review);
    	LinkDto ldt = new LinkDto("create-book","/books"+isbn.get()+"/reviews/"+review.getId(),"POST");
    	Map<Object,Object> map = new HashMap<Object,Object>();
    	map.put("link", ldt);
    	return Response.status(201).entity(map).build();
		//return ldt;
	}
	    

}
