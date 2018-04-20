package controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.Archivio;
import model.CD;
import model.Musicista;
import javafx.scene.control.TextArea;

import javafx.scene.control.ComboBox;

import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;

public class ModificaCDController implements Initializable{
	@FXML private TextField titoloTextField;
	@FXML private TextArea descrizioneTextArea;
	@FXML private ComboBox<Musicista> autoreComboBox;
	@FXML private DatePicker dataPicker;
	@FXML private Button aggiungiButton;
	@FXML private ComboBox<String> genereComboBox;
	@FXML private ListView<String> canzoniListView;
	@FXML private ListView<Pair<Musicista, String>> partecipantiListView;
	@FXML private ComboBox<Musicista> musicistaComboBox;
	@FXML private TextField canzoniTextField;
	@FXML private Button aggiungiCanzoneButton;
	@FXML private Button aggiungiPartecipanteButton;
	@FXML private Button eliminaCanzoneButton;
	@FXML private Button EliminaPartecipanteButton;
	@FXML private ComboBox<String> strumentoComboBox;
	@FXML private Button selezionaImmagineButton;
	@FXML private Label copertinaLabel;
	@FXML private TextField prezzoTextField;

	private Archivio archivio = Archivio.getArchivio();
	private Stage myStage;
	private Pair<CD, Integer> pair = null;

	public void setStage(Stage modificaCDStage) {
		this.myStage = modificaCDStage;
		this.myStage.setTitle("Crea nuovo CD");
	}

	/**
	 * Salvo l'oggetto da modificare.
	 * Se � in corso l'aggiunta di un nuovo musicista,
	 * i campi verranno riempiti con informazioni di default.
	 * 
	 * @param musicista
	 */
	public void setCD(Pair<CD, Integer> pair) {
		if (pair != null) {
			myStage.setTitle("Modifica CD");
			aggiungiButton.setText("Conferma modifica");
			this.pair = pair;
			CD cd = this.pair.getKey();

			titoloTextField.setText(cd.getTitolo());
			descrizioneTextArea.setText(cd.getDescrizione());
			genereComboBox.getSelectionModel().select(cd.getGenere());
			autoreComboBox.getSelectionModel().select(cd.getAutore());
			dataPicker.setValue(cd.getDataVendita());
			canzoniListView.setItems(cd.getCanzoni());
			partecipantiListView.setItems(cd.getPartecipanti());
			prezzoTextField.setText(String.format("%.2f", cd.getPrezzo()));
			if (cd.getCopertina() != null)
				copertinaLabel.setText(cd.getCopertina());
		}
	}

	public Pair<CD, Integer> cdAggiunto() {
		return pair;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		genereComboBox.setItems(archivio.getGeneri());
		autoreComboBox.setItems(archivio.getMusicisti());
		musicistaComboBox.setItems(archivio.getMusicisti());
		partecipantiListView.setCellFactory(myListView -> new ElencoPartecipantiCell());
		//strumentoComboBox.setItems(archivio.getStrumenti());
	}

	public void aggiungiPartecipante() {
		Musicista musicistaSelezionato = musicistaComboBox.getSelectionModel().getSelectedItem();
		String strumentoSelezionato = strumentoComboBox.getSelectionModel().getSelectedItem();
		if (musicistaSelezionato != null && strumentoSelezionato != null)
			partecipantiListView.getItems().add(new Pair<Musicista, String>(musicistaSelezionato, strumentoSelezionato));
		else
			warningAlert("Non hai selezionato un partecipante");
		musicistaComboBox.getSelectionModel().clearSelection();
		strumentoComboBox.getSelectionModel().clearSelection();
	}

	public void selezionatoMusicista() {
		Musicista musicistaSelezionato = musicistaComboBox.getSelectionModel().getSelectedItem();
		if (musicistaSelezionato == null)
			strumentoComboBox.setItems(null);
		else {
			strumentoComboBox.setItems(FXCollections.observableArrayList(musicistaSelezionato.getStrumentiSuonati()));
			for (Pair<Musicista, String> partecipante : partecipantiListView.getItems())
				if(partecipante.getKey().equals(musicistaSelezionato))
					strumentoComboBox.getItems().remove(partecipante.getValue());
		}
	}

	public void eliminaPartecipante() {
		Pair<Musicista, String> selezionato = partecipantiListView.getSelectionModel().getSelectedItem();
		if (selezionato == null)
			warningAlert("Devi selezionare un musicista partecipante");
		else
			partecipantiListView.getItems().remove(selezionato);
		partecipantiListView.getSelectionModel().clearSelection();
	}

	public void aggiungiCD(){
		if(inputValido()){
			if(pair == null)
				pair = new Pair<CD, Integer>(new CD(), 0);
			pair.getKey().setTitolo(titoloTextField.getText());
			pair.getKey().setGenere(genereComboBox.getSelectionModel().getSelectedItem());
			pair.getKey().setDescrizione(descrizioneTextArea.getText());
			pair.getKey().setAutore(autoreComboBox.getSelectionModel().getSelectedItem());
			pair.getKey().setDataVendita(dataPicker.getValue());
			pair.getKey().setCanzoni(canzoniListView.getItems());
			pair.getKey().setPartecipanti(partecipantiListView.getItems());
			pair.getKey().setCopertina(copertinaLabel.getText());
			pair.getKey().setPrezzo(Double.parseDouble(prezzoTextField.getText().replace(',', '.')));
			pair.getKey().setID();
			myStage.close();
		}
	}

	private boolean inputValido() {
		if (titoloTextField.getText() == null || titoloTextField.getText().equals("")) {
			warningAlert("Devi inserire un titolo per il CD");
			return false;
		}
		if (genereComboBox.getSelectionModel().isEmpty()) {
			warningAlert("Devi scegliere un genere");
			return false;
		}
		if (autoreComboBox.getSelectionModel().isEmpty()) {
			warningAlert("Devi selezionare un autore");
			return false;
		}
		if (dataPicker.getValue() == null) {
			warningAlert("Devi selezionare una data di vendita");
			return false;
		}
		if (canzoniListView.getItems().isEmpty()) {
			warningAlert("Il CD deve contenere almeno una canzone");
			return false;
		}
		if (partecipantiListView.getItems().isEmpty()) {
			warningAlert("Ci deve essere almeno un partecipante");
			return false;
		}
		try {
			if (prezzoTextField.getText() == null || Double.parseDouble(prezzoTextField.getText().replace(',', '.')) < 0)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			warningAlert("Prezzo non valido");
			return false;
		}
		return true;
	}

	public void aggiungiCanzone() {
		String canzone = canzoniTextField.getText();
		if (canzone == null || canzone.length() == 0)
			warningAlert("Inserisci una canzone");
		else if (canzoniListView.getItems().contains(canzone))
			warningAlert("Stai inserendo una canzone duplicata");
		else
			canzoniListView.getItems().add(canzone);	
		canzoniTextField.setText("");
	}

	public void eliminaCanzone() {
		String canzone = canzoniListView.getSelectionModel().getSelectedItem();
		if (canzone != null) {
			canzoniListView.getItems().remove(canzone);
		} else {
			warningAlert("Nessuna canzone selezionata");
		}
		canzoniListView.getSelectionModel().clearSelection();
	}

	private void warningAlert(String messaggio) {
		Alert nessunoSelezionato = new Alert(AlertType.WARNING);
		nessunoSelezionato.setTitle("Attenzione");
		nessunoSelezionato.setContentText(null);
		nessunoSelezionato.setHeaderText(messaggio);
		nessunoSelezionato.show();
	}

	public void aggiungiCopertina() {
		FileChooser myChooser = new FileChooser();
		myChooser.setTitle("Seleziona immagine di copertina");
		File fileScelto = myChooser.showOpenDialog(myStage);
		if (fileScelto != null)
			copertinaLabel.setText(fileScelto.getAbsolutePath());
		else
			copertinaLabel.setText("Nessuna immagine selezionata");
	}

}
