package server.model;

import java.util.Objects;

/**
 * Classe per la gestione degli Speech, un evento può avere più speech.
 */

public class Speech {
    private String titolo;
    private String relatore;
    private String descrizione;


    public Speech() {
        // Costruttore senza parametri per la deserializzazione con Gson
        // e per la creazione di un oggetto vuoto
    	}

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String s){
        titolo=s;
    }

    public String getRelatore() {
        return relatore;
    }

    public void setRelatore(String s){
        relatore=s;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String s){
        descrizione=s;
    }

    @Override
    public String toString() {
        return "{"
                + "titolo: " + titolo + ", "
                + "relatore: " + relatore + ", "
                + "descrizione: " + descrizione
                + "}";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Speech speech = (Speech) o;
        return Objects.equals(titolo, speech.titolo) &&
            Objects.equals(relatore, speech.relatore) &&
            Objects.equals(descrizione, speech.descrizione);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titolo, relatore, descrizione);
    }
}