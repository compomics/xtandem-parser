package de.proteinms.xtandemparser.viewer;

import de.proteinms.xtandemparser.xtandem.InputParams;
import de.proteinms.xtandemparser.xtandem.PerformParams;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * The table retrieves the parameters and layouts them in a table.
 *
 * @author Thilo Muth
 */
public class ParameterTable extends JTable {

    /**
     * This constructor takes the input parameters
     *
     * @param inputParams the input parameters
     */
    public ParameterTable(InputParams inputParams) {
        super();
        setupTable();
    }

    /**
     * This constructor takes the performance parameters
     *
     * @param performParams the performance parameters
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
