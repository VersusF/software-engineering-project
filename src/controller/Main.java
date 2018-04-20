package controller;

import java.io.IOException;
import java.util.Optional;


import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
	private static final String LOGIN_FXML_PATH = "/view/Login.fxml";
	private static final String MAGAZZINO_FXML_PATH = "/view/Magazzino.fxml";
	public static final String LOGO_ICONA_PATH = "file:img/programIcon.png";

	private Stage primaryStage;
	private MagazzinoController myController;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Gestione Magazzino");
		this.primaryStage.getIcons().add(new Image(LOGO_ICONA_PATH));

		/* 
		 * Apro la finestra di login.
		 * Se l'utente inserisce username e password validi,
		 * si prosegue all'apertura della finestra principale del programma
		 */
		if (! eseguitoLogin())
			return;
		
		showMagazzino();
	}

	/**
	 * Finestra principale del programma.
	 * Consente la visione dei prodotti presenti nel magazzino, la lettura dei
	 * dettagli per ciascuno di essi e la gestione di CD, generi, strumenti e musicisti.
	 */
	private void showMagazzino() {
		FXMLLoader magazzinoLoader = new FXMLLoader();
		magazzinoLoader.setLocation(getClass().getResource(MAGAZZINO_FXML_PATH));
		Parent root = null;
		try {
			root = (Parent) magazzinoLoader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		myController = magazzinoLoader.getController();
		myController.setStage(primaryStage);

		primaryStage.setScene(new Scene(root));
		primaryStage.setMaximized(true);
		primaryStage.setOnCloseRequest(event -> chiusura(event));
		primaryStage.show();
	}

	/**
	 * Metodo che richiama la finestra di login.
	 * @return true se l'utente inserisce username e password validi.
	 */
	private boolean eseguitoLogin() {
		try {
			FXMLLoader loginLoader = new FXMLLoader();
			loginLoader.setLocation(Main.class.getResource(LOGIN_FXML_PATH));
			GridPane loginRoot = (GridPane) loginLoader.load();

			//creo stage e scene
			Stage loginStage = new Stage();
			loginStage.setTitle("Login");
			loginStage.initModality(Modality.APPLICATION_MODAL);
			loginStage.initOwner(primaryStage);
			Scene loginMainScene = new Scene(loginRoot);
			loginStage.setScene(loginMainScene);

			//Assegno controller
			LoginController myController = (LoginController) loginLoader.getController();
			myController.setLoginStage(loginStage);

			loginStage.showAndWait();
			return myController.esitoLogin();
		} catch (IOException e) {
			System.err.println("File " + LOGIN_FXML_PATH + " non trovato");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void chiusura(Event event){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Conferma");
		alert.setHeaderText("Vuoi salvare le modifiche prima di uscire?");
		alert.setContentText(null);

		ButtonType si = new ButtonType("Si");
		ButtonType no = new ButtonType("No");
		ButtonType annulla = new ButtonType("Annulla", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(si, no, annulla);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == si){
			myController.salvaDati();
		}
		else if (result.get() == annulla) {
			event.consume();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
