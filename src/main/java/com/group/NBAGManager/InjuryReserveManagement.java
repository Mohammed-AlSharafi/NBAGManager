package com.group.NBAGManager;

import com.group.NBAGManager.model.Player;
import com.group.NBAGManager.repository.PlayerRepository;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;




public class InjuryReserveManagement implements ActionListener {
    JFrame frame = new JFrame();
    JLabel label = new JLabel("Injury");
    JButton myButton = new JButton("Add players");
    JButton myButton2 = new JButton("Remove");
    JButton myButton3 = new JButton("Back");

    Stack<Player> injuryReserve = new Stack<>(); // stack to store players who are injured
    DefaultTableModel tableModel;

    InjuryReserveManagement() {
        myButton.setBounds(30, 300, 100, 50);  // Adjusted width
        myButton.setFocusable(false);
        myButton.setFont(new Font("Arial", Font.PLAIN, 12));  // Smaller font size
        myButton.addActionListener(this);

        myButton2.setBounds(150, 300, 100, 50);  // Adjusted width
        myButton2.setFocusable(false);
        myButton2.setFont(new Font("Arial", Font.PLAIN, 12));  // Smaller font size
        myButton2.addActionListener(this);

        myButton3.setBounds(270, 300, 100, 50);  // Adjusted width
        myButton3.setFocusable(false);
        myButton3.setFont(new Font("Arial", Font.PLAIN, 12));  // Smaller font size
        myButton3.addActionListener(this);

        frame.setTitle("Injury Reserve Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(420, 420);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.getContentPane().setBackground(new Color(0, 0, 50));

        label.setText("Injuries");
        label.setForeground(new Color(255, 255, 255));
        label.setFont(new Font("MV Boli", Font.PLAIN, 20));
        label.setBounds(150, 5, 200, 200);

        frame.add(label);
        frame.add(myButton);
        frame.add(myButton2);
        frame.add(myButton3);

        TablePanel tablePanel = new TablePanel();
        JScrollPane tableScrollPane = tablePanel.getTableScrollPane();
        tableScrollPane.setBounds(30, 150, 360, 120);  // Adjust the position and size
        frame.add(tableScrollPane);

        tableModel = tablePanel.getTableModel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == myButton) {
            String playerName = JOptionPane.showInputDialog("Enter player's name:");
            String injury = JOptionPane.showInputDialog("Enter player's injury:");
            if (playerName != null && injury != null) {
                addPlayerToInjuryReserve(playerName, injury);
            }
        } else if (e.getSource() == myButton2) {
            String playerName = JOptionPane.showInputDialog("Enter player's name to remove:");
            if (playerName != null) {
                removePlayerFromInjuryReserve(playerName);
            }
        } else if (e.getSource() == myButton3) {
            frame.dispose(); // Close the current frame
        }
    }

    public void addPlayerToInjuryReserve(String playerName, String injury) {
        PlayerRepository playerRepository = new PlayerRepository();
        Player player = playerRepository.findById(1);
        injuryReserve.push(player);

        // Add the new player to the table
        tableModel.addRow(new Object[]{playerName, injury});

        System.out.println("-- Adding Player to Injury Reserve --");
        System.out.println("Player: " + playerName);
        System.out.println("Injury: " + injury);
        System.out.println("Status: Added to Injury Reserve\n");
    }

    public void removePlayerFromInjuryReserve(String playerName) {
        Player removedPlayer = null;
        Stack<Player> tempStack = new Stack<>();

        while (!injuryReserve.isEmpty()) {
            Player currentPlayer = injuryReserve.pop();
            if (currentPlayer.getFirstName().equals(playerName)) {
                removedPlayer = currentPlayer;
                break;
            }
            tempStack.push(currentPlayer);
        }

        while (!tempStack.isEmpty()) {
            injuryReserve.push(tempStack.pop());
        }

        if (removedPlayer != null) {
            System.out.println("-- Removing Player from Injury Reserve --");
            System.out.println("Player: " + removedPlayer.getFirstName());
            System.out.println("Status: Cleared to Play\n");

            // Remove the player from the table
            int rowCount = tableModel.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                if (tableModel.getValueAt(i, 0).equals(playerName)) {
                    tableModel.removeRow(i);
                    break;
                }
            }
        } else {
            System.out.println("Player " + playerName + " not found in Injury Reserve.\n");
        }
    }
}
