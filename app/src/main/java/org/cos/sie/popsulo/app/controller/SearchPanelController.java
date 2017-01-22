package org.cos.sie.popsulo.app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import org.cos.sie.popsulo.search.SearchService;
import org.cos.sie.popsulo.search.dto.VideoResult;
import org.cos.sie.popsulo.search.impl.YoutubeSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


// TODO: [jgolda] migrate to javafx.stage.popup
public class SearchPanelController {

	private static final Logger logger = LoggerFactory.getLogger(SearchPanelController.class);

	@FXML
	private TextField searchTextField;

	private SearchService queryService;

	@FXML
	private TableView<VideoResult> results;

	@FXML
	private ObservableList<VideoResult> currentResults = FXCollections.observableArrayList();

	@FXML
	private TableColumn<VideoResult, String> title;

	@FXML
	private TableColumn<VideoResult, String> author;

	@FXML
	private TableColumn<VideoResult, Image> miniature;

	@FXML
	private TableColumn<VideoResult, Boolean> isCached;

	@FXML
	private CheckBox onlyLocalCheckBox;

	private PlayerController playerController;

	private final static Map<Boolean, Image> cachedIcons = new HashMap<>();

	static {
		cachedIcons.put(true, new Image(SearchPanelController.class.getResourceAsStream("/icons/is_cached_tiny.png")));
		cachedIcons.put(false, new Image(SearchPanelController.class.getResourceAsStream("/icons/is_not_cached_tiny.png")));
	}

	@FXML
	@SuppressWarnings("unused")
	private void initialize() {
		searchTextField.setOnKeyPressed(event -> {
			if ( KeyCode.ENTER.equals(event.getCode()) ) {
				updateQueries();
			}
		});
		logger.info("Registered listener for search box");

		queryService = new YoutubeSearchService();
		initializeResultsGrid();
	}

	private void initializeResultsGrid() {
		title.setCellValueFactory(new PropertyValueFactory<>("title"));
		author.setCellValueFactory(new PropertyValueFactory<>("author"));
		miniature.setCellValueFactory(new PropertyValueFactory<>("miniature"));
		isCached.setCellValueFactory(new PropertyValueFactory<>("cached"));
		miniature.setCellFactory(param -> new TableCell<VideoResult, Image>() {
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
		isCached.setCellFactory(param -> new TableCell<VideoResult, Boolean>() {
			@Override
			protected void updateItem(Boolean isCached, boolean empty) {
				if ( isCached != null ) {
					if ( isCached ) {
						setText("cached");
					} else {
						setText("not cached");
					}
				}
//				if ( isCached != null ) {
//					ImageView imageView = new ImageView();
//					imageView.setFitHeight(50);
//					imageView.setFitHeight(50);
//					imageView.setImage(cachedIcons.get(isCached));
//					setGraphic(imageView);
//					setAlignment(Pos.CENTER);
//				}
			}
		});

		logger.info("Defined value factories ");

		results.setItems(currentResults);
		logger.info("Defined the collection for result grid");
	}

	@FXML
	public void onResultClicked(MouseEvent event) {
		logger.info("Result clicked");
		if ( event.getClickCount() == 2 ) {
			VideoResult selectedItem = results.getSelectionModel().getSelectedItem();
			playerController.updateState(selectedItem);
		}
	}

	private void updateQueries() {
		logger.info("Querying youtube requested");
		String query = searchTextField.getText();

		List<VideoResult> queryResults = new ArrayList<>();
		Task queryTask = new Task() {
			@Override
			protected Object call() throws Exception {
				queryResults.addAll(queryService.searchYoutube(query));
				return null;
			}
		};

		queryTask.setOnSucceeded(event -> updateResultList(queryResults));

		Thread asynchRequest = new Thread(queryTask);
		asynchRequest.setDaemon(true);
		asynchRequest.start();
	}

	private void updateResultList(List<VideoResult> results) {
		currentResults.clear();
		logger.debug("Query succeded");
		currentResults.addAll(FXCollections.observableList(results));
	}

	void setPlayerController(PlayerController playerController) {
		this.playerController = playerController;
	}
}
