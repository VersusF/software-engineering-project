package model;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.util.Pair;

public class CD implements Serializable, Comparable<CD> {
	private int ID;
	private String titolo, genere, descrizione;
	private double prezzo;
	private Musicista autore;
	private LocalDate dataVendita;
	private String copertina = null;
	private ObservableList<String> canzoni = FXCollections.observableArrayList();
	private ObservableList<Pair<Musicista, String>> partecipanti = FXCollections.observableArrayList();

	public CD(){
	}
	
	public CD(String titolo, Musicista autore, String genere, LocalDate dataVendita) {
		this.titolo = titolo;
		this.autore = autore;
		this.genere = genere;
		this.dataVendita = dataVendita;
		setID();
		descrizione = "Nessuna descrizione.";
		Archivio.getArchivio().addGenere(genere);
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	
	public void setAutore(Musicista autore) {
		this.autore = autore;
	}
	
	public void setGenere(String genere) {
		this.genere = genere;
	}
	
	public void setDataVendita(LocalDate dataVendita) {
		this.dataVendita = dataVendita;
	}
	
	public void setID() {
		this.ID = titolo.hashCode() ^ genere.hashCode() ^ autore.hashCode();
		ID = Math.abs(ID);
	}
	
	public Musicista getAutore() {
		return autore;
	}
	public ObservableList<String> getCanzoni() {
		return canzoni;
	}
	public String getCopertina() {
		return copertina;
	}
	public LocalDate getDataVendita() {
		return dataVendita;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public String getGenere() {
		return genere;
	}
	public int getID() {
		return ID;
	}
	public ObservableList<Pair<Musicista, String>> getPartecipanti() {
		return partecipanti;
	}
	public String getTitolo() {
		return titolo;
	}

	public void setCanzoni(ObservableList<String> canzoni) {
		this.canzoni = canzoni;
	}

	public void setPartecipanti(ObservableList<Pair<Musicista, String>> partecipanti) {
		this.partecipanti = partecipanti;
		
		/*
		NON CI SERVE PIU!!!
		for (Pair<Musicista, String> p : partecipanti) {
			Archivio.getArchivio().addMusicista(p.getKey());
			Archivio.getArchivio().addStrumento(p.getValue());
		}
		*/
	}

	public void setCopertina(String copertina) {
		this.copertina = copertina;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	@Override
	public int hashCode() {
		return ID;
	}

	
	@Override
	public boolean equals(Object other) {
		if(other instanceof CD)
			return this.ID == ((CD) other).ID;
		return false;
	}
	
	/*
	 * Modifico il metodo writeObject di modo che
	 * sia possibile serializzare la classe CD
	 * nonostante contenga due campi non serializzabili
	 */
	private void writeObject(ObjectOutputStream outStream){
		try{
			outStream.writeInt(ID);
			outStream.writeObject(titolo);
			outStream.writeObject(genere);
			outStream.writeObject(descrizione);
			outStream.writeObject(autore);
			outStream.writeObject(dataVendita);
			outStream.writeObject(copertina);
			outStream.writeDouble(prezzo);
			outStream.writeObject(new ArrayList<String>(canzoni));
			outStream.writeObject(new ArrayList<Pair<Musicista, String>>(partecipanti));
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 * Modifico il metodo readObject di modo che
	 * sia possibile leggere la classe CD serializzata
	 */
	private void readObject(ObjectInputStream inStream){
		try{
			ID = inStream.readInt();
			titolo = (String) inStream.readObject();
			genere = (String) inStream.readObject();
			descrizione = (String) inStream.readObject();
			autore = (Musicista) inStream.readObject();
			dataVendita = (LocalDate) inStream.readObject();
			copertina = (String) inStream.readObject();
			prezzo = inStream.readDouble();
			canzoni = FXCollections.observableArrayList((ArrayList<String>) inStream.readObject());
			partecipanti = FXCollections.observableArrayList((ArrayList<Pair<Musicista, String>>) inStream.readObject());
		} catch(Exception e){
			if(!(e instanceof EOFException))
				e.printStackTrace();
		}
	}

	@Override
	public int compareTo(CD o) {
		if (titolo.equals(o.titolo))
			return autore.compareTo(o.autore);
		return titolo.compareToIgnoreCase(o.titolo);
	}

	public double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(double prezzo) {
		this.prezzo = prezzo;
	}

}
