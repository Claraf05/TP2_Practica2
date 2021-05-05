package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.MassLossingBody;

public class MassLossingBodyBuilder<T> extends Builder<Body>{
	
	public MassLossingBodyBuilder() { //constructor
		this.typeTag = "mlb";
		this.desc = "cuerpo que pierde masa";
	}
	
	@Override
	protected Body createTheInstance(JSONObject j) { //crea una instancia de un mass lossing body
		
		JSONObject data = j.getJSONObject("data");
		
		Vector2D v2 = new Vector2D(data.getJSONArray("v").getDouble(0), data.getJSONArray("v").getDouble(1)); //guardamos la velocidad en un vector
		Vector2D p2 = new Vector2D(data.getJSONArray("p").getDouble(0), data.getJSONArray("p").getDouble(1)); //guardamos la posicion en un vector
		
		MassLossingBody mlb = new MassLossingBody(data.getString("id"), v2, p2, data.getDouble("m"), data.getDouble("factor"), data.getDouble("freq"));
		//creamos una instancia de mass lossing body con los datos dados en el argumento
		return mlb;
	}
	
	//metodo que crea la plantilla de una estructura Json de mass lossing body
	protected JSONObject createData() {
		
		JSONObject j = new JSONObject();
		
		j.put("id", "identificador del cuerpo que pierde masa");
		j.put("p", "vector posicion del cuerpo que pierde masa");
		j.put("v", "vector velocidad del cuerpo que pierde masa");
		j.put("m", "masa del cuerpo que pierde masa");
		j.put("freq", "tiempo que tarda en perder masa el cuerpo");
		j.put("factor", "factor de perdida de masa");
		
		return j;
	}
}
