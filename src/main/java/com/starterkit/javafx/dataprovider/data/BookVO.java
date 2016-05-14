package com.starterkit.javafx.dataprovider.data;

// REV: ta klasa powinna byc w pakiecie bookprovider

/**
 * Person data.
 *
 * @author Leszek
 */
public class BookVO {
	
	private Long id;
	private String title;
	private String authors;
	private BookStatusVO status;
	
	public BookVO() {
		
	}
	
	public BookVO(Long id, String title, String authors, BookStatusVO status) {
		this.id = id;
		this.authors = authors;
		this.title = title;
		this.status = status;
	}

	public String getId() {
		return id.toString();
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status.toString();
	}

	public void setStatus(BookStatusVO status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "BookVO [id=" + id + ", authors=" + authors + ", title=" + title + ", status=" + status.toString() + "]";
	}
	
}
