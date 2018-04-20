package controller;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.Archivio;
import model.CD;
import model.Magazzino;
import model.Musicista;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MagazzinoController implements Initializable {
	private Magazzino magazzino;
	private Stage primaryStage;
	private boolean cercato = false;

	private final static String GESTIONE_GENERI_STRUMENTI_FXML_PATH = "/view/GestioneGeneriStrumenti.fxml";
	private static final String GESTIONE_MUSICISTI_FXML_PATH = "/view/GestioneMusicisti.fxml";
	private static final String MODIFICA_CD_FXML_PATH = "/view/ModificaCD.fxml";

	@FXML private Button creaNuovoButton;
	@FXML private Button gestisciStrumentiButton;
	@FXML private Button gestisciMusicistiButton;
	@FXML private Button gestisciGeneriButton;
	@FXML private TextField ricercaTextField;
	@FXML private Button cercaButton;
	@FXML private ListView<Pair<CD, Integer>> elencoListView;
	@FXML private Label titoloLabel;
	@FXML private AnchorPane copertinaAnchorPane;
	@FXML private ImageView copertinaImageView;
	@FXML private Label autoreLabel;
	@FXML private Label genereLabel;
	@FXML private Label idLabel;
	@FXML private Label dataVenditaLabel;
	@FXML private Label descrizioneLabel;
	@FXML private ListView<String> canzoniListView;
	@FXML private ListView<Pair<Musicista, String>> partecipantiListView;
	@FXML private Button rifornisciButton;
	@FXML private Button modificaButton;
	@FXML private Button eliminaButton;
	@FXML private Label prezzoLabel;
	@FXML private BorderPane dettagliCDBorderPane;
	@FXML private SplitPane splitterSplitPane;
	@FXML private Label titoloElencoLabel;
	@FXML private Label autoreElencoLabel;
	@FXML private Label prezzoElencoLabel;
	@FXML private Label giacenzaElencoLabel;

	public MagazzinoController() {
		magazzino = Magazzino.getMagazzino();

		// DATI DI TEST =================================================================
		/*
		Musicista autore = new Musicista("MicMarte", 1996, "schifo");
		HashSet<String> strumenti = new HashSet<>();
		strumenti.add("Chitarra");
		autore.setStrumentiSuonati(strumenti);

		CD cdDiProva = new CD("Titolo caccoso", autore, "rock", new Date(88888888)); 
		cdDiProva.setDescrizione("come dice il titolo il mike ha fatto un cd caccoso");

		ObservableList<String> canzoni = FXCollections.observableArrayList();
		canzoni.addAll("canzone 1", "o bella ciao", "quanto vorrei aver finito", "e bermi tre birre", "e un panino al salame");
		cdDiProva.setCanzoni(canzoni);

		cdDiProva.setPartecipanti(FXCollections.observableArrayList(new Pair<Musicista, String>(autore, "Campana")));

		magazzino.addCD(cdDiProva, 10);
		magazzino.addCD(cdDiProva, 30);
		magazzino.addCD(new CD("titolo molto lungo di prova", autore, "genere molto lungo di prova", new Date(777777777)), 88);
		 */
		// ==============================================================================
	}

	public void setStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		elencoListView.setItems(magazzino.getGiacenza());
		sortByTitle();
		// Imposto il formato delle celle della ListView
		elencoListView.setCellFactory(myListView -> new ElencoMagazzinoCell());

		// Sul lato destro dello SplitPane appariranno i dettagli del CD selezionato
		elencoListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showCDDetails(newValue));

	}

	/**
	 * Mostra i dettagli del CD selezionato.
	 * @param myCD CD da cui vengono ricavati i dettagli.
	 */
	private void showCDDetails(Pair<CD, Integer> myCD) {
		if (myCD == null) {
			autoreLabel.setText("");
			genereLabel.setText("");
			idLabel.setText("");
			dataVenditaLabel.setText("");
			prezzoLabel.setText("");
			descrizioneLabel.setText("Descrizione");
			titoloLabel.setText("DETTAGLI CD");
			canzoniListView.setItems(null);
			partecipantiListView.setItems(null);
			copertinaImageView.setImage(null);
		} else {
			titoloLabel.setText(myCD.getKey().getTitolo());
			autoreLabel.setText(myCD.getKey().getAutore().getNomeArte());
			genereLabel.setText(myCD.getKey().getGenere());
			idLabel.setText(myCD.getKey().getID()+"");
			dataVenditaLabel.setText(myCD.getKey().getDataVendita().toString());
			prezzoLabel.setText(String.format("%.2f €", myCD.getKey().getPrezzo()));
			descrizioneLabel.setText(myCD.getKey().getDescrizione());
			canzoniListView.setItems(myCD.getKey().getCanzoni());
			Image copertina = new Image("file:" + myCD.getKey().getCopertina());
			if (myCD.getKey().getCopertina() == null || myCD.getKey().getCopertina().equals("Nessuna immagine selezionata"))
				copertina = new Image(Main.LOGO_ICONA_PATH);
			//System.out.println(myCD.getKey().getCopertina());
			copertinaImageView.setImage(copertina);
			partecipantiListView.setItems(myCD.getKey().getPartecipanti());
			partecipantiListView.setCellFactory(partecipantiListView -> new ElencoPartecipantiCell());
		}
	}

	public void elimina() {
		Pair<CD, Integer> selezionato = elencoListView.getSelectionModel().getSelectedItem();
		if (selezionato == null) {
			Alert nessunoSelezionato = new Alert(AlertType.WARNING);
			nessunoSelezionato.setTitle("Attenzione");
			nessunoSelezionato.setContentText(null);
			nessunoSelezionato.setHeaderText("Nessun CD selezionato");
			nessunoSelezionato.show();
		}
		else {
			Alert confirmation = new Alert(AlertType.CONFIRMATION);
			confirmation.setTitle("Conferma");
			confirmation.setHeaderText(null);
			confirmation.setContentText("Sei sicuro di voler eliminare il cd");
			Optional<ButtonType> result = confirmation.showAndWait();
			if (result.get() == ButtonType.OK) {
				magazzino.cancella(selezionato);
				if (elencoListView.getSelectionModel().isEmpty())
					showCDDetails(null);
			}
		}
	}

	public void rifornisci() {
		Pair<CD, Integer> selezionato = elencoListView.getSelectionModel().getSelectedItem();
		if (selezionato != null) {
			TextInputDialog rifornimento = new TextInputDialog();
			rifornimento.setTitle("Rifornisci prodotto");
			rifornimento.setHeaderText(null);
			rifornimento.setContentText("Inserisci la quantità del prodotto da aggiungere: ");
			Optional<String> result = rifornimento.showAndWait();
			if (result.isPresent()) {
				int numeroInserito;
				try {
					numeroInserito = Integer.parseInt(result.get());
					if (numeroInserito < 1)
						throw new NumberFormatException();
				} catch (NumberFormatException e) {
					notificaServeNumero();
					rifornisci();
					return;
				}
				magazzino.rifornisci(selezionato, numeroInserito);
			}
		}

	}

	/**
	 * Apre una finestra di avviso per indicare all'utente
	 * che il numero inserito non � valido.
	 */
	private void notificaServeNumero() {
		Alert numeroInvalido = new Alert(AlertType.WARNING);
		numeroInvalido.setTitle("Attenzione");
		numeroInvalido.setContentText(null);
		numeroInvalido.setHeaderText("Devi inserire un numero valido");
		numeroInvalido.showAndWait();
	}

	public void aggiungiCD(){
		Pair<CD, Integer> pairNew = showModificaCD(null); 
		if (pairNew != null)
			magazzino.addCD(pairNew.getKey(), pairNew.getValue());
	}


	public void modificaCD(){
		Pair<CD, Integer> cdSelezionato = elencoListView.getSelectionModel().getSelectedItem();
		if(cdSelezionato != null) {
			if (showModificaCD(cdSelezionato) != null){
				elencoListView.refresh();
				showCDDetails(cdSelezionato);
			}
		}
		else {
			Alert nessunoSelezionatoAlert = new Alert(AlertType.WARNING);
			//alert.initOwner(mainApp.getPrimaryStage()); TODO
			nessunoSelezionatoAlert.setTitle("Attenzione");
			nessunoSelezionatoAlert.setHeaderText("Nessun CD selezionato.");
			nessunoSelezionatoAlert.setContentText("Selezionare un CD dall'elenco.");

			nessunoSelezionatoAlert.showAndWait();
		}
		elencoListView.getSelectionModel().clearSelection();
	}


	private Pair<CD, Integer> showModificaCD(Pair<CD, Integer> pair) {
		FXMLLoader modificaCDLoader = new FXMLLoader();
		modificaCDLoader.setLocation(MagazzinoController.class.getResource(MODIFICA_CD_FXML_PATH));
		Parent root = null;
		try{
			root = modificaCDLoader.load();
		} catch(IOException e){
			System.err.println("Non ho trovato il file: + " + MODIFICA_CD_FXML_PATH);
		}

		Stage modificaCDStage = new Stage();
		//modificaCDStage.setTitle("Nuovo CD");
		modificaCDStage.setScene(new Scene(root));
		modificaCDStage.initModality(Modality.APPLICATION_MODAL);

		ModificaCDController controller = modificaCDLoader.getController();
		controller.setStage(modificaCDStage);
		controller.setCD(pair);

		modificaCDStage.showAndWait();

		return controller.cdAggiunto();
	}



	public void gestisciGeneri() {
		FXMLLoader gestisciGeneriLoader = new FXMLLoader();
		gestisciGeneriLoader.setLocation(MagazzinoController.class.getResource(GESTIONE_GENERI_STRUMENTI_FXML_PATH));
		Parent root = null;
		gestisciGeneriLoader.setController(new GestisciGeneriController());
		try {
			root = gestisciGeneriLoader.load();
		}  catch (IOException e) {
			System.err.println("Non ho trovato il file " + GESTIONE_GENERI_STRUMENTI_FXML_PATH);
		}

		Stage gestisciGeneriStage = new Stage();
		gestisciGeneriStage.setTitle("Gestione generi");
		gestisciGeneriStage.setScene(new Scene(root));
		gestisciGeneriStage.initModality(Modality.APPLICATION_MODAL);
		gestisciGeneriStage.initOwner(primaryStage);
		gestisciGeneriStage.showAndWait();
	}

	public void gestisciStrumenti() {
		FXMLLoader gestisciStrumentiLoader = new FXMLLoader();
		gestisciStrumentiLoader.setLocation(MagazzinoController.class.getResource(GESTIONE_GENERI_STRUMENTI_FXML_PATH));
		Parent root = null;
		gestisciStrumentiLoader.setController(new GestisciStrumentiController());
		try {
			root = gestisciStrumentiLoader.load();
		}  catch (IOException e) {
			System.err.println("Non ho trovato il file " + GESTIONE_GENERI_STRUMENTI_FXML_PATH);
		}

		Stage gestisciStrumentiStage = new Stage();
		gestisciStrumentiStage.setTitle("Gestione strumenti");
		gestisciStrumentiStage.setScene(new Scene(root));
		gestisciStrumentiStage.initModality(Modality.APPLICATION_MODAL);
		gestisciStrumentiStage.initOwner(primaryStage);
		gestisciStrumentiStage.showAndWait();
	}

	public void gestisciMusicisti() {
		FXMLLoader gestisciMusicistiLoader = new FXMLLoader();
		gestisciMusicistiLoader.setLocation(MagazzinoController.class.getResource(GESTIONE_MUSICISTI_FXML_PATH));
		Parent root = null;
		gestisciMusicistiLoader.setController(new GestisciMusicistiController());
		try {
			root = gestisciMusicistiLoader.load();
		}  catch (IOException e) {
			System.err.println("Non ho trovato il file " + GESTIONE_MUSICISTI_FXML_PATH);
		}

		Stage gestisciMusicistiStage = new Stage();
		gestisciMusicistiStage.setTitle("Gestione musicisti");
		gestisciMusicistiStage.setScene(new Scene(root));
		gestisciMusicistiStage.initModality(Modality.APPLICATION_MODAL);
		gestisciMusicistiStage.initOwner(primaryStage);
		gestisciMusicistiStage.showAndWait();
	}

	public void salvaDati() {
		Archivio.getArchivio().salvaDati();
		Magazzino.getMagazzino().salvaDati();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Conferma");
		alert.setHeaderText(null);
		alert.setContentText("I dati sono stati salvati correttamente");
		alert.showAndWait();
	}
	
	private void warningAlert(String messaggio) {
		Alert nessunTesto = new Alert(AlertType.WARNING);
		nessunTesto.setTitle("Attenzione");
		nessunTesto.setHeaderText(messaggio);
		nessunTesto.setContentText(null);
		nessunTesto.showAndWait();
	}
	
	public void ricerca() {
		if (cercato) {
			cercato = false;
			cercaButton.setText("Cerca");
			elencoListView.setItems(magazzino.getGiacenza());
			ricercaTextField.setEditable(true);
			ricercaTextField.setVisible(true);
			ricercaTextField.setManaged(true);
			return;
		}
		String testo = ricercaTextField.getText();
		if (testo == null || testo.length() < 3) {
			warningAlert("Devi inserire almeno 3 caratteri nella barra di ricerca");
			return;
		}
		testo = testo.toUpperCase();
		cercato = true;
		cercaButton.setText("Annulla ricerca");
		ricercaTextField.setText(null);
		ricercaTextField.setEditable(false);
		ricercaTextField.setManaged(false);
		ricercaTextField.setVisible(false);
		ObservableList<Pair<CD, Integer>> risultatoRicerca = FXCollections.observableArrayList(elencoListView.getItems());
		for (Pair<CD, Integer> entry : elencoListView.getItems()) {
			CD cd = entry.getKey();
			if (!cd.getTitolo().toUpperCase().contains(testo) && !cd.getAutore().getNomeArte().toUpperCase().contains(testo) && !cd.getGenere().toUpperCase().contains(testo))
				risultatoRicerca.remove(entry);
		}
		elencoListView.setItems(risultatoRicerca);
	}
	
	public void sortByTitle() {
		resetElencoFont();
		titoloElencoLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
		titoloElencoLabel.setUnderline(true);
		FXCollections.sort(elencoListView.getItems(), new Comparator<Pair<CD, Integer>>() {

			@Override
			public int compare(Pair<CD, Integer> o1, Pair<CD, Integer> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
			
		});
	}
	
	private void resetElencoFont() {
		titoloElencoLabel.setFont(Font.font("System", FontWeight.NORMAL, 13));
		autoreElencoLabel.setFont(Font.font("System", FontWeight.NORMAL, 13));
		prezzoElencoLabel.setFont(Font.font("System", FontWeight.NORMAL, 13));
		giacenzaElencoLabel.setFont(Font.font("System", FontWeight.NORMAL, 13));
		titoloElencoLabel.setUnderline(false);
		autoreElencoLabel.setUnderline(false);
		prezzoElencoLabel.setUnderline(false);
		giacenzaElencoLabel.setUnderline(false);
	}

	public void sortByAuthor() {
		resetElencoFont();
		autoreElencoLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
		autoreElencoLabel.setUnderline(true);
		FXCollections.sort(elencoListView.getItems(), new Comparator<Pair<CD, Integer>>() {

			@Override
			public int compare(Pair<CD, Integer> o1, Pair<CD, Integer> o2) {
				return o1.getKey().getAutore().compareTo(o2.getKey().getAutore());
			}
			
		});
	}
	
	public void sortByPrice() {
		resetElencoFont();
		prezzoElencoLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
		prezzoElencoLabel.setUnderline(true);
		FXCollections.sort(elencoListView.getItems(), new Comparator<Pair<CD, Integer>>() {

			@Override
			public int compare(Pair<CD, Integer> o1, Pair<CD, Integer> o2) {
				return  new Double(o1.getKey().getPrezzo()).compareTo(o2.getKey().getPrezzo());
			}
			
		});
	}
	
	public void sortByRemaining() {
		resetElencoFont();
		giacenzaElencoLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
		giacenzaElencoLabel.setUnderline(true);
		FXCollections.sort(elencoListView.getItems(), new Comparator<Pair<CD, Integer>>() {

			@Override
			public int compare(Pair<CD, Integer> o1, Pair<CD, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
			
		});
	}
}
