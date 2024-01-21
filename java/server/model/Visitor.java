package server.model;

/**
 * Interfaccia Visitor
 * ha visit overloaded per consentire l'implementazione
 * anche del visitor che hanno lo StatoData come parametro 
 */

public interface Visitor <T>{
	
	public T visit (ResourceRoom o, StateDate sd);
	public T visit (ResourceRoom o);

}
