package server.model;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

/**
 * Il primo costruttore della classe consente di assegnare nome e costo a tutte le risorse
 * mette a disponibile lo stato dalla data di istanza della risorsa suno alla endDate
 * 
 * Il secondo costruttore viene utilizzato quando si recuperano i dati dal file json.
 * 
 * Implementa i metodi condivisi da tutte le Risorse
 */
public abstract class Resource  {
	
	protected List<StateDate> disponibilita; //altrimenti Gson non riesce a serializarlo e deserializarlo
	protected String nome;
	protected Double costo;
	
	protected Resource (String nome, Double costo, LocalDate endDate) {
		this.nome = nome;
		this.costo = costo;
		disponibilita = new ArrayList <>(); 
	    for(LocalDate date = LocalDate.now(); !date.isAfter(endDate); date = date.plusDays(1)) {
	        disponibilita.add(new StateDate(date, State.DISPONIBILE));
	    }	
	}
	
	public Resource(String nome, Double costo, List<StateDate> disponibilita) {
	    this.nome = nome;
	    this.costo = costo;
	    this.disponibilita = disponibilita;
	}
	
	public Resource(){}
	

	public abstract <T> T accept (Visitor <T> v, StateDate sd);
	public abstract <T> T accept (Visitor <T> v);
	
	public StateDate selectData(LocalDate data) {
		for (StateDate curr: disponibilita){
			if (curr.data.compareTo(data) ==0) {return curr;}
		}
		return null;
	}
	
	public void setDisponibile(LocalDate data) {
		selectData(data).stato = State.DISPONIBILE;
	}
	
	public void setImpegnato(LocalDate data) {
		selectData(data).stato = State.PRENOTATA;	
	}
	
	public void setInuso(LocalDate data) {
		selectData(data).stato = State.INUSO;	
	}
	
	public void setPulizia(LocalDate data) {
		selectData(data).stato = State.PULIZIA;	
	}
	
	public State getStato(LocalDate data) {
		return selectData(data).stato;
	}
	
   // @JsonProperty("costo")  //specifico il nome usato nel json
	public Double getCosto() {
		return this.costo;
	}
	
    //@JsonProperty("nome")
	public String getNome() {
		return this.nome;
	}
	
    //@JsonProperty("disponibilita")
	public List<StateDate> getDisponibilita(){
		return this.disponibilita;
	}
	
}
