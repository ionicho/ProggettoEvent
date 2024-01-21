package server.model;

/**
 * Classe per la gestione degli Speech, un evento può avere più speech.
 */
class Speech {
    protected String titolo;
    protected String relatore;
    protected String descrizione;

    // Costruttore, getter e setter

    public Speech(String titolo, String relatore, String descrizione) {
        this.titolo = titolo;
        this.relatore = relatore;
        this.descrizione = descrizione;
    }

    // altri metodi come necessario
}
