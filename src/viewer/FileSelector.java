package viewer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class FileSelector extends JFrame{
	
	private JPanel loadxmlPanel = null;
	private JTextField xmlSourceField = null;
	private JButton xmlfileBrow = null;
	private String filename = null;
	private JPanel lowerPanel = null;
	private JButton okBtn = null;
	private JPanel upperPanel = null;
	private JButton cancelBtn;
	private JPanel centerPanel;
	//private JPanel loadmgfPanel;
	private JTextField mgfSourceField;
	//private JButton mgffileBrow;
	
	/**
	 * Constructor gets a title string
	 * @param title
	 */
	public FileSelector(String title) {
        // Initialize frame.
        super(title);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                close();
            }
        });
       
        // GUI construction
        this.constructScreen();
	}
	
	private void constructScreen(){
		// Initialize frame
   	 
        int frameWidth = 350;
        int frameHeight = 130;
        this.setSize(frameWidth, frameHeight);
        this.setTitle("XTandem-Parser");        
        this.setLocation(300, 180);         
        Container cp = this.getContentPane();
        cp.setLayout(new BorderLayout());
        
        // Xml load panel  
        loadxmlPanel = new JPanel(new FlowLayout());
        xmlSourceField = new JTextField(12);
        xmlSourceField.setEditable(false);
        xmlSourceField.setEnabled(false);
        loadxmlPanel.add(xmlSourceField);
        xmlfileBrow = new JButton(openFileAction()); 
        loadxmlPanel.add(xmlfileBrow);
        
        // Mgf load panel  
        //loadmgfPanel = new JPanel(new FlowLayout());
//        mgfSourceField = new JTextField(12);
//        mgfSourceField.setEditable(false);
//        mgfSourceField.setEnabled(false);
//        loadmgfPanel.add(mgfSourceField);
//        mgffileBrow = new JButton(openMgfFileAction()); 
//        loadmgfPanel.add(mgffileBrow);
        
        upperPanel = new JPanel(new BorderLayout());
        upperPanel.setBorder(BorderFactory.createTitledBorder("Load xtandem output"));
        upperPanel.add(loadxmlPanel, BorderLayout.CENTER);
//        centerPanel = new JPanel(new BorderLayout());
//        centerPanel.setBorder(BorderFactory.createTitledBorder("Load mgf file"));
//        centerPanel.add(loadmgfPanel, BorderLayout.CENTER);
       
        // Down panel with ok and cancel button
        lowerPanel = new JPanel();
        okBtn = new JButton("Ok");
        okBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                new XTandemViewer(xmlSourceField.getText(), "", false);
            	setVisible(false);
            }
        });
        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                close();
            }
        });
        lowerPanel.add(okBtn);
        lowerPanel.add(cancelBtn);
        cp.add(upperPanel, BorderLayout.NORTH);
        //cp.add(centerPanel, BorderLayout.CENTER);
        cp.add(lowerPanel, BorderLayout.SOUTH);
        this.setResizable(false);
        this.setVisible(true);
	}
	
	/**
     * This method is called when the frame is closed. It shuts down the JVM.
     */	
	private void close(){
		System.exit(0);
	}	
	
	private Action openFileAction(){
	    	filename = System.getProperty("user.dir") + File.separator;//File.separator+"tmp";
		    JFileChooser fc = new JFileChooser(new File(filename));
		    JFrame loadFrame = new JFrame();
		    Action openAction = new OpenFileAction(loadFrame, fc);	    
		    return openAction;	    
	}
	
	private Action openMgfFileAction(){
    	filename = System.getProperty("user.dir") + File.separator;//File.separator+"tmp";
	    JFileChooser fc = new JFileChooser(new File(filename));
	    JFrame loadFrame = new JFrame();
	    Action openMgfAction = new OpenMgfFileAction(loadFrame, fc);	    
	    return openMgfAction;	    
}
	
	public static void main(String[] args) {
	    new FileSelector("XTandem-Parser");
	}
	
	   //  This action class creates and shows an open-file dialog.
    public class OpenFileAction extends AbstractAction {
        private JFrame frame;
        private JFileChooser chooser;
        private InnerXmlFileFilter filter = new InnerXmlFileFilter();
        private File file = null;
        private String name = null;
        
        OpenFileAction(JFrame frame, JFileChooser chooser) {
            super("Open xml-File...");
            this.chooser = chooser;
            this.frame = frame;
        }       

        public void actionPerformed(ActionEvent evt) {
            chooser.setFileFilter(filter);
            chooser.showOpenDialog(frame);
            // Get the selected file
            file = chooser.getSelectedFile();
            // Only get the path, if a file was selected
            if (file != null) { 
            	name = file.getAbsolutePath();
                xmlSourceField.setText(name);                
                xmlSourceField.setEnabled(true);                            	 
            }
        }        
    }   
    
	/**
	 * This action class creates and shows an open mgf-file dialog.	 
	 *
	 */
    public class OpenMgfFileAction extends AbstractAction {
        private JFrame frame;
        private JFileChooser chooser;
        private InnerMgfFileFilter filter = new InnerMgfFileFilter();
        private File file = null;
        private String mgffile = null;
        
        OpenMgfFileAction(JFrame frame, JFileChooser chooser) {
            super("Open mgf-File...");
            this.chooser = chooser;
            this.frame = frame;
        }       

        public void actionPerformed(ActionEvent evt) {
            chooser.setFileFilter(filter);
            chooser.showOpenDialog(frame);
            // Get the selected file
            file = chooser.getSelectedFile();
            // Only get the path, if a file was selected
            if (file != null) { 
            	mgffile = file.getAbsolutePath();
                mgfSourceField.setText(mgffile);                
                mgfSourceField.setEnabled(true);                            	 
            }
        }        
    }  
    
    private static class InnerXmlFileFilter extends javax.swing.filechooser.FileFilter {
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".xml");
        }

        public String getDescription() {
            return "*.xml";
        }
    }
    
    private static class InnerMgfFileFilter extends javax.swing.filechooser.FileFilter {
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".mgf");
        }

        public String getDescription() {
            return "*.mgf";
        }
    }
    
   
}
