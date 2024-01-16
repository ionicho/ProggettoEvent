package server.model;
import java.util.*;

/**
 * Interfaccia Visitor
 * ha visit overloade per consentire l'implementazione
 * anche del visitor che hanno lo StatoData come parametro 
 */

public interface Visitor <T>{
	
	public T visit (ResourceRoom o, StateDate sd);
	public T visit (ResourceRoom o);

}
