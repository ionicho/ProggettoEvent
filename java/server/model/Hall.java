package server.model;

import java.util.*;

/**
 * Classe per le risorse SALE
 */
public class Hall extends Resource implements HasName {

	private Integer numeroPosti;

	// Costruttore senza parametri per la deserializzazione con Gson
    public Hall() {
	}
	
	// Costruttore con parametro String per creare nuovi eventi.
	// Il parametro 's' non viene utilizzato, serve solo per distinguere questo costruttore dal costruttore senza parametri.
	public Hall (String s) {
		this.nome = Singleton.getInstance().getNext(this.getClass().getSimpleName());
		this.costo = 0.;
		this.disponibilita = new ArrayList <>(); 
	}

	public <T> T accept(Visitor<T> v, StateDate stato) {
		return v.visit(this, stato);
	}

	public <T> T accept(Visitor<T> v) {
		return v.visit(this);
	}
	
	public Integer getNumeroPosti() {
		return this.numeroPosti;
	}

	public void setNumeroPosti(Integer numeroPosti) {
		this.numeroPosti = numeroPosti;
	}

	public int compareName(String o) {
		return this.getNome().compareTo(o);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Hall hall = (Hall) o;
		return Objects.equals(nome, hall.nome) && 
			Objects.equals(costo, hall.costo) && 
			Objects.equals(numeroPosti, hall.numeroPosti) && 
			Objects.equals(disponibilita, hall.disponibilita);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome, costo, numeroPosti, disponibilita);
	}

		
	@Override
	public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("Salaa{");
			sb.append("id:");
			sb.append(nome);
			sb.append("costo:");
			sb.append(costo);
			sb.append("N. Posti: ");
			sb.append(numeroPosti);
			for (StateDate curr : disponibilita) {
				sb.append(curr.data);
				sb.append(curr.stato);
			}
			sb.append("}\n");
			return sb.toString();    		
	}
}

