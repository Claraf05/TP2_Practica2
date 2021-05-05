package simulator.model;

import org.json.JSONArray;
import org.json.JSONObject;

import jdk.nashorn.api.scripting.JSObject;
import simulator.misc.Vector2D;

public class Body {
	
	protected String id; //identificador del body
	protected Vector2D speed; //velocidad del body
	protected Vector2D force; //fuerza del body
	protected Vector2D position; //posicion del body
	protected double mass; //masa del body
	
	public Body(String id, Vector2D speed, Vector2D position, double mass) { //Constructor
		
		this.id = id;
		this.speed = speed;
		this.force = new Vector2D();
		this.position = position;
		this.mass = mass;
	}

	//Getters
	public String getId() {
		return id;
	}

	public Vector2D getSpeed() {
		return speed;
	}

	public Vector2D getForce() {
		return force;
	}

	public Vector2D getPosition() {
		return position;
	}

	public double getMass() {
		return mass;
	}
	
	//annadir la fuerza que se pasa como argumento al body
	public void addForce(Vector2D f) {
		this.force = this.force.plus(f);
	}
	
	//pone la fuerza a 0 al inicio del advance
	public void resetForce() {
		this.force = new Vector2D();
	}
	
	//movimiento del body
	public void move(double t) {
		
		Vector2D aceleration = new Vector2D(this.force.scale(1/this.mass)); //vector aceleracion sacada de la fuerza
		Vector2D at2;
		
		position = this.position.plus(this.speed.scale(t)); //posicion = posicion * velocidad * tiempo
		at2 = aceleration.scale(1/2 * (t * t)); //at2 = (aceleracion * tiempo * tiempo) / 2
		position = this.position.plus(at2); //posicion = posicion * velocidad * tiempo * at2
		
		this.speed = this.speed.plus(aceleration.scale(t)); //velocidad = velocidad * aceleracion * t
	}
	
	//Compara objetos ***************************************************************************
	public boolean equals(Object o) {	
		
		if (this == o) return true;
		
	    if (o == null) return false;
	    
	    if (getClass() != o.getClass()) return false;
	    
	    final Body other = (Body)o;
        
        if(id == null) {

            if(other.id != null)
                return false;

        }
        else if (!id.equals(other.id)) {
        	return false;
        }
        
        return true;
	}
	
	//crea un json object con el estado del body
	public JSONObject getState() {
		
		JSONObject j = new JSONObject();
		j.put("id", this.id);
		j.put("m", this.mass);
		j.put("p", this.position.asJSONArray());
		j.put("v", this.speed.asJSONArray());
		j.put("f", this.force.asJSONArray());
		
		return j;
	}
	
	//convierte el estado en string
	public String toString() {
		return getState().toString();
	}
	
}
