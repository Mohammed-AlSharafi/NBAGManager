package com.group.NBAGManager;

import com.group.NBAGManager.LoginPage.GuiCreator;
import com.group.NBAGManager.model.Player;
import com.group.NBAGManager.model.RepositoryHandler;
import com.group.NBAGManager.repository.TeamRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class PlayerPerformanceRanking {
    TeamRepository teamRepository;

    public void DisplayPlayerPerformance() {

        teamRepository = RepositoryHandler.getInstance().getTeamRepository();
        //store all the players in the current user's team in a list
        List<Player> players2 = teamRepository.findAll();
        //System.out.println("Player2: " + players2.toString());

        //make a new ArrayList to store the players with their composite score based on performancee
        ArrayList<PlayerPerformance> players = new ArrayList<>();
        for (int i = 0; players2.size() > i; i++) {
            players.add(new PlayerPerformance(players2.get(i).getFirstName() + " " + players2.get(i).getLastName(),
                    players2.get(i).getPosition(), players2.get(i).getPoints(), players2.get(i).getRebounds(),
                    players2.get(i).getSteals(), players2.get(i).getAssists(), players2.get(i).getBlocks(), 0.0));
            // call the calculatePerfomanceScore method with to calculate the players perfomance score based on their stats
            double compositeScore = players.get(i).calculatePerformanceScore();
            // save the performance score into the composotieScore element in the PlayerPerformance object
            players.get(i).compositeScore = compositeScore;
        }

        // Sort players based on composite performance score
        //players.sort(Comparator.comparingDouble(PlayerPerformance::calculatePerformanceScore).reversed());

        PlayerPerformanceRanking.selectionSort(players);

        // set up the gui main frame
        JFrame frame = new JFrame("Player Performance Rankings");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600); //addjusted the size for better display

        //convert playerPerformances list to a format suitable for JList
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (int i = 0; players.size() > i; i++) {
            PlayerPerformance player = players.get(i);
            listModel.addElement((i + 1) + ". " + players.get(i).name + " - Composite Performance Score: "
                    + players.get(i).compositeScore);
        }

        JList<String> playerList = new JList<>(listModel);
        playerList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        //create a JScrollPane for the playerList
        JScrollPane scrollPane = new JScrollPane(playerList);
        scrollPane.setPreferredSize(new Dimension(700, 500));

        //create a container panel with GridBagLayout
        JPanel containerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); //added padding
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        containerPanel.add(scrollPane, gbc); //added playerList wrapped in JScrollPane

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

        //center the container panel and adjust its size
        GridBagConstraints gbcContent = new GridBagConstraints();
        gbcContent.gridx = 0;
        gbcContent.gridy = 1;
        gbcContent.anchor = GridBagConstraints.CENTER;
        gbcContent.insets = new Insets(10, 10, 10, 10);
        gbcContent.fill = GridBagConstraints.BOTH; // Fill the space
        gbcContent.weightx = 1.0;
        gbcContent.weighty = 1.0;

        //add a new constraint for the button panel
        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.gridx = 0;
        gbcButton.gridy = 2;
        gbcButton.anchor = GridBagConstraints.SOUTH;
        gbcButton.insets = new Insets(10, 10, 10, 10);

        //adjust the preferred size of the container panel
        containerPanel.setPreferredSize(new Dimension(700, 500));

        backgroundLabel.add(titleLabel, gbcTitle); //add the label to the top of the layout
        backgroundLabel.add(containerPanel, gbcContent);//add the container panel to the center
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

        //display the window
        frame.setVisible(true);
        //set it to not be able to resize
        frame.setResizable(false);
    }

    //Selection Sort method to sort the players based on the calculated composite score
    public static void selectionSort(ArrayList<PlayerPerformance> players) {
        for (int i = 0; i < players.size() - 1; i++) {

            int minIndex = i;
            for (int j = i + 1; j < players.size(); j++) {
                if (players.get(j).getCompositeScore() > players.get(minIndex).getCompositeScore()) {
                    minIndex = j;
                }
            }

            //swap the found minimum element with the first element
            PlayerPerformance temp = players.get(minIndex);
            players.set(minIndex, players.get(i));
            players.set(i, temp);
        }
    }
}


// specific PlayerPerformance class for the PlayerPerformanceRanking
class PlayerPerformance {
    String name;
    String position;
    double avgPoints;
    double rebounds;
    double steals;
    double assists;
    double blocks;
    double compositeScore;

    // Constructor
    public PlayerPerformance(String name, String position, double avgPoints, double rebounds, double steals, double assists, double blocks, double compositeScore) {
        this.name = name;
        this.position = position;
        this.avgPoints = avgPoints;
        this.rebounds = rebounds;
        this.steals = steals;
        this.assists = assists;
        this.blocks = blocks;
        this.compositeScore = compositeScore;
    }

    // Method to calculate composite performance score
    public double calculatePerformanceScore() {
        // Define weights for different criteria based on player's position
        double pointsWeight = 1.7;
        double reboundsWeight = (position.equals("Center") || position.equals("Forward")) ? 3 : 1.0;
        double stealsWeight = (position.equals("Guard")) ? 3 : 1.0;
        double assistsWeight = (position.equals("Guard")) ? 3 : 1.0;
        double blocksWeight = (position.equals("Center") || position.equals("Forward")) ? 3 : 1.0;

        // Calculate composite performance score
        double performanceScore = avgPoints * pointsWeight + rebounds * reboundsWeight + steals * stealsWeight + assists * assistsWeight + blocks * blocksWeight;
        return performanceScore;
    }

    //getter to get the compositeScore
    public double getCompositeScore() {
        return compositeScore;
    }
}
