package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class NewtonUniversalGravitation implements ForceLaws{
	
	public final static double G = 6.67E-11;
	protected double g;
	
	public  NewtonUniversalGravitation(double g) {
		this.g = g;
	}

	@Override
	public void apply(List<Body> bs) {
		
		Vector2D f = new Vector2D();
		double res = 0.0;
		
		for(int i = 0; i< bs.size(); i++) { //recorre la lista de bodies
			for(int j = 0; j< bs.size(); j++) { //recorre la lista de bodies
				if(i != j) { //si los indices de los bodies son diferentes (los bodies tambien lo son)
					res = (bs.get(i).getMass() * bs.get(j).getMass()); //res = masa del body1 * masa del body2
					Vector2D m = bs.get(j).getPosition().minus(bs.get(i).getPosition()); //m = distancia entre body1 y body2
					double mag = m.magnitude(); //distancia pasada de formato vector a double
					res /= (mag*mag); //res = res / distancia al cuadrado
					res *= g; //res = res * g (constante gravitacional)
					f = (m.direction()).scale(res); //fuerza = direccion * res
					bs.get(i).addForce(f); //annade la fuerza calculada al body
				}
			}
		}
	}

	public String toString() { //devuelve el nombre de la fuerza
		return "Newton's Universal Gravitation with G=-" + this.g;
	}
}
