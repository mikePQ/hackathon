package org.cos.sie.popsulo.app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.cos.sie.popsulo.QueryResult;
import org.cos.sie.popsulo.SearchQueryService;
import org.cos.sie.popsulo.mocks.SearchQueryServiceMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class MainController {

	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	@FXML
	private TextField searchTextField;

	private SearchQueryService queryService;

	@FXML
	private TableView<QueryResult> results;

	@FXML
	private ObservableList<QueryResult> currentResults;

	@FXML
	private TableColumn<QueryResult, String> title;

	@FXML
	private TableColumn<QueryResult, String> author;

	@FXML
	@SuppressWarnings("unused")
	private void initialize() {
		searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if ( ! Objects.equals(oldValue, newValue) ) {
				updateQueries(newValue);
			}
		});
		logger.info("Registered listener for search box");

		title.setCellValueFactory(new PropertyValueFactory<>("title"));
		author.setCellValueFactory(new PropertyValueFactory<>("author"));
		logger.info("Defined value factories ");

		results.setItems(currentResults);
		logger.info("Defined the collection for result grid");

		queryService = new SearchQueryServiceMock();
	}

	private void updateQueries(String newValue) {
		logger.info("Querying youtube requested");
		currentResults = FXCollections.observableList(queryService.queryYoutube(newValue));
	}

	@FXML
	public void onButtonClicked(MouseEvent event) {

	}

	@FXML
	public void onResultClicked(MouseEvent event) {
		logger.info("button clicked");
		throw new UnsupportedOperationException("This feature is not yet implemented");
	}
}
