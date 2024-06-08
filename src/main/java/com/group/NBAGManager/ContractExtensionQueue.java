package com.group.NBAGManager;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.group.NBAGManager.model.CurrentSession;
import com.group.NBAGManager.model.Player;
import com.group.NBAGManager.model.RepositoryHandler;
import com.group.NBAGManager.repository.TeamRepository;
import com.group.NBAGManager.repository.UserRepository;

public class ContractExtensionQueue {

    private JPanel panelMain;
    private JLabel contractLabel;
    private JTable contractTable;
    private JButton removeButton;
    private JButton backButton;
    private JButton addButton;
    private JLabel heading;
    private JButton addToQueue;
    private JFrame frame;

    private PriorityQueue<Player> contractQueue;

    //repository connections
    TeamRepository teamRepository;

    public ContractExtensionQueue() {
        //queue for contract
        contractQueue = new PriorityQueue<>();

        //initializing repositories
        teamRepository = RepositoryHandler.getInstance().getTeamRepository();

        // contractTable for players in contract queue
        String[] columnNames = {"Player", "Status", "Player's Composite Score"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        contractTable.setModel(tableModel);

        // load existing queue
        loadQueueState();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPlayerFromRepository();
            }
        });
    }


    public void displayForm() { //to display form file for GUI
        frame = new JFrame("Contract Extension Queue");
        frame.setContentPane(panelMain);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
            }
        });

        //action listener
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                super.windowClosing(e);
            }
        });
    }


    private void addPlayerFromRepository() { //window popup containing players in current team
        JFrame playerListFrame = new JFrame("Select Player");
        playerListFrame.setSize(500, 400);
        playerListFrame.setLocationRelativeTo(null);

        // Retrieve players who are not in the contract extension queue from TeamRepository
        List<Player> availablePlayers = teamRepository.findIsContractExtensionQueued(false);

        // Create table data for players
        Object[][] rowData = new Object[availablePlayers.size()][3];
        for (int i = 0; i < availablePlayers.size(); i++) {
            rowData[i][0] = availablePlayers.get(i).getFullName();
            rowData[i][1] = contractStatus(availablePlayers.get(i));
            rowData[i][2] = availablePlayers.get(i).getCompositeScore();
        }

        // Create table with player data
        String[] columnNames = {"Player", "Status", "Player's Composite Score"};
        DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable playerTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(playerTable);
        playerListFrame.add(scrollPane);

        addToQueue = new JButton("Add");
        playerListFrame.add(addToQueue, BorderLayout.SOUTH);
        addToQueue.setEnabled(false);

        // action listener to handle player selection
        playerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addToQueue.setEnabled(true);
                int selectedRow = playerTable.getSelectedRow();
                if (selectedRow != -1) {
                    addToQueue.setEnabled(true);
                }else{
                    addToQueue.setEnabled(false);
                }
            }
        });
        addToQueue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = playerTable.getSelectedRow();
                if (selectedRow != -1) {
                    String playerName = (String) playerTable.getValueAt(selectedRow, 0);
                    Player player = findPlayerByName(playerName);
                    if (player != null) {
                        //add player
                        addPlayerToQueue(player);
                    } else {
                        JOptionPane.showMessageDialog(panelMain, "Player not found in the team.", "Player not found", JOptionPane.WARNING_MESSAGE);
                    }
                    playerListFrame.dispose();
                }
            }
        });

        playerListFrame.setVisible(true);
    }

    //removing player from queue and updating values
    private void removePlayerFromQueue() {
//        DefaultTableModel contractTableModel = (DefaultTableModel) contractTable.getModel();
        if (!contractQueue.isEmpty()) {
            // Remove player from the contractQueue
            Player removedPlayer = contractQueue.poll();
            removedPlayer.setContractRenewQueued(false);
            teamRepository.update(removedPlayer);
            // Update the table
            updateTable();
        } else {
            //if queue empty
            JOptionPane.showMessageDialog(panelMain, "No players in the contract queue.", "Queue Empty", JOptionPane.WARNING_MESSAGE);
        }
    }
    //adding player to queue and updating table
    private void addPlayerToQueue(Player addedPlayer){
        addedPlayer.setContractRenewQueued(true);
        teamRepository.update(addedPlayer);
        contractQueue.offer(addedPlayer);
        updateTable();
    }

    private void updateTable(){
        DefaultTableModel contractTableModel = (DefaultTableModel) contractTable.getModel();
        contractTableModel.setRowCount(0);
        for(Player player : contractQueue){
            contractTableModel.addRow(new Object[]{player.getFullName(), contractStatus(player), player.getCompositeScore()});
        }
    }

    //load queue from database
    private void loadQueueState() {
        List<Player> players = teamRepository.findIsContractExtensionQueued(true);
        for(Player player : players) {
            contractQueue.offer(player);
        }
        // Update the table
        updateTable();
    }

    //find player from database by name
    private Player findPlayerByName(String name) {
        List<Player> players = teamRepository.findAll();
        for (Player player : players) {
            if (player.getFullName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    //return contract status
    private String contractStatus(Player player){
        return player.isContractRenewQueued()? "Pending to renew contract" : "Active";
    }
}
