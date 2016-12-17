package org.cos.sie.popsulo.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.cos.sie.popsulo.app.utils.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

public class StartupClass extends Application {

	private static final Logger logger = LoggerFactory.getLogger(StartupClass.class);

	public static void main(String [] args) {
		logger.info("Application is starting");
		launch(args);
		logger.info("Exiting application");
	}

	public void start(Stage primaryStage) throws Exception {
		ResourceBundle resources = ResourceUtils.loadLabelsForDefaultLocale();
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainForm.fxml"), resources);
		logger.info("Loaded main fxml from file");
		Image mainIcon = new Image(getClass().getResourceAsStream("/icons/mainIcon.png"));
		primaryStage.getIcons().add(mainIcon);
		primaryStage.setTitle(resources.getString("labels.app.name"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		logger.info("Main window showed");
	}
}
