package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Archivio;

public class GestisciStrumentiController implements Initializable {
	@FXML private Label titoloLabel;
	@FXML private TextField nuovoTextField;
	@FXML private Button aggiungiButton;
	@FXML private ListView<String> elencoListView;
	@FXML private Button eliminaButton;
	
	private Archivio archivio;
	
	public GestisciStrumentiController() {
		archivio = Archivio.getArchivio();
	}

	public void aggiungi() {
		String nuovoStrumento = nuovoTextField.getText().trim();
		if (archivio.getStrumenti().contains(nuovoStrumento)) {
			Alert duplicatoAlert = new Alert(AlertType.WARNING);
			duplicatoAlert.setTitle("Attenzione");
			duplicatoAlert.setHeaderText("Stai inserendo uno strumento che � gi� in archivio");
			duplicatoAlert.setContentText(null);
			duplicatoAlert.show();
		}
		else
			if(nuovoStrumento.length() != 0)
				archivio.addStrumento(nuovoStrumento);
		nuovoTextField.setText("");
		FXCollections.sort(elencoListView.getItems());
	}
	
	public void elimina() {
		String selezionato = elencoListView.getSelectionModel().getSelectedItem();
		if (selezionato == null) {
			Alert nessunoSelezionato = new Alert(AlertType.WARNING);
			nessunoSelezionato.setTitle("Attenzione");
			nessunoSelezionato.setContentText(null);
			nessunoSelezionato.setHeaderText("Nessuno strumento selezionato");
			nessunoSelezionato.show();
		}
		else {
			Alert confirmation = new Alert(AlertType.CONFIRMATION);
			confirmation.setTitle("Conferma");
			confirmation.setHeaderText(null);
			confirmation.setContentText("Sei sicuro di voler eliminare questo strumento?");
			Optional<ButtonType> result = confirmation.showAndWait();
			if (result.get() == ButtonType.OK)
				archivio.removeStrumento(selezionato);
		}
		elencoListView.getSelectionModel().clearSelection();
	}
	
    public void keyboardEnterPressed(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER))
			aggiungiButton.fire();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		titoloLabel.setText("Gestione Strumenti");
		elencoListView.setItems(archivio.getStrumenti());
		FXCollections.sort(elencoListView.getItems());
	}
	
}
