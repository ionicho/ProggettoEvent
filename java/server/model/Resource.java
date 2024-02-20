package server.model;

import java.time.LocalDate;
import java.util.*;

/**
 * SuperClasse delle risporse (Implementa i metodi condivisi da tutte le Risorse)
 * 
 * Il primo costruttore della classe consente di assegnare nome e costo a tutte le risorse
 * mette a disponibile lo stato dalla data di istanza della risorsa suno alla endDate
 * 
 * Il secondo costruttore viene utilizzato quando si recuperano i dati dal file json.
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
	
	protected Resource(String nome, Double costo, List<StateDate> disponibilita) {
	    this.nome = nome;
	    this.costo = costo;
	    this.disponibilita = disponibilita;
	}
	
	protected Resource(){}
	

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
		return selectData(data).stato;
	}
	
   // @JsonProperty("costo")  //specifico il nome usato nel json
	public Double getCosto() {
		return this.costo;
	}
	
	public void setCosto(Double costo) {
		this.costo = costo;
	}
	
	//@JsonProperty("nome")
	public String getNome() {
		return this.nome;
	}
	
    //@JsonProperty("disponibilita")
	public List<StateDate> getDisponibilita(){
		return this.disponibilita;
	}

	public void setDisponibilita(List<StateDate> disponibilita) {
		this.disponibilita = disponibilita;
	}
	
}