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
	private static final String APP_NAME_RESOURCE_KEY = "labels.app.name";
	private static final String MAIN_FORM_FXML_LOCATION = "/fxml/mainForm.fxml";
	private static final String CSS_STYLESHEET_LOCATION = "/css/style.css";
	private static final String MAIN_ICON_LOCATION = "/icons/mainIcon.png";

	public static void main(String [] args) {
		logger.info("Application is starting");
		launch(args);
		logger.info("Exiting application");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ResourceBundle resources = ResourceUtils.loadLabelsForDefaultLocale();
		Scene scene = prepareScene(resources);

		String appTitle = getAppTitle(resources);
		prepareStage(primaryStage, scene, appTitle);
		showMainWindow(primaryStage);
		logger.info("Main window showed");
	}

	private Scene prepareScene(ResourceBundle resources) throws java.io.IOException {
		Scene scene = buildScene(resources);

		setStyleSheet(scene);
		return scene;
	}

	private Scene buildScene(ResourceBundle resources) throws java.io.IOException {
		Parent rootNode = loadFXMLForm(resources);
		logger.info("Loaded main fxml from file");
		return new Scene(rootNode);
	}

	private Parent loadFXMLForm(ResourceBundle resources) throws java.io.IOException {
		return FXMLLoader.load(getClass().getResource(MAIN_FORM_FXML_LOCATION), resources);
	}

	private void setStyleSheet(Scene scene) {
		String css = this.getClass().getResource(CSS_STYLESHEET_LOCATION).toExternalForm();
		scene.getStylesheets().add(css);
	}

	private String getAppTitle(ResourceBundle resources) {
		return resources.getString(APP_NAME_RESOURCE_KEY);
	}

	private void prepareStage(Stage primaryStage, Scene scene, String appTitle) {
		setMainIcon(primaryStage);
		primaryStage.setScene(scene);
		primaryStage.setTitle(appTitle);
	}

	private void setMainIcon(Stage primaryStage) {
		Image icon = new Image(getClass().getResourceAsStream(MAIN_ICON_LOCATION));
		primaryStage.getIcons().add(icon);
	}

	private void showMainWindow(Stage primaryStage) {
		primaryStage.show();
	}
}
