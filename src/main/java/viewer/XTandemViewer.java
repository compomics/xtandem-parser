package viewer;

import interfaces.Modification;
import interfaces.Peaklist;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTableHeader;
import org.xml.sax.SAXException;

import xtandem.FixedModification;
import xtandem.FragmentIon;
import xtandem.MgfPeak;
import xtandem.MgfPeaklist;
import xtandem.Parameters;
import xtandem.Peptide;
import xtandem.PeptideMap;
import xtandem.Spectrum;
import xtandem.SupportData;
import xtandem.VariableModification;
import xtandem.XTandemFile;
import be.proteomics.util.gui.spectrum.DefaultSpectrumAnnotation;
import be.proteomics.util.gui.spectrum.SpectrumPanel;

import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.DesertBlue;

/**
 * This class provides a basic viewer for the spectra.
 * @author Thilo Muth
 *
 */
public class XTandemViewer extends JFrame {
	public final static String APPTITLE = "X!Tandem Viewer";
	public final static String VERSION = "v. 1.0";
	private SpectrumPanel spectrumPanel;
	private String iXTandemFileString;
	private String iRawFile;
    private HashMap<Integer, ArrayList<Peptide>> peptideMap;
    private HashMap<Integer, ArrayList<Double>> allMzValues;
    private HashMap<Integer, ArrayList<Double>> allIntensityValues;
    private HashMap<Integer, ArrayList<Double>> scaledMzValues;
    private HashMap<Integer, ArrayList<Double>> scaledIntensityValues;
    private HashMap<Integer, ArrayList<Modification>> allFixMods;
    private HashMap<Integer, ArrayList<Modification>> allVarMods;  
    private HashMap<String, FragmentIon[]> ionsMap;	
    private HashMap<Integer, String> accMap;
    private Vector spectraTableColToolTips;
    private Vector spectrumTableColToolTips;
    private Vector spectrumJXTableColToolTips;
    private Vector identificationsJXTableColumnToolTips;
    private HashMap<String, Vector<DefaultSpectrumAnnotation>> allAnnotations;
    private JCheckBox aIonsJCheckBox;
    private JCheckBox bIonsJCheckBox;
    private JCheckBox cIonsJCheckBox;
    private JCheckBox chargeOneJCheckBox;    
    private JCheckBox chargeTwoJCheckBox;
    private JCheckBox chargeOverTwoJCheckBox;
    private JXTable identificationsTable;
    private JLabel jLabel1;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane3;
    private JScrollPane jScrollPane4;
    private JSeparator jSeparator1;
    private JSeparator jSeparator2;
    private JLabel modificationDetailsJLabel;   
    private JXTable spectraTable;
    private JPanel spectrumJPanel;
    private JXTable spectrumJXTable;
    private JCheckBox xIonsJCheckBox;
    private JCheckBox yIonsJCheckBox;
    private JCheckBox zIonsJCheckBox;    
    private boolean iParseAll = false;
    private double ionCoverageErrorMargin = 0.0;
    private ProgressDialog progressDialog;
	private JMenuItem openMenuItem;
	private JMenuItem exitMenuItem;
	private JMenuItem aboutMenuItem;
	private JMenuItem helpMenuItem;
    
	/**
     * Constructor gets the xml output file the raw file and boolean for parsing.
     */
    public XTandemViewer(String aXTandemXmlFile, String aRawFile, boolean aParseAll) {
    	iRawFile = aRawFile;    	
    	iParseAll = aParseAll;
    	// Set JGoodies Look&Feel
		//configureUI();

		// Construct the menu
		constructMenu();
		
        initComponents();
        
        // Sets icon image
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().
                getResource("/xtandemviewer.gif")));
        spectraTable.getColumn(" ").setMaxWidth(35);
        spectraTable.getColumn(" ").setMinWidth(35);
        spectraTable.getColumn("m/z").setMaxWidth(65);
        spectraTable.getColumn("m/z").setMinWidth(65);
        spectraTable.getColumn("Charge").setMaxWidth(65);
        spectraTable.getColumn("Charge").setMinWidth(65);
        spectraTable.getColumn("Identified").setMaxWidth(80);
        spectraTable.getColumn("Identified").setMinWidth(80);
        spectrumJXTable.getColumn(" ").setMaxWidth(35);
        spectrumJXTable.getColumn(" ").setMinWidth(35);
        identificationsTable.getColumn(" ").setMaxWidth(35);
        identificationsTable.getColumn(" ").setMinWidth(35);
        identificationsTable.getColumn("Start").setMaxWidth(45);
        identificationsTable.getColumn("Start").setMinWidth(45);
        identificationsTable.getColumn("End").setMaxWidth(45);
        identificationsTable.getColumn("End").setMinWidth(45);
        identificationsTable.getColumn("Exp. Mass").setMaxWidth(75);
        identificationsTable.getColumn("Exp. Mass").setMinWidth(75);
        identificationsTable.getColumn("Theo. Mass").setMaxWidth(75);
        identificationsTable.getColumn("Theo. Mass").setMinWidth(75);
        identificationsTable.getColumn("E-value").setMinWidth(75);
        identificationsTable.getColumn("E-value").setMaxWidth(75);
        identificationsTable.getColumn("Accession").setPreferredWidth(10);
        spectraTable.getTableHeader().setReorderingAllowed(false);
        spectrumJXTable.getTableHeader().setReorderingAllowed(false);
        identificationsTable.getTableHeader().setReorderingAllowed(false);
        spectraTableColToolTips = new Vector();
        spectraTableColToolTips.add("Spectrum Number");
        spectraTableColToolTips.add("Spectrum File Name");
        spectraTableColToolTips.add("Precursor Mass Over Charge Ratio");
        spectraTableColToolTips.add("Precursor Charge");
        spectraTableColToolTips.add("Spectrum Identified");
        spectrumTableColToolTips = new Vector();
        spectrumTableColToolTips.add(null);
        spectrumTableColToolTips.add("Mass Over Charge Ratio");
        spectrumTableColToolTips.add("Intensity");
        spectrumJXTableColToolTips = new Vector();
        spectrumJXTableColToolTips.add(null);
        spectrumJXTableColToolTips.add("Mass Over Charge Ratio");
        spectrumJXTableColToolTips.add("Intensity");
        identificationsJXTableColumnToolTips = new Vector();
        identificationsJXTableColumnToolTips.add("Spectrum Number");
        identificationsJXTableColumnToolTips.add("Peptide Sequence");
        identificationsJXTableColumnToolTips.add("Modified Peptide Sequence");
        identificationsJXTableColumnToolTips.add("Peptide Start Index");
        identificationsJXTableColumnToolTips.add("Peptide End Index");
        identificationsJXTableColumnToolTips.add("Experimental Mass");
        identificationsJXTableColumnToolTips.add("Theoretical Mass");
        identificationsJXTableColumnToolTips.add("E-value");
        identificationsJXTableColumnToolTips.add("Protein Accession Number");
        setMinimumSize(new Dimension(900, 600));    
        setLocationRelativeTo(null);
        setVisible(true);
        insertFiles(aXTandemXmlFile);
        
    }
    /**
	 * Constructing the menu at the top of the frame
	 */
	private void constructMenu() {

		JMenuBar menuBar = new JMenuBar();
		menuBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.SINGLE);
		menuBar.putClientProperty(PlasticLookAndFeel.IS_3D_KEY, Boolean.FALSE);
        
		// Defining the menus
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');
		
		menuBar.add(fileMenu);
		
		menuBar.add(helpMenu);
		
		// The menu items
		openMenuItem = new JMenuItem();
        exitMenuItem = new JMenuItem();
        helpMenuItem = new JMenuItem();
        aboutMenuItem = new JMenuItem();
		setJMenuBar(menuBar);
		
		helpMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
		helpMenuItem.setMnemonic('H');
		helpMenuItem.setText("Help");
		helpMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	helpTriggered();
            }
        });
        helpMenu.add(helpMenuItem);
        
		aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	aboutTriggered();
            }
        });
        helpMenu.add(aboutMenuItem);
		
		openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
		openMenuItem.setMnemonic('O');
		openMenuItem.setText("Open");
		openMenuItem.setToolTipText("Open a New X!Tandem XML File");
		openMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                openActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.setToolTipText("Exit XTandem Viewer");
        exitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
	}

	/**
	 * Configure the user interface + Look&Feel
	 */
	private void configureUI() {
		try {
			UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY, Boolean.TRUE);
	        Options.setDefaultIconSize(new Dimension(30, 30));

			PlasticLookAndFeel.setPlasticTheme(new DesertBlue());
			UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
		} catch (Exception e) {
			System.err.println("Can't set look & feel:" + e.getMessage());
		}		
	}	
	
	/**
	 * The method that builds the help frame.
	 */
	private void helpTriggered() {
		setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        new HelpFrame(this, getClass().getResource("/help.html"));
        setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
	}

	/**
	 * The method that builds the about dialog.
	 */
	private void aboutTriggered() {
		StringBuffer tMsg = new StringBuffer();
		tMsg.append(APPTITLE + " " + VERSION);
		tMsg.append("\n");
		tMsg.append("\n");
		tMsg.append("The XTandem parser is a Java project for extracting information from X!Tandem output xml files.");
		tMsg.append("\n");
		tMsg.append("\n");
		tMsg.append("The latest version is available at http://code.google.com/p/xtandem-parser");
		tMsg.append("\n");
		tMsg.append("\n");
		tMsg.append("If any questions arise, contact the corresponding author: ");
		tMsg.append("\n");
		tMsg.append("Thilo.Muth@uni-jena.de");
		tMsg.append("\n");
		tMsg.append("\n");
		tMsg.append("");
		tMsg.append("");
		JOptionPane.showMessageDialog(this, tMsg,
				"About " + APPTITLE + " " + VERSION, JOptionPane.INFORMATION_MESSAGE);
	}

    /**
     * This method initializes all the gui components.
     */
    private void initComponents() {
        
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        spectraTable = new JXTable() {
            protected JXTableHeader createDefaultTableHeader() {
                return new JXTableHeader(columnModel) {
                    public String getToolTipText(MouseEvent e) {
                        String tip = null;
                        java.awt.Point p = e.getPoint();
                        int index = columnModel.getColumnIndexAtX(p.x);
                        int realIndex = columnModel.getColumn(index).getModelIndex();
                        tip = (String) spectraTableColToolTips.get(realIndex);
                        return tip;
                    }
                };
            }
        };
        jPanel2 = new javax.swing.JPanel();
        modificationDetailsJLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        identificationsTable = new JXTable() {
            protected JXTableHeader createDefaultTableHeader() {
                return new JXTableHeader(columnModel) {
                    public String getToolTipText(MouseEvent e) {
                        String tip = null;
                        java.awt.Point p = e.getPoint();
                        int index = columnModel.getColumnIndexAtX(p.x);
                        int realIndex = columnModel.getColumn(index).getModelIndex();
                        tip = (String) identificationsJXTableColumnToolTips.get(realIndex);
                        return tip;
                    }
                };
            }
        };
        jPanel3 = new JPanel();
        spectrumJPanel = new JPanel();
        jPanel4 = new JPanel();
        aIonsJCheckBox = new JCheckBox();
        bIonsJCheckBox = new JCheckBox();
        cIonsJCheckBox = new JCheckBox();
        jSeparator1 = new JSeparator();
        yIonsJCheckBox = new JCheckBox();
        xIonsJCheckBox = new JCheckBox();
        zIonsJCheckBox = new JCheckBox();
        jSeparator2 = new JSeparator();
        chargeOneJCheckBox = new JCheckBox();
        chargeTwoJCheckBox = new JCheckBox();
        chargeOverTwoJCheckBox = new JCheckBox();
        jScrollPane1 = new JScrollPane();
        spectrumJXTable = new JXTable() {
            protected JXTableHeader createDefaultTableHeader() {
                return new JXTableHeader(columnModel) {
                    public String getToolTipText(MouseEvent e) {
                        String tip = null;
                        java.awt.Point p = e.getPoint();
                        int index = columnModel.getColumnIndexAtX(p.x);
                        int realIndex = columnModel.getColumn(index).getModelIndex();
                        tip = (String) spectrumJXTableColToolTips.get(realIndex);
                        return tip;
                    }
                };
            }
        };       
        

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Spectra Files", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 0))); // NOI18N

        spectraTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {" ", "Filename", "m/z", "Charge", "Identified"}
        ) { 
            Class[] types = new Class [] {
                Integer.class, String.class, Double.class, Integer.class, Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spectraTable.setOpaque(false);
        
        spectraTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                spectraJXTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(spectraTable);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                .addContainerGap())
        );
        

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Identifications", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 0))); // NOI18N

        modificationDetailsJLabel.setFont(modificationDetailsJLabel.getFont().deriveFont((modificationDetailsJLabel.getFont().getStyle() | java.awt.Font.ITALIC)));

        jLabel1.setFont(jLabel1.getFont().deriveFont((jLabel1.getFont().getStyle() | java.awt.Font.ITALIC)));
        jLabel1.setText("Legend:   ");

        identificationsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                " ", "Sequence", "Modified Sequence", "Start", "End", "Exp. Mass", "Theo. Mass", "E-value", "Accession"
            }
        ) {
            Class[] types = new Class [] {
                Integer.class, String.class, String.class, Integer.class, Integer.class, Double.class, Double.class, Float.class, Float.class, String.class, String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        identificationsTable.setOpaque(false);        
        
        jScrollPane4.setViewportView(identificationsTable);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(modificationDetailsJLabel))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1146, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(modificationDetailsJLabel)))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Spectrum", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 0))); // NOI18N

        spectrumJPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        spectrumJPanel.setLayout(new javax.swing.BoxLayout(spectrumJPanel, javax.swing.BoxLayout.LINE_AXIS));

        aIonsJCheckBox.setSelected(true);
        aIonsJCheckBox.setText("a");
        aIonsJCheckBox.setToolTipText("Show a-ions");
        aIonsJCheckBox.setMaximumSize(new java.awt.Dimension(39, 23));
        aIonsJCheckBox.setMinimumSize(new java.awt.Dimension(39, 23));
        aIonsJCheckBox.setPreferredSize(new java.awt.Dimension(39, 23));
        aIonsJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aIonsJCheckBoxActionPerformed(evt);
            }
        });

        bIonsJCheckBox.setSelected(true);
        bIonsJCheckBox.setText("b");
        bIonsJCheckBox.setToolTipText("Show b-ions");
        bIonsJCheckBox.setMaximumSize(new java.awt.Dimension(39, 23));
        bIonsJCheckBox.setMinimumSize(new java.awt.Dimension(39, 23));
        bIonsJCheckBox.setPreferredSize(new java.awt.Dimension(39, 23));
        bIonsJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bIonsJCheckBoxActionPerformed(evt);
            }
        });

        cIonsJCheckBox.setSelected(true);
        cIonsJCheckBox.setText("c");
        cIonsJCheckBox.setToolTipText("Show c-ions");
        cIonsJCheckBox.setMaximumSize(new java.awt.Dimension(39, 23));
        cIonsJCheckBox.setMinimumSize(new java.awt.Dimension(39, 23));
        cIonsJCheckBox.setPreferredSize(new java.awt.Dimension(39, 23));
        cIonsJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cIonsJCheckBoxActionPerformed(evt);
            }
        });

        yIonsJCheckBox.setSelected(true);
        yIonsJCheckBox.setText("y");
        yIonsJCheckBox.setToolTipText("Show y-ions");
        yIonsJCheckBox.setMaximumSize(new java.awt.Dimension(39, 23));
        yIonsJCheckBox.setMinimumSize(new java.awt.Dimension(39, 23));
        yIonsJCheckBox.setPreferredSize(new java.awt.Dimension(39, 23));
        yIonsJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yIonsJCheckBoxActionPerformed(evt);
            }
        });

        xIonsJCheckBox.setSelected(true);
        xIonsJCheckBox.setText("x");
        xIonsJCheckBox.setToolTipText("Show x-ions");
        xIonsJCheckBox.setMaximumSize(new java.awt.Dimension(39, 23));
        xIonsJCheckBox.setMinimumSize(new java.awt.Dimension(39, 23));
        xIonsJCheckBox.setPreferredSize(new java.awt.Dimension(39, 23));
        xIonsJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xIonsJCheckBoxActionPerformed(evt);
            }
        });

        zIonsJCheckBox.setSelected(true);
        zIonsJCheckBox.setText("z");
        zIonsJCheckBox.setToolTipText("Show z-ions");
        zIonsJCheckBox.setMaximumSize(new java.awt.Dimension(39, 23));
        zIonsJCheckBox.setMinimumSize(new java.awt.Dimension(39, 23));
        zIonsJCheckBox.setPreferredSize(new java.awt.Dimension(39, 23));
        zIonsJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zIonsJCheckBoxActionPerformed(evt);
            }
        });

        chargeOneJCheckBox.setSelected(true);
        chargeOneJCheckBox.setText("+");
        chargeOneJCheckBox.setToolTipText("Show ions with charge 1");
        chargeOneJCheckBox.setMaximumSize(new java.awt.Dimension(39, 23));
        chargeOneJCheckBox.setMinimumSize(new java.awt.Dimension(39, 23));
        chargeOneJCheckBox.setPreferredSize(new java.awt.Dimension(39, 23));
        chargeOneJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargeOneJCheckBoxActionPerformed(evt);
            }
        });        

        chargeTwoJCheckBox.setSelected(true);
        chargeTwoJCheckBox.setText("++");
        chargeTwoJCheckBox.setToolTipText("Show ions with charge 2");
        chargeTwoJCheckBox.setMaximumSize(new java.awt.Dimension(39, 23));
        chargeTwoJCheckBox.setMinimumSize(new java.awt.Dimension(39, 23));
        chargeTwoJCheckBox.setPreferredSize(new java.awt.Dimension(39, 23));
        chargeTwoJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargeTwoJCheckBoxActionPerformed(evt);
            }
        });
        
        chargeOverTwoJCheckBox.setSelected(true);
        chargeOverTwoJCheckBox.setText(">2");
        chargeOverTwoJCheckBox.setToolTipText("Show ions with charge >2");
        chargeOverTwoJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargeOverTwoJCheckBoxActionPerformed(evt);
            }
        });
        

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(yIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup()
                        .add(chargeOneJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                        .add(2, 2, 2))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, zIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .add(xIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, chargeTwoJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, chargeOverTwoJCheckBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(bIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .add(aIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .add(cIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jSeparator2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                        .addContainerGap())
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(aIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(bIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(13, 13, 13)
                .add(xIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(yIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(zIonsJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(12, 12, 12)
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chargeOneJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chargeTwoJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chargeOverTwoJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(13, 13, 13))
        );

        spectrumJXTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " ", "m/z", "Intensity"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spectrumJXTable.setOpaque(false);
       
        jScrollPane1.setViewportView(spectrumJXTable);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(spectrumJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(spectrumJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                .addContainerGap())
        );



        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pack();
    }
    
    
    public void insertFiles(String aXTandemFile) {

        iXTandemFileString = aXTandemFile;        
        progressDialog = new ProgressDialog(this);
        
        // Set the title of the application
        setTitle(APPTITLE + " " + VERSION + "  ---  " + new File(iXTandemFileString).getPath());
        
        // Thread for the progress dialog.
        final Thread t = new Thread(new Runnable() {

            public void run() {
                progressDialog.setTitle("Parsing XML File. Please Wait...");
                progressDialog.setIndeterminate(true);
                progressDialog.setVisible(true);
            }
        }, "ProgressDialog");

        t.start();
        
        // Thread for the parsing of the XTandem xml file.
        new Thread("ParserThread") {
            private XTandemFile iXTandemFile;

            public void run() {

                spectraTable.setSortable(false);
                while (((DefaultTableModel) spectraTable.getModel()).getRowCount() > 0) {
                    ((DefaultTableModel) spectraTable.getModel()).removeRow(0);
                }

                while (((DefaultTableModel) spectrumJXTable.getModel()).getRowCount() > 0) {
                    ((DefaultTableModel) spectrumJXTable.getModel()).removeRow(0);
                }

                while (((DefaultTableModel) identificationsTable.getModel()).getRowCount() > 0) {
                    ((DefaultTableModel) identificationsTable.getModel()).removeRow(0);
                }

                modificationDetailsJLabel.setText("");

                while (spectrumJPanel.getComponents().length > 0) {
                    spectrumJPanel.remove(0);
                }

                spectrumJPanel.validate();
                spectrumJPanel.repaint();

                // Parse the X!Tandem file.
                try {
                	iXTandemFile = new XTandemFile(iXTandemFileString, iRawFile);
                	
                } catch (OutOfMemoryError error) {
                    Runtime.getRuntime().gc();
                    JOptionPane.showMessageDialog(null,
                            "The task used up all the available memory and had to be stopped.\n" +
                            "Memory boundaries are set in ../Properties/JavaOptions.txt.",
                            "Out of Memory Error",
                            JOptionPane.ERROR_MESSAGE);
                    error.printStackTrace();
                    System.exit(0);
                } catch (SAXException saxException){
                	saxException.getMessage();
                	JOptionPane.showMessageDialog(null,                			
                            "Error during parsing the xml file!\n" +
                            saxException.getMessage()+"\n" +
                            "Please load xml file in correct format...",
                            "Parser error",
                            JOptionPane.ERROR_MESSAGE);
                	System.exit(0);                	
                } 
                 
                ionCoverageErrorMargin = Parameters.FRAGMENTMASSERROR;
                
                // Set up the hash maps
                peptideMap = new HashMap<Integer, ArrayList<Peptide>>();
                accMap = new HashMap<Integer, String>();
                allMzValues = new HashMap<Integer, ArrayList<Double>>();
                allIntensityValues = new HashMap<Integer, ArrayList<Double>>();
                allFixMods = new HashMap<Integer, ArrayList<Modification>>();
                allVarMods = new HashMap<Integer, ArrayList<Modification>>();
                scaledMzValues = new HashMap<Integer, ArrayList<Double>>();
                scaledIntensityValues = new HashMap<Integer, ArrayList<Double>>();                  
                ionsMap = new HashMap<String, FragmentIon[]>();
                
                
                if(iParseAll){
                	  int specNumber = iXTandemFile.getRawFileSpectraNumber();
                	  if(iXTandemFile.getRawFileType().equals("mgf")){
                    	  HashMap<Integer, Peaklist> mgfPeaklistMap = iXTandemFile.getRawFileMap();
                    	  for(int i = 1; i <= specNumber; i++){
                    		  MgfPeaklist mgfPeaklist = (MgfPeaklist) mgfPeaklistMap.get(i);
                    		  // Get the peptide hits.
                    		  if(mgfPeaklist.isIdentfied()){
                    			  ArrayList<Peptide> pepList = mgfPeaklist.getIdentifiedPeptides();
                    			  // Fill the map: for each spectrum get the corressponding peptide list.
                            	  peptideMap.put(i, pepList);
                    		  }
//                        	  
                        	  String label = mgfPeaklist.getTitle();
                        	  
                        	  int precursorCharge = Integer.parseInt(mgfPeaklist.getCharge());
                        	  double precursorMh = mgfPeaklist.getPepmass();
                    		  boolean identified = mgfPeaklist.isIdentfied();
                        	  // Add the values to the table (model).
                        	  ((DefaultTableModel) spectraTable.getModel()).addRow(new Object[]{
                        			i,
                        			label,
                        			precursorMh,
                                    precursorCharge,
                                    identified
                                });  
                        	  // Initialize the array lists
                              ArrayList<Double> mzValues = new ArrayList();
                              ArrayList<Double> intensityValues = new ArrayList();
                              
                              // Get the spectrum fragment mz and intensity values
                              ArrayList<MgfPeak> peaks = mgfPeaklist.getPeaks();
                              for (MgfPeak mgfPeak : peaks) {
								mzValues.add(mgfPeak.getMZ());
								intensityValues.add(mgfPeak.getIntensity());
                              }
                           
                              // Fill the maps
                              allMzValues.put(i, mzValues);
                              allIntensityValues.put(i, intensityValues);
                              
                              // The scaled version of the maps (data mz and intensities from the output xml
                              // Get the support data for each spectrum.
                        	  SupportData supportData = iXTandemFile.getSupportData(mgfPeaklist.getIdentifiedSpectrumNumber());
                              intensityValues = supportData.getYValuesFragIonMass2Charge();
                              scaledMzValues.put(i, supportData.getXValuesFragIonMass2Charge());
                              scaledIntensityValues.put(i, supportData.getYValuesFragIonMass2Charge());
                              
                              // Do the modifications
                              if(mgfPeaklist.isIdentfied()){
	                              ArrayList<Modification> fixModList = iXTandemFile.getModificationMap().getFixedModifications(mgfPeaklist.getIdentifiedSpectrumNumber());
	                              ArrayList<Modification> varModList = iXTandemFile.getModificationMap().getVariableModifications(mgfPeaklist.getIdentifiedSpectrumNumber());
	                              allFixMods.put(i, fixModList);
	                              allVarMods.put(i, varModList);
                              }
                    	  }
                      }
                  } else {
                	  // Iterate over all the spectra
                      Iterator<Spectrum> iter = iXTandemFile.getSpectraIterator();

                      // Prepare everything for the peptides.
                      PeptideMap pepMap = iXTandemFile.getPeptideMap();
                      
                      
                      while (iter.hasNext()){
                    	  
                    	  // Get the next spectrum.
                    	  Spectrum spectrum = iter.next();
                    	  int spectrumNumber = spectrum.getSpectrumNumber();

                          // Get the peptide hits.
                          ArrayList<Peptide> pepList = pepMap.getAllPeptides(spectrumNumber);
                          for (Peptide peptide : pepList) {
                        	// Get the b and y ions
                        	  Vector IonVector= iXTandemFile.getFragmentIonsForPeptide(peptide);
                        	  
                        	  // Get all the ion types from the vector
                        	  for (int i = 0; i < IonVector.size(); i++){
                        		  FragmentIon[] ions = (FragmentIon[])IonVector.get(i);
                        		  ionsMap.put(peptide.getDomainID() + "_" + i, ions);
                        	  }                        	  
                          }
                          
                    	  // Get the support data for each spectrum.
                    	  SupportData supportData = iXTandemFile.getSupportData(spectrumNumber);
                    	  
                    	  // Fill the map: for each spectrum get the corressponding peptide list.
                    	  peptideMap.put(spectrumNumber, pepList);
                    	  
                    	  
                    	  //int spectrumID = spectrum.getSpectrumId();
                    	  String label = supportData.getFragIonSpectrumDescription();
                    	  int precursorCharge = spectrum.getPrecursorCharge();
                    	  double precursorMh = spectrum.getPrecursorMh();
                    	  String accession = spectrum.getLabel();
                    	  accMap.put(spectrumNumber, accession);
                    	  // Add the values to the table (model).
                    	  ((DefaultTableModel) spectraTable.getModel()).addRow(new Object[]{
                    			spectrumNumber,
                    			label,
                    			precursorMh,
                                precursorCharge,
                                true
                            });     	  
                    	  
                    	  
                    	  // Initialize the array lists
                          ArrayList<Double> mzValues = new ArrayList();
                          ArrayList<Double> intensityValues = new ArrayList();
                          
                          // Get the spectrum fragment mz and intensity values
                          mzValues = supportData.getXValuesFragIonMass2Charge();
                          
                          intensityValues = supportData.getYValuesFragIonMass2Charge();
                          
                          // Fill the maps
                          allMzValues.put(new Integer(spectrumNumber), mzValues);
                          allIntensityValues.put(new Integer(spectrumNumber), intensityValues);
                          
                          // Do the modifications
                          ArrayList<Modification> fixModList = iXTandemFile.getModificationMap().getFixedModifications(spectrumNumber);
                          ArrayList<Modification> varModList = iXTandemFile.getModificationMap().getVariableModifications(spectrumNumber);
                          allFixMods.put(spectrumNumber, fixModList);
                          allVarMods.put(spectrumNumber, varModList);
                  }
                }   
                spectraTable.setSortable(true);
                progressDialog.setVisible(false);
                progressDialog.dispose();
            }
        }.start();
    }
    
    /**
     * This method filters the annotations.
     * 
     * @param annotations the annotations to be filtered
     * @return the filtered annotations
     */
    private Vector<DefaultSpectrumAnnotation> filterAnnotations(Vector<DefaultSpectrumAnnotation> annotations) {

        Vector<DefaultSpectrumAnnotation> filteredAnnotations = new Vector();

        for (int i = 0; i < annotations.size(); i++) {
            String currentLabel = annotations.get(i).getLabel();

            boolean useAnnotation = true;

            // check ion type
            if (currentLabel.lastIndexOf("a") != -1) {
                if (!aIonsJCheckBox.isSelected()) {
                    useAnnotation = false;
                }
            } else if (currentLabel.lastIndexOf("b") != -1) {
                if (!bIonsJCheckBox.isSelected()) {
                    useAnnotation = false;
                }
            } else if (currentLabel.lastIndexOf("c") != -1) {
                if (!cIonsJCheckBox.isSelected()) {
                    useAnnotation = false;
                }
            } else if (currentLabel.lastIndexOf("x") != -1) {
                if (!xIonsJCheckBox.isSelected()) {
                    useAnnotation = false;
                }
            } else if (currentLabel.lastIndexOf("y") != -1) {
                if (!yIonsJCheckBox.isSelected()) {
                    useAnnotation = false;
                }
            } else if (currentLabel.lastIndexOf("z") != -1) {
                if (!zIonsJCheckBox.isSelected()) {
                    useAnnotation = false;
                }
            }

            // check ion charge
            if (useAnnotation) {
                if (currentLabel.lastIndexOf("+") == -1) {
                    if (!chargeOneJCheckBox.isSelected()) {
                        useAnnotation = false;
                    }
                } else if (currentLabel.lastIndexOf("+++") != -1) {
                    if (!chargeOverTwoJCheckBox.isSelected()) {
                        useAnnotation = false;
                    }
                } else if (currentLabel.lastIndexOf("++") != -1) {
                    if (!chargeTwoJCheckBox.isSelected()) {
                        useAnnotation = false;
                    }
                }
            }

            if (useAnnotation) {
                filteredAnnotations.add(annotations.get(i));
            }
        }

        return filteredAnnotations;
    }
    
//    /**
//     * The spectrum gets updated if the normalization is used.
//     */
//    private void updateSpectrumScalingUsed(boolean selected){
//    	// Set the cursor into the wait status.
//    	this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
//
//        int row = spectraTable.getSelectedRow();   
//        List<Double> mzValues = null;
//        List<Double> intensityValues = null;
//        // Condition if one row is selected.
//        if (row != -1) {
//        	if(selected){      		
//        		mzValues = scaledMzValues.get((Integer) spectraTable.getValueAt(row, 0));
//        		 intensityValues = scaledIntensityValues.get((Integer) spectraTable.getValueAt(row, 0));
//        	}
//        	if(!selected){
//        		mzValues = allMzValues.get((Integer) spectraTable.getValueAt(row, 0));
//        		intensityValues = allIntensityValues.get((Integer) spectraTable.getValueAt(row, 0));
//        	}
//            // Empty the spectrum table.
//            while (spectrumJXTable.getRowCount() > 0) {
//                ((DefaultTableModel) spectrumJXTable.getModel()).removeRow(0);
//            }
//
//            spectrumJXTable.scrollRectToVisible(spectrumJXTable.getCellRect(0, 0, false));
//
//            // Empty the spectrum panel.
//            while (spectrumJPanel.getComponents().length > 0) {
//                spectrumJPanel.remove(0);
//            }
//
//            // needed as input to the spectrum panel
//            double[] mzValuesAsDouble = new double[mzValues.size()];
//            double[] intensityValuesAsDouble = new double[mzValues.size()];
//
//            // Insert the spectrum details
//            for (int i = 0; i < mzValues.size(); i++) {
//
//                ((DefaultTableModel) spectrumJXTable.getModel()).addRow(new Object[]{
//                            new Integer(i + 1),
//                            mzValues.get(i),
//                            intensityValues.get(i)
//                        });
//               
//                mzValuesAsDouble[i] = mzValues.get(i);
//                intensityValuesAsDouble[i] = intensityValues.get(i);
//            }
//
//            // Do the spectrum panel
//            spectrumPanel = new SpectrumPanel(
//                    mzValuesAsDouble,
//                    intensityValuesAsDouble,
//                    ((Double) spectraTable.getValueAt(row, 2)),
//                    "" + spectraTable.getValueAt(row, 3),
//                    ((String) spectraTable.getValueAt(row, 1)),
//                    60, true, false);
//
//            spectrumJPanel.add(spectrumPanel);
//            spectrumJPanel.validate();
//            spectrumJPanel.repaint();
//        }
//        // At the end set the cursor back to default.
//        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//    }
    
    /**
     * Opens the file selector dialog for loading another X!Tandem xml file.
     *
     * @param evt
     */
    private void openActionPerformed(ActionEvent evt) {
        new FileSelector(this, APPTITLE);
        
    }
    
    private void bIonsJCheckBoxActionPerformed(ActionEvent evt) {
        aIonsJCheckBoxActionPerformed(null);
    }
    
    private void cIonsJCheckBoxActionPerformed(ActionEvent evt) {
        aIonsJCheckBoxActionPerformed(null);
    }
    
    private void yIonsJCheckBoxActionPerformed(ActionEvent evt) {
        aIonsJCheckBoxActionPerformed(null);
    }
    
    private void xIonsJCheckBoxActionPerformed(ActionEvent evt) {
        aIonsJCheckBoxActionPerformed(null);
    }
    
    private void zIonsJCheckBoxActionPerformed(ActionEvent evt) {
        aIonsJCheckBoxActionPerformed(null);
    }
    
    private void chargeOneJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeOneJCheckBoxActionPerformed
        aIonsJCheckBoxActionPerformed(null);
    }
    
    private void chargeTwoJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeTwoJCheckBoxActionPerformed
        aIonsJCheckBoxActionPerformed(null);
    }
    
    private void chargeOverTwoJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeOverTwoJCheckBoxActionPerformed
        aIonsJCheckBoxActionPerformed(null);
    }
    
    /**
     * Updates the ion coverage annotations
     *
     * @param evt
     */
    private void aIonsJCheckBoxActionPerformed(ActionEvent evt) {
        if (identificationsTable.getRowCount() > 0) {

            int selectedRow = 0;

            if (identificationsTable.getRowCount() > 1 &&
                    identificationsTable.getSelectedRow() != -1) {
                selectedRow = identificationsTable.getSelectedRow();
            }
            
            Vector<DefaultSpectrumAnnotation> currentAnnotations = allAnnotations.get(
                    identificationsTable.getValueAt(selectedRow, 1) + "_" +
                    identificationsTable.getValueAt(selectedRow, 7));

            spectrumPanel.setAnnotations(filterAnnotations(currentAnnotations));
            spectrumPanel.validate();
            spectrumPanel.repaint();
        }
    }
    
    private void spectraJXTableMouseClicked(MouseEvent evt) {
    	// Set the cursor into the wait status.
    	this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

        int row = spectraTable.getSelectedRow();        
        // Condition if one row is selected.
        if (row != -1) {
            List<Double> mzValues = allMzValues.get((Integer) spectraTable.getValueAt(row, 0));
            List<Double> intensityValues = allIntensityValues.get((Integer) spectraTable.getValueAt(row, 0));

            // Empty the spectrum table.
            while (spectrumJXTable.getRowCount() > 0) {
                ((DefaultTableModel) spectrumJXTable.getModel()).removeRow(0);
            }

            spectrumJXTable.scrollRectToVisible(spectrumJXTable.getCellRect(0, 0, false));

            // Empty the spectrum panel.
            while (spectrumJPanel.getComponents().length > 0) {
                spectrumJPanel.remove(0);
            }

            double[] mzValuesAsDouble = new double[mzValues.size()];
            double[] intensityValuesAsDouble = new double[mzValues.size()];

            // Insert the spectrum details into the table
            for (int i = 0; i < mzValues.size(); i++) {

                ((DefaultTableModel) spectrumJXTable.getModel()).addRow(new Object[]{
                            new Integer(i + 1),
                            mzValues.get(i),
                            intensityValues.get(i)
                        });
               
                mzValuesAsDouble[i] = mzValues.get(i);
                intensityValuesAsDouble[i] = intensityValues.get(i);
            }

            // Updating the spectrum panel
            spectrumPanel = new SpectrumPanel(
                    mzValuesAsDouble,
                    intensityValuesAsDouble,
                    ((Double) spectraTable.getValueAt(row, 2)),
                    "" + spectraTable.getValueAt(row, 3),
                    ((String) spectraTable.getValueAt(row, 1)),
                    60, true, false);

            spectrumJPanel.add(spectrumPanel);
            spectrumJPanel.validate();
            spectrumJPanel.repaint();

            // Empty the identifications tables
            while (identificationsTable.getRowCount() > 0) {
                ((DefaultTableModel) identificationsTable.getModel()).removeRow(0);               
            }
            
            allAnnotations = new HashMap();
            
            // Clear the modifications details legend
            modificationDetailsJLabel.setText("");

            // Iterate over all the peptides as identifications (domains)
            if(peptideMap.get((Integer) spectraTable.getValueAt(row, 0)) != null){
            	ArrayList<Peptide> domainList = peptideMap.get((Integer) spectraTable.getValueAt(row, 0));
                Iterator domainIter = domainList.iterator();
                
                String modificationDetails = "";
                
                while (domainIter.hasNext()) {

                    Peptide domain = (Peptide) domainIter.next();
                    String sequence = domain.getDomainSequence();

                    String[] modifications = new String[sequence.length()];
                    for (int i = 0; i < modifications.length; i++) {
                        modifications[i] = "";
                    }
                    String modifiedSequence = "";
                    String nTerminal = "";
                    String cTerminal = "";

                    ArrayList<Modification> fixedModList = allFixMods.get((Integer) spectraTable.getValueAt(row, 0));
                    ArrayList<Modification> varModList = allVarMods.get((Integer) spectraTable.getValueAt(row, 0));
                    
                    // Handle fixed modifications
    	            if (fixedModList != null) {
    						for (int i = 0; i < fixedModList.size(); i++) {
    							FixedModification fixMod = (FixedModification) fixedModList.get(i);
    							Vector<String> modifiedResidues = new Vector<String>();
    							if (domain.getDomainID().equals(fixMod.getDomainID())) {
    								modifiedResidues.add(fixMod.getModifiedResidue());
    							}
    							for (int j = 0; j < modifiedResidues.size(); j++) {
    								
    								int index = sequence.indexOf(modifiedResidues.get(j));
    								while (index != -1) {
    									modifications[index] += "<" + "M*" + ">";
    	
    									index = sequence.indexOf(modifiedResidues.get(j), index + 1);
    								}
    							}
    						}
    	                }
    	                if (varModList != null) {	
    						for (int i = 0; i < varModList.size(); i++) {
    							VariableModification varMod = (VariableModification) varModList.get(i);
    							Vector<String> modifiedResidues = new Vector<String>();
    							if (domain.getDomainID().equals(varMod.getDomainID())) {
    								modifiedResidues.add(varMod.getModifiedResidue());
    							}
    							for (int j = 0; j < modifiedResidues.size(); j++) {
    								
    								int index = sequence.indexOf(modifiedResidues.get(j));
    								while (index != -1) {
    									modifications[index] += "<" + "M*" + ">";
    	
    									index = sequence.indexOf(modifiedResidues.get(j), index + 1);
    								}
    							}					}
    	                }
    						
    	                // Cycle through all the modifications and extract the modification type if possible
    					for (int i = 0; i < modifications.length; i++) {
    						// Add the amino acid itself to the sequence
    						modifiedSequence += sequence.substring(i, i + 1);

    						if (!modifications[i].equalsIgnoreCase("")) {
    							String[] residues = modifications[i].split(">");
    							for (int j = 0; j < residues.length; j++) {

    								String currentMod = residues[j] + ">";
    								if (modificationDetails.lastIndexOf(currentMod) == -1) {
    									if (fixedModList.size() > 0){
    										modificationDetails += currentMod + " "	+ fixedModList.get(j).getName()	+ ", ";
    									} else if (varModList.size() > 0) {
    										modificationDetails += currentMod + " "	+ varModList.get(j).getName()	+ ", ";
    									}    									

    									modifiedSequence += currentMod;
    								} else {
    									modifiedSequence += currentMod;
    								}
    							}
    						}
    					}
                    
                    // N-Terminal
                    if (nTerminal.length() == 0) {
                        nTerminal = "NH2-";
                    } else {
                        nTerminal += "-";
                    }

                    // C-Terminal
                    if (cTerminal.length() == 0) {
                        cTerminal = "-COOH";
                    } else {
                        cTerminal = "-" + cTerminal; 
                    }
                    
                   Vector<DefaultSpectrumAnnotation> currentAnnotations = new Vector();
                   for(int i = 0; i < 12; i++){
                	   FragmentIon[] ions = ionsMap.get(domain.getDomainID() + "_" + i); 
                	   for (FragmentIon ion : ions) {                	   
                           int ionNumber = ion.getNumber();
                           String ionType = ion.getType();
                           double mzValue = ion.getMZ(); 
                           Color color; 
                           if( i % 2 == 0){
                        	   color = Color.BLUE;   
                           } else {
                        	   color = Color.BLACK;
                           }
                           
                           currentAnnotations.add(new DefaultSpectrumAnnotation(mzValue, ionCoverageErrorMargin, color, ionType + (ionNumber)));
                           
                           // The ion coverage!
                           //ionCoverage[ionNumber][0]++;
                       }   
                   }
                   
//                   FragmentIon[] yIons = ionsMap.get(domain.getDomainID() + "_y");
//                   FragmentIon[] aIons = ionsMap.get(domain.getDomainID() + "_a");
//                   FragmentIon[] cIons = ionsMap.get(domain.getDomainID() + "_c");
//                   FragmentIon[] xIons = ionsMap.get(domain.getDomainID() + "_x");
//                   FragmentIon[] zIons = ionsMap.get(domain.getDomainID() + "_z");
//                   
//                   for (FragmentIon ion : ions) {                	   
//                       int ionNumber = bIon.getNumber();
//                       String ionType = bIon.getType();
//                       double mzValue = bIon.getMZ();  
//                       Color color = Color.BLUE;
//                       currentAnnotations.add(new DefaultSpectrumAnnotation(mzValue, ionCoverageErrorMargin, color, ionType + (ionNumber)));
//                       
//                       // The ion coverage!
//                       //ionCoverage[ionNumber][0]++;
//                   }   
//                   
//                   for (FragmentIon yIon : yIons) {                	   
//                       int ionNumber = yIon.getNumber();
//                       String ionType = yIon.getType();
//                       double mzValue = yIon.getMZ();  
//                       Color color = Color.BLACK;;
//                       currentAnnotations.add(new DefaultSpectrumAnnotation(mzValue, ionCoverageErrorMargin, color, ionType + (ionNumber)));
//                       
//                       // The ion coverage!
//                       //ionCoverage[ionNumber][0]++;
//                   }
//                   
//                   for (FragmentIon aIon : aIons) {                	   
//                       int ionNumber = aIon.getNumber();
//                       String ionType = aIon.getType();
//                       double mzValue = aIon.getMZ();  
//                       Color color = Color.BLUE;
//                       currentAnnotations.add(new DefaultSpectrumAnnotation(mzValue, ionCoverageErrorMargin, color, ionType + (ionNumber)));
//                       
//                       // The ion coverage!
//                       //ionCoverage[ionNumber][0]++;
//                   }   
//                   
//                   for (FragmentIon cIon : cIons) {                	   
//                       int ionNumber = cIon.getNumber();
//                       String ionType = cIon.getType();
//                       double mzValue = cIon.getMZ();  
//                       Color color = Color.BLUE;
//                       currentAnnotations.add(new DefaultSpectrumAnnotation(mzValue, ionCoverageErrorMargin, color, ionType + (ionNumber)));
//                       
//                       // The ion coverage!
//                       //ionCoverage[ionNumber][0]++;
//                   }   
//                   
//                   for (FragmentIon xIon : xIons) {                	   
//                       int ionNumber = xIon.getNumber();
//                       String ionType = xIon.getType();
//                       double mzValue = xIon.getMZ();  
//                       Color color = Color.BLACK;;
//                       currentAnnotations.add(new DefaultSpectrumAnnotation(mzValue, ionCoverageErrorMargin, color, ionType + (ionNumber)));
//                       
//                       // The ion coverage!
//                       //ionCoverage[ionNumber][0]++;
//                   }
//                   
//                   for (FragmentIon zIon : zIons) {                	   
//                       int ionNumber = zIon.getNumber();
//                       String ionType = zIon.getType();
//                       double mzValue = zIon.getMZ();  
//                       Color color = Color.BLACK;;
//                       currentAnnotations.add(new DefaultSpectrumAnnotation(mzValue, ionCoverageErrorMargin, color, ionType + (ionNumber)));
//                       
//                       // The ion coverage!
//                       //ionCoverage[ionNumber][0]++;
//                   }
                   

                   allAnnotations.put((sequence + "_" + domain.getDomainExpect()), currentAnnotations);

                        // only add the annotations for the first identification
                        if (allAnnotations.size() == 1) {
                            // add the ion coverage annotations to the spectrum panel
                            spectrumPanel.setAnnotations(filterAnnotations(currentAnnotations));
                            spectrumPanel.validate();
                            spectrumPanel.repaint();
                        }
                    
                    String modifiedSequenceColorCoded = "<html>";
                    
                    for (int i = 0; i < modifiedSequence.length(); i++) {

                        if (modifiedSequence.charAt(i) == '<') {
                        	modifiedSequenceColorCoded += "&lt;";
                            i++;
                            while (modifiedSequence.charAt(i) != '>') {
                                modifiedSequenceColorCoded += modifiedSequence.charAt(i++);
                            }
                            modifiedSequenceColorCoded += "&gt;";

                        } else {                        

                            modifiedSequenceColorCoded += modifiedSequence.charAt(i);
                        }
                    }
                    modifiedSequenceColorCoded += "</html>";
                     
                    // Calculate the theoretical mass of the domain
                    double theoMass = (domain.getDomainMh() + domain.getDomainDeltaMh());
                    String accession = accMap.get((Integer) spectraTable.getValueAt(row, 0));

                     ((DefaultTableModel) identificationsTable.getModel()).addRow(new Object[]{
                                    (Integer) spectraTable.getValueAt(row, 0),
                                    sequence,
                                    modifiedSequenceColorCoded,
                                    domain.getDomainStart(),
                                    domain.getDomainEnd(),
                                    new Double(domain.getDomainMh()),
                                    new Double(theoMass),
                                    new Float(domain.getDomainExpect()),                                    
                                    accession,                                    
                                });
                }
                if (modificationDetails.endsWith(", ")) {
                    modificationDetails = modificationDetails.substring(0, modificationDetails.length() - 2);
                }

                if (modificationDetails.length() > 0) {
                    modificationDetailsJLabel.setText("Modifications: " + modificationDetails);
                }
                
                if (identificationsTable.getRowCount() > 1) {
                     identificationsTable.setRowSelectionInterval(0, 0);
                 }
            }
            // At the end set the cursor back to default.
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
}
