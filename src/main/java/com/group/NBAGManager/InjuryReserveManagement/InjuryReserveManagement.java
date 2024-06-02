package com.group.NBAGManager.InjuryReserveManagement;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.group.NBAGManager.model.Player;
import com.group.NBAGManager.repository.TeamRepository;

public class InjuryReserveManagement {

    private JPanel panelMain;
    private JLabel label1;
    private JTable injuryTable;
    private JButton removeButton;
    private JButton backButton;
    private JButton addButton;

    private Stack<String> injuryStack;

    //similar to contract, just change queue to stack
    public InjuryReserveManagement() {
        injuryStack = new Stack<>();

        panelMain = new JPanel();
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));

        label1 = new JLabel("Injury Reserve");
        label1.setFont(new Font("MV Boli", Font.PLAIN, 20));
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMain.add(label1);

        panelMain.add(Box.createVerticalStrut(20));

        String[] columnNames = {"Player", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        injuryTable = new JTable(tableModel);

        loadStackState();

        JScrollPane tableScrollPane = new JScrollPane(injuryTable);
        tableScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMain.add(tableScrollPane);

        panelMain.add(Box.createVerticalStrut(20));

        addButton = new JButton("Add Player");
        removeButton = new JButton("Remove Player");
        backButton = new JButton("Back");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(addButton);
        buttonPanel.add(Box.createHorizontalStrut(10)); // Add space between buttons
        buttonPanel.add(removeButton);
        buttonPanel.add(Box.createHorizontalStrut(10)); // Add space between buttons
        buttonPanel.add(backButton);

        panelMain.add(buttonPanel);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPlayerFromRepository();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveStackState();
            }
        });
    }

    public void displayForm() {
        JFrame frame = new JFrame("Injury Reserve");
        frame.setContentPane(panelMain);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1250, 700);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveStackState();
                frame.dispose();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removePlayerFromStack();
            }
        });

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                saveStackState();
                super.windowClosing(e);
            }
        });
    }

    private void addPlayerFromRepository() {
        JFrame playerListFrame = new JFrame("Select Player");
        playerListFrame.setSize(500, 400);
        playerListFrame.setLocationRelativeTo(null);

        TeamRepository teamRepository = new TeamRepository();
        List<Player> availablePlayers = new ArrayList<>(teamRepository.findAll());

        Object[][] rowData = new Object[availablePlayers.size()][2];
        for (int i = 0; i < availablePlayers.size(); i++) {
            rowData[i][0] = availablePlayers.get(i).getFirstName() + " " + availablePlayers.get(i).getLastName();
            rowData[i][1] = "Active";
        }

        String[] columnNames = {"Player", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames);
        JTable playerTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(playerTable);
        playerListFrame.add(scrollPane);

        playerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = playerTable.getSelectedRow();
                if (selectedRow != -1) {
                    String playerName = (String) playerTable.getValueAt(selectedRow, 0);

                    DefaultTableModel injuryTableModel = (DefaultTableModel) injuryTable.getModel();
                    injuryTableModel.addRow(new Object[]{playerName, "Injured"});
                    injuryStack.push(playerName); //add to stack

                    for (Iterator<Player> iterator = availablePlayers.iterator(); iterator.hasNext();) {
                        Player player = iterator.next();
                        if ((player.getFirstName() + " " + player.getLastName()).equals(playerName)) {
                            iterator.remove();
                            break;
                        }
                    }

                    Object[][] updatedRowData = new Object[availablePlayers.size()][2];
                    for (int i = 0; i < availablePlayers.size(); i++) {
                        updatedRowData[i][0] = availablePlayers.get(i).getFirstName() + " " + availablePlayers.get(i).getLastName();
                        updatedRowData[i][1] = "Active";
                    }
                    tableModel.setDataVector(updatedRowData, columnNames);
                }
                playerListFrame.dispose();
            }
        });

        playerListFrame.setVisible(true);
    }

    private void removePlayerFromStack() {
        DefaultTableModel injuryTableModel = (DefaultTableModel) injuryTable.getModel();
        if (!injuryStack.isEmpty()) {
            String removedPlayer = injuryStack.pop(); //remove from stack
            for (int i = 0; i < injuryTableModel.getRowCount(); i++) {
                String playerName = (String) injuryTableModel.getValueAt(i, 0);
                if (playerName.equals(removedPlayer)) {
                    injuryTableModel.removeRow(i);
                    break;
                }
            }
        } else {
            //empty stack
            JOptionPane.showMessageDialog(panelMain, "No players in the injury reserve stack.", "Stack Empty", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void saveStackState() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("injuryStack.ser"))) {
            out.writeObject(injuryStack);

            DefaultTableModel model = (DefaultTableModel) injuryTable.getModel();
            int rowCount = model.getRowCount();
            ArrayList<String[]> tableData = new ArrayList<>();
            for (int i = 0; i < rowCount; i++) {
                String[] row = new String[2];
                row[0] = (String) model.getValueAt(i, 0);
                row[1] = (String) model.getValueAt(i, 1);
                tableData.add(row);
            }
            out.writeObject(tableData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadStackState() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("injuryStack.ser"))) {
            injuryStack = (Stack<String>) in.readObject();

            DefaultTableModel model = (DefaultTableModel) injuryTable.getModel();
            ArrayList<String[]> tableData = (ArrayList<String[]>) in.readObject();
            for (String[] row : tableData) {
                model.addRow(row);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
