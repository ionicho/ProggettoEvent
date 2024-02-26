package server.model;

import java.time.LocalDate;
/**
 * Classe che rappresenta il calendario della struttura
 * che ospita gli eventi e le camere.
 */

public class Calendar extends Resource implements HasName{
	
	// Costruttore senza parametri per la deserializzazione con Gson
    public Calendar() {
	}
	
	public void setStatoData(LocalDate data, State stato) {
		for (StateDate curr: disponibilita) {
			if (data.compareTo(curr.data) == 0) curr.stato = stato;
		}
	}
	
	public void setCalendario(LocalDate startDate, LocalDate endDate) {
	    for(LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
	        StateDate stateDate = selectData(date);
	        if (stateDate == null) { // definisce lo stato solo se la data non esiste
	        	StateDate newSD = new StateDate(date, State.DISPONIBILE);
	        	disponibilita.add(newSD);
	        }
	    }
	}

	public <T> T accept(Visitor<T> v, StateDate stato) {
        return v.visit(this, stato);
    }

    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}