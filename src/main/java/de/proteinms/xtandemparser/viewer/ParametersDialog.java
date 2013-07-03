package de.proteinms.xtandemparser.viewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * In the parameter dialog, the user can set the fragment ion mass accuracy.
 *
 * @author Thilo Muth
 */
public class ParametersDialog extends JDialog {

    private XTandemViewer parent;
    private JButton okBtn;
    private JButton cancelBtn;
    private JPanel bottomPnl;
    private JPanel centerPnl;
    private JLabel fragmentIonMassAccuracyLbl;
    private JTextField fragmentIonMassAccuracyTtf;

    /**
     * Constructs a parameter dialog for setting the fragment
     * ion mass accuracy.
     *
     * @param parent Parent frame.
     * @param modal Modal state.
     */
    public ParametersDialog(XTandemViewer parent, boolean modal) {
        this.parent = parent;
        setTitle(XTandemViewer.APPTITLE + " --- " + "Parameters");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        Container cp = this.getContentPane();

        okBtn = new JButton("OK");
        okBtn.setPreferredSize(new Dimension(100, 20));
        okBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                okButtonActionPerformed();
            }
        });

        cancelBtn = new JButton("Cancel");
        cancelBtn.setPreferredSize(new Dimension(100, 20));
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed();
            }
        });

        // The main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        bottomPnl = new JPanel();
        bottomPnl.add(okBtn);
        bottomPnl.add(cancelBtn);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        centerPnl = new JPanel();

        fragmentIonMassAccuracyLbl = new JLabel("Fragment Ion Mass Accuracy: ");
        fragmentIonMassAccuracyTtf = new JTextField(10);
        fragmentIonMassAccuracyTtf.setText(String.valueOf(parent.getFragmentIonMassAccuracy()));
        centerPnl.add(fragmentIonMassAccuracyLbl);
        centerPnl.add(fragmentIonMassAccuracyTtf);

        mainPanel.add(centerPnl, BorderLayout.CENTER);
        mainPanel.add(bottomPnl, BorderLayout.SOUTH);
        cp.add(mainPanel);

        pack();
        setSize(350, 100);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /**
     * When the ok button is pressed, the parameters are given back to the main frame.
     */
    private void okButtonActionPerformed() {
        parent.setFragmentIonMassAccuracy(Double.valueOf(fragmentIonMassAccuracyTtf.getText()));
        parent.insertFiles(parent.getXTandemFile(), "user.home");
        this.setVisible(false);
        this.dispose();
    }

    /**
     * When the close button is pressed, the dialog is discarded.
     */
    private void closeButtonActionPerformed() {
        this.setVisible(false);
        this.dispose();
    }

    /**
     * Closes the help frame.
     */
    private void close() {
        this.dispose();
    }
}
