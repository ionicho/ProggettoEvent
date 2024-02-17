package server.model;

/**
 * Visitor che rende il prezzo di tutte le risorse
 */

public class VisitorGetPrice implements Visitor<Double> {

	public Double visit(Room o) {
		return o.getCosto();
	}

	@Override
	public Double visit(ConferenceRoom o) {
		return o.getCosto();
	}
	
	public Double visit(Calendar o) {
		return null;
	}

	@Override
	public Double visit(Room o, StateDate sd) {
		return null;
	}

	@Override
	public Double visit(Calendar o, StateDate sd) {
		return null;
	}

	@Override
	public Double visit(ConferenceRoom o, StateDate sd) {
		return null;
	}


	
}
