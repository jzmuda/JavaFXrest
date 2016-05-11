package pl.spring.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pl.spring.demo.constants.ModelConstants;
import pl.spring.demo.constants.ViewNames;
import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

/**
 * Book controller
 * 
 * @author mmotowid
 *
 */
@Controller
@RequestMapping("/books")
public class BookController {
	@Autowired
	private BookService bookService;
	
	private static final String INFO_TEXT = "All titles in our service";
	
	@RequestMapping
	public String list(Model model) {
		model.addAttribute(ModelConstants.BOOK_LIST, bookService.findAllBooks());
		model.addAttribute(ModelConstants.INFO, INFO_TEXT);
		return ViewNames.BOOKS;
	}

	/**
	 * Method collects info about all books
	 */
	@RequestMapping("/all")
	public ModelAndView allBooks() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(ModelConstants.BOOK_LIST, bookService.findAllBooks());
		modelAndView.addObject(ModelConstants.INFO, "All titles in our service");	
		modelAndView.setViewName(ViewNames.BOOKS);
		return modelAndView;
	}


	/**
	 * Book search by author and title
	 * 
	 */
	
	@RequestMapping("/search")
	public ModelAndView searchBooks(@RequestParam("authorPrefix") String authorPrefix,@RequestParam("titlePrefix") String titlePrefix) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(ViewNames.BOOKS);
		String criteria="Items matching ";
		if(!authorPrefix.isEmpty())
			criteria+="Author = "+authorPrefix;
		if(!titlePrefix.isEmpty())
			criteria+="Title = "+titlePrefix;
		modelAndView.addObject(ModelConstants.INFO, criteria);
		modelAndView.addObject(ModelConstants.BOOK_LIST, bookService.findBooksByAuthorOrTitle(authorPrefix,titlePrefix));
		return modelAndView;
	}
	
	/**
	 * Redirects to the "add" view
	 */
	
	@RequestMapping(value="/add", method = RequestMethod.GET)
	public String goToAddNewBook(@ModelAttribute("newBook") BookTo newBook) {
		return ViewNames.ADD_BOOK;
	}
	
	/**
	 * Here you save a book through the service
	 */

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addNewBook(@ModelAttribute("newBook") BookTo newBook, BindingResult result) {
		bookService.saveBook(newBook);
		return "redirect:/"+ViewNames.BOOKS;
	}
	
	/**
	 * Redirects to the "delete" view
	 */
		
	@RequestMapping("/delete")
	public ModelAndView goToDeleteBook() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(ModelConstants.INFO, "Here you delete a book");
		modelAndView.setViewName(ViewNames.DELETE_BOOK);
		return modelAndView;
	}
	
	/**
	 * Deletes one book with given ID
	 */
	
	@RequestMapping(value= "/deleteID")
	public ModelAndView deleteBook(@RequestParam("ID") String ID) {
		boolean deleted=true;
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(ViewNames.BOOKS);
		try {
			bookService.deleteBook(Long.parseLong(ID));
		}
		catch (Exception e) {
			deleted=false;
		}
		if(deleted){
			modelAndView.addObject(ModelConstants.INFO, "Deleted Book with ID="+ID);
		}
		else
			modelAndView.addObject(ModelConstants.INFO, "Book with requested ID="+ID+" does not exist");
		modelAndView.addObject(ModelConstants.BOOK_LIST, bookService.findAllBooks());
		return modelAndView;
	}

	/**
	 * Binder initialization
	 */
	@InitBinder
	public void initialiseBinder(WebDataBinder binder) {
		binder.setAllowedFields("id", "title", "authors", "status");
	}

}
