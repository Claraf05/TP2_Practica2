package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {
	
	private String[] _header = {"id", "Mass", "Position", "Speed", "Force"}; //nombres de las columnas
	
	private List<Body> _bodies; //listta de bodies
	
	BodiesTableModel(Controller ctrl) { //constructor
		_bodies = new ArrayList<>();
		ctrl.addObserver(this); //se annade como observador (dependiendo de la lista de bodies la tabla cambia)
	}
	
	@Override
	public int getRowCount() { //contador de filas, tamanno de la lista
		return _bodies.size();
	}
	
	@Override
	public int getColumnCount() { //contador de columnas, longitud de _header
		return _header.length;
	}
	
	@Override
	public String getColumnName(int column) { //nombre de la columna, el elemento con indice column de header
		return _header[column];
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) { //poner los valores de la tabla
		Body b = _bodies.get(rowIndex);
		switch (columnIndex) {
			case 0:  //en la columna 0 van los id
				return b.getId(); 
			
			case 1: //en la columna 1 van las masas
				return b.getMass();
			
			case 2: //en la columna 2 van las posiciones
				return b.getPosition().toString();
			
			case 3: //en la columna 3 van las velocidades
				return b.getSpeed().toString();
			
			case 4: //en la columna 4 van las fuerzas
				return b.getForce().toString();
		}
		return null;
		
	}
	
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			//al registrarse esta vista necesita la lista de bodies
			@Override
			public void run() {
				_bodies = bodies; //actualiza la lista de bodies
				fireTableStructureChanged(); //actualiza el modelo de la tabla
			}
		});
		
	}
	
	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			//al resetear cambian los bodies, tmbn es necesario 
			@Override
			public void run() {
				_bodies = bodies; //actualiza la lista de bodies
				fireTableStructureChanged(); //actualiza el modelo de la tabla
				
			}
		});
		
	}
	
	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		SwingUtilities.invokeLater(new Runnable() {
			//al annadir un uevo body la tabla se tiene que enterar para modificarse
			@Override
			public void run() {
				_bodies = bodies; //actualiza la lista de bodies
				fireTableStructureChanged(); //actualiza el modelo de la tabla
			}
		});
		
	}
	
	@Override
	public void onAdvance(List<Body> bodies, double time) {
		SwingUtilities.invokeLater(new Runnable() {
			//al ejecutar pasos de simulacion la informacion de los bodies se modifica
			@Override
			public void run() {
				_bodies = bodies; //actualiza la lista de bodies
				fireTableStructureChanged(); //actualiza el modelo de la tabla
			}
		});
		
	}
	
	@Override
	public void onDeltaTimeChanged(double dt) {
		//no influye en la tabla
	}
	
	@Override
	public void onForceLawsChanged(String fLawsDesc) {
		//no influye en la tabla
	} 
}
