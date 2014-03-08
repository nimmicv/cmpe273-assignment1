package edu.sjsu.cmpe.library.api.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.yammer.dropwizard.jersey.params.LongParam;

import edu.sjsu.cmpe.library.domain.Author;
import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Review;
import edu.sjsu.cmpe.library.dto.AuthorDto;
import edu.sjsu.cmpe.library.dto.LinkDto;

@Path("/v1/books/{isbn}/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
	
private BookResource BookRes;
	
	public AuthorResource(BookResource brs)
	{
		BookRes = brs;
	}
	
	@GET
	public Response getAuthors(@PathParam("isbn") LongParam isbn)
	{
		Book book = BookRes.getBookRepository().getBookByISBN(isbn.get());
		List<Author> authors = book.getAuthors();
		Map<Object,Object> map = new HashMap<Object,Object>();
		map.put("authors", authors);
		map.put("links", new ArrayList<LinkDto>());
		return Response.status(200).entity(map).build();
		
		//return null;
	}
	@GET
    @Path("/{id}")
	public AuthorDto getAuthor(@PathParam("isbn") LongParam isbn,@PathParam("id") LongParam id)
	{
		Book book = BookRes.getBookRepository().getBookByISBN(isbn.get());
		List<Author> authors = book.getAuthors();
		
		long thisid =id.get();
		int newid = (int) thisid;
		Author author = authors.get(newid);
		AuthorDto authordto =new AuthorDto(author);
		//Map<Object,Object> map = new HashMap<Object,Object>();
		authordto.addLink(new LinkDto("view-Review", "/books/" + isbn.get() +"/authors/"+newid,
			"GET"));
		//map.put("author", author);
		//map.put("links", authordto.getLinks());
		return authordto;
		//return Response.status(200).entity(map).build();
		
		//return null;
	}

}
