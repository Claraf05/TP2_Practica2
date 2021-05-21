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
	
	private DefaultComboBoxModel<String> _forcesModel;
	private JComboBox<String> _fuerzas;
	private int _status;
	private List<JSONObject> listilla;
	private ForcesTableModel ta;
	private JTable tabla;
	private int forceIndex;
	
	public ForceLawsDialog(Frame papi, List<JSONObject> listilla) {
		super(papi, true);
		this.listilla = listilla;
		initGUI();
		
	}
	
	private class ForcesTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;
		
		private String[] _header = {"key", "value", "description"};
		private String[][] _data;
		
		public ForcesTableModel() {
			_data = new String[0][3];
			clear();
		}
		
		private void clear() {
			
			JSONObject data = listilla.get(forceIndex).getJSONObject("data");
			_data = new String[data.keySet().size()][3];
			Iterator<String> keys = data.keys();
			
	        for (int i = 0; i < data.keySet().size(); i++) {
	        	if(keys.hasNext()) {
	        		String n = keys.next();
	        		_data[i][0] = n;
	        		_data[i][1] = "";
	        		_data[i][2] = data.getString(n);
	        	}
	        }
	            
	        fireTableStructureChanged();
		}
		
		public String getData() {
			StringBuilder s = new StringBuilder();
	        s.append('{');
	        
	        for(int i = 0; i < getRowCount(); i++) {
	        	if(getValueAt(i, 1) != "" ) {
		        	s.append('"');
		        	s.append(getValueAt(i, 0));
		        	s.append('"');
		        	s.append(':');
		        	s.append(getValueAt(i, 1));
		        	s.append(',');
	        	}
	        	
	        }
	        
	        if (s.length() > 1) {
        		s.deleteCharAt(s.length() - 1);
        	}
			s.append('}');
				
	        return s.toString();
		}
		
		@Override
        public String getColumnName(int column) {
            return _header[column];
        }

		@Override
		public int getColumnCount() {
			
			return _header.length;
		}
		
		@Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            
			if(columnIndex == 1) {
            	return true;
            }
			
			return false;
        }

		@Override
		public int getRowCount() {
			
			return listilla.get(forceIndex).getJSONObject("data").keySet().size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			
			return _data[rowIndex][columnIndex];
		}
		
		@Override
        public void setValueAt(Object o, int rowIndex, int columnIndex) {
            _data[rowIndex][columnIndex] = o.toString();
        }
		
	}
	
	private void initGUI() {

		_status = 2;

		setTitle("Force Laws Selector");
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		JLabel helpMsg = new JLabel("Select the force");
		helpMsg.setAlignmentX(CENTER_ALIGNMENT);

		mainPanel.add(helpMsg);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		
		ta = new ForcesTableModel();
		tabla = new JTable(ta){
			
            private static final long serialVersionUID = 1L;

            // we override prepareRenderer to resized rows to fit to content
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
        
        //mainPanel.add(tabla);
        
        JScrollPane tabelScroll = new JScrollPane(tabla, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        mainPanel.add(tabelScroll);

		JPanel viewsPanel = new JPanel();
		viewsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(viewsPanel);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(buttonsPanel);

		_forcesModel = new DefaultComboBoxModel<>();
        
		_fuerzas = new JComboBox<>(_forcesModel);
		_fuerzas.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				forceIndex = _fuerzas.getSelectedIndex();
				ta.clear();
			}
		});
		
		viewsPanel.add(_fuerzas);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0;
				ForceLawsDialog.this.setVisible(false);
			}
		});
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_forcesModel.getSelectedItem() != null) {
					_status = 1;
					ForceLawsDialog.this.setVisible(false);
				}
			}
		});
		buttonsPanel.add(okButton);

		setPreferredSize(new Dimension(700, 500));
		pack();
		setResizable(false);
		setVisible(false);
	}

	public int open() {
		_forcesModel.removeAllElements();
		for(JSONObject j: this.listilla) {
			_forcesModel.addElement(j.getString("desc"));
		}
		
		setLocation(getParent().getLocation().x + 50, getParent().getLocation().y + 200);
		setVisible(true);
		
		return _status;
	}

	public String getForce() {
		
		StringBuilder s = new StringBuilder();
        s.append('{');
        s.append('"');
        s.append("type");
        s.append('"');
        s.append(':');
        
        s.append(listilla.get(forceIndex).getString("type"));
        s.append(',');
        
        s.append('"');
        s.append("data");
        s.append('"');
        s.append(':');
        
        s.append(ta.getData());
        
        s.append('}');
        
        return s.toString();
	}
	
}
