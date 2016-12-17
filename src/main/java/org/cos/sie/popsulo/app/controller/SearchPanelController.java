package org.cos.sie.popsulo.app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.cos.sie.popsulo.app.QueryResult;
import org.cos.sie.popsulo.youtubeSearch.SearchQueryService;
import org.cos.sie.popsulo.youtubeSearch.impl.DefaultSearchQueryService;
import org.cos.sie.popsulo.youtubeSearch.mocks.SearchQueryServiceMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class SearchPanelController {

	private static final Logger logger = LoggerFactory.getLogger(SearchPanelController.class);

	@FXML
	private TextField searchTextField;

	private SearchQueryService queryService;

	@FXML
	private TableView<QueryResult> results;

	@FXML
	private ObservableList<QueryResult> currentResults = FXCollections.observableArrayList();

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

		initializeResultsGrid();

		queryService = new DefaultSearchQueryService();
	}

	private void initializeResultsGrid() {
		title.setCellValueFactory(new PropertyValueFactory<>("title"));
		author.setCellValueFactory(new PropertyValueFactory<>("author"));
		logger.info("Defined value factories ");

		results.setItems(currentResults);
		logger.info("Defined the collection for result grid");
	}

	@FXML
	public void onResultClicked(MouseEvent event) {
		logger.info("Result clicked");
		throw new UnsupportedOperationException("This feature is not yet implemented");
	}

	private void updateQueries(String newValue) {
		logger.info("Querying youtube requested");
		List<QueryResult> youtubeResult;
		try {
			youtubeResult = queryService.queryYoutube(newValue);
		} catch ( IOException exc ) {
			logger.error("Failed to contact youtube webservice due to: " + exc.getMessage(), exc);
			new Alert(Alert.AlertType.ERROR, "Failed to contact youtube", ButtonType.OK).showAndWait();
			return;
		}
		currentResults.clear();
		logger.debug("Query succeded");
		currentResults.addAll(FXCollections.observableList(youtubeResult));
	}
}
