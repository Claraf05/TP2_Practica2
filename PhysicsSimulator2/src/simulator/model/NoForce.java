package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class NoForce implements ForceLaws{
	
	public NoForce() {
		//vacio, fuerza igual a 0
	}

	@Override
	public void apply(List<Body> bs) {
		//vacio, fuerza igual a cero
	}
	
	public String toString() { //devuelve el nombre de la fuerza (en este caso te indica que no tiene fuerza)
		
		return "No Force";
	}
}
