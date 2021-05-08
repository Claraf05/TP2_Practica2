package simulator.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.json.JSONObject;

public class PhysicsSimulator {
	
	private ForceLaws f; //ley de fuerza para aplicar en el simulador
	private double deltaTime;
	private List<Body> list; //lista de bodys del simulador
	private double time;
	private List<SimulatorObserver> listaObservadora;
	
	public PhysicsSimulator (ForceLaws f, double deltaTime) { //constructor
		
		if(deltaTime <= 0 || f == null) { //si el tiempo es igual a 0 o no existe la fuerza salta excepcion
			throw new IllegalArgumentException();
		}
		
		this.f = f;
		this.deltaTime = deltaTime;
		this.time = 0.0;
		this.list = new ArrayList<Body>();
		this.listaObservadora = new ArrayList<SimulatorObserver>();
	}
	
	//annade un body a la lista de bodies
	public void addBody(Body b) {
		
		if(this.list.contains(b)) { //si el objeto esta en la lista salta excepcion
			throw new IllegalArgumentException();
		}
		
		this.list.add(b); //annade el body a la lista
		
		for(SimulatorObserver o: listaObservadora) {
			o.onBodyAdded(list, b);
		}
	}
	
	//ejecuta un paso de simulacion
	public void advance() {
		
		for(Body l: this.list ) { //se resetean las fuerzas de todos los bodies
			l.resetForce();
		}
		
		f.apply(this.list); //aplica la fuerza pasada como argumento
		
		for(Body l2: this.list ) { //recorre la lista de bodies para moverlos
			l2.move(deltaTime);
		}
		
		this.time += deltaTime; //actualiza el tiempo actual
		
		for(SimulatorObserver o: listaObservadora) {
			o.onAdvance(list, deltaTime);
		}
	}
	
	//genera el estado actual de la simulacion
	public JSONObject getState() {
		
		JSONObject j = new JSONObject();
		List<JSONObject> lista = new ArrayList<JSONObject>();
		
		for(Body l: this.list ) { //recorre la lista de bodies para sacar sus estados
			lista.add(l.getState());
		}
		
		j.put("time", this.time); //incluye el tiempo actual en el estado
		j.put("bodies", lista); //incluye la lista de estados de los bodies
	
		return j;	
	}
	
	public String toString() {
		return getState().toString();
	}
	
	public void reset() { 
		this.list.clear();
		this.time = 0;
		
		for(SimulatorObserver o: listaObservadora) {
			o.onRegister(list, time, deltaTime, f.toString());
		}
	}
	
	public void setDeltaTime(double dt) {
		if(dt <= 0) {
			throw new IllegalArgumentException();
		}
		this.deltaTime = dt;
		
		for(SimulatorObserver o: listaObservadora) {
			o.onDeltaTimeChanged(this.deltaTime);
		}
	}
	
	public void setForceLaws(ForceLaws fl) {
		if(fl == null) {
			throw new IllegalArgumentException();
		}
		this.f = fl;
		
		for(SimulatorObserver o: listaObservadora) {
			o.onForceLawsChanged(this.f.toString());
		}
	}
	
	public void addObserver(SimulatorObserver o) {
		if(!this.listaObservadora.contains(o)) {
			this.listaObservadora.add(o);
		}
		o.onRegister(list, time, deltaTime, f.toString());
	}

	public double getDeltaTime() {
		return deltaTime;
	}
	
	
}
