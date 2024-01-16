package server.model;
import java.time.LocalDate;

public class VisitorGetState implements Visitor<State> {
	
/**
 * Visitor che rende lo stato della risorsa ad una certa data
 */

	public State visit(ResourceRoom o, StateDate sd) {
		return o.getStato(sd.data);
	}

	public State visit (ResourceRoom o) {
		return null;
	}

}
