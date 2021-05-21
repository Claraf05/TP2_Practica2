package simulator.view;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	Controller _ctrl;
	private ControlPanel controlPanel;
	private BodiesTable bt;
	private Viewer v;
	
	public MainWindow(Controller ctrl) {
		
		super("Physics Simulator");
		_ctrl = ctrl;
		initGUI();
		
	}
	
	private void initGUI() {
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		setContentPane(mainPanel);
		
		setSize(1200, 1000);
		setVisible(true);
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.controlPanel = new ControlPanel(_ctrl);
		
		mainPanel.add(this.controlPanel, BorderLayout.PAGE_START);
		
		JPanel bodiesPanel = new JPanel();
		bodiesPanel.setLayout(new BoxLayout(bodiesPanel, BoxLayout.Y_AXIS));
		bt = new BodiesTable(_ctrl);
		bodiesPanel.add(bt);
		
		v = new Viewer(_ctrl);
		bodiesPanel.add(v);
		
		mainPanel.add(bodiesPanel);
		
		StatusBar statusBar = new StatusBar(_ctrl); 
		
		mainPanel.add(statusBar, BorderLayout.PAGE_END);
		
		pack();
	}
	
	// Añade private/protected methods
}

