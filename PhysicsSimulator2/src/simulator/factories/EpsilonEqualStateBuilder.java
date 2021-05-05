package simulator.factories;

import org.json.JSONObject;

import simulator.control.EpsilonEqualStates;
import simulator.control.StateComparator;
import simulator.model.NewtonUniversalGravitation;

public class EpsilonEqualStateBuilder<T> extends Builder<StateComparator>{

	public EpsilonEqualStateBuilder() { //constructor
		this.typeTag = "epseq";
		this.desc = "igualdad modulo epsilon";
	}
	
	@Override
	protected StateComparator createTheInstance(JSONObject j) { //crea una instancia del comparador con epsilon
		
		JSONObject data = j.getJSONObject("data");
		EpsilonEqualStates epseq;
		
		if(data.has("eps")) { //si el data tiene un epsilon epecificado se crea una instancia con ese epsilon
			epseq = new EpsilonEqualStates(data.getDouble("eps"));
			
		}
		else { //sino la instancia se crea con el epsilon por defecto igual a 0
			epseq = new EpsilonEqualStates(0.0);
		}
		
		return epseq;
	}
	
	 //crea la plantilla de un json del coparador de epsilon
	protected JSONObject createData() {
		JSONObject j = new JSONObject();
		
		j.put("eps", "modulo epsilon");
		
		return j;
	}
	
}
