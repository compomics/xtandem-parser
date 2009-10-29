package de.proteinms.xtandemparser.viewer;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Thilo Muth
 * Date: 17.10.2009
 * Time: 15:53:29
 * To change this template use File | Settings | File Templates.
 */
public class ParameterDialog extends JFrame {

    private String iType;
    private JButton closeButton;
    private JScrollPane scrollPane;
    private JPanel panel;

    public ParameterDialog(JFrame parent, String aType, ParameterTable table) {
        iType = aType;
        setTitle(XTandemViewer.APPTITLE + " --- " + "Help");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        scrollPane = new JScrollPane(table);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                close();
            }
        });
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.setMinimumSize(new java.awt.Dimension(10, 10));
        panel.setPreferredSize(new java.awt.Dimension(10, 10));
        scrollPane.setViewportView(panel);

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
}
