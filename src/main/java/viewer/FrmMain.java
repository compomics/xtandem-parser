package viewer;

import interfaces.Modification;
import interfaces.Peaklist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.layout.GroupLayout;
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

import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;


/**
 *
 * @author Thilo Muth
 *
 */

public class FrmMain extends JFrame{
	private final static String APPTITLE = "X!Tandem Viewer";
	private final static String VERSION = "v. 0.8";
	private SpectrumPanel spectrumPanel;
	private String iXTandemFileString;
	private String iRawFile;
    
	private FrmMain iFrmMain = null;
	private Container tContentPane;
	private JPanel spectraTablePanel;
	private JScrollPane jScrollPane1;	
	private JPanel spectrumJPanel;
	private JCheckBox aIonsJCheckBox;
	private JCheckBox bIonsJCheckBox;
	private JCheckBox cIonsJCheckBox;
	private JCheckBox xIonsJCheckBox;
    private JCheckBox yIonsJCheckBox;
    private JCheckBox zIonsJCheckBox;  
	private JCheckBox chargeOneJCheckBox;
	private JCheckBox scalingJCheckBox;
	private JCheckBox chargeTwoJCheckBox;
	private JLabel modificationDetailsJLabel;   	
	private JXTable spectraTable;
	private JXTable spectrumTable;	
	private JXTable identificationsTable;
	private HashMap<Integer, ArrayList<Peptide>> peptideMap;
    private HashMap<Integer, ArrayList<Double>> allMzValues;
    private HashMap<Integer, ArrayList<Double>> allIntensityValues;
    private HashMap<Integer, ArrayList<Double>> scaledMzValues;
    private HashMap<Integer, ArrayList<Double>> scaledIntensityValues;
    private HashMap<Integer, ArrayList<Modification>> allFixMods;
    private HashMap<Integer, ArrayList<Modification>> allVarMods; 
    private HashMap<String, Vector<DefaultSpectrumAnnotation>> allAnnotations;
    private HashMap<String, FragmentIon[]> bIonMap;
	private HashMap<String, FragmentIon[]> yIonMap;
    private HashMap<Integer, String> accMap;	
    private String[] entryArray;
	private Vector spectraTableColToolTips;
	private Vector spectrumTableColToolTips;
	private Vector spectrumJXTableColToolTips;
	private Vector identificationsJXTableColumnToolTips;
    private boolean iParseAll = false;
    private double ionCoverageErrorMargin = 0.0;
	private String iXTandemXmlFile;

	/**
	 * The constructor receives title and version number.
	 *
	 */
	public FrmMain(String aXTandemXmlFile, String aRawFile, boolean aParseAll) {
		iXTandemXmlFile = aXTandemXmlFile;
		iRawFile = aRawFile;
		iParseAll = aParseAll;		
		
		// GUI construction
		constructScreen();		
	}

	/**
	 * This method is called when the frame is closed. It shuts down the JVM.
	 */
	private void close() {
		System.exit(0);
	}
	


	/**
	 * This method constructs the screen.
	 */
	private void constructScreen() {
		int frameWidth = 1230;
		int frameHeight = 650;

		// Initialize frame
		iFrmMain = this; 
		iFrmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		iFrmMain.setSize(frameWidth, frameWidth);
		iFrmMain.setTitle(APPTITLE + " " + VERSION);		
		tContentPane = iFrmMain.getContentPane();
		tContentPane.setLayout(new BorderLayout());
			
		// Set JGoodies Look&Feel
		configureUI();

		// Construct the menu
		contructMenu();
		
		// Construct the different panels
		spectraTablePanel = buildSpectraTablePanel();
		
		// Setup the table
		setupTable();
		
		tContentPane.add(spectraTablePanel, BorderLayout.NORTH);
		setContentPane(tContentPane);
		setMinimumSize(new Dimension(900, 600));    
        setLocationRelativeTo(null);
        
        insertFiles(iXTandemXmlFile);
		pack();		
		setVisible(true);
	}

	/**
	 * Constructing the menu at the top of the frame
	 */
	private void contructMenu() {

		JMenuBar menuBar = new JMenuBar();

		// Defining the menus
		JMenu fileMenu = new JMenu("File");
		JMenu helpMenu = new JMenu("Help");
		menuBar.add(fileMenu);
		
		menuBar.add(helpMenu);
		iFrmMain.setJMenuBar(menuBar);

		// Defining the actions
		Action exitAction = new AbstractAction("Exit") {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};

		Action aboutAction = new AbstractAction("About") {
			public void actionPerformed(ActionEvent e) {
				aboutTriggered();
			}
		};		
		fileMenu.add(exitAction);		
		helpMenu.add(aboutAction);
	}

	/**
	 * Configure the user interface + Look&Feel
	 */
	private void configureUI() {
		try {
			PlasticLookAndFeel laf = new PlasticXPLookAndFeel();
			PlasticLookAndFeel.setCurrentTheme(new ExperienceBlue());
			UIManager.setLookAndFeel(laf);
		} catch (Exception e) {
			System.err.println("Can't set look & feel:" + e.getMessage());
		}		
	}
	

	/**
	 * The method that builds the about dialog.
	 */
	private void aboutTriggered() {
		StringBuffer tMsg = new StringBuffer();
		tMsg.append(APPTITLE + " " + VERSION);
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
	 * This method initializes the table components
	 */
	private void setupTable(){
		spectraTable.getColumn(" ").setMaxWidth(35);
        spectraTable.getColumn(" ").setMinWidth(35);
        spectraTable.getColumn("m/z").setMaxWidth(65);
        spectraTable.getColumn("m/z").setMinWidth(65);
        spectraTable.getColumn("Charge").setMaxWidth(65);
        spectraTable.getColumn("Charge").setMinWidth(65);
        spectraTable.getColumn("Identified").setMaxWidth(80);
        spectraTable.getColumn("Identified").setMinWidth(80);
//        spectrumTable.getColumn(" ").setMaxWidth(35);
//        spectrumTable.getColumn(" ").setMinWidth(35);
//        identificationsTable.getColumn(" ").setMaxWidth(35);
//        identificationsTable.getColumn(" ").setMinWidth(35);
//        identificationsTable.getColumn("Start").setMaxWidth(45);
//        identificationsTable.getColumn("Start").setMinWidth(45);
//        identificationsTable.getColumn("End").setMaxWidth(45);
//        identificationsTable.getColumn("End").setMinWidth(45);
//        identificationsTable.getColumn("Exp. Mass").setMaxWidth(75);
//        identificationsTable.getColumn("Exp. Mass").setMinWidth(75);
//        identificationsTable.getColumn("Theo. Mass").setMaxWidth(75);
//        identificationsTable.getColumn("Theo. Mass").setMinWidth(75);
//        identificationsTable.getColumn("E-value").setMinWidth(75);
//        identificationsTable.getColumn("E-value").setMaxWidth(75);
//        identificationsTable.getColumn("Accession").setPreferredWidth(10);
        spectraTable.getTableHeader().setReorderingAllowed(false);
//        spectrumTable.getTableHeader().setReorderingAllowed(false);
//        identificationsTable.getTableHeader().setReorderingAllowed(false);
//        spectraTableColToolTips = new Vector();
//        spectraTableColToolTips.add("Spectrum Number");
//        spectraTableColToolTips.add("Spectrum File Name");
//        spectraTableColToolTips.add("Precursor Mass Over Charge Ratio");
//        spectraTableColToolTips.add("Precursor Charge");
//        spectraTableColToolTips.add("Spectrum Identified");
//        spectrumTableColToolTips = new Vector();
//        spectrumTableColToolTips.add(null);
//        spectrumTableColToolTips.add("Mass Over Charge Ratio");
//        spectrumTableColToolTips.add("Intensity");
//        spectrumJXTableColToolTips = new Vector();
//        spectrumJXTableColToolTips.add(null);
//        spectrumJXTableColToolTips.add("Mass Over Charge Ratio");
//        spectrumJXTableColToolTips.add("Intensity");
//        identificationsJXTableColumnToolTips = new Vector();
//        identificationsJXTableColumnToolTips.add("Spectrum Number");
//        identificationsJXTableColumnToolTips.add("Peptide Sequence");
//        identificationsJXTableColumnToolTips.add("Modified Peptide Sequence");
//        identificationsJXTableColumnToolTips.add("Peptide Start Index");
//        identificationsJXTableColumnToolTips.add("Peptide End Index");
//        identificationsJXTableColumnToolTips.add("Experimental Mass");
//        identificationsJXTableColumnToolTips.add("Theoretical Mass");
//        identificationsJXTableColumnToolTips.add("E-value");
//        identificationsJXTableColumnToolTips.add("Protein Accession Number");
	}
	/**
	 * Constructs the spectra table panel.
	 * 
	 * @return spectraTablePanel
	 */
	private JPanel buildSpectraTablePanel(){
		
		JPanel spectraTablePanel = new JPanel();
		spectraTablePanel.setBackground(Color.WHITE);		
		jScrollPane1 = new JScrollPane();
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
        
        spectraTablePanel.setBorder(BorderFactory.createTitledBorder(null, "Spectra Files", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 0))); // NOI18N

        spectraTable.setModel(new DefaultTableModel(
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
        
        spectraTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                spectraJXTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(spectraTable);
        GroupLayout spectraTablePanelLayout = new GroupLayout(spectraTablePanel);
        spectraTablePanel.setLayout(spectraTablePanelLayout);
        spectraTablePanelLayout.setHorizontalGroup(
        		spectraTablePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(spectraTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
                .addContainerGap())
        );
        spectraTablePanelLayout.setVerticalGroup(
        		spectraTablePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(spectraTablePanelLayout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                .addContainerGap())
        );
		
		
		return spectraTablePanel;
	}
	
	 public void insertFiles(String aXTandemFile) {

	        iXTandemFileString = aXTandemFile;        

	        new Thread("ParserThread") {
	            private XTandemFile iXTandemFile;

	            public void run() {

	                spectraTable.setSortable(false);
	                while (((DefaultTableModel) spectraTable.getModel()).getRowCount() > 0) {
	                    ((DefaultTableModel) spectraTable.getModel()).removeRow(0);
	                }

	                while (((DefaultTableModel) spectrumTable.getModel()).getRowCount() > 0) {
	                    ((DefaultTableModel) spectrumTable.getModel()).removeRow(0);
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
	                bIonMap = new HashMap<String, FragmentIon[]>();
	                yIonMap = new HashMap<String, FragmentIon[]>();
	                
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
	                        	  FragmentIon[] bIons = (FragmentIon[]) IonVector.get(0);
	                        	  FragmentIon[] yIons = (FragmentIon[]) IonVector.get(1);
	                        	  bIonMap.put(peptide.getDomainID(), bIons);
	                        	  yIonMap.put(peptide.getDomainID(), yIons);
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
//	              

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
//	            if (useAnnotation) {
//	                if (currentLabel.lastIndexOf("+") == -1) {
//	                    if (!chargeOneJCheckBox.isSelected()) {
//	                        useAnnotation = false;
//	                    }
//	                } else if (currentLabel.lastIndexOf("+++") != -1) {
//	                    if (!chargeOverTwoJCheckBox.isSelected()) {
//	                        useAnnotation = false;
//	                    }
//	                } else if (currentLabel.lastIndexOf("++") != -1) {
//	                    if (!chargeTwoJCheckBox.isSelected()) {
//	                        useAnnotation = false;
//	                    }
//	                }
//	            }

	            if (useAnnotation) {
	                filteredAnnotations.add(annotations.get(i));
	            }
	        }

	        return filteredAnnotations;
	    }
	    
	    /**
	     * The spectrum gets updated if the normalization is used.
	     */
	    private void updateSpectrumScalingUsed(boolean selected){
	    	// Set the cursor into the wait status.
	    	this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

	        int row = spectraTable.getSelectedRow();   
	        List<Double> mzValues = null;
	        List<Double> intensityValues = null;
	        // Condition if one row is selected.
	        if (row != -1) {
	        	if(selected){      		
	        		mzValues = scaledMzValues.get((Integer) spectraTable.getValueAt(row, 0));
	        		 intensityValues = scaledIntensityValues.get((Integer) spectraTable.getValueAt(row, 0));
	        	}
	        	if(!selected){
	        		mzValues = allMzValues.get((Integer) spectraTable.getValueAt(row, 0));
	        		intensityValues = allIntensityValues.get((Integer) spectraTable.getValueAt(row, 0));
	        	}
	            // Empty the spectrum table.
	            while (spectrumTable.getRowCount() > 0) {
	                ((DefaultTableModel) spectrumTable.getModel()).removeRow(0);
	            }

	            spectrumTable.scrollRectToVisible(spectrumTable.getCellRect(0, 0, false));

	            // Empty the spectrum panel.
	            while (spectrumJPanel.getComponents().length > 0) {
	                spectrumJPanel.remove(0);
	            }

	            // needed as input to the spectrum panel
	            double[] mzValuesAsDouble = new double[mzValues.size()];
	            double[] intensityValuesAsDouble = new double[mzValues.size()];

	            // Insert the spectrum details
	            for (int i = 0; i < mzValues.size(); i++) {

	                ((DefaultTableModel) spectrumTable.getModel()).addRow(new Object[]{
	                            new Integer(i + 1),
	                            mzValues.get(i),
	                            intensityValues.get(i)
	                        });
	               
	                mzValuesAsDouble[i] = mzValues.get(i);
	                intensityValuesAsDouble[i] = intensityValues.get(i);
	            }

	            // Do the spectrum panel
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
	        }
	        // At the end set the cursor back to default.
	        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	    }
	    private void bIonsJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
	        aIonsJCheckBoxActionPerformed(null);
	    }
	    
	    private void yIonsJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
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
	            if (Boolean.valueOf(spectraTable.getValueAt(row, 4).toString())){
	            	scalingJCheckBox.setEnabled(true);
	            } else {
	            	scalingJCheckBox.setEnabled(false);
	            }
	            List<Double> intensityValues = allIntensityValues.get((Integer) spectraTable.getValueAt(row, 0));

	            // Empty the spectrum table.
	            while (spectrumTable.getRowCount() > 0) {
	                ((DefaultTableModel) spectrumTable.getModel()).removeRow(0);
	            }

	            spectrumTable.scrollRectToVisible(spectrumTable.getCellRect(0, 0, false));

	            // Empty the spectrum panel.
	            while (spectrumJPanel.getComponents().length > 0) {
	                spectrumJPanel.remove(0);
	            }

	            double[] mzValuesAsDouble = new double[mzValues.size()];
	            double[] intensityValuesAsDouble = new double[mzValues.size()];

	            // Insert the spectrum details into the table
	            for (int i = 0; i < mzValues.size(); i++) {

	                ((DefaultTableModel) spectrumTable.getModel()).addRow(new Object[]{
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
	                   FragmentIon[] bIons = bIonMap.get(domain.getDomainID());
	                   FragmentIon[] yIons = yIonMap.get(domain.getDomainID());
	                   
	                   for (FragmentIon bIon : bIons) {                	   
	                       int ionNumber = bIon.getNumber();
	                       String ionType = bIon.getType();
	                       double mzValue = bIon.getMZ();  
	                       Color color = Color.BLUE;
	                       currentAnnotations.add(new DefaultSpectrumAnnotation(mzValue, ionCoverageErrorMargin, color, ionType + (ionNumber)));
	                       
	                       // The ion coverage!
	                       //ionCoverage[ionNumber][0]++;
	                   }   
	                   
	                   for (FragmentIon yIon : yIons) {                	   
	                       int ionNumber = yIon.getNumber();
	                       String ionType = yIon.getType();
	                       double mzValue = yIon.getMZ();  
	                       Color color = Color.BLACK;;
	                       currentAnnotations.add(new DefaultSpectrumAnnotation(mzValue, ionCoverageErrorMargin, color, ionType + (ionNumber)));
	                       
	                       // The ion coverage!
	                       //ionCoverage[ionNumber][0]++;
	                   }                       

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
	
	public static void main(String[] args) {
		new FrmMain("C:/XTandem/output.xml", "", false);
	}
	
}
