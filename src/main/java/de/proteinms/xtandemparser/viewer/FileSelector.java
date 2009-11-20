package de.proteinms.xtandemparser.viewer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
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
import javax.swing.UIManager;

import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.SkyKrupp;

/**
 * This class represents the file selection frame.
 * The user can load the X!Tandem Output XML file here.
 *
 * @author Thilo Muth
 */
public class FileSelector extends JFrame {

    private JPanel loadxmlPanel = null;
    private JTextField xmlSourceField = null;
    private JButton xmlfileBrow = null;
    private String filename = null;
    private JPanel lowerPanel = null;
    private JButton okBtn = null;
    private JPanel upperPanel = null;
    private JButton cancelBtn = null;
    private JPanel centerPanel = null;
    private JFrame iParent = null;

    /**
     * Constructor gets a title string
     *
     * @param title
     */
    public FileSelector(JFrame aParent, String title) {
        // Initialize frame.
        super(title);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                cancelButtonActionPerformed();
            }
        });
        iParent = aParent;
        // GUI construction
        this.constructScreen();
    }

    /**
     * Entry point to the XTandem-Parser application.
     *
     * @param args
     */
    public static void main(String[] args) {
        new FileSelector(null, "XTandem-Parser");
    }

    /**
     * This method constructs the screen.
     */
    private void constructScreen() {
        // Initialize frame
        this.setTitle(XTandemViewer.APPTITLE);
        this.setLocationRelativeTo(null);

        // Set JGoodies Look&Feel
        configureUI();

        Container cp = this.getContentPane();
        // The main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());


        // Sets icon image
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/xtandemviewer.gif")));

        // Xml load panel  
        loadxmlPanel = new JPanel(new FlowLayout());

        xmlSourceField = new JTextField(35);
        xmlSourceField.setEditable(false);
        xmlSourceField.setEnabled(false);
        loadxmlPanel.add(xmlSourceField);
        xmlfileBrow = new JButton(openFileAction());
        loadxmlPanel.add(xmlfileBrow);
        upperPanel = new JPanel(new BorderLayout());
        upperPanel.setBorder(BorderFactory.createTitledBorder("Load X!Tandem Output"));
        upperPanel.add(loadxmlPanel, BorderLayout.CENTER);

        // Down panel with ok and cancel button
        lowerPanel = new JPanel();

        okBtn = new JButton("OK");
        okBtn.setEnabled(false);
        okBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                okButtonActionPerformed();
            }
        });
        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                cancelButtonActionPerformed();
            }
        });
        lowerPanel.add(okBtn);
        lowerPanel.add(cancelBtn);
        mainPanel.add(upperPanel, BorderLayout.NORTH);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel = new JPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(lowerPanel, BorderLayout.SOUTH);
        cp.add(mainPanel);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Configure the user interface + Look&Feel
     */
    private void configureUI() {
        try {
            UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY, Boolean.TRUE);
            Options.setDefaultIconSize(new Dimension(30, 30));

            PlasticLookAndFeel.setPlasticTheme(new SkyKrupp());
            UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
        } catch (Exception e) {
            System.err.println("Can't set look & feel:" + e.getMessage());
        }
    }

    /**
     * This method is called when ok button is pressed. It loads the xtandemviewer.
     */
    private void okButtonActionPerformed() {
        this.setVisible(false);
        new XTandemViewer(xmlSourceField.getText(), "user.home");
        if (iParent != null) {
            iParent.dispose();
        }
        this.dispose();
    }

    /**
     * This method is called when the frame is closed. It shuts down the JVM.
     */
    private void cancelButtonActionPerformed() {
        if (iParent == null) {
            System.exit(0);
        } else {
            if (!iParent.isVisible()) {
                System.exit(0);
            } else {
                this.setVisible(false);
                this.dispose();
            }
        }
    }

    private Action openFileAction() {
        filename = System.getProperty("user.dir") + File.separator;
        JFileChooser fc = new JFileChooser(new File(filename));
        JFrame loadFrame = new JFrame();
        return new OpenFileAction(loadFrame, fc);
    }

    //  This action class creates and shows an open-file dialog.
    public class OpenFileAction extends AbstractAction {

        private JFrame frame;
        private JFileChooser chooser;
        private InnerXmlFileFilter filter = new InnerXmlFileFilter();
        private File file = null;
        private String name = null;

        OpenFileAction(JFrame frame, JFileChooser chooser) {
            super("Open XML-File...");
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
                okBtn.setEnabled(true);
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
}
