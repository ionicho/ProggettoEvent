package server.model;

import java.util.*;
import java.time.LocalDate;

public class Event {
	private String id;
    private String data;
    private String oraInizio;
    private String oraFine;
    private String nomeOrganizzatore;
    private List<Speech> elencoInterventi;
    private double costoPartecipazione;

    // Costruttore, getter e setter

    public Event(String id, String data, String oraInizio, String oraFine, String nomeOrganizzatore, List<Speech> elencoInterventi, double costoPartecipazione) {
        this.id= id;
    	this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.nomeOrganizzatore = nomeOrganizzatore;
        this.elencoInterventi = elencoInterventi;
        this.costoPartecipazione = costoPartecipazione;
    }

    // altri metodi come necessario
    
    // Getter   
    public String getId() {
    	return id;
    }
    
    
    public String getData() {
        return data;
    }

    public String getOraInizio() {
        return oraInizio;
    }

    public String getOraFine() {
        return oraFine;
    }

    public String getNomeOrganizzatore() {
        return nomeOrganizzatore;
    }

    public List<Speech> getElencoInterventi() {
        return elencoInterventi;
    }

    public double getCostoPartecipazione() {
        return costoPartecipazione;
    }
    
    @Override
    public String toString() {
        return "Evento{" +
                "id:'" + id + '\'' +
                ", data:" + data +
                ", ora Inizio:'" + oraInizio + '\'' +
                ", ora Fine:'" + oraFine + '\'' +
                ", nome Organizzatore:'" + nomeOrganizzatore + '\'' +
                ", elenco Interventi:" + elencoInterventi +
                ", costo Partecipazione:" + costoPartecipazione +
                '}';
    }

}


