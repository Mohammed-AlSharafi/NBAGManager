package com.group.NBAGManager;

import com.group.NBAGManager.components.RoundedButton;
import com.group.NBAGManager.model.Player;
import com.group.NBAGManager.model.RepositoryHandler;
import com.group.NBAGManager.repository.TeamRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PlayerPerformanceRanking {
    TeamRepository teamRepository;

    public void DisplayPlayerPerformance() {

        teamRepository = RepositoryHandler.getInstance().getTeamRepository();
        //store all the players in the current user's team in a list
        List<Player> players = teamRepository.findAll();

        // sort players based on composite performance score
        PlayerPerformanceRanking.selectionSort(players);

        // set up the gui main frame
        JFrame frame = new JFrame("Player Performance Rankings");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600); //addjusted the size for better display


        //create column names and data for the table
        String[] columnNames = {"Rank", "Name", "Composite Score"};
        Object[][] data = new Object[players.size()][columnNames.length];
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            data[i][0] = i + 1; // Rank
            data[i][1] = player.getFirstName() + " " + player.getLastName(); // Name
            data[i][2] = player.getCompositeScore(); // Composite Score
        }

        //create the table model
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        //create the performace ranking table
        JTable playerTable = new JTable(tableModel);
        playerTable.setRowSorter(new TableRowSorter<>(tableModel));

        //adjust column widths
        playerTable.getColumnModel().getColumn(0).setPreferredWidth(50); // Rank
        playerTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
        playerTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Composite Score

        //make the column size to be set
        playerTable.getTableHeader().setResizingAllowed(false);


        // Set custom renderer for all columns
        for (int i = 0; i < playerTable.getColumnCount(); i++) {
            playerTable.getColumnModel().getColumn(i).setCellRenderer(new App.CustomCellRenderer());
        }
        //create scrollPane for the table
        JScrollPane scrollPane = new JScrollPane(playerTable);

        //create the return button to return to main page
        JButton returnButton = new RoundedButton("Return");
        returnButton.setMargin(new Insets(5, 30, 5, 30));

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); //close the current frame
            }
        });

        //set the label as the content pane
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setLayout(new GridBagLayout());
        frame.setContentPane(backgroundLabel);

        //set the title of the page
        JLabel titleLabel = new JLabel("Player Performance Ranking", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        //constraints for the title
        GridBagConstraints gbcTitle = new GridBagConstraints();
        gbcTitle.gridx = 0;
        gbcTitle.gridy = 0;
        gbcTitle.anchor = GridBagConstraints.NORTH;
        gbcTitle.insets = new Insets(10, 10, 10, 10);
        gbcTitle.weightx = 1.0;

        //create a panel to hold the table and its title
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER); // Add the scroll pane with the table to the panel

        //set up GridBagConstraints for the table panel
        GridBagConstraints gbcTable = new GridBagConstraints();
        gbcTable.gridx = 0;
        gbcTable.gridy = 1;
        gbcTable.weightx = 1.0;
        gbcTable.weighty = 1.0;
        gbcTable.fill = GridBagConstraints.BOTH;

        //add a new constraint for the button panel
        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.gridx = 0;
        gbcButton.gridy = 2;
        gbcButton.anchor = GridBagConstraints.SOUTH;
        gbcButton.insets = new Insets(10, 10, 10, 10);

        //adjust the preferred size of the container panel
        backgroundLabel.setPreferredSize(new Dimension(700, 500));

        backgroundLabel.add(titleLabel, gbcTitle); //add the label to the top of the layout
        backgroundLabel.add(tablePanel, gbcTable);//add the table panel to display the player and their performance score
        backgroundLabel.add(returnButton, gbcButton); //add the button panel to the bottom

        //display the window in the middle of the screen
        frame.setLocationRelativeTo(null);
        //display the window
        frame.setVisible(true);
        //set it to not be able to resize
        frame.setResizable(false);
    }

    //Selection Sort method to sort the players based on the calculated composite score
    public static void selectionSort(List<Player> players) {
        for (int i = 0; i < players.size() - 1; i++) {

            int minIndex = i;
            for (int j = i + 1; j < players.size(); j++) {
                if (players.get(j).getCompositeScore() > players.get(minIndex).getCompositeScore()) {
                    minIndex = j;
                }
            }

            //swap the found minimum element with the first element
            Player temp = players.get(minIndex);
            players.set(minIndex, players.get(i));
            players.set(i, temp);
        }
    }
}