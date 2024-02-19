package server.model;

/**
 * Interfaccia Visitor
 * ha visit overloaded per consentire l'implementazione
 * anche del visitor che hanno lo StatoData come parametro 
 */

public interface Visitor <T>{
	
	public T visit (Room o, StateDate sd);
	public T visit (Room o);
	public T visit (Hall o, StateDate sd);
	
	public T visit (Calendar o, StateDate sd);
	public T visit (Calendar o);
	public T visit (Hall o);


}