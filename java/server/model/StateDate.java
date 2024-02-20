package server.model;

import java.time.LocalDate;

/**
 * Classe per la gestione della disponibilità nella singola giornata nel
 * formato stato+data, nelle risorse sarà l'elemento di una collection
 */
public class StateDate {
	
    protected LocalDate data;
    protected State stato;

    public StateDate(LocalDate data, State stato) {
        this.data = data;
        this.stato = stato;
    }
    
    public String toString() {
    		StringBuilder sb = new StringBuilder();
    		sb.append("[");
    		sb.append(data);
    		sb.append(",");
    		sb.append(stato);
    		sb.append("]");
    		return sb.toString();	
    }
    
    public LocalDate getData() {
    		return data;
    }
    
    public State getStato() {
    		return stato;
    }
    
    public void setStato(State stato) {
    		this.stato = stato;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateDate stateDate = (StateDate) o;
        return data.equals(stateDate.data) && stato == stateDate.stato;
    }

    @Override
    public int hashCode() {
        return data.hashCode() + stato.hashCode();
    }

}
