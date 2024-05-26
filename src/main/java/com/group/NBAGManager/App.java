package com.group.NBAGManager;

import com.group.NBAGManager.LoginPage.LoginMenu;
import com.group.NBAGManager.model.CurrentSession;
import com.group.NBAGManager.model.User;
import com.group.NBAGManager.repository.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class App {
    private JButton addPlayerBtn;
    private JPanel panelMain;
    private JButton removeButton;
    private JButton injuriesButton;
    private JButton contractButton;
    private JButton rankingButton;
    private JButton journeyButton;
    private JButton filteredSearchButton;
    private JLabel heading;
    private JList playersList;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JSpinner spinner1;

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
                Filter filter = new Filter();
            }
        });
    }
    public void displayForm(){
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
