package viewer;

import xtandem.InputParams;
import xtandem.PerformParams;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * The table retrieves the parameters and layouts them in a table.
 */
public class ParameterTable extends JTable {

    /**
	 * This constructor takes the input parameters
	 *
	 * @param inputParams
	 */
	public ParameterTable(InputParams inputParams) {
        super();
        setupTable();
	}

    /**
	 * This constructor takes the performance parameters
	 *
	 * @param performParams
	 */
	public ParameterTable(PerformParams performParams) {
        super();
        setupTable();
	}

    /**
	 * Sets up the table with the correct headings and model
	 */
	public void setupTable() {

		DefaultTableModel tModel = new DefaultTableModel();

        setModel(tModel);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setPreferredScrollableViewportSize(new Dimension(1200, 500));	   
	}
}
