package pl.spring.demo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.ui.Model;

import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

import java.util.Map;
import java.util.Set;



@Controller
@ResponseBody
public class BookRestService {

	@Autowired
	private BookService bookService;

	/**
	 * gets a list of all books (get)
	 */
	
	@RequestMapping(value="/all", method=RequestMethod.GET)
		public List<BookTo> getAll(){
		return bookService.findAllBooks();
	}
	
	/**
	 * adds a book to list (put)
	 */
	
	@RequestMapping(value="/add", method=RequestMethod.PUT)
	public BookTo addBook(@RequestBody BookTo book){
		return bookService.saveBook(book);
	}
	
	/**
	 * updates a book with given ID. Returns true in case of success (post)
	 */
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public BookTo updateBook(@RequestBody BookTo book){
		return bookService.saveBook(book);
	}
	
	/**
	 * Deletes a book with given id, returns true if successful (delete).
	 */
	
	@RequestMapping(value="/delete", method=RequestMethod.DELETE)
	public void deleteBook(@RequestParam String id){
		bookService.deleteBook(Long.parseLong(id));
		}
	
	/**
	 * search books by title prefix
	 */
	
	@RequestMapping(value="/books-by-title", method=RequestMethod.GET)
	public List<BookTo> searchByTitle(@RequestParam String titlePrefix){
	return bookService.findBooksByTitle(titlePrefix);
	}
	
	/**
	 * search books by author prefix
	 */
	
	@RequestMapping(value="/books-by-author", method=RequestMethod.GET)
	public List<BookTo> searchByAuthor(@RequestParam String authorPrefix){
		return bookService.findBooksByAuthor(authorPrefix);
	}
	
	/**
	 * search books by author and title prefixes by RequestBody
	 */
	
	@RequestMapping(value="/books-by-author-and-title", method=RequestMethod.GET)
	public List<BookTo> searchByAuthorAndTitle(@RequestParam String authorPrefix, @RequestParam String titlePrefix){
		return bookService.findBooksByAuthorOrTitle(authorPrefix,titlePrefix);
	}
	
	/**
	 * Search books by a matrix parameterization. Can search by author or by title or show all books
	 */
	@RequestMapping(value="/matrix/{conditions}", method=RequestMethod.GET)
	public List<BookTo> matrixSearch(@MatrixVariable(pathVar="conditions") Map<String, List<String>> conditions, Model model){
		Set<String> searchTerms=conditions.keySet();
		String author="";
		if(searchTerms.contains("authors")&&conditions.get("authors").size()>0)
			author+=conditions.get("authors").get(0);
		String title="";
		if(searchTerms.contains("title")&&conditions.get("title").size()>0)
			title+=conditions.get("title").get(0);
		return  bookService.findBooksByAuthorOrTitle(author,title);
	}
}
