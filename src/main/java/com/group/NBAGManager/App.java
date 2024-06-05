package com.group.NBAGManager;

import com.group.NBAGManager.model.CurrentSession;
import com.group.NBAGManager.model.Player;
import com.group.NBAGManager.model.RepositoryHandler;
import com.group.NBAGManager.model.User;
import com.group.NBAGManager.repository.UserRepository;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class App {
    private JButton addPlayerBtn;
    private JPanel panelMain;
    private JButton removeButton;
    private JButton injuriesButton;
    private JButton contractButton;
    private JButton rankingButton;
    private JButton journeyButton;
    private JButton filteredSearchButton;
    private JButton resetButton;
    private JLabel heading;
    private JTable displayTable;
    private JScrollPane scroll;
    private JFrame frame;

    public App() {
        addPlayerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AddPlayer addPlayer = new AddPlayer();
                addPlayer.displayForm();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RemovePlayer removePlayer = new RemovePlayer();
                removePlayer.displayForm();
            }
        });
        injuriesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new InjuryReserveManagement();
            }
        });
        contractButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ContractExtensionQueue();
            }
        });
        rankingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlayerPerformanceRanking playerPerformanceRanking = new PlayerPerformanceRanking();
                playerPerformanceRanking.DisplayPlayerPerformance();
            }
        });
        journeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Journey journey = new Journey();
                journey.displayForm();
            }
        });
        filteredSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Filter filter = new Filter();
                filter.displayForm();
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new App().displayForm();
            }
        });
    }

    public void displayForm() {
        resetButton.setVisible(false);

        frame = new JFrame("App");
        frame.setContentPane(panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public void displayForm(List<Player> players) {
        frame = new JFrame("App");
        frame.setContentPane(panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        App app = new App();
        app.displayForm();
    }
}
