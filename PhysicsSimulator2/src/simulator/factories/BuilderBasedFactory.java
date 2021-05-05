package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.MassLossingBody;
import simulator.model.MovingTowardsFixedPoint;
import simulator.model.NewtonUniversalGravitation;
import simulator.model.NoForce;

public class BuilderBasedFactory<T> implements Factory<T>{

	private List<Builder<T>> list = new ArrayList<Builder<T>>();
	private List<JSONObject> listaObj = new ArrayList<JSONObject>();
	
	public BuilderBasedFactory(List<Builder<T>> l) { //constructor
		this.list = l; 
	}

	@Override
	public T createInstance(JSONObject info) { //metodo para crear la instancia del objeto
		T t = null;
		for(Builder<T> b: this.list) { //recorre la lista de builders y llama a sus createInstance
			t = b.createInstance(info);
			if(t != null) { //cuando se cree la instancia y no sea null se sale del bucle
				return t;
			}
		}
		
		return t;
	}

	@Override
	public List<JSONObject> getInfo() { //llama al getBuilderInfo de cada builder

		for(Builder<T> b: this.list) { //muestra la informacion de todos los builder de la lista de builders especifica
			for(int i = 0; i<this.list.size(); i++) {
				this.listaObj.add(b.getBuilderInfo());
			}
		}
		
		return this.listaObj;
	}

}
