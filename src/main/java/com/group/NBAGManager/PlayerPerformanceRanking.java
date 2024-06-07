package com.group.NBAGManager;

import com.group.NBAGManager.model.GuiCreator;
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
        JButton returnButton = GuiCreator.createButton("Return", new Font("Roboto Mono", Font.BOLD, 15), Color.decode("#646669"), Color.decode("#323437"), false, 1, 4);

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); //close the current frame
            }
        });

        //set the image as the content pane
        ImageIcon backgroundImage = new ImageIcon("src/main/resources/images/login-background.jpeg");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(new GridBagLayout());
        frame.setContentPane(backgroundLabel);

        //set the title of the page
        JLabel titleLabel = new JLabel("Player Performance Ranking", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 25));
        titleLabel.setForeground(Color.decode("#E2B714"));

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

        //ensure the image resizes with the window
        Image img = backgroundImage.getImage();
        ImageIcon finalBackgroundImage = new ImageIcon(img.getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_SMOOTH));

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Image img = finalBackgroundImage.getImage();
                Image resizedImg = img.getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_SMOOTH);
                finalBackgroundImage.setImage(resizedImg);
                backgroundLabel.setIcon(finalBackgroundImage);
            }
        });

        //close connection to the database on window close
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if(teamRepository != null) {
                    teamRepository.close();
                }
            }
        });

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