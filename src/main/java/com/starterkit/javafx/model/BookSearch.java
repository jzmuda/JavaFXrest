package com.starterkit.javafx.model;

import java.util.ArrayList;
import java.util.List;

import com.starterkit.javafx.dataprovider.data.BookVO;

import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

/**
 * Data displayed on the book search screen.
 *
 * @author Leszek/Jakub
 */
public class BookSearch {
	
	private final LongProperty id = new SimpleLongProperty();
	private final StringProperty authors = new SimpleStringProperty();
	private final StringProperty title = new SimpleStringProperty();
	private final StringProperty url = new SimpleStringProperty();
	private final ObjectProperty<BookStatus> status = new SimpleObjectProperty<>();
	private final ListProperty<BookVO> result = new SimpleListProperty<>(
			FXCollections.observableList(new ArrayList<>()));
	
	public final String getUrl() {
		return url.getValue();
	}


	public final void setUrl(String value) {
		url.set(value);
	}

	public StringProperty urlProperty() {
		return url;
	}
	
	public final String getId() {
		// REV: zadeklaruj jako string, a nie konwertuj, typ nie zgadza sie z typem idProperty
		return id.toString();
	}
	
	
	public final void setId(String value) {
		// REV: j.w.
		id.set(Integer.parseInt(value));
	}
	
	public LongProperty idProperty() {
		return id;
	}

	public final String getAuthors() {
		return authors.get();
	}


	public final void setAuthors(String value) {
		authors.set(value);
	}

	public StringProperty authorsProperty() {
		return authors;
	}
	
	public final String getTitle() {
		return title.get();
	}
	
	public final void setTitle(String value) {
		title.set(value);
	}
	
	public StringProperty titleProperty() {
		return title;
	}

	public final String getStatus() {
		// REV: j.w.
		return status.get().toString();
	}

	public final void setStatus(BookStatus value) {
		status.set(value);
	}
	
	// REV: nazwa metody sugeruje calkiem co innego
	public final void setBadUrl() {
		setAuthors("");
		setTitle("");
		setUrl("");
	}

	public ObjectProperty<BookStatus> statusProperty() {
		return status;
	}

	public final List<BookVO> getResult() {
		return result.get();
	}

	public final void setResult(List<BookVO> value) {
		result.setAll(value);
	}

	public ListProperty<BookVO> resultProperty() {
		return result;
	}

	@Override
	public String toString() {
		return "BookSearch [id=" + id + ", authors=" + authors + ", title=" + title + ", status=" + status + ", result="
				+ result + "]";
	}

	// REV: ta metoda nie jest uzywana
	public void checkURL() {
		// TODO Auto-generated method stub
		
	}
	
}
