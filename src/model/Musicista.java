package model;

import java.io.Serializable;
import java.util.HashSet;

import com.sun.media.jfxmedia.effects.EqualizerBand;

public class Musicista implements Serializable, Comparable<Musicista> {
	private String nomeArte, generePrincipale;
	private int annoNascita;
	private HashSet<String> strumentiSuonati;
	
	public Musicista(String nomeArte, int annoNascita, String generePrincipale) {
		this.nomeArte = nomeArte;
		this.annoNascita = annoNascita;
		this.generePrincipale = generePrincipale;
		Archivio.getArchivio().addGenere(generePrincipale);
		strumentiSuonati = new HashSet<>();
	}
	
	public Musicista() {
		this.nomeArte = "";
		this.annoNascita = 0;
		this.generePrincipale = "";
		strumentiSuonati = new HashSet<>();
	}

	public void setStrumentiSuonati(HashSet<String> strumentiSuonati) {
		this.strumentiSuonati = strumentiSuonati;
		for (String strumento : strumentiSuonati)
			Archivio.getArchivio().addStrumento(strumento);
	}
	
	public void setNomeArte(String nomeArte) {
		this.nomeArte = nomeArte;
	}
	public void setGenerePrincipale(String generePrincipale) {
		this.generePrincipale = generePrincipale;
	}
	public void setAnnoNascita(int annoNascita) {
		this.annoNascita = annoNascita;
	}
	
	public String getNomeArte() {
		return nomeArte;
	}
	public String getGenerePrincipale() {
		return generePrincipale;
	}
	public int getAnnoNascita() {
		return annoNascita;
	}
	public HashSet<String> getStrumentiSuonati() {
		return strumentiSuonati;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Musicista)
			return nomeArte.equals(((Musicista) other).nomeArte) &&
					annoNascita == ((Musicista) other).annoNascita &&
					generePrincipale.equals(((Musicista) other).generePrincipale);
		return false;
	}
	
	@Override
	public int hashCode() {
		return nomeArte.hashCode() ^ annoNascita ^ generePrincipale.hashCode();
	}
	
	public String toString(){
		return nomeArte;
	}

	@Override
	public int compareTo(Musicista o) {
		return nomeArte.compareToIgnoreCase(o.nomeArte);
	}
	
}
