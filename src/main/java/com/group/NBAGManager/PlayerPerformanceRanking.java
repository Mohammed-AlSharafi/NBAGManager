package com.group.NBAGManager;

import com.group.NBAGManager.model.GuiCreator;
import com.group.NBAGManager.model.Player;
import com.group.NBAGManager.model.RepositoryHandler;
import com.group.NBAGManager.repository.TeamRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PlayerPerformanceRanking {
    TeamRepository teamRepository;

    public void DisplayPlayerPerformance() {

        teamRepository = RepositoryHandler.getInstance().getTeamRepository();
        //store all the players in the current user's team in a list
        List<Player> players = teamRepository.findAll();
        //System.out.println("Player2: " + players2.toString());

        // Sort players based on composite performance score
        PlayerPerformanceRanking.selectionSort(players);

        // set up the gui main frame
        JFrame frame = new JFrame("Player Performance Rankings");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600); //addjusted the size for better display

        //convert playerPerformances list to a format suitable for JList
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (int i = 0; players.size() > i; i++) {
            Player player = players.get(i);
            listModel.addElement((i + 1) + ". " + players.get(i).getFirstName()+" "+players.get(i).getLastName() + " - Composite Performance Score: "
                    + players.get(i).getCompositeScore());
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
