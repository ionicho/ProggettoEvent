package server.model;

	import java.time.LocalDate;
	import java.util.*;

	/**
	 * Classe per le risorse SALE
	 * Il costruttore provvede a definire gli attributi specifici di questa risorsa
	 * ed ad accettare tutti i Visitors
	 */
	public class Hall extends Resource {

		private Integer numeroPosti;

		public Hall(String nome, Double costo, Integer numeroPosti, LocalDate endDate) {
			super(nome, costo, endDate);
			this.numeroPosti = numeroPosti;
		}
		
		public Hall(String nome, Double costo,Integer numeroPosti, List<StateDate> disponibilita) {
		    super(nome, costo, disponibilita);
			this.numeroPosti = numeroPosti;
		}

		public Hall() { //costruttore di default serve a Spring Boot a deseralizzare
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

