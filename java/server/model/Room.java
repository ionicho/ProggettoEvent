package server.model;

import java.time.LocalDate;
import java.util.*;

/**
 * Classe per le risorse CAMERE
 * Il costruttore provvede a definire gli attributi specifici di questa risorsa
 * ed ad accettare tutti i Visitors
 */

public class Room extends Resource {
	
	private Integer numeroLetti;
	private RoomType tipo;

	public Room(String nome, Double costo, Integer numeroLetti,RoomType tipo, LocalDate endDate) {
		super(nome, costo, endDate);
		this.numeroLetti = numeroLetti;
		this.tipo = tipo;
	}
	
	public Room(String nome, Double costo, Integer numeroLetti, RoomType tipo, List<StateDate> disponibilita) {
	    super(nome, costo, disponibilita);
	    this.numeroLetti = numeroLetti;
	    this.tipo = tipo;
	}

	public Room() { //costruttore di default serve a Spring Boot a deseralizzare
	}

    public <T> T accept(Visitor<T> v, StateDate stato) {
        return v.visit(this, stato);
    }

    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
    
    public Integer getNumeroLetti() {
    	return this.numeroLetti;
    }

	public void setNumeroLetti(Integer numeroLetti) {
		this.numeroLetti = numeroLetti;
	}
  
    public RoomType getTipo() {
    	return this.tipo;
    }

	public void setTipo(RoomType tipo) {	
		this.tipo = tipo;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Room room = (Room) o;
		return Objects.equals(nome, room.nome) &&
			Objects.equals(costo, room.costo) &&
			Objects.equals(numeroLetti, room.numeroLetti) &&
			Objects.equals(tipo, room.tipo) &&
			Objects.equals(disponibilita, room.disponibilita);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome, costo, numeroLetti, tipo, disponibilita);
	}
	
    @Override
    public String toString() {
    		StringBuilder sb = new StringBuilder();
    		sb.append("Camera{");
    		sb.append("id:");
    		sb.append(nome);
    		sb.append("costo:");
    		sb.append(costo);
    		sb.append("tipo camera: ");
    		sb.append(tipo);
    		sb.append("N. letti: ");
    		sb.append(numeroLetti);
    		for (StateDate curr : disponibilita) {
    			sb.append(curr.data);
    			sb.append(curr.stato);
    		}
    		sb.append("}\n");
    		return sb.toString();    		
    }
}