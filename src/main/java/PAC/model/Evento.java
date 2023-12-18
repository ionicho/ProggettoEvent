package PAC.model;

import java.util.*;

public class Evento {
	private String id;
    private String data;
    private String oraInizio;
    private String oraFine;
    private String nomeOrganizzatore;
    private List<Intervento> elencoInterventi;
    private double costoPartecipazione;

    // Costruttore, getter e setter

    public Evento(String id, String data, String oraInizio, String oraFine, String nomeOrganizzatore, List<Intervento> elencoInterventi, double costoPartecipazione) {
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

    public List<Intervento> getElencoInterventi() {
        return elencoInterventi;
    }

    public double getCostoPartecipazione() {
        return costoPartecipazione;
    }
    
    @Override
    public String toString() {
        return "Evento{" +
                "id='" + id + '\'' +
                ", data='" + data + '\'' +
                ", oraInizio='" + oraInizio + '\'' +
                ", oraFine='" + oraFine + '\'' +
                ", nomeOrganizzatore='" + nomeOrganizzatore + '\'' +
                ", elencoInterventi=" + elencoInterventi +
                ", costoPartecipazione=" + costoPartecipazione +
                '}';
    }

}


