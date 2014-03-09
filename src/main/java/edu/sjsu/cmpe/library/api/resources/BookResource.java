package edu.sjsu.cmpe.library.api.resources;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Request;





import javax.ws.rs.core.Response.ResponseBuilder;

import java.util.*;

import com.google.common.base.Optional;
import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.library.domain.Author;
import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Review;
import edu.sjsu.cmpe.library.dto.BookDto;
import edu.sjsu.cmpe.library.dto.LinkDto;
import edu.sjsu.cmpe.library.dto.LinksDto;
import edu.sjsu.cmpe.library.dto.ReviewDto;
import edu.sjsu.cmpe.library.error.ErrorMessage;
import edu.sjsu.cmpe.library.repository.BookRepository;
import edu.sjsu.cmpe.library.repository.BookRepositoryInterface;

@Path("/v1/books/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    /** bookRepository instance */
    private final BookRepositoryInterface bookRepository;

    /**
     * BookResource constructor
     * 
     * @param bookRepository
     *            a BookRepository instance
     */
    public BookResource(BookRepositoryInterface bookRepository) {
	this.bookRepository = bookRepository;
    }
    
    public BookRepositoryInterface getBookRepository()
    {
    	return this.bookRepository;
    }

    @GET
    @Path("/{isbn}")
    @Timed(name = "view-book")
    public Response getBookByIsbn(@Context HttpHeaders httpHeaders,@Context Request request,@PathParam("isbn") LongParam isbn) {
    	
    	httpHeaders.getRequestHeaders();
    	
	Book book = bookRepository.getBookByISBN(isbn.get());
	if(book==null)
	{
		return Response.status(404).entity("Book not Found").build();
	}
	EntityTag etag = computeEtagForBook(book); 
	//Date expirationDate = new Date(System.currentTimeMillis() + 3000);
	
	Response.ResponseBuilder responseBuilder = request.evaluatePreconditions(book.getLastupdated());

    if (responseBuilder != null) {
      // Etag match
     //("Book has not changed..returning unmodified response code");
    	responseBuilder.entity("Fetch from cache");
      return responseBuilder.build();
    }
	BookDto bookResponse = new BookDto(book);
	bookResponse.addLink(new LinkDto("view-book", "/books/" + book.getIsbn(),
		"GET"));
	bookResponse.addLink(new LinkDto("update-book",
		"/books/" + book.getIsbn(), "POST"));
	bookResponse.addLink(new LinkDto("delete-book",
			"/books/" + book.getIsbn(), "DELETE"));
	bookResponse.addLink(new LinkDto("create-review",
			"/books/" + book.getIsbn() +"/reviews", "POST"));
	bookResponse.addLink(new LinkDto("view-all-reviews",
			"/books/" + book.getIsbn() +"/reviews", "GET"));
	// add more links
	List<Review> reviews  = book.getReviews();
	LinksDto reviewldto = new LinksDto();
	for(Review r :reviews)
	{
		
		reviewldto.addLink(new LinkDto("view-review","/books"+isbn.get()+"/reviews/"+r.getId(),"GET"));
	}
	List<Author> authors  = book.getAuthors();
	LinksDto authldto = new LinksDto();
	for(Author ar :authors)
	{
		
		authldto.addLink(new LinkDto("view-Author","/books"+isbn.get()+"/authors/"+ar.getId(),"GET"));
	}
	Map<Object,Object> map = new HashMap<Object,Object>();
	map.put("isbn", book.getIsbn());
	map.put("title", book.getTitle());
	map.put("publication-date", book.getPublication_date());
	map.put("language", book.getLanguage());
	map.put("num-pages", book.getNum_pages());
	map.put("status", book.getStatus());
	map.put("reviews", reviewldto.getLinks());
	map.put("authors", authldto.getLinks());
	Map<Object,Object> Responsemap = new HashMap<Object,Object>();
	Responsemap.put("books", map);
	Responsemap.put("links", bookResponse.getLinks());
	responseBuilder = Response.ok(Responsemap).tag(etag);
	return responseBuilder.build();
    }
    
    private EntityTag computeEtagForBook(Book book) {
        return new EntityTag(""+book.getLastupdated().getTime());// + book.getLastupdated().getTime());
      }

    @POST
    @Timed(name = "create-book")
    @NotNull(message = "{request.empty.means}")
    public Response createBook(@NotNull Book request) {
    	int errorFlag =0;
    	Map<Object,Object> errorMap = new HashMap<Object,Object>();
    	errorMap.put("Error Code" , 400);
    	
    if(request.getTitle()==null)
    {
    	errorMap.put("Title", ErrorMessage.titleEmpty);
    	errorFlag =1;
    	
    	
    }
    if(request.getPublication_date()==null)
    {
    	errorMap.put("Date", ErrorMessage.pubDateEmpty);
    	errorFlag=1;
    	
    }
    if(request.getStatus()!=null)
    {
    	String status = request.getStatus();
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
    	if(flag==0)
    	{
    		errorFlag=1;
    		errorMap.put("Status", ErrorMessage.invalidStatus);
    		
    	}
    	
    }
    if(errorFlag==1)
    {
    	return Response.status(400).entity(errorMap).build();
    	
    }
	// Store the new book in the BookRepository so that we can retrieve it.
    	
	Book savedBook = bookRepository.saveBook(request);

	String location = "/books/" + savedBook.getIsbn();
	BookDto bookResponse = new BookDto(savedBook);
	bookResponse.addLink(new LinkDto("view-book", "/books/" + savedBook.getIsbn(),
			"GET"));
		bookResponse.addLink(new LinkDto("update-book",
			"/books/" + savedBook.getIsbn(), "PUT"));
		bookResponse.addLink(new LinkDto("delete-book",
				"/books/" + savedBook.getIsbn(), "DELETE"));
		bookResponse.addLink(new LinkDto("create-review",
				"/books/" + savedBook.getIsbn() +"/reviews", "POST"));
		if(savedBook.getReviews().size()>0)
		{
		bookResponse.addLink(new LinkDto("view-all-reviews",
				"/books/" + savedBook.getIsbn() +"/reviews", "GET"));
		}
	// Add other links if needed
		
	Map<String,List<LinkDto>> map = new HashMap<String,List<LinkDto>>();
	map.put("Links", bookResponse.getLinks());

	return Response.status(201).entity(map).build();
    	
    }
    
    
    @DELETE
    @Path("/{isbn}")
    @Timed(name = "delete-book")
    @NotNull(message = "{book.does.not.exist}")
    public Response deleteBook(@PathParam("isbn") LongParam isbn)
    {
    	boolean bn = bookRepository.deleteBook(isbn.get());
    	if(!bn)
    	{
    		return Response.status(404).entity("Book Not Found").build();
    	}
		LinkDto ldt = new LinkDto("create-book","/books","POST");
		Map<String,LinkDto> map = new HashMap<String,LinkDto>();
		map.put("links", ldt);
		return Response.status(200).entity(map).build();
    	
    }
    
    @PUT
    @Path("/{isbn}")
    @Timed(name = "update-book")
    public Response updateBook(@PathParam("isbn") LongParam isbn,@QueryParam("status") String status)
    {
    	int errorFlag =0;
    	Map<Object,Object> errorMap = new HashMap<Object,Object>();
    	errorMap.put("Error Code" , 400);
    	Book book;
    	BookDto bookResponse;
    	if(status !=null)
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
        	if(flag==0)
        	{
        		errorMap.put("Status", "Invalid Ststus");
        		return Response.status(400).entity(errorMap).build();
        		
        	}
    	book = bookRepository.updateBook(isbn.get(), status);
    	bookResponse = new BookDto(book);
    	
    	
    		bookResponse.addLink(new LinkDto("view-book", "/books/" + book.getIsbn(),
    			"GET"));
    		bookResponse.addLink(new LinkDto("update-book",
    			"/books/" + book.getIsbn(), "PUT"));
    		bookResponse.addLink(new LinkDto("delete-book",
    				"/books/" + book.getIsbn(), "DELETE"));
    		bookResponse.addLink(new LinkDto("create-review",
    				"/books/" + book.getIsbn() +"/reviews", "POST"));
    		if(book.getReviews().size()>0)
    		{
    		bookResponse.addLink(new LinkDto("view-all-reviews",
    				"/books/" + book.getIsbn() +"/reviews", "GET"));
    		}
    		Map<String,List<LinkDto>> map = new HashMap<String,List<LinkDto>>();
    		map.put("Links", bookResponse.getLinks());

    		return Response.status(200).entity(map).build();
    	}
    	else
    	{
    		errorMap.put("Status", "Nothing to update");
    		return Response.status(400).entity(errorMap).build();
    	}
    }
}

