package simulator.factories;

import org.json.JSONObject;

public abstract class Builder<T> {
	
	protected String typeTag; //typo de objeto que vamos a crear
	protected String desc; //descripcion de dicho objeto
	
	protected abstract T createTheInstance(JSONObject j);
	
	public JSONObject getBuilderInfo() { //devuelve un json del builder especifico del objeto
		
		JSONObject j = new JSONObject();
		j.put("type", this.typeTag);
		j.put("desc", this.desc);
		
		return j;
		
	}
	
	protected JSONObject createData() {
		
		JSONObject j = new JSONObject();
		
		return j;
	}
	
	//llama al createTheInstance especifico del builder
	public T createInstance(JSONObject info) {
		
		if(info == null) { //si el json info del argumento es null salta excepcion
			throw new IllegalArgumentException();
		}
		
		String type = info.getString("type");
		JSONObject data = info.getJSONObject("data");
		
		T t = null;
		
		if(type.equalsIgnoreCase(typeTag)) { //si el type es el mismo que el type del builder especifico llama al metodo createTheInstance
			t = createTheInstance(info);
		}
		
		return t;
	}
}
