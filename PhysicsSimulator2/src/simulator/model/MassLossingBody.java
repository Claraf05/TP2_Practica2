package simulator.model;

import simulator.misc.Vector2D;

public class MassLossingBody extends Body{
	
	private double lossFactor;//un double entre 0 y 1 que representa el factor de pérdida de masa
	private double lossFrequency; //un double positivo que indica el intervalo de tiempo despues del cual el objeto pierde masa

	public MassLossingBody(String id, Vector2D speed, Vector2D position, double mass, double lossFactor, double lossFrequency){
		//Constructor
		super(id, speed, position, mass);
		this.lossFactor = lossFactor;
		this.lossFrequency = lossFrequency;
	}
	
	//movimiento del body que pierde masa
	@Override
	public void move(double t) {
		
		Vector2D aceleration = new Vector2D();
		Vector2D at2;
		
		if(t >= this.lossFrequency) {//formula para perder masa
			this.mass = this.mass * (1-this.lossFactor);
		}
		//calculo de posicion, velocidad y aceleracion estandar (la misma que body)
		aceleration = this.force.scale(1/this.mass);
		position = this.position.plus(this.speed.scale(t));
		at2 = aceleration.scale(1/2 * (t * t));
		position = this.position.plus(at2);
		this.speed = this.speed.plus(aceleration.scale(t));
	}
}
