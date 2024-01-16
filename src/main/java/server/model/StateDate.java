package server.model;
import java.time.LocalDate;

/**
 * Classe stato e data, nelle risorse sarà l'elemento di una collection
 */
public class StateDate {
	
    public LocalDate data;
    public State stato;

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

}

