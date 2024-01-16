package server.model;
public class VisitorSetState implements Visitor<Void> {
	
/**
 * Visitor che esegue il cambio di data su tutte le risorse
 */

	public Void visit(ResourceRoom o, StateDate sd) {
		switch (sd.stato) {
			case DISPONIBILE: o.setDisponibile(sd.data); break;
			case PRENOTATA: o.setImpegnato(sd.data); break;
			case INUSO: o.setInuso(sd.data); break;
			case PULIZIA: o.setPulizia(sd.data); break;
		}
		return null;
	}

	@Override
	public Void visit (ResourceRoom o) {
		return null;
	}
}
