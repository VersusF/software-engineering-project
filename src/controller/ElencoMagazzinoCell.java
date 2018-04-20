package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import model.CD;

public class ElencoMagazzinoCell extends ListCell<Pair<CD, Integer>> {
	@FXML private GridPane cellGridPane;
	@FXML private Label titoloLabel;
	@FXML private Label musicistaLabel;
	@FXML private Label giacenzaLabel;
	@FXML private Label prezzoLabel;
	@FXML private ImageView warningImageView;
	private FXMLLoader cellLoader;
	private final static String FXML_CELL_PATH = "/view/CellaRiepilogoMagazzino.fxml" ;

	@Override
	protected void updateItem(Pair<CD, Integer> prodotto, boolean empty) {
		super.updateItem(prodotto, empty);

		if(empty || prodotto == null) {
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
			titoloLabel.setText(prodotto.getKey().getTitolo());
			musicistaLabel.setText(prodotto.getKey().getAutore().getNomeArte());
			giacenzaLabel.setText(prodotto.getValue().toString());
			prezzoLabel.setText(String.format("%.2f €", prodotto.getKey().getPrezzo()));
			if (prodotto.getValue() > 1)
				warningImageView.setVisible(false);
			else 
				warningImageView.setVisible(true);
			Tooltip warningTooltip = new Tooltip("Giacenza inferiore a 2");
			Tooltip.install(warningImageView, warningTooltip);
			setText(null);
			setGraphic(cellGridPane);
		}
	}
}
