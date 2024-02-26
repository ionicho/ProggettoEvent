package server.model;


/**
 * Interfaccia utilizzata per garantire che le classi che la implementano
 * abbiano un metodo getNome(). Questo metodo è utilizzato nella classe 
 * AbstractService per identificare univocamente ciascuna risorsa.
 */

public interface HasName {
	
	public String getNome();

}
