package server.model;

import java.util.*;

/**
 * Classe per le risorse CAMERE
 */

public class Room extends Resource implements HasName {
	
	private Integer numeroLetti;
	private RoomType tipo;

	// Costruttore senza parametri per la deserializzazione con Gson
    public Room() {
	}
	
    // Costruttore con parametro String per creare nuovi eventi.
    // Il parametro 's' non viene utilizzato, serve solo per distinguere questo costruttore dal costruttore senza parametri.
    public Room (String s) { //costruttore per il singleton
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
		sb.append("\n{SALA\n");
		sb.append("id:").append(nome).append(",\n");
		sb.append("tipo:").append(tipo).append(",\n");
		sb.append("costo:").append(costo).append(",\n");
		sb.append("N. Letti: ").append(numeroLetti).append(",\n[");
		for (StateDate curr : disponibilita) {
			sb.append(curr.data).append(" ").append(curr.stato).append(",\n");
		}
		sb.delete(sb.length() - 2, sb.length());  // Rimuovi l'ultima virgola e spazio
		sb.append("]}\n");
		return sb.toString();   		
    }
}