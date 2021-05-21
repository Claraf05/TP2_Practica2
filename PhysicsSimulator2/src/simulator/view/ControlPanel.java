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


import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.SimulatorObserver;

public class ControlPanel extends JPanel implements SimulatorObserver {
	
	private static final long serialVersionUID = 1L;
	
	private Controller _ctrl;
	private boolean _stopped; //booleano que determina si la ejecucion del simulador está en marcha o no
	
	private Double deltaT;
	private JFileChooser fc; //ventana de seleccion de fichero de bodies
	
	private ForceLawsDialog fld; //ventana de seleccion de leyes de fuerza
	private JButton _loadButt;
	private JButton _forceLawsButt;
	private JButton _runButt;
	private JButton _stopButt;
	private JButton _exitButt;
	
	private JTextField deltaText;
	private JSpinner sp; //spiner del numero de steps
	
	// Añade atributos para JToolBar, botones, Jspinner, JFileChooser,etc.
	
	ControlPanel(Controller ctrl) { //constructor
		
		_ctrl = ctrl;
		_stopped = true; //simulacion parada
		initGUI(); //inicia la gui
		_ctrl.addObserver(this); //annade esta vista como vista observadora
		
	}
	
	private void initGUI() {
		// Construye la tool bar con todos sus botones, etc.
		
		JToolBar toolBar = new JToolBar(); //crea una toolbar donde annadiremos todos los componenetes (botones, Jspinner y JTextField)
		setLayout(new BorderLayout());
		this.add(toolBar, BorderLayout.PAGE_START);
		
		_loadButt = creaLoadButton(); //creamos el loas button, mirar el metodo creaLoadButton
		toolBar.add(_loadButt); //lo annadimos al toolbar
		
		toolBar.addSeparator(); //ponemos un separador
		
		_forceLawsButt = creaForceLawsButton(); //creamos el boton de leyes de fuerza, mirar el metodo creaForceLawsButton
		toolBar.add(_forceLawsButt); //lo annadimos al toolbar
		
		toolBar.addSeparator(); //ponemos un separador
		
		_runButt = creaRunButton(); //creamos el boton de run, mirar creaRunButton
		toolBar.add(_runButt); //lo annadimos al toolbar
		
		_stopButt = creaStopButton(); //creamos el boton stop, mirar creaStop button
		toolBar.add(_stopButt); //lo annadimos al toolbar
		
		
		toolBar.addSeparator(); //ponemos un separador
		
		JLabel spinnoDesu = new JLabel(); //creamos el JLabel donde pone " Steps: "
		spinnoDesu.setText(" Steps: ");
		toolBar.add(spinnoDesu);
		
		//creamos el Jspiner con un modelo de Jspiner que tiene: val. inicial, val. minimo, val. maximo y cuanto sube o baja con las flechitas (de 1 en 1)
		SpinnerNumberModel stepsModel = new SpinnerNumberModel(_ctrl.getSteps(), 0, 1000000000, 1);
		sp = new JSpinner(stepsModel); // creamos el jspinner con ese modelo
		
		Dimension dsp = new Dimension(100, 30);
		sp.setMaximumSize(dsp);
		sp.setPreferredSize(dsp);
		toolBar.add(sp); //lo annadimos a la toolbar
		
		toolBar.addSeparator(); //ponemos un separador
		
		JLabel deltaDesu = new JLabel(); //creamos el JLabel donde pone " Delta- Time: "
		deltaDesu.setText(" Delta- Time: ");
		toolBar.add(deltaDesu);
		
		deltaText = new JTextField(); //creamos el jtext field
		deltaText.setText(String.valueOf(_ctrl.getPs().getDeltaTime())); //se metemos el texto del getDeltaTime
		deltaText.setMaximumSize(new Dimension(100, 30));
		deltaText.setPreferredSize(new Dimension(100, 30));
		
		toolBar.add(deltaText); //annadimos delta text al toolbar
		
		toolBar.add(Box.createHorizontalGlue()); //creamos un espacio en blanco para poner el exit al final
		
		_exitButt = createExitButton(); //creamos el exit button
		toolBar.add(_exitButt); //lo annadimos al toolbar
		
	}	
	
	private JButton createExitButton() { //crea exit button
		
		JButton _exitButt = new JButton(); //nuevo boton
		_exitButt.setIcon(new ImageIcon("resources/icons/exit.png")); //le asigna un icono
		_exitButt.setToolTipText("Exit"); //le asigna el texto que sale al poner el cursor encima
		_exitButt.addActionListener(new ActionListener() { //metodo para cuando se pulsa el boton
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] textoBotones = { "Que si!", "Que no!"};
				//mostramos un dialogo de opciones si o no, lo igualamos a un int
				int res = JOptionPane.showOptionDialog(_exitButt, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, textoBotones, textoBotones[1]);
				if(res == JOptionPane.YES_OPTION) { //si es si detiene la ejecucion de la aplicacion
					System.exit(0);
				}
			}
		});
		
		return _exitButt; //devuelve el boton exit
	}

	private JButton creaStopButton() { //boton parar
		//creas un boton, le asignas icono y texto cuando pones encima el cursor
		JButton _stopButt = new JButton();
		_stopButt.setIcon(new ImageIcon("resources/icons/stop.png"));
		_stopButt.setToolTipText("Stops the simulation");
		_stopButt.addActionListener(new ActionListener() { //al pulsar
			
			@Override
			public void actionPerformed(ActionEvent e) {
				_stopped = true; //pone el booleano a true, run_sim se encarga de parar la ejecucion si ese booleano es true
			}
		});
		
		return _stopButt; //devueve el boton
	}

	private JButton creaRunButton() { //boton run
		//crea un nuevo jbutton y le asigna el icono
		JButton _runButt = new JButton();
		_runButt.setIcon(new ImageIcon("resources/icons/run.png"));
		
		_runButt.setToolTipText("Runs the simulation"); //texto cuando pones el cursor encima
		_runButt.addActionListener(new ActionListener() { //al pulsarlo
			
			@Override
			public void actionPerformed(ActionEvent e) {
				_stopped = false; //el booleano es false porque no esta parado
				_ctrl.setDeltaTime(Double.valueOf(deltaText.getText())); //asigna al simulador el delta time del jtextfield
				desactivaBotones(); //desactiva todos los botones menos el stop
				run_sim(Integer.valueOf(sp.getValue().toString())); //llama a run_sim para ejecutar el simulador, con el valor steps dado por el Jspiner
			}
		});
		
		return _runButt; //devuelve el boton
	}

	private void desactivaBotones() { //desactiva todos los botones menos el stop, el jspinner y el jtextfield tmbn
		_loadButt.setEnabled(false);
		_forceLawsButt.setEnabled(false);
		_runButt.setEnabled(false);
		_exitButt.setEnabled(false);
	}
	
	private JButton creaForceLawsButton() { //boton de leyes de fuerza
		JButton _forceLawsButt = new JButton();
		_forceLawsButt.setIcon(new ImageIcon("resources/icons/physics.png"));
		
		_forceLawsButt.addActionListener(new ActionListener() { //al pulsarlo
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				selectFl(); //llamamos al metodo que crea el force laws dialog
				
			}
		});
		
		return _forceLawsButt; //devuelve el boton
	}
	
	private void selectFl() { //al pulsar el boton de eleccion de leyes de fuerza
		Frame jf = (Frame) SwingUtilities.getWindowAncestor(this); //guardamos el ancestor del control panel (main window)
		
		List<JSONObject> listilla = _ctrl.getForceLawsInfo(); //obtenemos la info e las leyes de fuerza y la guardamos en una lista
		
		if(fld == null) { //si no existe un force laws dialog iniciado lo iniciamos
			fld = new ForceLawsDialog(jf, listilla); //le pasamos el ancestor para centrarlo dependiendo de el
		}
		
		int status = fld.open(); //llamamos al open del force laws dialog
		
		if(status == 0) { //si devuelve 0 le dimos al boton de cancel
			JOptionPane.showMessageDialog(fld, "cancelled");
			fld = null;
		}
		else if(status == 1) { //si devuelve 1 le dimos a ok, por tanto se carga la fuerza
			
			//poner la tabla dependiendo de la fuerza 
			_ctrl.setForceLaws(new JSONObject(fld.getForce())); //mete la fuerza seleccionada al simulador
			
			JOptionPane.showMessageDialog(fld, "You chose: " + fld.getForce()); //te muestra que fuerza es (no necesario)
			fld = null;
		}
	}

	private JButton creaLoadButton() { //boton load
		//creamos un nuevo boton, le asignamos icono y tool tip text
		JButton _loadButt = new JButton();
		_loadButt.setIcon(new ImageIcon("resources/icons/open.png"));
		_loadButt.setToolTipText("Load bodies file into the editor");
		_loadButt.addActionListener(new ActionListener() { //al pulsarlo
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fc = new JFileChooser(); //creamos un J file chooser (pare seleccionar un archivo)
				fc.setCurrentDirectory(new File("resources/examples/")); //ponemos el directorio donde empieza
				int selection = fc.showOpenDialog(_loadButt); //mostramos el dialogo de operacion de abrir (importante en jfilechooser)
				
				if(selection == JFileChooser.APPROVE_OPTION) { //si le damos a ok al fichero seleccionado
					_ctrl.reset(); //reseteamos el simulador
					File ficherito = fc.getSelectedFile(); //ponemos el nuevo fichero
					InputStream in;
					try {
						in = new FileInputStream(ficherito);
						_ctrl.loadBodies(in); //lo metemos al simulador como flujo de entrada (input stream)
					} catch (Exception e1) { //si falla al abrir fichero salta excepcion se muestra con un dialogo
						JOptionPane.showMessageDialog(fc, "file could not be opened");
					}
					
				}
				else if(selection == JFileChooser.CANCEL_OPTION) { //en el caso de cancelar el load, se muestra un dialogo de cancelacion
					JOptionPane.showMessageDialog(_loadButt, "you cancelled the process");
				}
			}
		});
		
		return _loadButt; //devuelve el boton
	}
	
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1); //prueba el run para ver si está  todo correcto
			} catch (Exception e) {
			// Muestra el error con una ventana JOptionPane
				JOptionPane.showMessageDialog(_runButt, "Simulator failed");
				_stopped = true;
				// Pone enable todos los botones
				_loadButt.setEnabled(true);
				_forceLawsButt.setEnabled(true);
				_runButt.setEnabled(true);
				_exitButt.setEnabled(true);
				return;
			}
			SwingUtilities.invokeLater( new Runnable() { //invoke later es para que las vistas se vayan actualizando por cada run, si no estuviera saldria solo el estado final
				@Override
				public void run() {
					run_sim(n-1); //si la prueba no ha fallado lo ejecuta las veces que quedan
				} });
			} 
		else {
			// Pone enable todos los botones
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
			//al registrar una vista nueva, control panel debe modificar el valor de su jtextfield para tener el delta del simulador	
			@Override 
			public void run() { _ctrl.setDeltaTime(Double.valueOf(deltaText.getText())); }
				
		});
		
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		SwingUtilities.invokeLater( new Runnable() { 
			//al resetear tambien puede modificarse el delta
			@Override 
			public void run() { _ctrl.setDeltaTime(Double.valueOf(deltaText.getText())); }
				
		});
		
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		// vacio, no influye
		
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		// vacio, no influye
		
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		SwingUtilities.invokeLater( new Runnable() { 
			//si se cambia dt en el simulador debe aparecer en el jtextfield
			@Override 
			public void run() { _ctrl.setDeltaTime(Double.valueOf(deltaText.getText())); }
				
		});
		
	}

	@Override
	public void onForceLawsChanged(String fLawsDesc) {
		// vacio, no influye
	}
	
}
