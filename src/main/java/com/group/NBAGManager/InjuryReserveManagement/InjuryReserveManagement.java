package com.group.NBAGManager.InjuryReserveManagement;

import com.group.NBAGManager.model.Player;
import com.group.NBAGManager.model.RepositoryHandler;
import com.group.NBAGManager.repository.TeamRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

public class InjuryReserveManagement {

    private JPanel panelMain;
    private JLabel heading;
    private JTable injuryTable;
    private JButton removeButton;
    private JButton backButton;
    private JButton addButton;
    private JScrollPane scroll;
    private JFrame frame;

    private TeamRepository teamRepository;
    private List<Player> availablePlayers;
    private Stack<Player> injuredPlayers;

    // similar to contract, just change queue to stack
    public InjuryReserveManagement() {
        teamRepository = RepositoryHandler.getInstance().getTeamRepository();

        // retrieve all available players
        availablePlayers = teamRepository.findIsInjured(false);

        // retrieve all injured players and stack them
        List<Player> injured = teamRepository.findIsInjured(true);
        injured.sort((Comparator.comparing(Player::getInjuryDateTime)));
        injuredPlayers = new Stack<>();
        injured.forEach(player -> injuredPlayers.push(player));

        // update injured players table
        loadStackState();

        // Add listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPlayerFromRepository();
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removePlayerFromStack();
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    public void displayForm() {
        frame = new JFrame("Injury Reserve");
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
    }

    private void addPlayerFromRepository() {
        JFrame playerListFrame = new JFrame("Select Player");
        playerListFrame.setSize(500, 400);
        playerListFrame.setLocationRelativeTo(null);
        playerListFrame.setVisible(true);

        Object[][] rowData = new Object[availablePlayers.size()][2];
        for (int i = 0; i < availablePlayers.size(); i++) {
            rowData[i][0] = availablePlayers.get(i).getFullName();
            rowData[i][1] = "Active";
        }

        String[] columnNames = {"Player", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return Player.class;
                    case 1:
                        return String.class;
                    default:
                        return Object.class;
                }
            }
        };

        JTable addPlayerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(addPlayerTable);
        playerListFrame.add(scrollPane);

        JButton addToStack = new JButton("Add");
        playerListFrame.add(addToStack, BorderLayout.SOUTH);
        addToStack.setEnabled(false);

        addPlayerTable.addMouseListener(new MouseAdapter() {
            private Player findPlayerByName(String name) {
                for (Player player: availablePlayers) {
                    if (player.getFullName().equals(name)) {
                        return player;
                    }
                }
                return null;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                    int selectedRow = addPlayerTable.getSelectedRow();
                    if (selectedRow != -1) {
                        addToStack.setEnabled(true);
                    }else {
                        addToStack.setEnabled(false);
                    }
            }
        });

        addToStack.addActionListener(new ActionListener() {
            private Player findPlayerByName(String name) {
                for (Player player: availablePlayers) {
                    if (player.getFullName().equals(name)) {
                        return player;
                    }
                }
                return null;
            }
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = addPlayerTable.getSelectedRow();
                if (selectedRow != -1) {
                    String playerName = (String) addPlayerTable.getValueAt(selectedRow, 0);
                    Player player = findPlayerByName(playerName);

                    String injuryDescMessage = "Enter injury description:";
                    JTextField injuryDescField = new JTextField();
                    JOptionPane.showConfirmDialog(null, injuryDescField, injuryDescMessage, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    String injuryDesc = injuryDescField.getText();
                    if (player == null) throw new RuntimeException("player should not be null at this point");

                    // mark player as injured and update database
                    player.setInjured(true);
                    player.setInjuryDescription(injuryDesc);
                    player.setInjuryDateTime(LocalDateTime.now());
                    teamRepository.update(player);

                    // remove the player from displayed available players
                    tableModel.removeRow(availablePlayers.indexOf(player));

                    // update lists of available and injured players
                    availablePlayers.remove(player);
                    injuredPlayers.push(player);

                    // update the injured players table
                    loadStackState();
                }
                playerListFrame.dispose();
            }
        });

        playerListFrame.setVisible(true);
    }

    private void removePlayerFromStack() {
        if (!injuredPlayers.isEmpty()) {
            // update the injured players table
            DefaultTableModel injuryTableModel = (DefaultTableModel) injuryTable.getModel();
            injuryTableModel.removeRow(0);

            // remove player from injuredPlayers list
            Player player = injuredPlayers.pop();
            // add player to availablePlayers list
            availablePlayers.add(player);

            // update database
            player.setInjured(false);
            player.setInjuryDateTime(null);
            player.setInjuryDescription("");
            teamRepository.update(player);
        }
        else {
            JOptionPane.showMessageDialog(panelMain, "No players in the injury reserve stack.", "Stack Empty", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadStackState() {
        // load in reverse order so the last item pushed to stack is displayed on top
        String[] columnNames = {"Player", "Status", "Injury Description"};
        Object[][] data = new Object[injuredPlayers.size()][3];

        for (int i = 0; i < injuredPlayers.size(); i++) {
            data[i][0] = injuredPlayers.get(injuredPlayers.size()-1-i).getFullName();
            data[i][1] = "Injured";
            data[i][2] = injuredPlayers.get(injuredPlayers.size()-1-i).getInjuryDescription();
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                    case 1:
                        return String.class;
                    default:
                        return Object.class;
                }
            }
        };
        injuryTable.setModel(tableModel);
    }
}
