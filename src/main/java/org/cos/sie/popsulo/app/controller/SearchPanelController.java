package org.cos.sie.popsulo.app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.cos.sie.popsulo.LocalDiskCache;
import org.cos.sie.popsulo.app.QueryResult;
import org.cos.sie.popsulo.app.utils.timer.TimerService;
import org.cos.sie.popsulo.youtubeSearch.SearchQueryService;
import org.cos.sie.popsulo.youtubeSearch.impl.DefaultSearchQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private TableColumn<QueryResult, Image> miniature;

	@FXML
	@SuppressWarnings("unused")
	private void initialize() {
		searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if ( !Objects.equals(oldValue, newValue) ) {
				updateQueries();
			}
		});
		logger.info("Registered listener for search box");

		initializeResultsGrid();

		queryService = new DefaultSearchQueryService();
	}

	private void initializeResultsGrid() {
		title.setCellValueFactory(new PropertyValueFactory<>("title"));
		author.setCellValueFactory(new PropertyValueFactory<>("author"));
		miniature.setCellValueFactory(new PropertyValueFactory<>("miniature"));
		miniature.setCellFactory(param -> new TableCell<QueryResult, Image>() {
			@Override
			protected void updateItem(Image item, boolean empty) {
				if ( item != null ) {
					ImageView imageView = new ImageView();
					imageView.setFitHeight(50);
					imageView.setFitHeight(50);
					imageView.setImage(item);
					setGraphic(imageView);
				}
			}
		});

		logger.info("Defined value factories ");

		results.setItems(currentResults);
		logger.info("Defined the collection for result grid");
	}

	@FXML
	public void onResultClicked(MouseEvent event) {
		logger.info("Result clicked");
		if ( event.isPrimaryButtonDown() && event.getClickCount() == 2 ) {
			QueryResult selectedItem = results.getSelectionModel().getSelectedItem();
			LocalDiskCache.getInstance().cacheQueryResult(selectedItem);
		}
	}

	private void updateQueries() {
		logger.info("Querying youtube requested");

		TimerService.getTimerService().executeQuery(queryService,
				() -> searchTextField.getText(),
				this::updateResultList,
				() -> new Alert(Alert.AlertType.ERROR, "Failed to contact youtube", ButtonType.OK).showAndWait());

	}

	private void updateResultList(List<QueryResult> results) {
		currentResults.clear();
		logger.debug("Query succeded");
		currentResults.addAll(FXCollections.observableList(results));
	}
}
