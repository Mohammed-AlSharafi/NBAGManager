package com.group.NBAGManager;

import com.group.NBAGManager.model.Player;
import com.group.NBAGManager.repository.PlayerRepository;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;


import java.io.Serializable;


public class ContractExtensionQueue implements ActionListener {
    JFrame frame = new JFrame();
    JLabel label = new JLabel("Contract");
    JButton myButton = new JButton("Add players");
    JButton myButton2 = new JButton("Remove");
    JButton myButton3 = new JButton("Back");

    Queue<Player> extensionQueue = new LinkedList<>();
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
        loadQueueState();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == myButton) {
            addPlayerToExtensionQueue("Player " + (extensionQueue.size() + 1));
        } else if (e.getSource() == myButton2) {
            removePlayerFromExtensionQueue();
        } else if (e.getSource() == myButton3) {
            frame.dispose();
        }
    }

    public void addPlayerToExtensionQueue(String playerName) {
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
        if (!extensionQueue.isEmpty()) {
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
    }
}
