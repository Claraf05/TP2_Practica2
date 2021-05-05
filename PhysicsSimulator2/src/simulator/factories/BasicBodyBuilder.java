package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;


public class BasicBodyBuilder<T> extends Builder<Body>{
	
	public BasicBodyBuilder() { //constructor
		this.typeTag = "basic";
		this.desc = "cuerpo basico";
	}
	
	@Override
	protected Body createTheInstance(JSONObject j) { //crea una instancia de un basic body
		
		JSONObject data = j.getJSONObject("data");
		
		Vector2D v = new Vector2D(data.getJSONArray("v").getDouble(0), data.getJSONArray("v").getDouble(1)); //guardamos la velocidad en un vector
		Vector2D p = new Vector2D(data.getJSONArray("p").getDouble(0), data.getJSONArray("p").getDouble(1)); //guardamos la posicion en un vector
		
		Body b = new Body(data.getString("id"), v, p, data.getDouble("m")); //crea la intancia del body con los datos dados
		
		return b;
	}
	
	//metodo que crea la plantilla de una estructura Json de basic body
	protected JSONObject createData() {
		
		JSONObject j = new JSONObject();
		
		j.put("id", "identificador del cuerpo basico");
		j.put("p", "vector posicion del cuerpo basico");
		j.put("v", "vector velocidad del cuerpo basico");
		j.put("m", "masa del cuerpo basico");
		
		return j;
	}
	
}
