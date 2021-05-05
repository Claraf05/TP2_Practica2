package simulator.control;

import org.json.JSONObject;

import simulator.misc.Vector2D;

public class EpsilonEqualStates implements StateComparator{
	
	private double eps;

	public EpsilonEqualStates(double eps) { //constructor
		this.eps = eps;
	}
	
	@Override
	public boolean equal(JSONObject s1, JSONObject s2) { //compara 2 estados con el metodo que usa epsilon
		
		boolean ok = true;
		
		if(s1.getDouble("time") == s2.getDouble("time")) { //si el tiempo actual de ambos es igual
			if(s1.getJSONArray("bodies").length() == s2.getJSONArray("bodies").length()) { //si ambos estados tienen el mismo numero de bodies
				for(int i = 0; i< s1.getJSONArray("bodies").length(); i++) { //recorre la lista
					if(ok) {
						JSONObject obj1 = s1.getJSONArray("bodies").getJSONObject(i);
						JSONObject obj2 = s2.getJSONArray("bodies").getJSONObject(i);
						if(obj1.getString("id").equalsIgnoreCase(obj2.getString("id"))) { //si tienen el mismo id los 2 estados a comparar
							
							if(Math.abs(obj1.getDouble("m") - obj2.getDouble("m")) <= this.eps) {
								//si la diferencia de masas entre los cuerpos es menor que epsilon
								Vector2D v1, v2;
								v1 = new Vector2D(obj1.getJSONArray("p").getDouble(0), obj1.getJSONArray("p").getDouble(1));
								v2 = new Vector2D(obj2.getJSONArray("p").getDouble(0), obj2.getJSONArray("p").getDouble(1));
								
								if((v1.direction().minus(v2.direction()).magnitude()) <= this.eps){ //si la diferencia de posiciones entre los cuerpos es menor que epsilon
									v1 = new Vector2D(obj1.getJSONArray("v").getDouble(0), obj1.getJSONArray("v").getDouble(1));
									v2 = new Vector2D(obj2.getJSONArray("v").getDouble(0), obj2.getJSONArray("v").getDouble(1));
									
									if(v1.direction().minus(v2.direction()).magnitude() <= this.eps){ //si la diferencia de velocidades entre los cuerpos es menor que epsilon
										v1 = new Vector2D(obj1.getJSONArray("f").getDouble(0), obj1.getJSONArray("f").getDouble(1));
										v2 = new Vector2D(obj2.getJSONArray("f").getDouble(0), obj2.getJSONArray("f").getDouble(1));
										
										if(v1.direction().minus(v2.direction()).magnitude() <= this.eps){ //si la diferencia de fuerzas entre los cuerpos es menor que epsilon
											ok = true; //si cumple todo eso son iguales en modulo epsilon
										}
										else {
											ok = false;
										}
									}
									else {
										ok = false;
									}
								}
								else {
									ok = false;
								}
							}
							else {
								ok = false;
							}
						}
						else {
							ok = false;
						}
					}
				}
			}
			else {
				ok = false;
			}
		}
		else {
			ok = false;
		}
		return ok;
	}

}
