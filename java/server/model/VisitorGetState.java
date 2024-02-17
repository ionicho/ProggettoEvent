package server.model;

/**
* Visitor che rende lo stato della risorsa ad una certa data
*/

public class VisitorGetState implements Visitor<State> {

	public State visit(Room o, StateDate sd) {
		return o.getStato(sd.data);
	}

	public State visit(ConferenceRoom o, StateDate sd) {
		return o.getStato(sd.data);
	}

	public State visit(Calendar o, StateDate sd) {
		return o.getStato(sd.data);
	}
	
	public State visit (Room o) {
		return null;
	}

	public State visit (ConferenceRoom o) {
		return null;
	}

	public State visit (Calendar o) {
		return null;
	}
	
}
