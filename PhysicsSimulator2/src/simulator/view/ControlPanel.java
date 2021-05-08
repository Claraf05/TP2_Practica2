package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;

import com.sun.javafx.tk.Toolkit;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class ControlPanel extends JPanel implements SimulatorObserver {
	
	private Controller _ctrl;
	private boolean _stopped;
	
	// Añade atributos para JToolBar, botones, Jspinner, JFileChooser,etc.
	
	ControlPanel(Controller ctrl) {
		
		_ctrl = ctrl;
		_stopped = true;
		initGUI();
		_ctrl.addObserver(this);
		
	}
	
	private void initGUI() {
		// Construye la tool bar con todos sus botones, etc.
		
		JToolBar toolBar = new JToolBar();
		setLayout(new BorderLayout());
		this.add(toolBar, BorderLayout.PAGE_START);
		
		JButton _loadButt = new JButton();
		_loadButt.setIcon(new ImageIcon("resources/icons/open.png"));
		toolBar.add(_loadButt);
		
		toolBar.addSeparator();
		
		JButton _forceLawsButt = new JButton();
		_forceLawsButt.setIcon(new ImageIcon("resources/icons/physics.png"));
		toolBar.add(_forceLawsButt);
		
		toolBar.addSeparator();
		
		JButton _runButt = new JButton();
		_runButt.setIcon(new ImageIcon("resources/icons/run.png"));
		toolBar.add(_runButt);
		
		JButton _stopButt = new JButton();
		_stopButt.setIcon(new ImageIcon("resources/icons/stop.png"));
		toolBar.add(_stopButt);
		
		toolBar.addSeparator();
		
		JLabel spinnoDesu = new JLabel();
		spinnoDesu.setText(" Steps: ");
		toolBar.add(spinnoDesu);
		
		
		SpinnerNumberModel stepsModel = new SpinnerNumberModel(0, 0, _ctrl.getSteps(), 1);
		JSpinner sp = new JSpinner(stepsModel);
		Dimension dsp = new Dimension(100, 30);
		sp.setMaximumSize(dsp);
		sp.setPreferredSize(dsp);
		toolBar.add(sp);
		
		toolBar.addSeparator();
		
		JLabel deltaDesu = new JLabel();
		deltaDesu.setText(" Delta- Time: ");
		toolBar.add(deltaDesu);
		
		JTextField deltaText = new JTextField();
		deltaText.setText(String.valueOf(_ctrl.getPs().getDeltaTime()));
		deltaText.setMaximumSize(new Dimension(100, 30));
		deltaText.setPreferredSize(new Dimension(100, 30));
		toolBar.add(deltaText);
		
		toolBar.add(Box.createHorizontalGlue());
		JButton _exitButt = new JButton();
		_exitButt.setIcon(new ImageIcon("resources/icons/exit.png"));
		toolBar.add(_exitButt);
		
	}	
		
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onForceLawsChanged(String fLawsDesc) {
		// TODO Auto-generated method stub
		
	}
	
}
