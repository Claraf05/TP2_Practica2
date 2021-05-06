package simulator.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;

public class MainWindow extends JFrame {
	
	Controller _ctrl;
	
	public MainWindow(Controller ctrl) {
		
	super("Physics Simulator");
	_ctrl = ctrl;
	initGUI();
	
	}
	
	private void initGUI() {
		
	JPanel mainPanel = new JPanel(new BorderLayout());
	
	setContentPane(mainPanel);
	
	// Completa el m�todo para construir la GUI
	
	}
	
	// A�ade private/protected methods
}

