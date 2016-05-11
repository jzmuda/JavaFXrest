package pl.spring.demo.web.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.beans.PropertyEditor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import pl.spring.demo.constants.ModelConstants;
import pl.spring.demo.constants.ViewNames;
import pl.spring.demo.controller.BookController;
import pl.spring.demo.enumerations.BookStatus;
import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;
/**
 * Tests of the book service
 * @author JZMUDA
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "controller-test-configuration.xml")
public class BookControllerTest {

	@Autowired
	private BookService bookService;
	@Autowired
	private BookController bookController;
	
	private MockMvc mockMvc;
	/**
	 * We mock the book service and database. Optional mock of controller
	 */
	@Before
	public void setup() {

		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		mockMvc = MockMvcBuilders.standaloneSetup(new BookController()).setViewResolvers(viewResolver).build();
		Mockito.reset(bookService);
	}
	/**
	 * Only test with mock controller (by mmotowid)
	 * @throws Exception
	 */

	@Test
	public void testDummyAddBook() throws Exception {
		// given when
		ResultActions resultActions = mockMvc.perform(get("/books/add"));
		// then
		resultActions.andExpect(view().name(ViewNames.ADD_BOOK));
	}
	
	@Test
	public void testAllBooks() throws Exception {
		// given
		BookTo book = new BookTo(1L,"Title", "Author", BookStatus.FREE);
		Mockito.when(bookService.findAllBooks()).thenReturn(Arrays.asList(book));
		//when
		ModelAndView mav= bookController.allBooks();
		//then
		assertEquals(ViewNames.BOOKS,mav.getViewName());
		Map<String, Object> viewMap=  mav.getModel();
		String info=(String) viewMap.get(ModelConstants.INFO);
		assertNotNull(info);
		assertTrue(info.length()>0);
		List<BookTo> bookList=(List<BookTo>) viewMap.get(ModelConstants.BOOK_LIST);
		assertEquals(1, bookList.size());
		assertEquals(book, bookList.get(0));
	}
	
	@Test
	public void testShouldFindBookByAuthorAndTitle() throws Exception {
		// given
		String authorPrefix="aut";
		String author=authorPrefix+"hor";
		String titlePrefix="tit";
		String title=titlePrefix+"le";
		BookTo book = new BookTo(1L,title, author, BookStatus.FREE);
		Mockito.when(bookService.findBooksByAuthorOrTitle(authorPrefix, titlePrefix)).thenReturn(Arrays.asList(book));
		//when
		ModelAndView mav= bookController.searchBooks(authorPrefix,titlePrefix);
		//then
		assertEquals(ViewNames.BOOKS,mav.getViewName());
		Map<String, Object> viewMap=  mav.getModel();
		String info=(String) viewMap.get(ModelConstants.INFO);
		assertNotNull(info);
		assertTrue(info.length()>0);
		List<BookTo> bookList=(List<BookTo>) viewMap.get(ModelConstants.BOOK_LIST);
		assertEquals(1, bookList.size());
		assertEquals(book, bookList.get(0));
	}
	@Test
	public void testShouldFindBookByTitle() throws Exception {
		// given
		String authorPrefix="aut";
		String author=authorPrefix+"hor";
		String titlePrefix="tit";
		String title=titlePrefix+"le";
		BookTo book = new BookTo(1L,title, author, BookStatus.FREE);
		Mockito.when(bookService.findBooksByAuthorOrTitle("", titlePrefix)).thenReturn(Arrays.asList(book));
		//when
		ModelAndView mav= bookController.searchBooks("",titlePrefix);
		//then
		assertEquals(ViewNames.BOOKS,mav.getViewName());
		Map<String, Object> viewMap=  mav.getModel();
		String info=(String) viewMap.get(ModelConstants.INFO);
		assertNotNull(info);
		assertTrue(info.length()>0);
		List<BookTo> bookList=(List<BookTo>) viewMap.get(ModelConstants.BOOK_LIST);
		assertEquals(1, bookList.size());
		assertEquals(book, bookList.get(0));
	}
	
	@Test
	public void testShouldFindBookByAuthor() throws Exception {
		// given
		String authorPrefix="aut";
		String author=authorPrefix+"hor";
		String titlePrefix="tit";
		String title=titlePrefix+"le";
		BookTo book = new BookTo(1L,title, author, BookStatus.FREE);
		Mockito.when(bookService.findBooksByAuthorOrTitle(authorPrefix, "")).thenReturn(Arrays.asList(book));
		//when
		ModelAndView mav= bookController.searchBooks(authorPrefix,"");
		//then
		assertEquals(ViewNames.BOOKS,mav.getViewName());
		Map<String, Object> viewMap=  mav.getModel();
		String info=(String) viewMap.get(ModelConstants.INFO);
		assertNotNull(info);
		assertTrue(info.length()>0);
		List<BookTo> bookList=(List<BookTo>) viewMap.get(ModelConstants.BOOK_LIST);
		assertEquals(1, bookList.size());
		assertEquals(book, bookList.get(0));
	}
	
	@Test
	public void testShouldReturnRedirectToAddBook() throws Exception {
		// given
		String authorPrefix="aut";
		String author=authorPrefix+"hor";
		String titlePrefix="tit";
		String title=titlePrefix+"le";
		BookTo book = new BookTo(1L,title, author, BookStatus.FREE);
		//when
		String redirect= bookController.goToAddNewBook(book);
		//then
		assertEquals(ViewNames.ADD_BOOK,redirect);
	}
	
	@Test
	public void testShouldAddBook() throws Exception {
		// given
		String authorPrefix="aut";
		String author=authorPrefix+"hor";
		String titlePrefix="tit";
		String title=titlePrefix+"le";
		BookTo book = new BookTo(1L,title, author, BookStatus.FREE);
		BindingResult bindingResult = null;
		//when
		String result= bookController.addNewBook(book, bindingResult);
		//then
		assertEquals("redirect:/"+ViewNames.BOOKS,result);
		Mockito.verify(bookService).saveBook(book);
	}
	
	@Test
	public void testShouldReturnRedirectDeleteBook() throws Exception {
		// given
		//when
		ModelAndView mav= bookController.goToDeleteBook();
		//then
		assertEquals(ViewNames.DELETE_BOOK,mav.getViewName());
		Map<String, Object> viewMap=  mav.getModel();
		String info=(String) viewMap.get(ModelConstants.INFO);
		assertNotNull(info);
		assertTrue(info.length()>0);
	}
	
	@Test
	public void testShouldDeleteBook() throws Exception {
		// given
		String idToDelete="1";
		//when
		ModelAndView mav= bookController.deleteBook(idToDelete);
		//then
		assertEquals(ViewNames.BOOKS,mav.getViewName());
		Map<String, Object> viewMap=  mav.getModel();
		String info=(String) viewMap.get(ModelConstants.INFO);
		assertNotNull(info);
		assertTrue(info.length()>0);
		Mockito.verify(bookService).deleteBook(Long.parseLong(idToDelete));
	}
}
