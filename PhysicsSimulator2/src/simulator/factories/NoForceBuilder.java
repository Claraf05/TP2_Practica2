package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NoForce;

public class NoForceBuilder<T> extends Builder<ForceLaws>{

	public NoForceBuilder() { //constructor
		this.typeTag = "nf";
		this.desc = "No force";
	}
	
	@Override
	protected ForceLaws createTheInstance(JSONObject j) { //crea una instancia de la fuerza nula, fuerza = 0
		
		NoForce nf = new NoForce(); //crea la instancia
		
		return nf;
	}
}
