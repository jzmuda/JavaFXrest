package pl.spring.demo.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import pl.spring.demo.enumerations.BookStatus;
import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;
import pl.spring.demo.web.utils.FileUtils;

/**
 * Tests of the book rest service
 * @author JZMUDA
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class BookRestServiceTest {

	@Autowired
	private BookService bookService;
	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;
/**
 * We mock the book service and database
 */
	@Before
	public void setUp() {
		Mockito.reset(bookService);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testShouldGetAllBooks() throws Exception {

		// given:
		final BookTo bookTo1 = new BookTo(1L, "title", "Author1", BookStatus.FREE);

		// register response for bookService.findAllBooks() mock
		Mockito.when(bookService.findAllBooks()).thenReturn(Arrays.asList(bookTo1));
		// when
		ResultActions response = this.mockMvc.perform(get("/all").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content("1"));

		response.andExpect(status().isOk())//
				.andExpect(jsonPath("[0].id").value(bookTo1.getId().intValue()))
				.andExpect(jsonPath("[0].title").value(bookTo1.getTitle()))
				.andExpect(jsonPath("[0].authors").value(bookTo1.getAuthors()));
	}

	@Test
	public void testShouldSaveBook() throws Exception {
		// given
		File file = FileUtils.getFileFromClasspath("classpath:pl/spring/demo/web/json/bookToSave.json");
		String json = FileUtils.readFileToString(file);
		// when
		ResultActions response = this.mockMvc.perform(put("/add").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json.getBytes()));
		// then
		response.andExpect(status().isOk());
	}
	
	@Test
	public void testShouldUpdateBook() throws Exception {
		// given
		File file = FileUtils.getFileFromClasspath("classpath:pl/spring/demo/web/json/bookToSave.json");
		String json = FileUtils.readFileToString(file);
		// when
		ResultActions response = this.mockMvc.perform(post("/update").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json.getBytes()));
		// then
		response.andExpect(status().isOk());
	}
	
	@Test
	public void testShouldDelete() throws Exception {
		// given
		Long id = 10L;
		// when
		ResultActions response = this.mockMvc.perform(delete("/delete?id="+id.toString()).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(id.toString()));
		// then
		response.andExpect(status().isOk());
		Mockito.verify(bookService).deleteBook(id);
	}
	
	@Test
	public void testShouldFindBookByTitle() throws Exception {
		//given
		String title = "Title";
		BookTo book = new BookTo(1L,title, "Author", BookStatus.FREE);
		Mockito.when(bookService.findBooksByTitle(title)).thenReturn(Arrays.asList(book));
		// when
		ResultActions response = this.mockMvc.perform(get("/books-by-title?titlePrefix=" + title)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON));
		// then
		response.andExpect(status().isOk())
		.andExpect(jsonPath("[0].id").value(book.getId().intValue()))
		.andExpect(jsonPath("[0].title").value(book.getTitle()))
		.andExpect(jsonPath("[0].authors").value(book.getAuthors()))
		.andExpect(jsonPath("[0].status").value(book.getStatus().name()));	
		Mockito.verify(bookService).findBooksByTitle(title);
	}
	
	@Test
	public void testShouldFindBookByAuthor() throws Exception {
		//given
		String author = "Author";
		BookTo book = new BookTo(1L,"Title", author, BookStatus.FREE);
		Mockito.when(bookService.findBooksByAuthor(author)).thenReturn(Arrays.asList(book));
		// when
		ResultActions response = this.mockMvc.perform(get("/books-by-author/?authorPrefix=" + author)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON));
		// then
		response.andExpect(status().isOk())
		.andExpect(jsonPath("[0].id").value(book.getId().intValue()))
		.andExpect(jsonPath("[0].title").value(book.getTitle()))
		.andExpect(jsonPath("[0].authors").value(book.getAuthors()))
		.andExpect(jsonPath("[0].status").value(book.getStatus().name()));
		Mockito.verify(bookService).findBooksByAuthor(author);
	}
	
	@Test
	public void testShouldFindBookByTitleAndAuthor() throws Exception {
		// given
		String authorPrefix="aut";
		String titlePrefix="tit";
		BookTo book = new BookTo(1L,"Title", "Author", BookStatus.FREE);
		Mockito.when(bookService.findBooksByAuthorOrTitle(authorPrefix, titlePrefix)).thenReturn(Arrays.asList(book));
		// when
		ResultActions response = this.mockMvc.perform(get("/books-by-author-and-title?authorPrefix="+authorPrefix+"&titlePrefix="+titlePrefix)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON));
		// then
		response.andExpect(status().isOk())
		.andExpect(jsonPath("[0].id").value(book.getId().intValue()))
		.andExpect(jsonPath("[0].title").value(book.getTitle()))
		.andExpect(jsonPath("[0].authors").value(book.getAuthors()))
		.andExpect(jsonPath("[0].status").value(book.getStatus().name()));
		Mockito.verify(bookService).findBooksByAuthorOrTitle(authorPrefix,titlePrefix);
	}
	
	@Test
	public void testEmptyMatrixsearchShouldFindAllBooks() throws Exception {
		// given
		BookTo book = new BookTo(1L,"Title", "Author", BookStatus.FREE);
		Mockito.when(bookService.findBooksByAuthorOrTitle("","")).thenReturn(Arrays.asList(book));
		// when
		ResultActions response = this.mockMvc.perform(get("/matrix/{}")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON));
		// then
		response.andExpect(status().isOk())
		.andExpect(jsonPath("[0].id").value(book.getId().intValue()))
		.andExpect(jsonPath("[0].title").value(book.getTitle()))
		.andExpect(jsonPath("[0].authors").value(book.getAuthors()))
		.andExpect(jsonPath("[0].status").value(book.getStatus().name()));
		Mockito.verify(bookService).findBooksByAuthorOrTitle("","");
	}
	
	@Test
	public void testAuthorMatrixsearchShouldFindBookByAutor() throws Exception {
		// given
		BookTo book = new BookTo(1L,"Title", "Author", BookStatus.FREE);
		Mockito.when(bookService.findBooksByAuthorOrTitle(book.getAuthors(),"")).thenReturn(Arrays.asList(book));
		// when
		ResultActions response = this.mockMvc.perform(get("/matrix/authors="+book.getAuthors())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON));
		// then
		response.andExpect(status().isOk())
		.andExpect(jsonPath("[0].id").value(book.getId().intValue()))
		.andExpect(jsonPath("[0].title").value(book.getTitle()))
		.andExpect(jsonPath("[0].authors").value(book.getAuthors()))
		.andExpect(jsonPath("[0].status").value(book.getStatus().name()));
		Mockito.verify(bookService).findBooksByAuthorOrTitle(book.getAuthors(),"");
	}
	
	@Test
	public void testAuthorMatrixsearchShouldFindBookByAutorAndTitle() throws Exception {
		// given
		BookTo book = new BookTo(1L,"Title", "Author", BookStatus.FREE);
		Mockito.when(bookService.findBooksByAuthorOrTitle(book.getAuthors(),book.getTitle())).thenReturn(Arrays.asList(book));
		// when
		ResultActions response = this.mockMvc.perform(get("/matrix/title="+book.getTitle()+";authors="+book.getAuthors())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON));
		// then
		response.andExpect(status().isOk())
		.andExpect(jsonPath("[0].id").value(book.getId().intValue()))
		.andExpect(jsonPath("[0].title").value(book.getTitle()))
		.andExpect(jsonPath("[0].authors").value(book.getAuthors()))
		.andExpect(jsonPath("[0].status").value(book.getStatus().name()));
		Mockito.verify(bookService).findBooksByAuthorOrTitle(book.getAuthors(),book.getTitle());
	}
	
	@Test
	public void testAuthorMatrixsearchShouldFindBookByTitle() throws Exception {
		// given
		BookTo book = new BookTo(1L,"Title1", "Author1", BookStatus.FREE);
		Mockito.when(bookService.findBooksByAuthorOrTitle("",book.getTitle())).thenReturn(Arrays.asList(book));
		// when
		ResultActions response = this.mockMvc.perform(get("/matrix/title="+book.getTitle())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON));
		// then
		response.andExpect(status().isOk())
		.andExpect(jsonPath("[0].id").value(book.getId().intValue()))
		.andExpect(jsonPath("[0].title").value(book.getTitle()))
		.andExpect(jsonPath("[0].authors").value(book.getAuthors()))
		.andExpect(jsonPath("[0].status").value(book.getStatus().name()));
		Mockito.verify(bookService).findBooksByAuthorOrTitle("",book.getTitle());
	}
}