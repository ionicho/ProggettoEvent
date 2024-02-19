package server.model;

	import java.time.LocalDate;
	import java.util.*;

	/**
	 * Classe per le risorse CAMERE
	 * Il costruttore provvede a definire gli attributi specifici di questa risorsa
	 * ed ad accettare tutti i Visitors
	 */
	public class ConferenceRoom extends Resource {

		private Integer numeroPosti;

		public ConferenceRoom(String nome, Double costo, Integer numeroPosti, LocalDate endDate) {
			super(nome, costo, endDate);
			this.numeroPosti = numeroPosti;
		}
		
		public ConferenceRoom(String nome, Double costo,Integer numeroPosti, List<StateDate> disponibilita) {
		    super(nome, costo, disponibilita);
			this.numeroPosti = numeroPosti;
		}

		public ConferenceRoom() { //costruttore di default serve a Spring Boot a deseralizzare
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
		
	    @Override
	    public String toString() {
	    		StringBuilder sb = new StringBuilder();
	    		sb.append("Camera{");
	    		sb.append("id:");
	    		sb.append(nome);
	    		sb.append("tipo camera: ");
	    		sb.append("numero di letti: ");
	    		sb.append(numeroPosti);
	    		for (StateDate curr : disponibilita) {
	    			sb.append(curr.data);
	    			sb.append(curr.stato);
	    		}
	    		sb.append("}\n");
	    		return sb.toString();    		
	    }
	}

