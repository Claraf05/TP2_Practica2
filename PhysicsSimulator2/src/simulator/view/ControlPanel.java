package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import org.json.JSONObject;

import com.sun.corba.se.impl.io.InputStreamHook;
import com.sun.javafx.tk.Toolkit;

import javafx.stage.FileChooser;
import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.SimulatorObserver;

public class ControlPanel extends JPanel implements SimulatorObserver {
	
	private Controller _ctrl;
	private boolean _stopped;
	
	private Double deltaT;
	private JFileChooser fc;
	
	private ForceLawsDialog fld;
	private JButton _loadButt;
	private JButton _forceLawsButt;
	private JButton _runButt;
	private JButton _stopButt;
	private JButton _exitButt;
	
	private JTextField deltaText;
	private JSpinner sp;
	
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
		
		_loadButt = creaLoadButton();
		toolBar.add(_loadButt);
		
		toolBar.addSeparator();
		
		_forceLawsButt = creaForceLawsButton();
		toolBar.add(_forceLawsButt);
		
		toolBar.addSeparator();
		
		_runButt = creaRunButton();
		toolBar.add(_runButt);
		
		_stopButt = new JButton();
		_stopButt.setIcon(new ImageIcon("resources/icons/stop.png"));
		toolBar.add(_stopButt);
		
		
		toolBar.addSeparator();
		
		JLabel spinnoDesu = new JLabel();
		spinnoDesu.setText(" Steps: ");
		toolBar.add(spinnoDesu);
		
		
		SpinnerNumberModel stepsModel = new SpinnerNumberModel(0, 0, _ctrl.getSteps(), 1);
		sp = new JSpinner(stepsModel);
		Dimension dsp = new Dimension(100, 30);
		sp.setMaximumSize(dsp);
		sp.setPreferredSize(dsp);
		toolBar.add(sp);
		
		toolBar.addSeparator();
		
		JLabel deltaDesu = new JLabel();
		deltaDesu.setText(" Delta- Time: ");
		toolBar.add(deltaDesu);
		
		deltaText = new JTextField();
		deltaText.setText(String.valueOf(_ctrl.getPs().getDeltaTime()));
		deltaText.setMaximumSize(new Dimension(100, 30));
		deltaText.setPreferredSize(new Dimension(100, 30));
		
		deltaText.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		
		toolBar.add(deltaText);
		
		toolBar.add(Box.createHorizontalGlue());
		_exitButt = new JButton();
		_exitButt.setIcon(new ImageIcon("resources/icons/exit.png"));
		toolBar.add(_exitButt);
		
	}	
	
	private JButton creaRunButton() {
		
		JButton _runButt = new JButton();
		_runButt.setIcon(new ImageIcon("resources/icons/run.png"));
		
		_runButt.setToolTipText("Load bodies file into the editor");
		_runButt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				desactivaBotones();
				_stopped = false;
				_ctrl.setDeltaTime(Double.valueOf(deltaText.getText()));
				run_sim(Integer.valueOf(sp.getValue().toString()));
			}
		});
		
		return _runButt;
	}

	private void desactivaBotones() {
		_loadButt.setEnabled(false);
		_forceLawsButt.setEnabled(false);
		_runButt.setEnabled(false);
		_exitButt.setEnabled(false);
	}
	
	private JButton creaForceLawsButton() {
		JButton _forceLawsButt = new JButton();
		_forceLawsButt.setIcon(new ImageIcon("resources/icons/physics.png"));
		
		_forceLawsButt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JDialog dg = new JDialog();
				selectFl();
				
				
			}
		});
		
		return _forceLawsButt;
	}
	
	private void selectFl() {
		Frame jf = (Frame) SwingUtilities.getWindowAncestor(this);
		
		List<JSONObject> listilla = _ctrl.getForceLawsInfo();
		
		if(fld == null) {
			fld = new ForceLawsDialog(jf, listilla);
		}
		
		int status = fld.open();
		
		if(status == 0) {
			JOptionPane.showMessageDialog(fld, "cancelled");
			
		}
		else if(status == 1){
			
			JOptionPane.showMessageDialog(fld, "You chose: " + fld.getForce().getString("desc"));
			
			
			//poner la tabla dependiendo de la fuerza 
			
		}
	}

	private JButton creaLoadButton() {
		
		JButton _loadButt = new JButton();
		_loadButt.setIcon(new ImageIcon("resources/icons/open.png"));
		_loadButt.setToolTipText("Load bodies file into the editor");
		_loadButt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fc = new JFileChooser();
				fc.setCurrentDirectory(new File("resources/examples/"));
				int selection = fc.showOpenDialog(_loadButt);
				
				if(selection == JFileChooser.APPROVE_OPTION) {
					_ctrl.reset();
					File ficherito = fc.getSelectedFile();
					InputStream in;
					try {
						in = new FileInputStream(ficherito);
						_ctrl.loadBodies(in);
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(fc, "file could not be opened");
					}
					
				}
				else if(selection == JFileChooser.CANCEL_OPTION) {
					JOptionPane.showMessageDialog(_loadButt, "you cancelled the process");
				}
			}
		});
		
		return _loadButt;
	}
	
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1);
			} catch (Exception e) {
			// Muestra el error con una ventana JOptionPane
			// Pon enable todos los botones
				JOptionPane.showMessageDialog(_runButt, "Simulator failed");
				_stopped = true;
				_loadButt.setEnabled(true);
				_forceLawsButt.setEnabled(true);
				_runButt.setEnabled(true);
				_exitButt.setEnabled(true);
				return;
			}
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					run_sim(n-1);
				} });
			} 
		else {
			_stopped = true;
			_loadButt.setEnabled(true);
			_forceLawsButt.setEnabled(true);
			_runButt.setEnabled(true);
			_exitButt.setEnabled(true);
		}
				
	}
		
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		SwingUtilities.invokeLater( new Runnable() { 
				
			@Override 
			public void run() { _ctrl.setDeltaTime(Double.valueOf(deltaText.getText())); }
				
		});
		
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		SwingUtilities.invokeLater( new Runnable() { 
			
			@Override 
			public void run() { _ctrl.setDeltaTime(Double.valueOf(deltaText.getText())); }
				
		});
		
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
		SwingUtilities.invokeLater( new Runnable() { 
			
			@Override 
			public void run() { _ctrl.setDeltaTime(Double.valueOf(deltaText.getText())); }
				
		});
		
	}

	@Override
	public void onForceLawsChanged(String fLawsDesc) {
		// TODO Auto-generated method stub
		
	}
	
}
