package com.starterkit.javafx.bookprovider.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.starterkit.javafx.bookprovider.BookProvider;
import com.starterkit.javafx.dataprovider.data.BookStatusVO;
import com.starterkit.javafx.dataprovider.data.BookVO;
/**
 * provides book list from rest service requests to local server
 * @author JZMUDA
 *
 */
public class BookProviderImpl implements BookProvider {
	
	private String url = "";
	private static final Logger LOG = Logger.getLogger(BookProviderImpl.class);
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	
	@Override
	public Collection<BookVO> findBooks(String authors, String title) throws ProtocolException, IOException {
		//we do not like null values, let empty be default
		title = unNull(title);
		authors = unNull(authors);
		
		String searchUrl = url+"matrix/authors="+authors+";title="+title;

		URL obj = new URL(searchUrl);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", null);
		
		InputStream inputStream = con.getInputStream();
		
		CollectionType constructCollectionType = objectMapper.getTypeFactory().constructCollectionType(List.class,
				BookVO.class);
		return (List<BookVO>) extractBook(inputStream, constructCollectionType);
	}
	
	private List<BookVO> extractBook(InputStream inputStream, CollectionType constructCollectionType) {
		Object value = null;
		try {
			value = objectMapper.readValue(inputStream, constructCollectionType);
		} catch (IOException e) {
			throw new IllegalStateException("Corrupt data: could not parse");
		}
		return value instanceof List ? (List<BookVO>) value:new ArrayList<BookVO>();
	}

	@Override
	public void addBook(String author, String title, BookStatusVO status) {
		BookVO book = new BookVO(0L,author,title,status);
		String bookJSON = "";
		try {
			bookJSON = new ObjectMapper().writeValueAsString(book);
			LOG.debug("json created");
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Add method EROR: could not create JSON");
		}

		String addUrl = url+"update";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(addUrl);
		postRequest.setHeader("Content-Type", "application/json");
		try {
			postRequest.setEntity(new StringEntity(bookJSON));
			LOG.debug("json added");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Add method EROR: could not parse data to POST");
		}
		HttpResponse response2;
		try {
			response2 = client.execute(postRequest);
			LOG.debug("json posted under "+addUrl);
		} catch (IOException e) {
			throw new IllegalStateException("Add method EROR: POST request failure");
		}		
	}

	@Override
	public Boolean checkURL(String url) {
		URL urlToCheck;
		try {
			urlToCheck = new URL(url);
			HttpURLConnection huc;
			try {
				huc = (HttpURLConnection)  urlToCheck.openConnection();
				huc.setRequestMethod("HEAD"); 
			    huc.connect();
			    huc.disconnect();
			} catch (IOException e) {
				this.url="";
				throw new IllegalStateException("Bad URL");
			}
		} catch (MalformedURLException e1) {
			this.url="";
			throw new IllegalStateException("Bad URL");
		}
		this.url=url;
		return true;
	}
	
	public String unNull(String text) {
		if(text == null){
			return "";
		}
		else return text;
	}

}
