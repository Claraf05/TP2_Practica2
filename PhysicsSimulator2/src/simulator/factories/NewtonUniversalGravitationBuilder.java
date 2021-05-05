package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder<T> extends Builder<ForceLaws>{

	public NewtonUniversalGravitationBuilder() { //constructor
		this.typeTag = "nlug";
		this.desc = "Newton's law of universal gravitation";
	}
	
	@Override
	protected ForceLaws createTheInstance(JSONObject j) { //crea una instancia de la fuerza de la Ley de Newton
		
		JSONObject data = j.getJSONObject("data");
		NewtonUniversalGravitation nlug;
		
		if(data.has("G")) { //si tiene un G especificado se crea una fuerza de Newton con dicha G
			nlug = new NewtonUniversalGravitation(data.getDouble("G"));
		}
		else { //sino aplicamos la G por defecto especificada en la clase nlug
			nlug = new NewtonUniversalGravitation(NewtonUniversalGravitation.G);
		}
		
		return nlug;
	}
	
	//plantilla del json object de la fuerza de newton
	protected JSONObject createData() {
		JSONObject j = new JSONObject();
		
		j.put("G", "the gravitational constant (a number)");
		
		return j;
	}

}
