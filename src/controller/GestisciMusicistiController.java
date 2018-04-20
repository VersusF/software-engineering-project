package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Archivio;
import model.Musicista;

public class GestisciMusicistiController implements Initializable {
	@FXML private Label titoloLabel;
	@FXML private Button aggiungiButton;
	@FXML private Button modificaButton;
	@FXML private ListView<Musicista> elencoListView;
	@FXML private ListView<String> strumentiUsatiListView;
	@FXML private Button eliminaButton;
	@FXML private Label nomeArteLabel;
	@FXML private Label annoNascitaLabel;
	@FXML private Label generePrincipaleLabel;

	private static final String MODIFICA_MUSICISTA_FXML_PATH = "/view/ModificaMusicista.fxml";

	private Archivio archivio;

	public GestisciMusicistiController() {
		archivio = Archivio.getArchivio();
	}

	/**
	 * Apertura di una nuova finestra in cui inserire i dati
	 * relativi al nuovo musicista.
	 */
	public void aggiungi() {
		Musicista musicistaNew = showModificaMusicista(null);
		if (musicistaNew != null)
			archivio.addMusicista(musicistaNew);
		FXCollections.sort(elencoListView.getItems());
	}

	/**
	 * Analogo al metodo aggiungi ma la finestra conterr� gi�,
	 * al momento dell'apertura, le informazioni attuali del musicista.
	 */
	public void modifica(){
		Musicista musicistaSelezionato = elencoListView.getSelectionModel().getSelectedItem();
		if(musicistaSelezionato != null) {
			if (showModificaMusicista(musicistaSelezionato) != null){
				//System.out.println(musicistaSelezionato + musicistaSelezionato.getStrumentiSuonati().toString());
				elencoListView.refresh();
				showDettagli(musicistaSelezionato);
			}
		}
		else {
			Alert nessunoSelezionatoAlert = new Alert(AlertType.WARNING);
			//alert.initOwner(mainApp.getPrimaryStage()); TODO
			nessunoSelezionatoAlert.setTitle("Nessuna seleziona");
			nessunoSelezionatoAlert.setHeaderText("Nessun musicista selezionato.");
			nessunoSelezionatoAlert.setContentText("Selezionare un musicista dall'elenco.");

			nessunoSelezionatoAlert.showAndWait();
		}
		elencoListView.getSelectionModel().clearSelection();
	}

	private Musicista showModificaMusicista(Musicista musicista) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(GestisciMusicistiController.class.getResource(MODIFICA_MUSICISTA_FXML_PATH));
		Parent root = null;
		try{
			root = loader.load();
		} catch(IOException e){
			System.err.println("Non ho trovato il file: + " + MODIFICA_MUSICISTA_FXML_PATH);
		}

		Stage modificaMusicistaStage = new Stage();
		modificaMusicistaStage.setTitle("Modifica musicista");
		modificaMusicistaStage.setScene(new Scene(root));
		modificaMusicistaStage.initModality(Modality.APPLICATION_MODAL);

		ModificaMusicistaController controller = loader.getController();
		controller.setStage(modificaMusicistaStage);
		controller.setMusicista(musicista);

		modificaMusicistaStage.showAndWait();

		return controller.musicistaAggiunto();
	}

	public void elimina() {
		Musicista selezionato = elencoListView.getSelectionModel().getSelectedItem();
		if (selezionato == null) {
			Alert nessunoSelezionato = new Alert(AlertType.WARNING);
			nessunoSelezionato.setTitle("Attenzione");
			nessunoSelezionato.setContentText(null);
			nessunoSelezionato.setHeaderText("Nessun musicista selezionato");
			nessunoSelezionato.show();
		}
		else {
			Alert confirmation = new Alert(AlertType.CONFIRMATION);
			confirmation.setTitle("Conferma");
			confirmation.setHeaderText(null);
			confirmation.setContentText("Sei sicuro di voler eliminare questo musicista?");
			Optional<ButtonType> result = confirmation.showAndWait();
			if (result.get() == ButtonType.OK)
				archivio.removeMusicista(selezionato);
		}
		elencoListView.getSelectionModel().clearSelection();
	}

	public void keyboardEnterPressed(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER))
			aggiungiButton.fire();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		elencoListView.setItems(archivio.getMusicisti());
		FXCollections.sort(elencoListView.getItems());

		// Quando viene selezionato un artista, gli strumenti
		// che questi suona sono visualizzati nella ListView in basso
		elencoListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showDettagli(newValue));
	}

	private void showDettagli(Musicista musicista) {
		strumentiUsatiListView.getItems().clear();
		if (musicista != null) {
			strumentiUsatiListView.getItems().addAll(musicista.getStrumentiSuonati());
			nomeArteLabel.setText(musicista.getNomeArte());
			generePrincipaleLabel.setText(musicista.getGenerePrincipale());
			annoNascitaLabel.setText("" + musicista.getAnnoNascita());
		} else {
			nomeArteLabel.setText("");
			annoNascitaLabel.setText("");
			generePrincipaleLabel.setText("");
		}
	}

}
