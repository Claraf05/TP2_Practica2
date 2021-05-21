package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {
	
	private String[] _header = {"id", "Mass", "Position", "Speed", "Force"};
	
	private List<Body> _bodies;
	
	BodiesTableModel(Controller ctrl) {
		_bodies = new ArrayList<>();
		ctrl.addObserver(this);
	}
	
	@Override
	public int getRowCount() {
		return _bodies.size();
	}
	
	@Override
	public int getColumnCount() {
		return _header.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return _header[column];
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Body b = _bodies.get(rowIndex);
		switch (columnIndex) {
			case 0: 
				return b.getId();
			
			case 1: 
				return b.getMass();
			
			case 2:
				return b.getPosition().toString();
			
			case 3:
				return b.getSpeed().toString();
			
			case 4:
				return b.getForce().toString();
		}
		return null;
		
	}
	
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				_bodies = bodies;
				fireTableStructureChanged();
				
			}
		});
		
	}
	
	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				_bodies = bodies;
				fireTableStructureChanged();
				
			}
		});
		
	}
	
	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				_bodies = bodies;
				fireTableStructureChanged();
				
			}
		});
		
	}
	
	@Override
	public void onAdvance(List<Body> bodies, double time) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				_bodies = bodies;
				fireTableStructureChanged();
				
			}
		});
		
	}
	
	@Override
	public void onDeltaTimeChanged(double dt) {

		
	}
	
	@Override
	public void onForceLawsChanged(String fLawsDesc) {
		
	} 
}
