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
import sun.java2d.loops.DrawLine;

public class Viewer extends JComponent implements SimulatorObserver {
	
	private static final long serialVersionUID = 1L;
	
	private static final int _WIDTH = 600;
	private static final int _HEIGHT = 100;
	
	// Añade constantes para los colores
	
	private final Color red = Color.red;
	private final Color blue = Color.blue;
	private final Color green = Color.green;
	private final Color black = Color.black;
	
	private String help = "h: toggle help, +: zoom-in, -: zoom-out, =: fit";
	
	private int _centerX;
	private int _centerY;
	private double _scale;
	private List<Body> _bodies;
	private boolean _showHelp;
	private boolean _showVectors;
	
	Viewer(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}
	
	private void initGUI() {
		
		// Suma border con title
		
		setLayout(new BorderLayout());
		
		setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(black, 2), "Viewer", 
				TitledBorder.LEFT, TitledBorder.TOP));
		
		
		
		_bodies = new ArrayList<>();
		_scale = 1.0; 
		_showHelp = true; 
		_showVectors = true;
		
		// Fija el tamaño usando _HEIGHT y _WIDTH
		setPreferredSize(new Dimension(_HEIGHT, _WIDTH));
		
		addKeyListener(new KeyListener() {
			// Completa con métodos vacíos de la interfaz
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
				case '-': _scale = _scale * 1.1; repaint();
				break;
				case '+': _scale = Math.max(1000.0, _scale / 1.1);
				repaint(); break;
				case '=': autoScale();
				repaint();break;
				case 'h': _showHelp = !_showHelp;
				repaint();break;
				case 'v': _showVectors = !_showVectors;
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
			gr.drawString(help, 10, getHeight()- 570);
			String scale = "Scaling ratio: " + _scale;
			gr.drawString(scale, 10, getHeight()- 550);
		}
		
	}
	
	// Añade otros metodos
	
	private void dibujarBodies(Graphics g) {
		
		for(Body b: _bodies ) {
			
			int bx = _centerX + (int) (b.getPosition().getX()/_scale);
			int by = _centerY - (int) (b.getPosition().getY()/_scale);
			
			g.setColor(blue);
			g.fillOval( bx -5 , by -5, 11, 11);
			g.setColor(black);
			g.drawString(b.getId(), bx -5 , by -10);
			
			if(_showVectors) {
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
			
			@Override
			public void run() {
				_bodies = bodies;
				autoScale();
				repaint();
			}
		});
		
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			
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
			
			@Override
			public void run() {
				_bodies = bodies;
				repaint();
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
