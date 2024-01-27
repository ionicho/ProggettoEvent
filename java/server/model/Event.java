package server.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


/**
 * Classe per la gestione degli eventi
 * La classe ha due costruttori per evitare che Gson quando serializza e/o
 * deserializza un'istanza, chimando il costruttore, incrementi il contatore
 * degli eventi. 
 */

public class Event {
	private String id;
    private LocalDate data;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private String nomeOrganizzatore;
    private List<Speech> elencoInterventi;
    private double costoPartecipazione;

    // Costruttore, getter e setter
    
    public Event() {  // costruttore usato da Gson
    }
    
   public Event (String s) { //costruttore usato da me 
	   this.id = Singleton.getInstance().getNext(this.getClass().getSimpleName());
	   elencoInterventi = new ArrayList <>();
   }

   public void setNomeOrg(String nome) {
	   nomeOrganizzatore = nome;
   }
   
    public void setCostoPar(Double costo) {
    	costoPartecipazione = costo;
    }
    
    public void setData(LocalDate data) {
    	this.data = data;
    }

    public void setOraInizio(LocalTime inizio) {
    	oraInizio = inizio;
    }
    
    public void setOraFine(LocalTime fine) {
    	oraFine = fine;
    }
    
    public void addIntervento(Speech intervento) {
    	elencoInterventi.add(intervento);
    }
    


    // altri metodi come necessario
    
    // Getter   
    public String getId() {
    	return id;
    }
    
    
    public LocalDate getData() {
        return data;
    }

    public LocalTime getOraInizio() {
        return oraInizio;
    }

    public LocalTime getOraFine() {
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
    	StringBuilder sb = new StringBuilder();
    	sb.append("Evento {");
    	sb.append(id);
    	sb.append("data: ");
    	sb.append(data);
    	sb.append("ora inizio: ");
    	sb.append(oraInizio);
    	sb.append("ora fine: ");
    	sb.append(oraFine);
    	sb.append("organizzato da: ");
    	sb.append(nomeOrganizzatore);
    	sb.append("costo partecipazione: ");
    	sb.append(costoPartecipazione);
    	if (!elencoInterventi.isEmpty()) {
    		for (Speech curr : elencoInterventi) {
    			sb.append(curr.titolo);
    			sb.append(curr.relatore);
    			sb.append(curr.descrizione);
    		}    		
    	}
    	return sb.toString();
    }

}


