package server.model;

import java.time.LocalDate;
import java.util.*;

/**
 * SuperClasse delle risorse (Implementa i metodi condivisi da tutte le Risorse)
 */
public abstract class Resource  {
	
	protected List<StateDate> disponibilita; //altrimenti Gson non riesce a serializarlo e deserializarlo
	protected String nome;
	protected Double costo;
	
	// Costruttore senza parametri per la deserializzazione con Gson
    protected Resource(){
	}

	public abstract <T> T accept (Visitor <T> v, StateDate sd);
	public abstract <T> T accept (Visitor <T> v);
	
	public StateDate selectData(LocalDate data) {
		for (StateDate curr: disponibilita){
			if (curr.data.compareTo(data) ==0) {return curr;}
		}
		return null;
	}
	
	public void changeStato (StateDate sd) {
		for (StateDate curr: disponibilita){
			if (curr.data.compareTo(sd.data) ==0) {
				curr.setStato(sd.stato);		
				}
		}
	}
	
	public State getStato(LocalDate data) {
		if (selectData(data) == null){
			StateDate sd = new StateDate(data, State.DISPONIBILE);
			disponibilita.add(sd);
			return sd.stato;
		}
		else return selectData(data).stato;
	}
	
	public Double getCosto() {
		return this.costo;
	}
	
	public void setCosto(Double costo) {
		this.costo = costo;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public List<StateDate> getDisponibilita(){
		return this.disponibilita;
	}

	public void setDisponibilita(List<StateDate> disponibilita) {
		this.disponibilita = disponibilita;
	}
	
}