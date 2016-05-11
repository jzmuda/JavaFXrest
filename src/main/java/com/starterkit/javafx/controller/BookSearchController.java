package com.starterkit.javafx.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.starterkit.javafx.bookprovider.BookProvider;
import com.starterkit.javafx.dataprovider.data.BookStatusVO;
import com.starterkit.javafx.dataprovider.data.BookVO;
import com.starterkit.javafx.model.BookSearch;
import com.starterkit.javafx.model.BookStatus;
import com.starterkit.javafx.texttospeech.Speaker;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * Controller for the person search screen.
 * <p>
 * The JavaFX runtime will inject corresponding objects in the @FXML annotated
 * fields. The @FXML annotated methods will be called by JavaFX runtime at
 * specific points in time.
 * </p>
 *
 * @author Jakub/Leszek
 */
public class BookSearchController {

	private static final Logger LOG = Logger.getLogger(BookSearchController.class);

	/**
	 * Resource bundle loaded with this controller. JavaFX injects a resource
	 * bundle specified in {@link FXMLLoader#load(URL, ResourceBundle)} call.
	 * <p>
	 * NOTE: The variable name must be {@code resources}.
	 * </p>
	 */
	@FXML
	private ResourceBundle resources;

	/**
	 * URL of the loaded FXML file. JavaFX injects an URL specified in
	 * {@link FXMLLoader#load(URL, ResourceBundle)} call.
	 * <p>
	 * NOTE: The variable name must be {@code location}.
	 * </p>
	 */
	@FXML
	private URL location;

	/**
	 * JavaFX injects an object defined in FXML with the same "fx:id" as the
	 * variable name.
	 */
	@FXML
	private TextField authorsField;

	@FXML
	private TextField urlField;

	@FXML
	private TextField titleField;

	@FXML
	private ComboBox<BookStatus> statusField;

	@FXML
	private Button enterUrlButton;


	@FXML
	private Button searchButton;

	@FXML
	private Button addButton;

	@FXML
	private TableView<BookVO> resultTable;

	@FXML
	private TableColumn<BookVO, String> idColumn;

	@FXML
	private TableColumn<BookVO, String> authorsColumn;

	@FXML
	private TableColumn<BookVO, String> titleColumn;

	@FXML
	private TableColumn<BookVO, String> statusColumn;



	private final BookProvider bookProvider = BookProvider.INSTANCE;

	private final Speaker speaker = Speaker.INSTANCE;

	private final BookSearch model = new BookSearch();


	/**
	 * The JavaFX runtime instantiates this controller.
	 * <p>
	 * The @FXML annotated fields are not yet initialized at this point.
	 * </p>
	 */
	public BookSearchController() {
		LOG.debug("Constructor: authorsField = " + authorsField);
	}

	/**
	 * The JavaFX runtime calls this method after loading the FXML file.
	 * <p>
	 * The @FXML annotated fields are initialized at this point.
	 * </p>
	 * <p>
	 * NOTE: The method name must be {@code initialize}.
	 * </p>
	 */
	@FXML
	private void initialize() {
		LOG.debug("initialize(): authorsField = " + authorsField);

		initializeStatusField();

		initializeResultTable();
	
		/*
		 * Bind controls properties to model properties.
		 */
		urlField.textProperty().bindBidirectional(model.urlProperty());
		authorsField.textProperty().bindBidirectional(model.authorsProperty());
		titleField.textProperty().bindBidirectional(model.titleProperty());
		statusField.valueProperty().bindBidirectional(model.statusProperty());
		resultTable.itemsProperty().bind(model.resultProperty());

		/*
		 * Preselect the default value for status.
		 */
		model.setStatus(BookStatus.FREE);

		/*
		 * This works also, because we are using bidirectional binding.
		 */
		// statusField.setValue(BookStatus.FREE);

		/*
		 * Make the buttons inactive when the URL field is empty.
		 */

		authorsField.setDisable(true);
		titleField.setDisable(true);
		searchButton.setDisable(true);
		//BooleanBinding booleanSearchBinding = 
		//		authorsField.textProperty().isEmpty().and(
		//				titleField.textProperty().isEmpty());
		BooleanBinding booleanAddBinding = 
				authorsField.textProperty().isEmpty().or(
						titleField.textProperty().isEmpty());
		
		//searchButton.disableProperty().bind(booleanSearchBinding);
		addButton.disableProperty().bind(booleanAddBinding);
	}

	private void initializeStatusField() {
		/*
		 * Add items to the list in combo box.
		 */
		for (BookStatus status : BookStatus.values()) {
			statusField.getItems().add(status);
		}

		/*
		 * Set cell factory to render internationalized texts for list items.
		 */
		statusField.setCellFactory(new Callback<ListView<BookStatus>, ListCell<BookStatus>>() {

			@Override
			public ListCell<BookStatus> call(ListView<BookStatus> param) {
				return new ListCell<BookStatus>() {

					@Override
					protected void updateItem(BookStatus item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							return;
						}
						String text = getInternationalizedText(item);
						setText(text);
					}
				};
			}
		});

		/*
		 * Set converter to display internationalized text for selected value.
		 */
		statusField.setConverter(new StringConverter<BookStatus>() {

			@Override
			public String toString(BookStatus object) {
				return getInternationalizedText(object);
			}

			@Override
			public BookStatus fromString(String string) {
				/*
				 * Not used, because combo box is not editable.
				 */
				return null;
			}
		});
	}

	private void initializeResultTable() {
		/*
		 * Define what properties of PersonVO will be displayed in different
		 * columns.
		 */
		idColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getId()));
		authorsColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getAuthors()));
		titleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
		statusColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getStatus()));
		
		/*
		 * Show specific text for an empty table. This can also be done in FXML.
		 */
		resultTable.setPlaceholder(new Label(resources.getString("table.emptyText")));

		/*
		 * When table's row gets selected say given person's name.
		 */
		resultTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BookVO>() {

			@Override
			public void changed(ObservableValue<? extends BookVO> observable, BookVO oldValue, BookVO newValue) {
				LOG.debug(newValue + " selected");

				if (newValue != null) {
					Task<Void> backgroundTask = new Task<Void>() {

						@Override
						protected Void call() throws Exception {
							speaker.say(newValue.getAuthors());
							return null;
						}

						@Override
						protected void failed() {
							LOG.error("Could not say name: " + newValue.getAuthors(), getException());
						}
					};
					new Thread(backgroundTask).start();
				}
			}
		});
	}

	/**
	 * Gets an internationalized text for given {@link Sex} value.
	 *
	 * @param item
	 *            status
	 * @return text
	 */
	private String getInternationalizedText(BookStatus item) {
		return resources.getString("status." + item.name());
	}

	/**
	 * The JavaFX runtime calls this method when the <b>Search</b> button is
	 * clicked.
	 *
	 * @param event
	 *            {@link ActionEvent} holding information about this event
	 */
	@FXML
	private void acceptUrl(ActionEvent event) {
		LOG.debug("'Enter URL' button clicked");
		validateUrlAction();
	}

	/**
	 * This implementation is correct.
	 * <p>
	 * The {@link DataProvider#findPersons(String, SexVO)} call is executed in a
	 * background thread.
	 * </p>
	 */
	private void validateUrlAction() {
		/*
		 * Use task to execute the potentially long running call in background
		 * (separate thread), so that the JavaFX Application Thread is not
		 * blocked.
		 */
		Task<Boolean> backgroundTask = new Task<Boolean>() {

			/**
			 * This method will be executed in a background thread.
			 * @return 
			 */
			@Override
			protected Boolean call() throws Exception {
				LOG.debug("call() called for ValidateUrl");
				return bookProvider.checkURL(model.getUrl());
			}			

			/**
			 * This method will be executed in the JavaFX Application Thread
			 * when the task finishes.
			 */
			@Override
			protected void succeeded() {
				boolean result = getValue();
				LOG.debug("succeeded() called for ValidateUrl with result "+result);
				if(!result)
					model.setBadUrl();
				else {
						urlField.setDisable(true);
						enterUrlButton.setDisable(true);
						authorsField.setDisable(false);
						titleField.setDisable(false);
						searchButton.setDisable(false);
				}
			}
		};

		/*
		 * Start the background task. In real life projects some framework
		 * manages background tasks. You should never create a thread on your
		 * own.
		 */
		new Thread(backgroundTask).start();
	}


	@FXML
	private void searchAction(ActionEvent event) {
		LOG.debug("'Search' button clicked");
		searchButtonAction();
	}


	/**
	 * This implementation is executed in a background thread
	 */
	private void searchButtonAction() {
		/*
		 * Use task to execute the potentially long running call in background
		 * (separate thread), so that the JavaFX Application Thread is not
		 * blocked.
		 */
		Task<Collection<BookVO>> backgroundTask = new Task<Collection<BookVO>>() {

			/**
			 * This method will be executed in a background thread.
			 */
			@Override
			protected Collection<BookVO> call() throws Exception {
				LOG.debug("call() called");

				/*
				 * Get the data.
				 */
				Collection<BookVO> result =  bookProvider.findBooks( //
						model.getAuthors(), //
						model.getTitle());
				
				/*
				 * Value returned from this method is stored as a result of task
				 * execution.
				 */
				return result;
			}

			/**
			 * This method will be executed in the JavaFX Application Thread
			 * when the task finishes.
			 */
			@Override
			protected void succeeded() {
				LOG.debug("succeeded() called");

				/*
				 * Get result of the task execution.
				 */
				Collection<BookVO> result = getValue();

				/*
				 * Copy the result to model.
				 */
				model.setResult(new ArrayList<BookVO>(result));
				/*
				 * Reset sorting in the result table.
				 */
				resultTable.getSortOrder().clear();
			}
		};

		/*
		 * Start the background task. In real life projects some framework
		 * manages background tasks. You should never create a thread on your
		 * own.
		 */
		new Thread(backgroundTask).start();
	}

	@FXML
	private void addAction(ActionEvent event) {
		LOG.debug("'Add' button clicked");
		saddButtonAction();
	}
	
	private void saddButtonAction() {
		/*
		 * Use task to execute the potentially long running call in background
		 * (separate thread), so that the JavaFX Application Thread is not
		 * blocked.
		 */
		Task<Boolean> backgroundTask = new Task<Boolean>() {

			/**
			 * This method will be executed in a background thread.
			 */
			@Override
			protected Boolean call() throws Exception {
				bookProvider.addBook(
						model.getTitle(),
						model.getAuthors(),
						BookStatusVO.valueOf(model.getStatus()));
				
				/*
				 * Value returned from this method is stored as a result of task
				 * execution.
				 */
				return true;
			}

			/**
			 * This method will be executed in the JavaFX Application Thread
			 * when the task finishes.
			 */
			@Override
			protected void succeeded() {
				LOG.debug("succeeded() called");
			}
		};

		/*
		 * Start the background task. In real life projects some framework
		 * manages background tasks. You should never create a thread on your
		 * own.
		 */
		new Thread(backgroundTask).start();
	}


}
