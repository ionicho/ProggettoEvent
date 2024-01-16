package server.model;

/**
 * Visito che rende il prezzo di tutte le risorse
 */
public class VisitorGetPrice implements Visitor<Double> {

	public Double visit(ResourceRoom o) {
		return o.getCosto();
	}

	@Override
	public Double visit(ResourceRoom o, StateDate sd) {
		return null;
	}



}
