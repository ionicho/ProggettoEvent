package server.model;

/**
* Visitor che esegue il cambio di data su tutte le risorse
*/

public class VisitorSetState implements Visitor<Void> {

	public Void visit(Room o, StateDate sd) {
		o.changeStato(sd);
		return null;
	}

	@Override
	public Void visit(ConferenceRoom o, StateDate sd) {
		o.changeStato(sd);
		return null;
	}

	public Void visit(Calendar o, StateDate sd) {
		o.changeStato(sd);
		return null;
	}
	
	@Override
	public Void visit (Room o) {
		return null;
	}
	
	@Override
	public Void visit (Calendar o) {
		return null;
	}

	@Override
	public Void visit(ConferenceRoom o) {
		return null;
	}
}