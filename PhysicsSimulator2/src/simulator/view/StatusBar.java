package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
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
	private JLabel _numOfBodies; //para numero de bodies
	private JLabel _title; //para "Time: "
	private JLabel _titleBodies; //para "Bodies: "
	private JLabel _titleForce; //para "Force Laws: "
	
	StatusBar(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this); //registra esta vista como observadora
	}
	
	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT)); //asigna un layout que va de izquierda a derecha
		this.setBorder( BorderFactory.createBevelBorder( 1 )); //creamos un border
		//inicializamos los Jlabel
		_title = new JLabel();
		_currLaws = new JLabel();
		_currTime = new JLabel();
		_numOfBodies = new JLabel();
		_titleBodies = new JLabel();
		_titleForce = new JLabel();
		
		//asigno un tamanno al jlabel del tiempo porque varia mucho y queda mejor
		_currTime.setMinimumSize(new Dimension(80,10));
		_currTime.setPreferredSize(new Dimension(80,10));
		_currTime.setMaximumSize(new Dimension(80,10));
		
		_title.setText("Time: ");
		this.add(_title);
		this.add(_currTime); //muestra el tiempo actual que se actualiza con los metodos de simulator observer
		JSeparator js = new JSeparator(JSeparator.VERTICAL);
		JSeparator js2 = new JSeparator(JSeparator.VERTICAL);
		js.setPreferredSize(new Dimension(10,20));
		js2.setPreferredSize(new Dimension(10,20));
		this.add(js); //ponemos un serparador vertical
		
		_titleBodies.setText("Bodies: ");
		this.add(_titleBodies);
		this.add(_numOfBodies); //muestra el numero de bodies que se actualiza con los metodos de simulator observer
		this.add(js2); //ponemos un separador vertical
		
		_titleForce.setText("Laws: ");
		this.add(_titleForce);
		this.add(_currLaws); //muestra la informacion de la ley de fuerza actual, se actualiza con los metodos de simulator observer
	
	}

	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		SwingUtilities.invokeLater( new Runnable() {
			//se annade a esta vista la lista de bodies actualizada, el tiempo actual y la descripcion de la ley de fuerzas
			@Override
			public void run() {
				_currTime.setText(String.valueOf(time)); //actualiza el label del tiempo actual
				_numOfBodies.setText(String.valueOf(bodies.size())); //actualiza el label del numero de bodies
				_currLaws.setText(fLawsDesc); //actualiza el label de la descripcion de la fuerza
				
			} 	
		});
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		SwingUtilities.invokeLater( new Runnable() {
			//se annade a esta vista la lista de bodies actualizada, el tiempo actual y la descripcion de la ley de fuerzas
			@Override
			public void run() {
		
				_currTime.setText(String.valueOf(time)); //actualiza el label del tiempo actual
				_numOfBodies.setText(String.valueOf(bodies.size())); //actualiza el label del numero de bodies
				_currLaws.setText(fLawsDesc); //actualiza el label de la descripcion de la fuerza
			} 	
		});
		
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		SwingUtilities.invokeLater( new Runnable() {
			//se annade a esta vista la lista de bodies actualizada
			@Override
			public void run() {
				_numOfBodies.setText(String.valueOf(bodies.size())); //actualiza el label del numero de bodies
			} 	
		});
		
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		SwingUtilities.invokeLater( new Runnable() {
			//se annade a esta vista el tiempo actual
			@Override
			public void run() {
				_currTime.setText(String.valueOf(time)); //actualiza el label del tiempo actual
				
			} 	
		});
		
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		//no influye en esta vista
	}

	@Override
	public void onForceLawsChanged(String fLawsDesc) {
		SwingUtilities.invokeLater( new Runnable() {
			//se annade a esta vista la descripcion de la ley de fuerzas
			@Override
			public void run() {
				_currLaws.setText(fLawsDesc); //actualiza el label de la descripcion de la fuerza
				
			} 	
		});
		
	}
	
}