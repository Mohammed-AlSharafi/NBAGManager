package com.group.NBAGManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.group.NBAGManager.model.Player;
import com.group.NBAGManager.repository.TeamRepository;

public class ContractExtensionQueue {

    private JPanel panelMain;
    private JLabel contractLabel;
    private JTable contractTable;
    private JButton removeButton;
    private JButton backButton;
    private JButton addButton;

    private Queue<String> contractQueue;

    public ContractExtensionQueue() {
        //queue for contract
        contractQueue = new LinkedList<>();

        //initialise GUI
        panelMain = new JPanel();
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));

        contractLabel = new JLabel("Contract");
        contractLabel.setFont(new Font("MV Boli", Font.PLAIN, 20));
        contractLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMain.add(contractLabel);

        // this one to add spacing between text and table
        panelMain.add(Box.createVerticalStrut(20));

        // contractTable for players in contract queue
        String[] columnNames = {"Player", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        contractTable = new JTable(tableModel);

        // load existing queue
        loadQueueState();


        JScrollPane tableScrollPane = new JScrollPane(contractTable);
        tableScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMain.add(tableScrollPane);

        panelMain.add(Box.createVerticalStrut(20));

        //buttons
        addButton = new JButton("Add Player");
        removeButton = new JButton("Remove Player");
        backButton = new JButton("Back");

        //panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add buttons to buttonPanel
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createHorizontalStrut(10)); //spacing
        buttonPanel.add(removeButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(backButton);

        // Add buttonPanel to panelMain
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
                saveQueueState(); //queue is saved when back is clicked
            }
        });
    }


    public void displayForm() { //to display form file for GUI
        JFrame frame = new JFrame("Contract");
        frame.setContentPane(panelMain);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1250, 700);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        //action listener
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveQueueState(); //save when click back
                frame.dispose(); //close
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removePlayerFromQueue();
            }
        });

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                saveQueueState();
                super.windowClosing(e);
            }
        });
    }


    private void addPlayerFromRepository() { //window popup containing players in current team
        JFrame playerListFrame = new JFrame("Select Player");
        playerListFrame.setSize(500, 400);
        playerListFrame.setLocationRelativeTo(null);

        // Retrieve players from TeamRepository
        TeamRepository teamRepository = new TeamRepository();
        java.util.List<Player> availablePlayers = new ArrayList<>(teamRepository.findAll());

        // Create table data for players
        Object[][] rowData = new Object[availablePlayers.size()][2];
        for (int i = 0; i < availablePlayers.size(); i++) {
            rowData[i][0] = availablePlayers.get(i).getFirstName() + " " + availablePlayers.get(i).getLastName();
            rowData[i][1] = "Active";
        }

        // Create table with player data
        String[] columnNames = {"Player", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames);
        JTable playerTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(playerTable);
        playerListFrame.add(scrollPane);

        // Add action listener to handle player selection
        playerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = playerTable.getSelectedRow();
                if (selectedRow != -1) {
                    String playerName = (String) playerTable.getValueAt(selectedRow, 0);

                    // Add player to contract table
                    DefaultTableModel contractTableModel = (DefaultTableModel) contractTable.getModel();
                    contractTableModel.addRow(new Object[]{playerName, "Pending to renew contract"});
                    //add to queue
                    contractQueue.offer(playerName);

                    // Remove player from the list of available players
                    for (Iterator<Player> iterator = availablePlayers.iterator(); iterator.hasNext();) {
                        Player player = iterator.next();
                        if ((player.getFirstName() + " " + player.getLastName()).equals(playerName)) {
                            iterator.remove();
                            break;
                        }
                    }

                    // Update table model with new list of available players
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

    private void removePlayerFromQueue() {
        DefaultTableModel contractTableModel = (DefaultTableModel) contractTable.getModel();
        if (!contractQueue.isEmpty()) {
            // Remove player from the contractQueue
            String removedPlayer = contractQueue.poll();
            // Remove player from the contractTable
            for (int i = 0; i < contractTableModel.getRowCount(); i++) {
                String playerName = (String) contractTableModel.getValueAt(i, 0);
                if (playerName.equals(removedPlayer)) {
                    contractTableModel.removeRow(i);
                    break;
                }
            }
        } else {
            //if queue empty
            JOptionPane.showMessageDialog(panelMain, "No players in the contract queue.", "Queue Empty", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void saveQueueState() { //save queue
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("contractQueue.ser"))) {
            out.writeObject(contractQueue);

            DefaultTableModel model = (DefaultTableModel) contractTable.getModel();
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
    private void loadQueueState() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("contractQueue.ser"))) {
            contractQueue = (Queue<String>) in.readObject();

            DefaultTableModel model = (DefaultTableModel) contractTable.getModel();
            ArrayList<String[]> tableData = (ArrayList<String[]>) in.readObject();
            for (String[] row : tableData) {
                model.addRow(row);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
