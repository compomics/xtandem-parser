package de.proteinms.xtandemparser.viewer;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;

/**
 * The help frame gives the user the possibility to retrieve helpful information
 * about the X!Tandem Viewer. It displays a given html-sheet.
 *
 * @author Thilo Muth
 */
public class HelpFrame extends JFrame {

    private JEditorPane editorPane;
    private URL iUrl;
    private JButton closeButton;
    private JScrollPane scrollPane;

    /**
     * Constructor constructs the frame with an editorpane.
     *
     * @param parent
     * @param aUrl
     */
    public HelpFrame(JFrame parent, URL aUrl) {
        // Sets icon image
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/xtandemviewer.gif")));
        setTitle(XTandemViewer.APPTITLE + " --- " + "Help");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        closeButton = new JButton();
        scrollPane = new JScrollPane();

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                close();
            }
        });

        editorPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
        editorPane.setMinimumSize(new java.awt.Dimension(10, 10));
        editorPane.setPreferredSize(new java.awt.Dimension(10, 10));
        editorPane.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent evt) {
                editorPaneHyperlinkUpdate(evt);
            }
        });
        scrollPane.setViewportView(editorPane);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
                getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                layout.createSequentialGroup().addContainerGap().add(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(
                scrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                235, Short.MAX_VALUE).add(closeButton)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                layout.createSequentialGroup().addContainerGap().add(scrollPane,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE).addPreferredGap(
                org.jdesktop.layout.LayoutStyle.UNRELATED).add(closeButton).addContainerGap()));
        pack();

        try {
            iUrl = aUrl;
            if (iUrl != null) {
                try {
                    editorPane.setPage(iUrl);
                } catch (IOException e) {
                    editorPane.setText("The selected help file is not yet available.");
                }
            } else {
                editorPane.setText("The selected help file is not yet available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        editorPane.setCaretPosition(0);
        setSize(550, 500);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /**
     * Closes the help frame.
     */
    private void close() {
        this.dispose();
    }

    /**
     * Updates the hyperlinks
     *
     * @param evt
     */
    private void editorPaneHyperlinkUpdate(HyperlinkEvent evt) {
        if (evt.getEventType().toString().equalsIgnoreCase(
                HyperlinkEvent.EventType.ENTERED.toString())) {
            setCursor(new java.awt.Cursor(Cursor.HAND_CURSOR));
        } else if (evt.getEventType().toString().equalsIgnoreCase(
                javax.swing.event.HyperlinkEvent.EventType.EXITED.toString())) {
            setCursor(new java.awt.Cursor(Cursor.DEFAULT_CURSOR));
        } else if (evt.getEventType().toString().equalsIgnoreCase(
                HyperlinkEvent.EventType.ACTIVATED.toString())) {
            if (evt.getDescription().startsWith("#")) {
                editorPane.scrollToReference(evt.getDescription());
            } else {
                this.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                BareBonesBrowserLaunch.openURL(evt.getDescription());
                this.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        }
    }
}
