package controller;

import java.net.URL;
import java.util.Calendar;
import java.util.HashSet;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Archivio;
import model.Musicista;

public class ModificaMusicistaController implements Initializable {
	@FXML private Button aggiungiButton;
	@FXML private TextField nomeTextField;
	@FXML private ComboBox<String> genereComboBox;
	@FXML private TextField annoTextField;
	@FXML private ListView<String> strumentiListView;
	@FXML private Button aggiungiStrumentoButton;
	@FXML private Button eliminaStrumentoButton;
	@FXML private ComboBox<String> strumentoComboBox;

	private Stage myStage;
	private Musicista musicista;
	private Archivio archivio = Archivio.getArchivio();
	private ObservableList<String> strumentiSuonatiList;
	private ObservableList<String> strumentiSuonabiliList;

	public void setStage(Stage modificaMusicistaStage) {
		this.myStage = modificaMusicistaStage;
	}

	/**
	 * Salvo l'oggetto da modificare.
	 * Se � in corso l'aggiunta di un nuovo musicista,
	 * i campi verranno riempiti con informazioni di default.
	 * 
	 * @param musicista
	 */
	public void setMusicista(Musicista musicista) {
		if (musicista == null)
			this.musicista = null;
		else {
			this.musicista = musicista;
			myStage.setTitle("Modifica musicista");
			aggiungiButton.setText("Conferma modifica");
			strumentiSuonatiList.addAll(musicista.getStrumentiSuonati());
			for (String s : strumentiSuonatiList)
				strumentiSuonabiliList.remove(s);
			nomeTextField.setText(musicista.getNomeArte());
			annoTextField.setText(musicista.getAnnoNascita()+"");
			genereComboBox.getSelectionModel().select(musicista.getGenerePrincipale());
		}
	}

	public void addStrumento() {
		String strumento = strumentoComboBox.getSelectionModel().getSelectedItem();
		if (strumento != null) {
			strumentiSuonabiliList.remove(strumento);
			strumentiSuonatiList.add(strumento);
			strumentoComboBox.getSelectionModel().clearSelection();
		} else {
			nessunSelezionatoAlert();
		}
	}

	private void nessunSelezionatoAlert() {
		Alert nessunoSelezionato = new Alert(AlertType.WARNING);
		nessunoSelezionato.setTitle("Attenzione");
		nessunoSelezionato.setContentText(null);
		nessunoSelezionato.setHeaderText("Nessuno strumento selezionato");
		nessunoSelezionato.show();
	}

	public void removeStrumento() {
		String strumento = strumentiListView.getSelectionModel().getSelectedItem();
		if (strumento != null) {
			strumentiSuonatiList.remove(strumento);
			strumentiSuonabiliList.add(strumento);
			strumentiListView.getSelectionModel().clearSelection();
		} else {
			nessunSelezionatoAlert();
		}
	}

	public void aggiungi(){
		if(inputValido()){
			if (musicista == null)
				musicista = new Musicista();
			musicista.setNomeArte(nomeTextField.getText());
			musicista.setGenerePrincipale(genereComboBox.getSelectionModel().getSelectedItem());
			musicista.setAnnoNascita(Integer.parseInt(annoTextField.getText()));
			musicista.setStrumentiSuonati(new HashSet<>(strumentiListView.getItems()));
			myStage.close();
		}
	}


	private boolean inputValido() {
		//TODO alert
		if (nomeTextField.getText() == null || nomeTextField.getText().equals("")) {
			warningAlert("Inserisci un nome d'arte");
			return false;
		}
		if (annoTextField.getText() == null || annoTextField.getText().equals("")){
			warningAlert("Inserisci un anno di nascita");
			return false;
		}
		int anno;
		try {
			anno = Integer.parseInt(annoTextField.getText());
			if (anno > Calendar.getInstance().get(Calendar.YEAR))
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			warningAlert("L'anno di nascita deve essere un numero valido");
			return false;
		}
		if (genereComboBox.getSelectionModel().getSelectedItem() == null){
			warningAlert("Seleziona un genere");
			return false;
		}
		if (strumentiSuonatiList.isEmpty()){
			warningAlert("Devi inserire almeno uno strumento");
			return false;
		}
		for(Musicista m: archivio.getMusicisti()){
			if(m != musicista && m.equals(new Musicista(nomeTextField.getText(), anno, genereComboBox.getSelectionModel().getSelectedItem()))){
				warningAlert("Stai inserendo un duplicato");
				return false;
			}
		}
		return true;
	}

	private void warningAlert(String messaggio) {
		Alert nessunoSelezionato = new Alert(AlertType.WARNING);
		nessunoSelezionato.setTitle("Attenzione");
		nessunoSelezionato.setContentText(null);
		nessunoSelezionato.setHeaderText(messaggio);
		nessunoSelezionato.show();
	}

	public Musicista musicistaAggiunto() {
		//System.out.println(musicista + musicista.getStrumentiSuonati().toString());
		return musicista;
	}

	public void keyboardEnterPressed(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER))
			aggiungiButton.fire();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		genereComboBox.setItems(archivio.getGeneri());
		strumentiSuonatiList = FXCollections.observableArrayList();
		strumentiListView.setItems(strumentiSuonatiList);
		strumentiSuonabiliList = FXCollections.observableArrayList();
		strumentiSuonabiliList.addAll(archivio.getStrumenti());
		strumentoComboBox.setItems(strumentiSuonabiliList);
	}

}
