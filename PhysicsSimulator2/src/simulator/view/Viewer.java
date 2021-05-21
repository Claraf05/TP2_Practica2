package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;
import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class Viewer extends JComponent implements SimulatorObserver {
	
	private static final long serialVersionUID = 1L;
	
	private static final int _WIDTH = 600;
	private static final int _HEIGHT = 100;
	
	//atributos para los colores que vamos a utilizar
	private final Color red = Color.red;
	private final Color blue = Color.blue;
	private final Color green = Color.green;
	private final Color black = Color.black;
	
	private String help = "h: toggle help, +: zoom-in, -: zoom-out, =: fit";
	
	private int _centerX; //el x = 0 de viewer
	private int _centerY; //el y = 0 de viewer
	private double _scale; //la escala a la que se dibujan los bodies
	private List<Body> _bodies; //la lista de bodies
	private boolean _showHelp; //booleano para mostrar la ayuda
	private boolean _showVectors; //booleano para mostrar los vectores
	
	Viewer(Controller ctrl) { //constructor
		initGUI();
		ctrl.addObserver(this); //annade esta vista como observadora
	}
	
	private void initGUI() {
		
		// Suma border con title	
		setLayout(new BorderLayout());
		
		setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(black, 2), "Viewer", 
				TitledBorder.LEFT, TitledBorder.TOP));
		
		_bodies = new ArrayList<>(); //inicializa la lista de bodies
		_scale = 1.0; 
		_showHelp = true; 
		_showVectors = true;
		
		// Fija el tamanno usando _HEIGHT y _WIDTH
		setPreferredSize(new Dimension(_HEIGHT, _WIDTH));
		
		addKeyListener(new KeyListener() {
			// Completa con metodos vacios de la interfaz
			@Override
			public void keyPressed(KeyEvent e) { //al pulsar un boton
				switch (e.getKeyChar()) {
				case '-': _scale = _scale * 1.1; repaint(); //boton - aumenta la escala
				break;
				case '+': _scale = Math.max(1000.0, _scale / 1.1); //boton + zoom (disminuye la escala)
				repaint(); break;
				case '=': autoScale(); // boton = re escala con el metodo dado
				repaint();break;
				case 'h': _showHelp = !_showHelp; //a la h cambia de estado el showhelp
				repaint();break;
				case 'v': _showVectors = !_showVectors; //a la v cambia el estado de showvectors
				repaint();break;
				default:
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				//vacio
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				//vacio
				
			}
		});
		
		addMouseListener(new MouseListener() {
			
			// Completa con métodos vacíos de la interfaz
			
			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				//vacio
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				//vacio
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				//vacio
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				//vacio
				
			}
		});
		
		repaint();
		
	}
	
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D gr = (Graphics2D) g;
		
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		// Usa ’gr’ para dibujar, no ’g’
		
		_centerX = getWidth() / 2;
		_centerY = getHeight() / 2;
		
		// Dibuja una cruz en el centro
		gr.setColor(red);
		gr.drawLine(_centerX - 10, _centerY, _centerX + 10, _centerY);
		gr.drawLine(_centerX, _centerY - 10, _centerX, _centerY + 10);
		
		// Dibuja los bodies (con vectores si _showVectors es true)
		dibujarBodies(g);
		
		// Dibuja help si _showHelp es true
		if(_showHelp) {
			gr.setColor(red);
			gr.drawString(help, 20, getHeight() - (getHeight() - 30));
			String scale = "Scaling ratio: " + _scale;
			gr.drawString(scale, 20, getHeight() - (getHeight() - 50));
		}
		
	}
	
	private void dibujarBodies(Graphics g) { //dibuja los bodies
		
		for(Body b: _bodies ) {
			
			int bx = _centerX + (int) (b.getPosition().getX()/_scale); //valor x de la posicion del body b
			int by = _centerY - (int) (b.getPosition().getY()/_scale); //valor y de la posicion del body b
			
			g.setColor(blue); 
			g.fillOval( bx - 5 , by - 5, 11, 11); //ponemos el color azul y pintamos un circulo
			g.setColor(black);
			g.drawString(b.getId(), bx -5 , by -10); //ponemos el color negro y pintamos los id de los bodies 
			
			if(_showVectors) { //si showvectors es true se pintan los vectores
				this.drawLineWithArrow(g, bx, by, bx + (int) b.getSpeed().direction().scale(20).getX(), by - (int) b.getSpeed().direction().scale(20).getY(), 3, 3, green, green);
				this.drawLineWithArrow(g, bx, by, bx + (int) b.getForce().direction().scale(20).getX(), by - (int) b.getForce().direction().scale(20).getY(), 3, 3, red, red);
			}
		}
	}

	private void autoScale() {
		
		double max = 1.0;
		for (Body b : _bodies) {
			Vector2D p = b.getPosition();
			max = Math.max(max, Math.abs(p.getX()));
			max = Math.max(max, Math.abs(p.getY()));
		}
		
		double size = Math.max(1.0, Math.min((double) getWidth(),
				(double) getHeight()));
		_scale = max > size ? 4.0 * max / size : 1.0;
	}
	
	// Este método dibuja una linea de (x1,y1) a (x2,y2) con una flecha.
	// La flecha es de altura h and ancho w.
	// Los dos últimos argumentos son los colores de la flecha y la linea
	
	private void drawLineWithArrow(//
			Graphics g, //
			int x1, int y1, //
			int x2, int y2, //
			int w, int h, //
			Color lineColor, Color arrowColor) {
		int dx = x2 - x1, dy = y2 - y1;
		double D = Math.sqrt(dx * dx + dy * dy);
		double xm = D - w, xn = xm, ym = h, yn = -h, x;
		double sin = dy / D, cos = dx / D;
		x = xm * cos - ym * sin + x1;
		ym = xm * sin + ym * cos + y1;
		xm = x;
		x = xn * cos - yn * sin + x1;
		yn = xn * sin + yn * cos + y1;
		xn = x;
		int[] xpoints = { x2, (int) xm, (int) xn };
		int[] ypoints = { y2, (int) ym, (int) yn };
			g.setColor(lineColor);
			g.drawLine(x1, y1, x2, y2);
			g.setColor(arrowColor);
			g.fillPolygon(xpoints, ypoints, 3);
	}

	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			//necesitas la informacion de la lista de bodies para esta vista
			@Override
			public void run() {
				_bodies = bodies; //actualiza la lista de bodies
				autoScale(); //auto escala
				repaint(); //repinta
			}
		});
		
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			//necesitas la informacion de la lista de bodies actualizada para esta vista			
			@Override
			public void run() {
				_bodies = bodies;
				autoScale();
				repaint();
			}
		});
		
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		SwingUtilities.invokeLater(new Runnable() {
			//necesitas la informacion de la lista de bodies actualizada para esta vista			
			@Override
			public void run() {
				_bodies = bodies;
				autoScale();
				repaint();
				
			}
		});
		
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		SwingUtilities.invokeLater(new Runnable() {
			//necesitas la informacion de la lista de bodies actualizada para esta vista			
			@Override
			public void run() {
				_bodies = bodies;
				repaint();
			}
		});
		
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		//no influye en esta vista
	}

	@Override
	public void onForceLawsChanged(String fLawsDesc) {
		//no influye en esta vista		
	}
}
