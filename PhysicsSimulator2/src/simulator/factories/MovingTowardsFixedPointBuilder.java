package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder<T> extends Builder<ForceLaws>{

	public MovingTowardsFixedPointBuilder() { //constructor
		this.typeTag = "mtfp";
		this.desc = "Moving towards a fixed point";
	}
	
	@Override
	protected ForceLaws createTheInstance(JSONObject j) { //crea una instancia de la fuerza hacia un punto fijo
		
		JSONObject data = j.getJSONObject("data");
		MovingTowardsFixedPoint mtcp;
		Vector2D c;
		if(data.has("c")) { //si el data tiene un punto especificado se genera una instancia de la fuerza hacia dicho punto
			c = new Vector2D(data.getJSONArray("c").getDouble(0), data.getJSONArray("c").getDouble(1));
		}
		else { //sino el punto por defecto es el centro, punto (0,0) y la g es 9,81
			c = new Vector2D(0,0);
			
		}
		if(data.has("g")) {
			mtcp = new MovingTowardsFixedPoint(c, data.getDouble("g"));
		}
		else {
			mtcp = new MovingTowardsFixedPoint(c, 9.81);
		}
		
		return mtcp;
	}
	
	//plantilla del json de mtfp
	protected JSONObject createData() {
		JSONObject j = new JSONObject();
		
		j.put("c", "the point towards which bodies move (a json list of 2 numbers, e.g., [100.0,50.0])");
		j.put("g", "the length of the acceleration vector (a number)");
		
		return j;
	}

}
