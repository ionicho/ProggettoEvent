package server.model;

/**
* Visitor che esegue il cambio di data su tutte le risorse
*/

public class VisitorSetState implements Visitor<Void> {

	public Void visit(ResourceRoom o, StateDate sd) {
		o.changeStato(sd);
		return null;
	}

	@Override
	public Void visit (ResourceRoom o) {
		return null;
	}
}
