package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;

public class BodiesTable extends JPanel {
	
	private static final long serialVersionUID = 1L;

	BodiesTable(Controller ctrl) {
		
		setLayout(new BorderLayout());
		//crea un borde nero con el titulo arriba a la izquierda, muy chulo
		setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black, 2), "Bodies", 
				TitledBorder.LEFT, TitledBorder.TOP));
	
		BodiesTableModel btm = new BodiesTableModel(ctrl); //inicializa el modelo de la tabla
		JTable jt = new JTable(btm); //construye la tabla a partir de ese modelo
		//mete la tabla en un jscrollpanel para tener scroll de ser necesario
		this.add(new JScrollPane(jt, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		setPreferredSize(new Dimension(900, 300));
	}	
	
}
