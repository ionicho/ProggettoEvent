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
    private Double costoPartecipazione;
    private String sala;
    private Integer partPrevisti;
    private TipoCatering catering;
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

   public void setNomeOrganizzatore(String nome) {
	   if (nome != null && !nome.trim().isEmpty()) nomeOrganizzatore = nome;
   }
   
    public void setCostoPartecipazione(Double costo) {
    	costoPartecipazione = costo;
    }

    public void setPartPrevisti(Integer part) {
    	partPrevisti = part;
    }
    
    public void setSala(String salaName) {
        this.sala = salaName;
    }

    public void setCatering(TipoCatering catering) {
    	this.catering = catering;
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

    public String getSala() {
        return sala;
    }

    public Integer getPartPrevisti() {
        return partPrevisti;
    }

    public TipoCatering getCatering() {
        return catering;
    }

    public List<Speech> getElencoInterventi() {
        return elencoInterventi;
    }

    public Double getCostoPartecipazione() {
        return costoPartecipazione;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Evento {\n");
        sb.append("id: ").append(id).append(",\n");
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
        return Objects.equals(id, event.id) &&
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
        return Objects.hash(id, data, oraInizio, oraFine, 
        nomeOrganizzatore, costoPartecipazione, 
        partPrevisti, catering, sala, elencoInterventi);
    }


}

