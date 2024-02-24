package server.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventInfo {
    protected LocalDate data;
    protected LocalTime oraInizio;
    protected LocalTime oraFine;
    protected String nomeOrganizzatore;
    protected Double costoPartecipazione;
    protected String sala;
    protected Integer partPrevisti;
    protected TipoCatering catering;

    public EventInfo() {//costruttore per Gson
    }

    public void setNomeOrganizzatore(String nome) {
    	nomeOrganizzatore = nome;
    }

    public String getNomeOrganizzatore() {
        return nomeOrganizzatore;
    }

    public void setCostoPartecipazione(Double costo) {
    	costoPartecipazione = costo;
    }
    
    public Double getCostoPartecipazione() {
        return costoPartecipazione;
    }

    public void setPartPrevisti(Integer partecipanti) {
    	partPrevisti = partecipanti;
    }

    public Integer getPartPrevisti() {
        return partPrevisti;
    }

    public void setSala(String salaName) {
        this.sala = salaName;
    }
    
    public String getSala() {
        return sala;
    }

    public void setCatering(TipoCatering catering) {
        this.catering = catering;
    }

    public TipoCatering getCatering() {
        return catering;
    }

    public void setData(LocalDate data) {
    	this.data = data;
    }
    
    public LocalDate getData() {
        return data;
    }

    public void setOraInizio(LocalTime inizio) {
    	oraInizio = inizio;
    }

    public LocalTime getOraInizio() {
        return oraInizio;
    }
    
    public void setOraFine(LocalTime fine) {
    	oraFine = fine;
    }

    public LocalTime getOraFine() {
        return oraFine;
    }

}
