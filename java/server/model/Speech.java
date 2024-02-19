package server.model;

import java.util.Objects;

/**
 * Classe per la gestione degli Speech, un evento può avere più speech.
 */
public class Speech {
    private String titolo;
    private String relatore;
    private String descrizione;

    // Costruttore, getter e setter

    // Costruttore senza argomenti
    public Speech() {
    }

    // Costruttore con argomenti
    public Speech(String titolo, String relatore, String descrizione) {
        this.titolo = titolo;
        this.relatore = relatore;
        this.descrizione = descrizione;
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