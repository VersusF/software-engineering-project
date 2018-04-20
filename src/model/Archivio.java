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

/**
 * Archivio di tutti i generi, strumenti e musicisti
 * pattern: Singleton
 * @author filippo
 */
public class Archivio {
	private ObservableList<String> generi;
	private ObservableList<String> strumenti;
	private ObservableList<Musicista> musicisti;
	private static Archivio ilSoloEUnico = null;
	
	private static final String GENERI_DATA_PATH = "data/generi.ser";
	private static final String STRUMENTI_DATA_PATH = "data/strumenti.ser";
	private static final String MUSICISTI_DATA_PATH = "data/musicisti.ser";
	
	/**
	 * costruttore privato
	 */
	private Archivio(){
		// Carico i generi dall'apposito file 
		generi = FXCollections.observableArrayList();
		try{
			/* Dato che le ObservableList non sono serializzabili,
			 * i generi sono salvati nel file come lista.
			 */
			FileInputStream fileIn = new FileInputStream(GENERI_DATA_PATH);
			ObjectInputStream inStream = new ObjectInputStream(fileIn);
			List<String> temp = (List<String>) inStream.readObject();
			inStream.close();
			fileIn.close();
			
			// Una volta letta la lista, ne aggiungo il contenuto all'ObservableList
			for(String genere: temp)
				generi.add(genere);
		} catch(IOException e){
			if(e instanceof FileNotFoundException)
				System.err.println("Non sono riuscito a trovare il file: " + GENERI_DATA_PATH);
			else if(e instanceof EOFException){
			}
			else
				System.err.println("Non sono riuscito a recuperare i dati dal file: " + GENERI_DATA_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Carico gli strumenti dall'apposito file
		strumenti = FXCollections.observableArrayList();
		try{
			FileInputStream fileIn = new FileInputStream(STRUMENTI_DATA_PATH);
			ObjectInputStream inStream = new ObjectInputStream(fileIn);
			List<String> temp = (List<String>) inStream.readObject();
			inStream.close();
			fileIn.close();
			
			// Una volta letta la lista, ne aggiungo il contenuto all'ObservableList
			for(String strumento: temp)
				strumenti.add(strumento);
		} catch(IOException e){
			if(e instanceof FileNotFoundException)
				System.err.println("Non sono riuscito a trovare il file: " + STRUMENTI_DATA_PATH);
			else if(e instanceof EOFException){
			}
			else
				System.err.println("Non sono riuscito a recuperare i dati dal file: " + STRUMENTI_DATA_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Carico i musicisti dall'apposito file
		musicisti = FXCollections.observableArrayList();
		try{
			FileInputStream fileIn = new FileInputStream(MUSICISTI_DATA_PATH);
			ObjectInputStream inStream = new ObjectInputStream(fileIn);
			List<Musicista> temp = (List<Musicista>) inStream.readObject();
			inStream.close();
			fileIn.close();
			
			// Una volta letta la lista, ne aggiungo il contenuto all'ObservableList
			for(Musicista musicista: temp)
				musicisti.add(musicista);
		} catch(IOException e){
			if(e instanceof FileNotFoundException)
				System.err.println("Non sono riuscito a trovare il file: " + MUSICISTI_DATA_PATH);
			else if(e instanceof EOFException){
			}
			else
				System.err.println("Non sono riuscito a recuperare i dati dal file: " + MUSICISTI_DATA_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * getter per l'archivio
	 * Se non esiste lo inizializza
	 * @return l'archivio statico
	 */
	public static Archivio getArchivio(){
		if (ilSoloEUnico == null)
			ilSoloEUnico = new Archivio();
		return ilSoloEUnico;
	}
	/**
	 * aggiungi un genere all'archivio dei generi
	 * @param genere da aggiungere
	 */
	public void addGenere(String genere){
		// Se il genere � gi� in archivio, non faccio nulla
		if(generi.contains(genere))
			return;
		
		generi.add(genere);
	}
	/**
	 * aggiungi uno strumento all'archivio degli strumenti
	 * @param strumento da aggiungere
	 */
	public void addStrumento(String strumento){
		// Se lo strumento � gi� in archivio, non faccio nulla
		if(strumenti.contains(strumento))
			return;
		
		strumenti.add(strumento);
	}
	/**
	 * aggiungi un musicista all'archivio dei musicisti
	 * @param musicista da aggiungere
	 */
	public void addMusicista(Musicista musicista){
		if (musicisti.contains(musicista))
			return;
		
		musicisti.add(musicista);
	}
	
	/**
	 * Quando l'utente chiude la finestra principale del programma,
	 * salvo tutti i generi, gli strumenti ed i musicisti negli appositi file.
	 */
	public void salvaDati(){
		// Salvo i generi
		try{
			FileOutputStream fileOut = new FileOutputStream(GENERI_DATA_PATH);
			ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
			outStream.writeObject(new ArrayList<String>(generi));
			outStream.close();
			fileOut.close();
		} catch(FileNotFoundException e){
			System.err.println("Non ho trovato il file: " + GENERI_DATA_PATH);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		// Salvo gli strumenti
		try{
			FileOutputStream fileOut = new FileOutputStream(STRUMENTI_DATA_PATH);
			ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
			outStream.writeObject(new ArrayList<String>(strumenti));
			outStream.close();
			fileOut.close();
		} catch(FileNotFoundException e){
			System.err.println("Non ho trovato il file: " + STRUMENTI_DATA_PATH);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		// Salvo i musicisti
		try{
			FileOutputStream fileOut = new FileOutputStream(MUSICISTI_DATA_PATH);
			ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
			outStream.writeObject(new ArrayList<Musicista>(musicisti));
			outStream.close();
			fileOut.close();
		} catch(FileNotFoundException e){
			System.err.println("Non ho trovato il file: " + MUSICISTI_DATA_PATH);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// getters
	public ObservableList<Musicista> getMusicisti() {
		return musicisti;
	}
	public ObservableList<String> getStrumenti() {
		return strumenti;
	}
	public ObservableList<String> getGeneri() {
		return generi;
	}
	// removers
	public void removeGenere(String genere) {
		generi.remove(genere);
	}
	public void removeStrumento(String strumento) {
		strumenti.remove(strumento);
	}
	public void removeMusicista(Musicista musicista) {
		musicisti.remove(musicista);
	}
}
