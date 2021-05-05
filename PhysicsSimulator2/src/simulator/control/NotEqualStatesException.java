package simulator.control;
import org.json.JSONObject;

public class NotEqualStatesException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	private JSONObject _actual;
	private JSONObject _expected;
	private int _step;
	
	public NotEqualStatesException(JSONObject exp, JSONObject act, int step) { //constructor de la excepcion
		super("States are different at step " + step + System.lineSeparator() + 
				"Actual: " + act + System.lineSeparator() + 
				"Expected: " + exp + System.lineSeparator());
		
		this._actual = act;
		this._expected = exp;
		this._step = step;
	}
	
	//Getters
	public JSONObject get_actual() {
		return _actual;
	}

	public JSONObject get_expected() {
		return _expected;
	}

	public int get_step() {
		return _step;
	}
	
	
}
