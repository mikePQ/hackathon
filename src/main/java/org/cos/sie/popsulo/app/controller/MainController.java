package org.cos.sie.popsulo.app.controller;

import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController {

	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	@FXML
	private SearchPanelController searchPanelController;

	@FXML
	private void initialize() {
		logger.info("Initializing MainController...");
	}
}
