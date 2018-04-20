package model;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

public class Magazzino {
	private static Magazzino ilSoloEUnico = null;
	private ObservableList<Pair<CD, Integer>> giacenza;
	
	private static final String CD_DATA_PATH = "data/cd.ser";
	
	// Costruttore privato
	private Magazzino(){
		giacenza = FXCollections.observableArrayList();
		
		// Carico i cd dall'apposito file
		try{
			/* Dato che le ObservableList non sono serializzabili,
			 * i cd sono salvati nel file come lista.
			 */
			FileInputStream fileIn = new FileInputStream(CD_DATA_PATH);
			ObjectInputStream inStream = new ObjectInputStream(fileIn);
			List<Pair<CD, Integer>> temp = (List<Pair<CD, Integer>>) inStream.readObject();
			inStream.close();
			fileIn.close();
			
			// Una volta letta la lista, ne aggiungo il contenuto all'ObservableList
			for(Pair<CD, Integer> cd: temp)
				giacenza.add(cd);
		} catch(IOException e){
			if(e instanceof FileNotFoundException)
				System.err.println("Non sono riuscito a trovare il file: " + CD_DATA_PATH);
			else if(e instanceof EOFException){
			}
			else
				System.err.println("Non sono riuscito a recuperare i dati dal file: " + CD_DATA_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo per ottenere l'unica instanza esistente del magazzino
	 * @return
	 */
	public static Magazzino getMagazzino(){
		if (ilSoloEUnico == null)
			ilSoloEUnico = new Magazzino();
		return ilSoloEUnico;
	}
	
	public ObservableList<Pair<CD, Integer>> getGiacenza() {
		return giacenza;
	}
	public void addCD(CD cd, int quantita){
		if(giacenza.contains(new Pair<CD, Integer>(cd, quantita)))
			return;
		
		giacenza.add(new Pair<CD, Integer>(cd, quantita));
	}
	public void cancella(Pair<CD, Integer> selezionato) {
		giacenza.remove(selezionato);
	}
	public void rifornisci(Pair<CD, Integer> prodotto, int quantita) {
		giacenza.remove(prodotto);
		giacenza.add(new Pair<CD, Integer>(prodotto.getKey(), prodotto.getValue()+quantita));
	}

	public void salvaDati() {
		// Salvo i generi
		try{
			FileOutputStream fileOut = new FileOutputStream(CD_DATA_PATH);
			ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
			outStream.writeObject(new ArrayList<Pair<CD, Integer>>(giacenza));
			outStream.close();
			fileOut.close();
		} catch(FileNotFoundException e){
			System.err.println("Non ho trovato il file: " + CD_DATA_PATH);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
