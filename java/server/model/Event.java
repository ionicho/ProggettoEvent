package server.model;

import java.util.*;

/**
 * Classe per la gestione degli eventi.
 */

public class Event extends EventInfo implements HasName {
	private String nome;

    private List<Speech> elencoInterventi;

    // Costruttore senza parametri per la deserializzazione con Gson
    public Event() {
 	   this.elencoInterventi = new ArrayList <>();
    }
    
    // Costruttore con parametro String per creare nuovi eventi.
    // Il parametro 's' non viene utilizzato, serve solo per distinguere questo costruttore dal costruttore senza parametri.
    public Event (String s) {//NOSONAR
	   this.nome = Singleton.getInstance().getNext(this.getClass().getSimpleName());
	   this.elencoInterventi = new ArrayList <>();
   }

   public String getNome() {
   	return nome;
   }
       
    public void addIntervento(Speech intervento) {
    	elencoInterventi.add(intervento);
    }

    public void addInterventi(List<Speech> interventi) {
        elencoInterventi.addAll(interventi);
    }

    public List<Speech> getElencoInterventi() {
        return elencoInterventi;
    }
   
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Evento {\n");
        sb.append("nome: ").append(nome).append(",\n");
        sb.append("data: ").append(data).append(",\n");
        sb.append("ora inizio: ").append(oraInizio).append(",\n");
        sb.append("ora fine: ").append(oraFine).append(",\n");
        sb.append("organizzato da: ").append(nomeOrganizzatore).append(",\n");
        sb.append("partecipanti previsti: ").append(partPrevisti).append(",\n");
        sb.append("catering: ").append(catering).append(",\n");
        sb.append("sala: ").append(sala).append(",\n");
        sb.append("costo partecipazione: ").append(costoPartecipazione).append(",\n");
        if (!elencoInterventi.isEmpty()) {
            sb.append("elenco interventi: [\n");
            for (Speech curr : elencoInterventi) {
                sb.append(curr.getTitolo()).append(",\n");
                sb.append(curr.getRelatore()).append(",\n");
                sb.append(curr.getDescrizione()).append(",\n");
            }
            sb.delete(sb.length() - 2, sb.length());  // Rimuovi l'ultima virgola e spazio
            sb.append("]\n");
        }
        sb.append("}\n");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(nome, event.nome) &&
            Objects.equals(data, event.data) &&
            Objects.equals(oraInizio, event.oraInizio) &&
            Objects.equals(oraFine, event.oraFine) &&
            Objects.equals(nomeOrganizzatore, event.nomeOrganizzatore) &&
            Objects.equals(costoPartecipazione, event.costoPartecipazione) &&
            Objects.equals(partPrevisti, event.partPrevisti) &&
            Objects.equals(catering, event.catering) &&
            Objects.equals(sala, event.sala) &&
            Objects.equals(elencoInterventi, event.elencoInterventi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, data, oraInizio, oraFine, 
        nomeOrganizzatore, costoPartecipazione, 
        partPrevisti, catering, sala, elencoInterventi);
    }


}

