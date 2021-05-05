package simulator.factories;

import org.json.JSONObject;

import simulator.control.MassEqualStates;
import simulator.control.StateComparator;
import simulator.model.NoForce;

public class MassEqualStateBuilder<T> extends Builder<StateComparator>{

	public MassEqualStateBuilder() { //constructor
		this.typeTag = "masseq";
		this.desc = "igualdad de masas";
	}
	
	@Override
	protected StateComparator createTheInstance(JSONObject j) { //crea una instancia del comparador de masas
		JSONObject data = j.getJSONObject("data");
		
		MassEqualStates masseq = new MassEqualStates(); //crea una instancia del comparador de masas sin necesidad de especificar datos extra
		
		return masseq;
		
	}
}
