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
    private Integer costoPartecipazione;
    private List<Speech> elencoInterventi;

    // Costruttore, getter e setter
    
    public Event() {  // costruttore usato da Gson
 	   this.elencoInterventi = new ArrayList <>();
    }
    
   public Event (String s) { //costruttore usato da me 
	   System.out.printf("COSTRUTTORE EVENT %s\n", s);
	   this.id = Singleton.getInstance().getNext(this.getClass().getSimpleName());
	   this.elencoInterventi = new ArrayList <>();
   }

   public void setNomeOrg(String nome) {
	   if (nome != null && !nome.trim().isEmpty()) nomeOrganizzatore = nome;
   }
   
    public void setCostoPar(Integer costo) {
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

    public void addInterventi(List<Speech> interventi) {
        elencoInterventi.addAll(interventi);
    }
    
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

    public Integer getCosto() {
        return costoPartecipazione;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Evento {");
        sb.append("id: ").append(id).append(", ");
        sb.append("data: ").append(data).append(", ");
        sb.append("ora inizio: ").append(oraInizio).append(", ");
        sb.append("ora fine: ").append(oraFine).append(", ");
        sb.append("organizzato da: ").append(nomeOrganizzatore).append(", ");
        sb.append("costo partecipazione: ").append(costoPartecipazione).append(", ");
        if (!elencoInterventi.isEmpty()) {
            sb.append("elenco interventi: [");
            for (Speech curr : elencoInterventi) {
                sb.append(curr.getTitolo()).append(", ");
                sb.append(curr.getRelatore()).append(", ");
                sb.append(curr.getDescrizione()).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());  // Rimuovi l'ultima virgola e spazio
            sb.append("]");
        }
        sb.append("}");
        return sb.toString();
    }


}


