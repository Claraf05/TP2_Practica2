package simulator.view;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	Controller _ctrl;
	private ControlPanel controlPanel; //atributo para el contol panel
	private BodiesTable bt; //atributo para la tabla de bodies
	private Viewer v; //atributo para el viewer
	
	public MainWindow(Controller ctrl) { //constructor
		
		super("Physics Simulator"); //pone de tutulo del JFrame Physics Simulator
		_ctrl = ctrl; //asigna el controler pasado por argumento
		initGUI(); //inicia la gui
		
	}
	
	private void initGUI() {
		
		JPanel mainPanel = new JPanel(new BorderLayout()); //panel principal donde se van a meter las 4 secciones
		
		setContentPane(mainPanel);
		
		setSize(1200, 1000); //tamanno de la ventana
		setVisible(true); //ventana visible
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.controlPanel = new ControlPanel(_ctrl); //creamos un control panel
		
		mainPanel.add(this.controlPanel, BorderLayout.PAGE_START); //ponemos el control panel arriba con page start
		
		JPanel bodiesPanel = new JPanel(); //creamos el panel donde pondremos la tabla y el viewer
		bodiesPanel.setLayout(new BoxLayout(bodiesPanel, BoxLayout.Y_AXIS)); //ponemos el panel para incluir las secciones de arriba a abajo
		bt = new BodiesTable(_ctrl); //creamos la nueva tabla de bodies
		bodiesPanel.add(bt); //lo annadimos al panel
		
		v = new Viewer(_ctrl); //creamos el viewer
		bodiesPanel.add(v); //lo annadimos al panel
		
		mainPanel.add(bodiesPanel); //annadimos el panel con la tabla y el viewer al main panel
		
		StatusBar statusBar = new StatusBar(_ctrl); //creamos el status bar
		
		mainPanel.add(statusBar, BorderLayout.PAGE_END); //lo annadimos abajo con Page end
		
		pack(); //metodo de jframe para ajustar los tamaños
	}
	
}

