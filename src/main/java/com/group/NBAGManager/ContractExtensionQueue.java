package com.group.NBAGManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private JLabel label1;
    private JTable contractTable;
    private JButton removeButton;
    private JButton backButton;
    private JButton addButton;

    private Queue<String> contractQueue;

    public ContractExtensionQueue() {

        contractQueue = new LinkedList<String>();

        // Initialize panelMain and set its layout
        panelMain = new JPanel();
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));

        // Initialize label and add it to panelMain
        label1 = new JLabel("Contract");
        label1.setFont(new Font("MV Boli", Font.PLAIN, 20));
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMain.add(label1);

        // Add vertical space (e.g., 20 pixels) between label and table
        panelMain.add(Box.createVerticalStrut(20));

        // Initialize table with columns "Player" and "Status"
        String[] columnNames = {"Player", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        contractTable = new JTable(tableModel);

        // Create a JScrollPane for the table and add it to panelMain
        JScrollPane tableScrollPane = new JScrollPane(contractTable);
        tableScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMain.add(tableScrollPane);

        // Add vertical space (e.g., 20 pixels) between table and buttons
        panelMain.add(Box.createVerticalStrut(20));

        // Initialize buttons
        addButton = new JButton("Add Player");
        removeButton = new JButton("Remove Player");
        backButton = new JButton("Back");

        // Create a panel for the buttons with horizontal layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add buttons to buttonPanel
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createHorizontalStrut(10)); // Add space between buttons
        buttonPanel.add(removeButton);
        buttonPanel.add(Box.createHorizontalStrut(10)); // Add space between buttons
        buttonPanel.add(backButton);

        // Add buttonPanel to panelMain
        panelMain.add(buttonPanel);

        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPlayerFromRepository();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
    }

    public void displayForm() {

        JFrame frame = new JFrame("Contract");
        frame.setContentPane(panelMain);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1250, 700);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        // Add action listener to backButton to close the window
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
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
                // Handle any cleanup here if needed
            }
        });
    }

    private void addPlayerFromRepository() {
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
            rowData[i][1] = "Pending";
        }

        // Create table with player data
        String[] columnNames = {"Player", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames);
        JTable playerTable = new JTable(tableModel);

        // Create a JScrollPane for the table
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
                    contractTableModel.addRow(new Object[]{playerName, "Pending"});
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
                        updatedRowData[i][1] = "Pending";
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
            // If the queue is empty, display a message
            JOptionPane.showMessageDialog(panelMain, "No players in the contract queue.", "Queue Empty", JOptionPane.WARNING_MESSAGE);
        }
    }






/*public class ContractExtensionQueue implements ActionListener {
    JFrame frame = new JFrame();
    JLabel label = new JLabel("Contract");
    JButton myButton = new JButton("Add players");
    JButton myButton2 = new JButton("Remove");
    JButton myButton3 = new JButton("Back");

    //Queue<Player> extensionQueue = new LinkedList<>();
    DefaultTableModel tableModel;

    private static final String FILE_NAME = "extensionQueue.ser"; // File to save the queue state

    ContractExtensionQueue() {
        myButton.setBounds(30, 300, 100, 50);
        myButton.setFocusable(false);
        myButton.setFont(new Font("Arial", Font.PLAIN, 12));
        myButton.addActionListener(this);

        myButton2.setBounds(150, 300, 100, 50);
        myButton2.setFocusable(false);
        myButton2.setFont(new Font("Arial", Font.PLAIN, 12));
        myButton2.addActionListener(this);

        myButton3.setBounds(270, 300, 100, 50);
        myButton3.setFocusable(false);
        myButton3.setFont(new Font("Arial", Font.PLAIN, 12));
        myButton3.addActionListener(this);

        frame.setTitle("Contract Extension Queue");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(420, 420);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.getContentPane().setBackground(new Color(0, 0, 50));

        label.setText("Contract");
        label.setForeground(new Color(255, 255, 255));
        label.setFont(new Font("MV Boli", Font.PLAIN, 20));
        label.setBounds(150, 5, 200, 200);

        frame.add(label);
        frame.add(myButton);
        frame.add(myButton2);
        frame.add(myButton3);

        TablePanel tablePanel = new TablePanel();
        JScrollPane tableScrollPane = tablePanel.getTableScrollPane();
        tableScrollPane.setBounds(30, 150, 360, 120);
        frame.add(tableScrollPane);

        tableModel = tablePanel.getTableModel();

        // Load the queue state from the file
        //loadQueueState();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == myButton) {
            //addPlayerToExtensionQueue("Player " + (extensionQueue.size() + 1));
        } else if (e.getSource() == myButton2) {
            //removePlayerFromExtensionQueue();
        } else if (e.getSource() == myButton3) {
            frame.dispose();
        }
    }*/

    /*public void addPlayerToExtensionQueue(String playerName) {
        PlayerRepository playerRepository = new PlayerRepository();
        Player player = playerRepository.findById(1);
        extensionQueue.add(player);

        // Add the new player to the table
        tableModel.addRow(new Object[]{playerName, "Pending"});

        System.out.println("-- Adding Player to Contract Extension Queue --");
        System.out.println("Player: " + playerName);
        System.out.println("Status: Added to Contract Extension Queue\n");

        saveQueueState();
    }

    public void removePlayerFromExtensionQueue() {
        /*if (!extensionQueue.isEmpty()) {
            Player player = extensionQueue.poll();
            System.out.println("-- Removing Player from Contract Extension Queue --");
            System.out.println("Player: " + player.getFirstName());
            System.out.println("Status: Contract Renewed\n");

            // Remove the player from the table
            int rowCount = tableModel.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                if (tableModel.getValueAt(i, 0).equals(player.getFirstName())) {
                    tableModel.removeRow(i);
                    break;
                }
            }

            saveQueueState();
        } else {
            System.out.println("No players in the Contract Extension Queue.\n");
        }
    }

    private void saveQueueState() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(new LinkedList<>(extensionQueue)); // Use LinkedList to ensure serialization
            System.out.println("Queue state saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadQueueState() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            extensionQueue = (Queue<Player>) in.readObject();
            System.out.println("Queue state loaded.");

            // Repopulate the table with the loaded queue
            for (Player player : extensionQueue) {
                tableModel.addRow(new Object[]{player.getFirstName(), "Pending"});
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }*/
    }
