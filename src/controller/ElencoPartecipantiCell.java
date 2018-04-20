package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import model.Musicista;

public class ElencoPartecipantiCell extends ListCell<Pair<Musicista, String>>{
	@FXML private GridPane partecipantiGridPane;
	@FXML private Label musicistaLabel;
	@FXML private Label strumentoLabel;
	private FXMLLoader cellLoader;
	private final static String FXML_CELL_PATH = "/view/CellaPartecipante.fxml" ;
	
	@Override
	protected void updateItem(Pair<Musicista, String> partecipante, boolean empty) {
		super.updateItem(partecipante, empty);
		
		if(empty || partecipante == null) {
			setText(null);
			setGraphic(null);
		}
		else {
			if(cellLoader == null) {
				cellLoader = new FXMLLoader();
				cellLoader.setLocation(ElencoMagazzinoCell.class.getResource(FXML_CELL_PATH));
				cellLoader.setController(this);
				try {
					cellLoader.load();
				} catch(IOException e) {
					System.err.println("Non ho trovato il file: " + FXML_CELL_PATH);
				}
			}
			musicistaLabel.setText(partecipante.getKey().getNomeArte());
			strumentoLabel.setText(partecipante.getValue());

			setText(null);
			setGraphic(partecipantiGridPane);
		}
	}
}
