package server.model;

/**
 * Enumerazione che rappresenta i valori di stato 
 * di una risorsa per una specifica data.
 */

public enum State {
	DISPONIBILE,
	PRENOTATA,
	INUSO,
	PULIZIA,
	CHIUSO
}