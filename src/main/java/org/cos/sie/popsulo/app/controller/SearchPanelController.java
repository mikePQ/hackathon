package org.cos.sie.popsulo.app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.cos.sie.popsulo.app.QueryResult;
import org.cos.sie.popsulo.app.utils.ResourceUtils;
import org.cos.sie.popsulo.app.utils.timer.TimerService;
import org.cos.sie.popsulo.youtubeSearch.SearchQueryService;
import org.cos.sie.popsulo.youtubeSearch.impl.DefaultSearchQueryService;
import org.cos.sie.popsulo.youtubeSearch.impl.LocalSearchQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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
	private TableColumn<QueryResult, Boolean> isCached;

    @FXML
    private CheckBox onlyLocalCheckBox;

    private PlayerController playerController;

    private static Map<Boolean, Image> cachedIcons = new HashMap<>();

    static {
    	cachedIcons.put(true, new Image(SearchPanelController.class.getResourceAsStream("/icons/is_cached_tiny.png")));
    	cachedIcons.put(false, new Image(SearchPanelController.class.getResourceAsStream("/icons/is_not_cached_tiny.png")));
	}

	@FXML
	@SuppressWarnings("unused")
	private void initialize() {
		searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if ( !Objects.equals(oldValue, newValue) ) {
				updateQueries();
			}
		});
		logger.info("Registered listener for search box");

		queryService = new DefaultSearchQueryService();
        ResourceBundle bundle = ResourceUtils.loadLabelsForDefaultLocale();
        onlyLocalCheckBox.setText(bundle.getString("labels.search.only.local"));
        onlyLocalCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (onlyLocalCheckBox.isSelected()) {
                if (queryService instanceof DefaultSearchQueryService) {
                    queryService = new LocalSearchQueryService();
                    currentResults.clear();
                    initializeResultsGrid();
                }
            } else {
                if (queryService instanceof LocalSearchQueryService) {
                    queryService = new DefaultSearchQueryService();
                    currentResults.clear();
                    initializeResultsGrid();
                }
            }
        });

        initializeResultsGrid();
    }

	private void initializeResultsGrid() {
		title.setCellValueFactory(new PropertyValueFactory<>("title"));
		author.setCellValueFactory(new PropertyValueFactory<>("author"));
		miniature.setCellValueFactory(new PropertyValueFactory<>("miniature"));
		isCached.setCellValueFactory(new PropertyValueFactory<>("cached"));
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
		isCached.setCellFactory(param -> new TableCell<QueryResult, Boolean>() {
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				if ( item != null ) {
					logger.info("boolean cached property wasn't null: " + item);
					ImageView imageView = new ImageView();
					imageView.setFitHeight(50);
					imageView.setFitHeight(50);
					imageView.setImage(cachedIcons.get(item));
					setGraphic(imageView);
					setAlignment(Pos.CENTER);
				} else {
					logger.info("boolean cached property was null");
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
            if (playerController != null) {
                playerController.updateState(selectedItem);
            }
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

    public void setPlayerController(PlayerController playerController) {
        this.playerController = playerController;
    }
}
