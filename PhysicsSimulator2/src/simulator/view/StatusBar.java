package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class StatusBar extends JPanel implements SimulatorObserver {
	
	private JLabel _currTime; // para current time
	private JLabel _currLaws; // para force laws
	private JLabel _numOfBodies; // para numero de bodies
	private JLabel _title;
	private JLabel _titleBodies;
	private JLabel _titleForce;
	
	StatusBar(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}
	
	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder( BorderFactory.createBevelBorder( 1 ));
	
		_title = new JLabel();
		_currLaws = new JLabel();
		_currTime = new JLabel();
		_numOfBodies = new JLabel();
		_titleBodies = new JLabel();
		_titleForce = new JLabel();
		
		_title.setText("Time: ");
		
		this.add(_title);
		this.add(_currTime);
		JSeparator js = new JSeparator(JSeparator.VERTICAL);
		JSeparator js2 = new JSeparator(JSeparator.VERTICAL);
		js.setPreferredSize(new Dimension(10,20));
		js2.setPreferredSize(new Dimension(10,20));
		this.add(js);
		
		_titleBodies.setText("Bodies: ");
		this.add(_titleBodies);
		this.add(_numOfBodies);
		this.add(js2);
		
		_titleForce.setText("Laws: ");
		this.add(_titleForce);
		this.add(_currLaws);
	
	}

	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		SwingUtilities.invokeLater( new Runnable() {

			@Override
			public void run() {
				_currTime.setText(String.valueOf(time));
				_numOfBodies.setText(String.valueOf(bodies.size()));
				_currLaws.setText(fLawsDesc);
				
			} 	
		});
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		SwingUtilities.invokeLater( new Runnable() {

			@Override
			public void run() {
		
				_currTime.setText(String.valueOf(time));
				_numOfBodies.setText(String.valueOf(bodies.size()));
				_currLaws.setText(fLawsDesc);
			} 	
		});
		
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		SwingUtilities.invokeLater( new Runnable() {

			@Override
			public void run() {
				_numOfBodies.setText(String.valueOf(bodies.size()));
			} 	
		});
		
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		SwingUtilities.invokeLater( new Runnable() {

			@Override
			public void run() {
				_currTime.setText(String.valueOf(time));
				
			} 	
		});
		
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		
	}

	@Override
	public void onForceLawsChanged(String fLawsDesc) {
		SwingUtilities.invokeLater( new Runnable() {

			@Override
			public void run() {
				_currLaws.setText(fLawsDesc);
				
			} 	
		});
		
	}
	
	// Añade private/protected methods
}