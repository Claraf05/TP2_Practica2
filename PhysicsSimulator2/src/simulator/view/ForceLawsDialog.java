package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
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
	
	public ForceLawsDialog(Frame papi, List<JSONObject> listilla) {
		super(papi, true);
		initGUI();
		this.listilla = listilla;
	}
	
	private class ForcesTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;
		
		private String[] _header = {"key", "value", "description"};
		private String[][] _data;
		
		public ForcesTableModel() {
			_data = new String[0][2];
			clear();
		}
		
		private void clear() {
			
	        for (int i = 0; i < 5; i++)
	            for (int j = 0; j < 3; j++)
	                _data[i][j] = "";
	        fireTableStructureChanged();
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
			//TODO
			return 0;
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

		JPanel viewsPanel = new JPanel();
		viewsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(viewsPanel);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(buttonsPanel);

		_forcesModel = new DefaultComboBoxModel<>();
		_fuerzas = new JComboBox<>(_forcesModel);

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

		setPreferredSize(new Dimension(500, 200));
		pack();
		setResizable(false);
		setVisible(false);
	}

	public int open() {
		_forcesModel.removeAllElements();
		for(JSONObject j: this.listilla) {
			_forcesModel.addElement(j.getString("desc"));
		}
		
		setLocation(getParent().getLocation().x + 400, getParent().getLocation().y + 400);
		setVisible(true);
		
		return _status;
	}

	public JSONObject getForce() {
		return (JSONObject) _forcesModel.getSelectedItem();
	}
	
}
