package org.cos.sie.popsulo.app.controller;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController {

	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	@FXML
	public void onButtonClicked(MouseEvent event) {
		logger.info("button clicked");
	}
}
