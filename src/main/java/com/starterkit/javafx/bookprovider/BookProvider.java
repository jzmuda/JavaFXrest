package com.starterkit.javafx.bookprovider;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.starterkit.javafx.bookprovider.impl.BookProviderImpl;
import com.starterkit.javafx.dataprovider.data.BookVO;
import com.starterkit.javafx.dataprovider.data.BookStatusVO;

/**
 * Provides book data.
 *
 * @author Jakub/Leszek
 */
public interface BookProvider {

	/**
	 * Instance of this interface.
	 */
	BookProvider INSTANCE = new BookProviderImpl();

	/**
	 * Finds books with their name containing specified author and/or given
	 * title.
	 *
	 * @param author
	 *            string contained in author
	 * @param title
	 *            string contained in title
	 * @return collection of books matching the given criteria
	 * @throws MalformedURLException 
	 * @throws IOException 
	 * @throws ProtocolException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Collection<BookVO> findBooks(String authors, String title) throws ProtocolException, IOException;
	/**
	 * Adds a new book with required fields
	 *
	 * @param author
	 * @param title
	 * @param status
	 *           
	 */
	void addBook(String author, String title, BookStatusVO status) throws Exception;
	Boolean checkURL(String url);
}
