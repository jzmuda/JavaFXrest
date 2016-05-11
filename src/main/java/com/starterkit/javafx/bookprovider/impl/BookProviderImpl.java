package com.starterkit.javafx.bookprovider.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import com.starterkit.javafx.bookprovider.BookProvider;
import com.starterkit.javafx.dataprovider.data.BookVO;
import com.starterkit.javafx.dataprovider.data.BookStatusVO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
/**
 * provides book list from rest service requests to local server
 * @author JZMUDA
 *
 */
public class BookProviderImpl implements BookProvider {
	
	private String url = "";
	private Collection<BookVO> books;
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

		int responseCode = con.getResponseCode();
		LOG.debug("\nSending 'GET' request to URL : " + searchUrl);
		LOG.debug("Response Code : " + responseCode);
		
//		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//        String line;
//        while ((line = br.readLine()) != null) {
//            parseData(line);
//        }
//        br.close();
//		
		
		InputStream inputStream = con.getInputStream();		
		ObjectMapper mapper = new ObjectMapper();
		
		CollectionType constructCollectionType = mapper.getTypeFactory().constructCollectionType(List.class,
				BookVO.class);
		
		books = extractBook(inputStream, constructCollectionType);
		
		LOG.debug("Books size: " + books.size());
		return (List<BookVO>) books;
	}
	
	private List<BookVO> extractBook(InputStream inputStream, CollectionType constructCollectionType) {
		Object value = null;
		try {
			value = objectMapper.readValue(inputStream, constructCollectionType);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String addUrl = url+"update";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(addUrl);
		postRequest.setHeader("Content-Type", "application/json");
		try {
			postRequest.setEntity(new StringEntity(bookJSON));
			LOG.debug("json added");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpResponse response2;
		try {
			response2 = client.execute(postRequest);
			LOG.debug("json posted under "+addUrl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				return false;
			}
		} catch (MalformedURLException e1) {
			this.url="";
			return false;
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
	
	
//old'n crappy parser	
	private void parseData(String line) {
		// TODO Auto-generated method stub
		books=new ArrayList<BookVO>();
		line=strip(line);
		String data[] = line.split(",");
		if(data.length>=4)
			for(int i =0; i <data.length-4;i=i+4){
				books.add(new BookVO(Long.parseLong(data[i]), data[i+1], data[i+2], BookStatusVO.valueOf(data[i+3])));
			}
	}


	private String strip(String line) {
		line=line.replace("\"", "");
		line=line.replace("[", "");
		line=line.replace("]", "");
		line=line.replace("{", "");
		line=line.replace("}", "");
		line=line.replace("id:", "");
		line=line.replace("title:", "");
		line=line.replace("authors:", "");
		line=line.replace("status:", "");
		return line;
	}


}
