package server.model;

/**
* Visitor che rende lo stato della risorsa ad una certa data
*/

public class VisitorGetState implements Visitor<State> {

	public State visit(ResourceRoom o, StateDate sd) {
		return o.getStato(sd.data);
	}

	public State visit (ResourceRoom o) {
		return null;
	}

}
