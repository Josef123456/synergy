/*
 *  NachoCalendar
 *
 * Project Info:  http://nachocalendar.sf.net
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 * Changes
 * -------
 * 
 * -------
 * 
 * JTableDemo.java
 * 
 * Created on 29/12/2004
 *
 */
package synergy.nachocalendar.demo;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import synergy.nachocalendar.table.JTableCustomizer;


/**
 *
 * @author  Ignacio Merani
 */
public class JTableDemo extends javax.swing.JDialog {
    private DefaultTableModel tablemodel;
    /** Creates new form JTableDemo */
    public JTableDemo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setTitle("NachoCalendar JTable Demo");
        tablemodel = (DefaultTableModel) table.getModel();
        JTableCustomizer.setEditorForRow(table, 3);
        bDelete.setEnabled(false);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent arg0) {
                bDelete.setEnabled(table.getSelectedRowCount() > 0);
            }
            
        });
        setLocationRelativeTo(parent);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        javax.swing.JPanel jPanel1;
        javax.swing.JScrollPane jScrollPane1;

        jPanel1 = new javax.swing.JPanel();
        bInsert = new javax.swing.JButton();
        bDelete = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        bClose = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        bInsert.setText("Insert");
        bInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bInsertActionPerformed(evt);
            }
        });

        jPanel1.add(bInsert);

        bDelete.setText("Delete");
        bDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDeleteActionPerformed(evt);
            }
        });

        jPanel1.add(bDelete);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setPreferredSize(new java.awt.Dimension(10, 0));
        jPanel1.add(jSeparator1);

        bClose.setText("Close");
        bClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCloseActionPerformed(evt);
            }
        });

        jPanel1.add(bClose);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        table.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Item", "Name", "Complete", "Due"
            }
        ) {
            Class[] types = new Class [] {
                Integer.class, String.class, Boolean.class, java.util.Date.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(table);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

    private void bDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDeleteActionPerformed
        tablemodel.removeRow(table.getSelectedRow());
        tablemodel.fireTableDataChanged();
    }//GEN-LAST:event_bDeleteActionPerformed

    private void bInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bInsertActionPerformed
        tablemodel.addRow(new Object[4]);
        tablemodel.fireTableDataChanged();
    }//GEN-LAST:event_bInsertActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        setVisible(true);
        dispose();
    }//GEN-LAST:event_formWindowClosing

    private void bCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCloseActionPerformed
        setVisible(true);
        dispose();
    }//GEN-LAST:event_bCloseActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bClose;
    private javax.swing.JButton bDelete;
    private javax.swing.JButton bInsert;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
    
}
