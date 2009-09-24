package viewer;

import interfaces.Modification;
import interfaces.Peaklist;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTableHeader;

import xtandem.FixedModification;
import xtandem.FragmentIon;
import xtandem.MgfPeak;
import xtandem.MgfPeaklist;
import xtandem.Parameters;
import xtandem.Peptide;
import xtandem.PeptideMap;
import xtandem.Protein;
import xtandem.Spectrum;
import xtandem.SupportData;
import xtandem.VariableModification;
import xtandem.XTandemFile;
import be.proteomics.util.gui.spectrum.DefaultSpectrumAnnotation;
import be.proteomics.util.gui.spectrum.SpectrumPanel;

/**
 * This class provides a basic viewer for the spectra.
 * @author Thilo Muth
 *
 */
public class XTandemViewer extends JFrame {
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
    private HashMap<String, FragmentIon[]> bIonMap;
	private HashMap<String, FragmentIon[]> yIonMap;
    private HashMap<Integer, String> accMap;
    private Vector spectraJXTableColumnToolTips;
    private Vector spectrumJTableColumnToolTips;
    private Vector spectrumJXTableColumnToolTips;
    private Vector identificationsJXTableColumnToolTips;
    private HashMap<String, Vector<DefaultSpectrumAnnotation>> allAnnotations;
    private JCheckBox aIonsJCheckBox;
    private JMenuItem aboutJMenuItem;
    private JCheckBox bIonsJCheckBox;
    private JCheckBox cIonsJCheckBox;
    private JCheckBox chargeOneJCheckBox;
    private JCheckBox scalingJCheckBox;
    private JCheckBox chargeTwoJCheckBox;
    private JMenuItem copyIdentificationsJMenuItem;
    private JPopupMenu copyIdentificationsJPopupMenu;
    private JMenuItem copySpectraJMenuItem;
    private JPopupMenu copySpectraJPopupMenu;
    private JMenuItem copySpectrumJMenuItem;
    private JPopupMenu copySpectrumJPopupMenu;
    private JMenuItem exitJMenuItem;
    private JMenuItem exportAllIdentificationsJMenuItem;
    private JMenuItem exportAllSpectraJMenuItem;
    private JMenuItem exportBestIdentificationsJMenuItem;
    private JMenu exportJMenu;
    private JMenuItem exportSelectedSpectrumJMenuItem;
    private JMenuItem exportSpectraFilesTableJMenuItem;
    private JMenu fileJMenu;
    private JMenu helpJMenu;
    private JMenuItem helpJMenuItem;
    private org.jdesktop.swingx.JXTable identificationsJXTable;
    private JLabel jLabel1;
    //private JMenuBar jMenuBar1;
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
    private JMenuItem openJMenuItem;
    private org.jdesktop.swingx.JXTable spectraJXTable;
    private JPanel spectrumJPanel;
    private org.jdesktop.swingx.JXTable spectrumJXTable;
    private JCheckBox xIonsJCheckBox;
    private JCheckBox yIonsJCheckBox;
    private JCheckBox zIonsJCheckBox;
    private String iRawFileType;
    private boolean iParseAll = false;
    private double ionCoverageErrorMargin = 0.0;

    
    
	/**
     * Constructor get
     */
    public XTandemViewer(String aXTandemXmlFile, String aRawFile, boolean aParseAll) {
    	iRawFile = aRawFile;    	
    	iParseAll = aParseAll;
        initComponents();
        setMinimumSize(new Dimension(900, 600));
        
        // sets the column sizes
        spectraJXTable.getColumn(" ").setMaxWidth(35);
        spectraJXTable.getColumn(" ").setMinWidth(35);
        spectraJXTable.getColumn("m/z").setMaxWidth(65);
        spectraJXTable.getColumn("m/z").setMinWidth(65);
        spectraJXTable.getColumn("Charge").setMaxWidth(65);
        spectraJXTable.getColumn("Charge").setMinWidth(65);
        spectraJXTable.getColumn("Identified").setMaxWidth(80);
        spectraJXTable.getColumn("Identified").setMinWidth(80);
        spectrumJXTable.getColumn(" ").setMaxWidth(35);
        spectrumJXTable.getColumn(" ").setMinWidth(35);
        identificationsJXTable.getColumn(" ").setMaxWidth(35);
        identificationsJXTable.getColumn(" ").setMinWidth(35);
        identificationsJXTable.getColumn("Start").setMaxWidth(45);
        identificationsJXTable.getColumn("Start").setMinWidth(45);
        identificationsJXTable.getColumn("End").setMaxWidth(45);
        identificationsJXTable.getColumn("End").setMinWidth(45);
        identificationsJXTable.getColumn("Exp. Mass").setMaxWidth(75);
        identificationsJXTable.getColumn("Exp. Mass").setMinWidth(75);
        identificationsJXTable.getColumn("Theo. Mass").setMaxWidth(75);
        identificationsJXTable.getColumn("Theo. Mass").setMinWidth(75);
        identificationsJXTable.getColumn("E-value").setMinWidth(75);
        identificationsJXTable.getColumn("E-value").setMaxWidth(75);
        identificationsJXTable.getColumn("Accession").setPreferredWidth(10);
        spectraJXTable.getTableHeader().setReorderingAllowed(false);
        spectrumJXTable.getTableHeader().setReorderingAllowed(false);
        identificationsJXTable.getTableHeader().setReorderingAllowed(false);
        spectraJXTableColumnToolTips = new Vector();
        spectraJXTableColumnToolTips.add("Spectrum Number");
        spectraJXTableColumnToolTips.add("Spectrum File Name");
        spectraJXTableColumnToolTips.add("Precursor Mass Over Charge Ratio");
        spectraJXTableColumnToolTips.add("Precursor Charge");
        spectraJXTableColumnToolTips.add("Spectrum Identified");
        spectrumJTableColumnToolTips = new Vector();
        spectrumJTableColumnToolTips.add(null);
        spectrumJTableColumnToolTips.add("Mass Over Charge Ratio");
        spectrumJTableColumnToolTips.add("Intensity");
        spectrumJXTableColumnToolTips = new Vector();
        spectrumJXTableColumnToolTips.add(null);
        spectrumJXTableColumnToolTips.add("Mass Over Charge Ratio");
        spectrumJXTableColumnToolTips.add("Intensity");
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

        setLocationRelativeTo(null);
        setVisible(true);
        insertFiles(aXTandemXmlFile);
        
    }
	
    private void initComponents() {

        copySpectraJPopupMenu = new javax.swing.JPopupMenu();
        copySpectraJMenuItem = new javax.swing.JMenuItem();
        copySpectrumJPopupMenu = new javax.swing.JPopupMenu();
        copySpectrumJMenuItem = new javax.swing.JMenuItem();
        copyIdentificationsJPopupMenu = new javax.swing.JPopupMenu();
        copyIdentificationsJMenuItem = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        spectraJXTable = new JXTable() {
            protected JXTableHeader createDefaultTableHeader() {
                return new JXTableHeader(columnModel) {
                    public String getToolTipText(MouseEvent e) {
                        String tip = null;
                        java.awt.Point p = e.getPoint();
                        int index = columnModel.getColumnIndexAtX(p.x);
                        int realIndex = columnModel.getColumn(index).getModelIndex();
                        tip = (String) spectraJXTableColumnToolTips.get(realIndex);
                        return tip;
                    }
                };
            }
        };
        jPanel2 = new javax.swing.JPanel();
        modificationDetailsJLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        identificationsJXTable = new JXTable() {
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
        jPanel3 = new javax.swing.JPanel();
        spectrumJPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        aIonsJCheckBox = new javax.swing.JCheckBox();
        bIonsJCheckBox = new javax.swing.JCheckBox();
        cIonsJCheckBox = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        yIonsJCheckBox = new javax.swing.JCheckBox();
        xIonsJCheckBox = new javax.swing.JCheckBox();
        zIonsJCheckBox = new javax.swing.JCheckBox();
        jSeparator2 = new javax.swing.JSeparator();
        chargeOneJCheckBox = new javax.swing.JCheckBox();
        chargeTwoJCheckBox = new javax.swing.JCheckBox();
        scalingJCheckBox = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        spectrumJXTable = new JXTable() {
            protected JXTableHeader createDefaultTableHeader() {
                return new JXTableHeader(columnModel) {
                    public String getToolTipText(MouseEvent e) {
                        String tip = null;
                        java.awt.Point p = e.getPoint();
                        int index = columnModel.getColumnIndexAtX(p.x);
                        int realIndex = columnModel.getColumn(index).getModelIndex();
                        tip = (String) spectrumJXTableColumnToolTips.get(realIndex);
                        return tip;
                    }
                };
            }
        };
        //jMenuBar1 = new javax.swing.JMenuBar();
        fileJMenu = new javax.swing.JMenu();
        openJMenuItem = new javax.swing.JMenuItem();
        exitJMenuItem = new javax.swing.JMenuItem();
        exportJMenu = new javax.swing.JMenu();
        exportSpectraFilesTableJMenuItem = new javax.swing.JMenuItem();
        exportAllIdentificationsJMenuItem = new javax.swing.JMenuItem();
        exportBestIdentificationsJMenuItem = new javax.swing.JMenuItem();
        exportSelectedSpectrumJMenuItem = new javax.swing.JMenuItem();
        exportAllSpectraJMenuItem = new javax.swing.JMenuItem();
        helpJMenu = new javax.swing.JMenu();
        helpJMenuItem = new javax.swing.JMenuItem();
        aboutJMenuItem = new javax.swing.JMenuItem();

        copySpectraJMenuItem.setMnemonic('C');
        copySpectraJMenuItem.setText("Copy");
        copySpectraJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //copySpectraJMenuItemActionPerformed(evt);
            }
        });
        copySpectraJPopupMenu.add(copySpectraJMenuItem);

        copySpectrumJMenuItem.setMnemonic('C');
        copySpectrumJMenuItem.setText("Copy");
        copySpectrumJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //copySpectrumJMenuItemActionPerformed(evt);
            }
        });
        copySpectrumJPopupMenu.add(copySpectrumJMenuItem);

        copyIdentificationsJMenuItem.setMnemonic('C');
        copyIdentificationsJMenuItem.setText("Copy");
        copyIdentificationsJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //copyIdentificationsJMenuItemActionPerformed(evt);
            }
        });
        copyIdentificationsJPopupMenu.add(copyIdentificationsJMenuItem);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("X!Tandem Viewer v0.7");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Spectra Files", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 0))); // NOI18N

        spectraJXTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " ", "Filename", "m/z", "Charge", "Identified"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Boolean.class
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
        spectraJXTable.setOpaque(false);
        spectraJXTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                //spectraJXTableKeyReleased(evt);
            }
        });
        spectraJXTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                spectraJXTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(spectraJXTable);

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

        identificationsJXTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " ", "Sequence", "Modified Sequence", "Start", "End", "Exp. Mass", "Theo. Mass", "E-value", "Accession"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Float.class, java.lang.Float.class, java.lang.String.class, java.lang.String.class
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
        identificationsJXTable.setOpaque(false);
        identificationsJXTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                //identificationsJXTableKeyReleased(evt);
            }
        });
        identificationsJXTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                //identificationsJXTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(identificationsJXTable);

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
                //aIonsJCheckBoxActionPerformed(evt);
            }
        });
        aIonsJCheckBox.setEnabled(false);

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
        bIonsJCheckBox.setEnabled(true);

        cIonsJCheckBox.setSelected(true);
        cIonsJCheckBox.setText("c");
        cIonsJCheckBox.setToolTipText("Show c-ions");
        cIonsJCheckBox.setMaximumSize(new java.awt.Dimension(39, 23));
        cIonsJCheckBox.setMinimumSize(new java.awt.Dimension(39, 23));
        cIonsJCheckBox.setPreferredSize(new java.awt.Dimension(39, 23));
        cIonsJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //cIonsJCheckBoxActionPerformed(evt);
            }
        });
        cIonsJCheckBox.setEnabled(false);

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
        yIonsJCheckBox.setEnabled(true);

        xIonsJCheckBox.setSelected(true);
        xIonsJCheckBox.setText("x");
        xIonsJCheckBox.setToolTipText("Show x-ions");
        xIonsJCheckBox.setMaximumSize(new java.awt.Dimension(39, 23));
        xIonsJCheckBox.setMinimumSize(new java.awt.Dimension(39, 23));
        xIonsJCheckBox.setPreferredSize(new java.awt.Dimension(39, 23));
        xIonsJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //xIonsJCheckBoxActionPerformed(evt);
            }
        });
        xIonsJCheckBox.setEnabled(false);

        zIonsJCheckBox.setSelected(true);
        zIonsJCheckBox.setText("z");
        zIonsJCheckBox.setToolTipText("Show z-ions");
        zIonsJCheckBox.setMaximumSize(new java.awt.Dimension(39, 23));
        zIonsJCheckBox.setMinimumSize(new java.awt.Dimension(39, 23));
        zIonsJCheckBox.setPreferredSize(new java.awt.Dimension(39, 23));
        zIonsJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //zIonsJCheckBoxActionPerformed(evt);
            }
        });
        zIonsJCheckBox.setEnabled(false);

        chargeOneJCheckBox.setSelected(true);
        chargeOneJCheckBox.setText("+");
        chargeOneJCheckBox.setToolTipText("Show ions with charge 1");
        chargeOneJCheckBox.setMaximumSize(new java.awt.Dimension(39, 23));
        chargeOneJCheckBox.setMinimumSize(new java.awt.Dimension(39, 23));
        chargeOneJCheckBox.setPreferredSize(new java.awt.Dimension(39, 23));
        chargeOneJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //chargeOneJCheckBoxActionPerformed(evt);
            }
        });
        chargeOneJCheckBox.setEnabled(false);

        chargeTwoJCheckBox.setSelected(true);
        chargeTwoJCheckBox.setText("++");
        chargeTwoJCheckBox.setToolTipText("Show ions with charge 2");
        chargeTwoJCheckBox.setMaximumSize(new java.awt.Dimension(39, 23));
        chargeTwoJCheckBox.setMinimumSize(new java.awt.Dimension(39, 23));
        chargeTwoJCheckBox.setPreferredSize(new java.awt.Dimension(39, 23));
        chargeTwoJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //chargeTwoJCheckBoxActionPerformed(evt);
            }
        });
        chargeTwoJCheckBox.setEnabled(false);
        
        scalingJCheckBox.setSelected(false);
        
        if(!iParseAll) {
        	scalingJCheckBox.setEnabled(false);
        } else {
        	scalingJCheckBox.setText("Scale");
            scalingJCheckBox.setToolTipText("Show mz and intensity values from xtandem file");
            scalingJCheckBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                	JCheckBox chBox = (JCheckBox) evt.getSource();            	
                	if(chBox.isSelected()){
                		updateSpectrumScalingUsed(true);
                	} else {
                		updateSpectrumScalingUsed(false);
                	}
                }
            });
        }
        

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
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, scalingJCheckBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
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
                .add(scalingJCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        spectrumJXTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	//spectrumJXTableMouseClicked(evt);
            }
        });
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

        fileJMenu.setMnemonic('F');
        fileJMenu.setText("File");

        openJMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        openJMenuItem.setMnemonic('O');
        openJMenuItem.setText("Open");
        openJMenuItem.setToolTipText("Open a New OMX File");
        openJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //openJMenuItemActionPerformed(evt);
            }
        });
        fileJMenu.add(openJMenuItem);

        exitJMenuItem.setMnemonic('x');
        exitJMenuItem.setText("Exit");
        exitJMenuItem.setToolTipText("Exit OMSSA Viewer");
        exitJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //exitJMenuItemActionPerformed(evt);
            }
        });
        fileJMenu.add(exitJMenuItem);

        //jMenuBar1.add(fileJMenu);

        exportJMenu.setMnemonic('E');
        exportJMenu.setText("Export");

        exportSpectraFilesTableJMenuItem.setMnemonic('P');
        exportSpectraFilesTableJMenuItem.setText("Spectra Files Table");
        exportSpectraFilesTableJMenuItem.setToolTipText("Export the Spectra Files Table as Tab Delimited Text File");
        exportSpectraFilesTableJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //exportSpectraFilesTableJMenuItemActionPerformed(evt);
            }
        });
        exportJMenu.add(exportSpectraFilesTableJMenuItem);

        exportAllIdentificationsJMenuItem.setMnemonic('I');
        exportAllIdentificationsJMenuItem.setText("All Identifications (all hits)");
        exportAllIdentificationsJMenuItem.setToolTipText("Export All Identifications (all hits) as Tab Delimited Text File");
        exportAllIdentificationsJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //exportAllIdentificationsJMenuItemActionPerformed(evt);
            }
        });
        exportJMenu.add(exportAllIdentificationsJMenuItem);

        exportBestIdentificationsJMenuItem.setMnemonic('I');
        exportBestIdentificationsJMenuItem.setText("All Identifications (best hits only)");
        exportBestIdentificationsJMenuItem.setToolTipText("Export All Identifications (best hits only) as Tab Delimited Text File");
        exportBestIdentificationsJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //exportBestIdentificationsJMenuItemActionPerformed(evt);
            }
        });
        exportJMenu.add(exportBestIdentificationsJMenuItem);

        exportSelectedSpectrumJMenuItem.setMnemonic('S');
        exportSelectedSpectrumJMenuItem.setText("Selected Spectrum");
        exportSelectedSpectrumJMenuItem.setToolTipText("Export the Selected Spectrum as Tab Delimited Text File");
        exportSelectedSpectrumJMenuItem.setEnabled(false);
        exportSelectedSpectrumJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //exportSelectedSpectrumJMenuItemActionPerformed(evt);
            }
        });
        exportJMenu.add(exportSelectedSpectrumJMenuItem);

        exportAllSpectraJMenuItem.setMnemonic('S');
        exportAllSpectraJMenuItem.setText("All Spectra");
        exportAllSpectraJMenuItem.setToolTipText("Export all the Spectra as DTA Files");
        exportAllSpectraJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //exportAllSpectraJMenuItemActionPerformed(evt);
            }
        });
        exportJMenu.add(exportAllSpectraJMenuItem);

        //jMenuBar1.add(exportJMenu);

        helpJMenu.setMnemonic('H');
        helpJMenu.setText("Help");

        helpJMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        helpJMenuItem.setMnemonic('H');
        helpJMenuItem.setText("Help");
        helpJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //helpJMenuItemActionPerformed(evt);
            }
        });
        helpJMenu.add(helpJMenuItem);

        aboutJMenuItem.setMnemonic('a');
        aboutJMenuItem.setText("About");
        aboutJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //aboutJMenuItemActionPerformed(evt);
            }
        });
        helpJMenu.add(aboutJMenuItem);

        //jMenuBar1.add(helpJMenu);

        //setJMenuBar(jMenuBar1);

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

        exportSelectedSpectrumJMenuItem.setEnabled(false);



        new Thread("ParserThread") {

            private XTandemFile iXTandemFile;
			private HashMap<Integer, ArrayList<Protein>> proteinMap;
			

			@Override
            public void run() {

                // turn off the auto row sorting
                spectraJXTable.setSortable(false);
//                spectraJTable.setRowSorter(null);
//                spectrumJTable.setRowSorter(null);
//                identificationsJTable.setRowSorter(null);

                // empty the tables and clear the spectrum panel
                while (((DefaultTableModel) spectraJXTable.getModel()).getRowCount() > 0) {
                    ((DefaultTableModel) spectraJXTable.getModel()).removeRow(0);
                }

                while (((DefaultTableModel) spectrumJXTable.getModel()).getRowCount() > 0) {
                    ((DefaultTableModel) spectrumJXTable.getModel()).removeRow(0);
                }

                while (((DefaultTableModel) identificationsJXTable.getModel()).getRowCount() > 0) {
                    ((DefaultTableModel) identificationsJXTable.getModel()).removeRow(0);
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
                    //Util.writeToErrorLog("OMSSA Viewer: Ran out of memory!");
                    error.printStackTrace();
                    System.exit(0);
                }
                 
                ionCoverageErrorMargin = Parameters.FRAGMENTMASSERROR;
                
                // Set up the hash maps
                peptideMap = new HashMap<Integer, ArrayList<Peptide>>();
                proteinMap = new HashMap<Integer, ArrayList<Protein>>();
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
                        	  ((DefaultTableModel) spectraJXTable.getModel()).addRow(new Object[]{
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
                    	  ((DefaultTableModel) spectraJXTable.getModel()).addRow(new Object[]{
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
   
                // switch the auto row sorting back on
                spectraJXTable.setSortable(true);
//                spectraJTable.setAutoCreateRowSorter(true);
//                spectrumJTable.setAutoCreateRowSorter(true);
//                identificationsJTable.setAutoCreateRowSorter(true);

            }
        }.start();
    }
    
    /**
     * Filters the annotations and returns the annotations matching the selected
     * list next to the spectrum panel.
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
//            if (useAnnotation) {
//                if (currentLabel.lastIndexOf("+") == -1) {
//                    if (!chargeOneJCheckBox.isSelected()) {
//                        useAnnotation = false;
//                    }
//                } else if (currentLabel.lastIndexOf("+++") != -1) {
//                    if (!chargeOverTwoJCheckBox.isSelected()) {
//                        useAnnotation = false;
//                    }
//                } else if (currentLabel.lastIndexOf("++") != -1) {
//                    if (!chargeTwoJCheckBox.isSelected()) {
//                        useAnnotation = false;
//                    }
//                }
//            }

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

        int row = spectraJXTable.getSelectedRow();   
        List<Double> mzValues = null;
        List<Double> intensityValues = null;
        // Condition if one row is selected.
        if (row != -1) {
        	if(selected){      		
        		mzValues = scaledMzValues.get((Integer) spectraJXTable.getValueAt(row, 0));
        		 intensityValues = scaledIntensityValues.get((Integer) spectraJXTable.getValueAt(row, 0));
        	}
        	if(!selected){
        		mzValues = allMzValues.get((Integer) spectraJXTable.getValueAt(row, 0));
        		intensityValues = allIntensityValues.get((Integer) spectraJXTable.getValueAt(row, 0));
        	}
            // Empty the spectrum table.
            while (spectrumJXTable.getRowCount() > 0) {
                ((DefaultTableModel) spectrumJXTable.getModel()).removeRow(0);
            }

            // scrolls the scrollbar to the top of the spectrum table
            spectrumJXTable.scrollRectToVisible(spectrumJXTable.getCellRect(0, 0, false));

            // Empty the spectrum panel.
            while (spectrumJPanel.getComponents().length > 0) {
                spectrumJPanel.remove(0);
            }

            // needed as input to the spectrum panel
            double[] mzValuesAsDouble = new double[mzValues.size()];
            double[] intensityValuesAsDouble = new double[mzValues.size()];

            // insert the spectrum details in the spectrum table
            for (int i = 0; i < mzValues.size(); i++) {

                ((DefaultTableModel) spectrumJXTable.getModel()).addRow(new Object[]{
                            new Integer(i + 1),
                            mzValues.get(i),
                            intensityValues.get(i)
                        });
               
                mzValuesAsDouble[i] = mzValues.get(i);
                intensityValuesAsDouble[i] = intensityValues.get(i);
            }

            //exportSelectedSpectrumJMenuItem.setEnabled(true);

            // updates the spectrum panel
            spectrumPanel = new SpectrumPanel(
                    mzValuesAsDouble,
                    intensityValuesAsDouble,
                    ((Double) spectraJXTable.getValueAt(row, 2)),
                    "" + spectraJXTable.getValueAt(row, 3),
                    ((String) spectraJXTable.getValueAt(row, 1)),
                    60, true, false);

            spectrumJPanel.add(spectrumPanel);
            spectrumJPanel.validate();
            spectrumJPanel.repaint();
        }
        // At the end set the cursor back to default.
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    private void bIonsJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yIonsJCheckBoxActionPerformed
        aIonsJCheckBoxActionPerformed(null);
    }
    
    private void yIonsJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yIonsJCheckBoxActionPerformed
        aIonsJCheckBoxActionPerformed(null);
    }
    
    /**
     * Updates the ion coverage annotations
     *
     * @param evt
     */
    private void aIonsJCheckBoxActionPerformed(ActionEvent evt) {//GEN-FIRST:event_aIonsJCheckBoxActionPerformed
        if (identificationsJXTable.getRowCount() > 0) {

            int selectedRow = 0;

            if (identificationsJXTable.getRowCount() > 1 &&
                    identificationsJXTable.getSelectedRow() != -1) {
                selectedRow = identificationsJXTable.getSelectedRow();
            }
            System.out.println(identificationsJXTable.getValueAt(selectedRow, 1));
            System.out.println(identificationsJXTable.getValueAt(selectedRow, 7));
            
            Vector<DefaultSpectrumAnnotation> currentAnnotations = allAnnotations.get(
                    identificationsJXTable.getValueAt(selectedRow, 1) + "_" +
                    identificationsJXTable.getValueAt(selectedRow, 7));

            // update the ion coverage annotations
            spectrumPanel.setAnnotations(filterAnnotations(currentAnnotations));
            spectrumPanel.validate();
            spectrumPanel.repaint();
        }
    }
    
    private void spectraJXTableMouseClicked(MouseEvent evt) {
    	// Set the cursor into the wait status.
    	this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

        int row = spectraJXTable.getSelectedRow();        
        // Condition if one row is selected.
        if (row != -1) {
            List<Double> mzValues = allMzValues.get((Integer) spectraJXTable.getValueAt(row, 0));
            if (Boolean.valueOf(spectraJXTable.getValueAt(row, 4).toString())){
            	scalingJCheckBox.setEnabled(true);
            } else {
            	scalingJCheckBox.setEnabled(false);
            }
            int value = (Integer) spectraJXTable.getValueAt(row, 0);
            List<Double> intensityValues = allIntensityValues.get((Integer) spectraJXTable.getValueAt(row, 0));

            // Empty the spectrum table.
            while (spectrumJXTable.getRowCount() > 0) {
                ((DefaultTableModel) spectrumJXTable.getModel()).removeRow(0);
            }

            // scrolls the scrollbar to the top of the spectrum table
            spectrumJXTable.scrollRectToVisible(spectrumJXTable.getCellRect(0, 0, false));

            // Empty the spectrum panel.
            while (spectrumJPanel.getComponents().length > 0) {
                spectrumJPanel.remove(0);
            }

            // needed as input to the spectrum panel
            double[] mzValuesAsDouble = new double[mzValues.size()];
            double[] intensityValuesAsDouble = new double[mzValues.size()];

            // insert the spectrum details in the spectrum table
            for (int i = 0; i < mzValues.size(); i++) {

                ((DefaultTableModel) spectrumJXTable.getModel()).addRow(new Object[]{
                            new Integer(i + 1),
                            mzValues.get(i),
                            intensityValues.get(i)
                        });
               
                mzValuesAsDouble[i] = mzValues.get(i);
                intensityValuesAsDouble[i] = intensityValues.get(i);
            }

            //exportSelectedSpectrumJMenuItem.setEnabled(true);

            // updates the spectrum panel
            spectrumPanel = new SpectrumPanel(
                    mzValuesAsDouble,
                    intensityValuesAsDouble,
                    ((Double) spectraJXTable.getValueAt(row, 2)),
                    "" + spectraJXTable.getValueAt(row, 3),
                    ((String) spectraJXTable.getValueAt(row, 1)),
                    60, true, false);

            spectrumJPanel.add(spectrumPanel);
            spectrumJPanel.validate();
            spectrumJPanel.repaint();

            // empty the identification table
            while (identificationsJXTable.getRowCount() > 0) {
                ((DefaultTableModel) identificationsJXTable.getModel()).removeRow(0);               
            }
            
            allAnnotations = new HashMap();
            
            // clear the modification details legend
            modificationDetailsJLabel.setText("");

            // Iterate over all the peptides as identifications (domains)
            if(peptideMap.get((Integer) spectraJXTable.getValueAt(row, 0)) != null){
            	ArrayList<Peptide> domainList = peptideMap.get((Integer) spectraJXTable.getValueAt(row, 0));
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

                    
                    
                    ArrayList<Modification> fixedModList = allFixMods.get((Integer) spectraJXTable.getValueAt(row, 0));
                    ArrayList<Modification> varModList = allVarMods.get((Integer) spectraJXTable.getValueAt(row, 0));
                    // Handle fixed modifications
                    if (true){
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
    						
    	                // cycle through all the modifications and extract the modification type if possible
    					for (int i = 0; i < modifications.length; i++) {
    						// add the amino acid itself to the sequence
    						modifiedSequence += sequence.substring(i, i + 1);

    						if (!modifications[i].equalsIgnoreCase("")) {
    							// have to check for multiple modifications on one
    							// residue
    							String[] residueMods = modifications[i].split(">");

    							for (int j = 0; j < residueMods.length; j++) {

    								String currentMod = residueMods[j] + ">";

    								// check if we've already mapped the modification
    								if (modificationDetails.lastIndexOf(currentMod) == -1) {
    									if (fixedModList.size() > 0){
    										modificationDetails += currentMod + " "	+ fixedModList.get(j).getName()	+ ", ";
    									} else if (varModList.size() > 0) {
    										modificationDetails += currentMod + " "	+ varModList.get(j).getName()	+ ", ";
    									}
    									

    									// OmssaModification tempOmssaModification = omssaOmxFile.getModifications().get(new Integer(residueMods[j].substring(1)));
    									// if (tempOmssaModification != null) {
    									// modificationDetails += currentMod + " " + tempOmssaModification.getModName() + " (" +
    									// tempOmssaModification.getModMonoMass() + "), ";
    									// if (tempOmssaModification.getModType() ==
    									// OmssaModification.MODAA) {
    									//
    									// "normal" modification
    									modifiedSequence += currentMod;
    									// }
    									// } else {
    									// modifiedSequence += currentMod;
    									// modificationDetails += currentMod + "
    									// unknown, ";
    									// }
    								} else {
    									modifiedSequence += currentMod;
    								}
    							}
    						}
    					}
                        
    				} else {
                        modificationDetailsJLabel.setText("Modifications: (Files with modification details were not provided. " +
                                "No modifications are shown.)");
                        modifiedSequence = sequence;
                    }
                    
                    // set the n-terminal
                    if (nTerminal.length() == 0) {
                        nTerminal = "NH2-"; // no terminal (or terminal modification) given
                    } else {
                        nTerminal += "-"; // add the "-" at the end, i.e. "NH2-"
                    }

                    // set the c-terminal
                    if (cTerminal.length() == 0) {
                        cTerminal = "-COOH"; // no terminal (or terminal modification) given
                    } else {
                        cTerminal = "-" + cTerminal; // add the "-" at the beginning, i.e. "-COOH"
                    }
                    
                   
                   Vector<DefaultSpectrumAnnotation> currentAnnotations = new Vector();
                   FragmentIon[] bIons = bIonMap.get(domain.getDomainID());
                   FragmentIon[] yIons = yIonMap.get(domain.getDomainID());
                   int[][] ionCoverage = new int[sequence.length()][2];
                   
                   for (FragmentIon bIon : bIons) {                	   
                       int ionNumber = bIon.getNumber();
                       String ionType = bIon.getType();
                       double mzValue = bIon.getMZ();  
                       Color color = Color.BLUE;
                       currentAnnotations.add(new DefaultSpectrumAnnotation(mzValue, ionCoverageErrorMargin, color, ionType + (ionNumber)));
                       
                       // TODO: The ion coverage!
                       //ionCoverage[ionNumber][0]++;
                   }   
                   
                   for (FragmentIon yIon : yIons) {                	   
                       int ionNumber = yIon.getNumber();
                       String ionType = yIon.getType();
                       double mzValue = yIon.getMZ();  
                       Color color = Color.BLACK;;
                       currentAnnotations.add(new DefaultSpectrumAnnotation(mzValue, ionCoverageErrorMargin, color, ionType + (ionNumber)));
                       
                       // TODO: The ion coverage!
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

                             
                    String accession = accMap.get((Integer) spectraJXTable.getValueAt(row, 0));

                     ((DefaultTableModel) identificationsJXTable.getModel()).addRow(new Object[]{
                                    (Integer) spectraJXTable.getValueAt(row, 0),
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
                
                if (identificationsJXTable.getRowCount() > 1) {
                     identificationsJXTable.setRowSelectionInterval(0, 0);
                 }
            }
            // At the end set the cursor back to default.
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
}
