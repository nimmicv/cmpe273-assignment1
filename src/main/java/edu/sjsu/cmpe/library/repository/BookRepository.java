package edu.sjsu.cmpe.library.repository;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import edu.sjsu.cmpe.library.domain.Author;
import edu.sjsu.cmpe.library.domain.Book;

public class BookRepository implements BookRepositoryInterface {
    /** In-memory map to store books. (Key, Value) -> (ISBN, Book) */
    private final ConcurrentHashMap<Long, Book> bookInMemoryMap;

    /** Never access this key directly; instead use generateISBNKey() */
    private long isbnKey;
   
    

    public BookRepository(ConcurrentHashMap<Long, Book> bookMap) {
	checkNotNull(bookMap, "bookMap must not be null for BookRepository");
	bookInMemoryMap = bookMap;
	isbnKey = 0;
    }

    /**
     * This should be called if and only if you are adding new books to the
     * repository.
     * 
     * @return a new incremental ISBN number
     */
    private final Long generateISBNKey() {
	// increment existing isbnKey and return the new value
	return Long.valueOf(++isbnKey);
    }
    
   

    /**
     * This will auto-generate unique ISBN for new books.
     */
    @Override
    public Book saveBook(Book newBook) {
	checkNotNull(newBook, "newBook instance must not be null");
	// Generate new ISBN
	Long isbn = generateISBNKey();
	AuthorRepository ar = new AuthorRepository();
	if(newBook.getAuthors()!=null)
	{
		int length = newBook.getAuthors().size();
		for(int i=0;i<length;i++)
		{
			Author author = newBook.getAuthors().get(i);
			ar.saveAuthor(author);
		}
	}
	newBook.setIsbn(isbn);
	newBook.setLastupdated(new Date());
	// TODO: create and associate other fields such as author

	// Finally, save the new book into the map
	bookInMemoryMap.putIfAbsent(isbn, newBook);

	return newBook;
    }
    
    public Book updateBook(Long isbn,String status)
    {
    	Book bk = bookInMemoryMap.get(isbn);
    	if(bk != null)
    	{
    		bk.setStatus(status);
    		bk.setLastupdated(new Date());
    		return bk;
    	}
    	else
    		return null;
    }

    public boolean deleteBook(Long isbn)
    {
    	Book bk = bookInMemoryMap.get(isbn);
    	if(bk != null)
    	{
    		bookInMemoryMap.remove(isbn);
    		return true;
    	}
    	else
    		return false;
    	
    }
    /**
     * @see edu.sjsu.cmpe.library.repository.BookRepositoryInterface#getBookByISBN(java.lang.Long)
     */
    @Override
    public Book getBookByISBN(Long isbn) {
	checkArgument(isbn > 0,
		"ISBN was %s but expected greater than zero value", isbn);
	return bookInMemoryMap.get(isbn);
    }

}
