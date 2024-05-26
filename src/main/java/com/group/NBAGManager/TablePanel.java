package com.group.NBAGManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TablePanel {

    private JScrollPane jScrollPane1;
    private JTable jTable1;
    private DefaultTableModel tableModel;

    public TablePanel() {
        initComponents();
    }

    private void initComponents() {
        jTable1 = new JTable();
        jScrollPane1 = new JScrollPane();

        tableModel = new DefaultTableModel(
            new Object [][] {},  // Start with no rows
            new String [] {
                "Name", "Status"
            }
        );
        jTable1.setModel(tableModel);
        jScrollPane1.setViewportView(jTable1);
    }

    public JScrollPane getTableScrollPane() {
        return jScrollPane1;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}
