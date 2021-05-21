package simulator.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.json.JSONObject;



public class ForceLawsDialog extends JDialog{
	
	private static final long serialVersionUID = 1L;
	
	private DefaultComboBoxModel<String> _forcesModel; //modelo del jcombo box
	private JComboBox<String> _fuerzas; //jcombo box donde seleccionas que fuerza usar
	private int _status; //estado, =1 es por que le has dado a ok, = 0 le diste a cancel
	private List<JSONObject> listilla;
	private ForcesTableModel ta; //modelo de la tabla de fuerzas
	private JTable tabla; //tabla de fuerzas en si
	private int forceIndex; //indice de la fuerza seleccionada del jcombo box
	
	public ForceLawsDialog(Frame papi, List<JSONObject> listilla) { //constructor
		super(papi, true);
		this.listilla = listilla;
		initGUI();
		
	}
	
	private class ForcesTableModel extends AbstractTableModel { //clase interna, modelo de la tabla

		private static final long serialVersionUID = 1L;
		
		private String[] _header = {"key", "value", "description"}; //titulos de las columnas
		private String[][] _data; //informacion de las celdas de la tabla
		
		public ForcesTableModel() { //constructor
			_data = new String[0][3]; //iniciamos la tabla con 0 filas y 3 columnas
			clear(); //limpia y actualiza la tabla
		}
		
		private void clear() { //metodo "refresh", cambia la tabla en los casos en los que cambia la fuerza
			//cogemos el data de la ley de fuerza que esta en la posicion forceIndex de la lista
			JSONObject data = listilla.get(forceIndex).getJSONObject("data");
			_data = new String[data.keySet().size()][3]; //cambiamos el tamanno de la tabla dependiendo de los keys que tenga la fuerza
			Iterator<String> keys = data.keys(); //usamos un iterador para recorrer los keys de 1 en 1 (JSONObject.keys() te obliga al uso de iteradores)
			
	        for (int i = 0; i < data.keySet().size(); i++) { //recorrido desde 0 hasta el numero de keys del data
	        	if(keys.hasNext()) { //vemos si hay mas keys despues
	        		String n = keys.next(); //cogemos el siguiente key (la primera vez es el primero)
	        		_data[i][0] = n; //ponemos en la columna 0 el nombre de la key
	        		_data[i][1] = ""; //la columna 1 vacia (es la que modificaremos)
	        		_data[i][2] = data.getString(n); //la columna 2 la descripcion del key
	        	}
	        }
	            
	        fireTableStructureChanged(); //metodo predeterminado para actualizar la tabla
		}
		
		public String getData() { //construimos un string en formato json de los datos de la tabla
			StringBuilder s = new StringBuilder();
	        s.append('{');
	        
	        for(int i = 0; i < getRowCount(); i++) { //recorremos la tabla por filas
	        	if(getValueAt(i, 1) != "" ) { //si la columna value no esta vacia cogemos los datos para guardarlos en el data
		        	s.append('"');
		        	s.append(getValueAt(i, 0));
		        	s.append('"');
		        	s.append(':');
		        	s.append(getValueAt(i, 1));
		        	s.append(',');
	        	}
	        	
	        }
	        
	        if (s.length() > 1) { //borra la ultima coma cuando se acaba
        		s.deleteCharAt(s.length() - 1);
        	}
			s.append('}');
				
	        return s.toString(); //devuelve el string generado
		}
		
		@Override
        public String getColumnName(int column) { //el nombre de la columna se coge del _header
            return _header[column];
        }

		@Override
		public int getColumnCount() { //el contador de columnas es la longitud de _header
			return _header.length;
		}
		
		@Override
        public boolean isCellEditable(int rowIndex, int columnIndex) { //para permitir la edicion de celdas
            
			if(columnIndex == 1) { //en la columna 1 (la columna values) las celdas se pueden editar
            	return true;
            }
			
			return false;
        }

		@Override
		public int getRowCount() {
			//el contador de filas es el numero de keys que tiene el JSON OBject data de nuestra fuerza
			//en caso de newton 1 (G), en caso de mtfp (c, g)
			return listilla.get(forceIndex).getJSONObject("data").keySet().size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			//para coger el dato de la celda [rowIndex][columnIndex]
			return _data[rowIndex][columnIndex];
		}
		
		@Override
        public void setValueAt(Object o, int rowIndex, int columnIndex) {
			//para poner el valor en la celda [rowIndex][columnIndex]
            _data[rowIndex][columnIndex] = o.toString();
        }
		
	}
	
	private void initGUI() {

		_status = 2;

		setTitle("Force Laws Selector");
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		//creamos un nuevo panel en Y_axis, para incluir los elementos de arriba a abajo
		JLabel helpMsg = new JLabel("Select the force");
		helpMsg.setAlignmentX(CENTER_ALIGNMENT); //texto centrado, no pegado al lado izquierdo

		mainPanel.add(helpMsg);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); //creamos un espacio en blanco
		
		ta = new ForcesTableModel(); //creamos el modelo
		tabla = new JTable(ta) { //creamos la tabla a partir de ese modelo de tabla
			
            private static final long serialVersionUID = 1L;

            // we override prepareRenderer to resized rows to fit to content
            //codigo dado en el enunciado
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(
                        Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                return component;
            }
        }; 
        
        JScrollPane tabelScroll = new JScrollPane(tabla, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //metemos la tabla en un jscrollpanel por si se necesita
        mainPanel.add(tabelScroll); //annadimos la tabla al panel que hemos creado

		JPanel viewsPanel = new JPanel(); //panel donde pondremos el jcombobox
		viewsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(viewsPanel);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		
		JPanel buttonsPanel = new JPanel(); //panel donde pondremos los botones ok y cancel
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(buttonsPanel);

		_forcesModel = new DefaultComboBoxModel<>(); //inicializamos el modelo del combo box
        
		_fuerzas = new JComboBox<>(_forcesModel); //creamos el combo box a parit de ese modelo
		_fuerzas.addActionListener(new ActionListener() { //al cambiar de fuerza en el combo box
			
			@Override
			public void actionPerformed(ActionEvent e) {
				forceIndex = _fuerzas.getSelectedIndex(); //ponemos el atributo force index el indice seleccionado del jcombobox
				ta.clear(); //limpia y actualiza la tabla
			}
		});
		
		viewsPanel.add(_fuerzas); //annade al panel el jcombobox
		
		JButton cancelButton = new JButton("Cancel"); //boton cancel
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0; //pone status a 0 y cierra el force dialog
				ForceLawsDialog.this.setVisible(false);
			}
		});
		buttonsPanel.add(cancelButton); //annade el cancel al panel de botones

		JButton okButton = new JButton("OK"); //ok boton
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_forcesModel.getSelectedItem() != null) { //si ha seleccionado algo en el jcombobox
					_status = 1; //pone el status a 1 y cierra force dialog
					ForceLawsDialog.this.setVisible(false);
				}
			}
		});
		buttonsPanel.add(okButton); //annade el boton al panel de botones

		setPreferredSize(new Dimension(700, 500));
		pack(); //para ajustar todas las medidas
		setResizable(false);
		setVisible(false);
	}

	public int open() { //al abrir el force laws dialog
		_forcesModel.removeAllElements(); //borra el modelo del jcombobox
		for(JSONObject j: this.listilla) { //recorre la lista de fuerzas y las va annadiendo al jcombo box
			_forcesModel.addElement(j.getString("desc")); //annade la desc solo
		}
		
		setLocation(getParent().getLocation().x + 50, getParent().getLocation().y + 200); //localizacion dependiente del padre
		setVisible(true);
		
		return _status; //devuelve el status
	}

	public String getForce() { //procesa todos los datos de la fuerza seleccionada guardandolo en un string
		//string en formato json
		StringBuilder s = new StringBuilder();
        s.append('{');
        s.append('"');
        s.append("type");
        s.append('"');
        s.append(':');
        
        s.append(listilla.get(forceIndex).getString("type")); //consigue el tipo de la fuerza y lo mete en el key type
        s.append(',');
        
        s.append('"');
        s.append("data");
        s.append('"');
        s.append(':');
        
        s.append(ta.getData()); //consigue los datos de la tabla para el json data        
        s.append('}');
        
        return s.toString();
	}
	
}
