package com.group.NBAGManager;

import com.group.NBAGManager.LoginPage.GuiCreator;
import com.group.NBAGManager.model.CurrentSession;
import com.group.NBAGManager.model.Player;
import com.group.NBAGManager.model.RepositoryHandler;
import com.group.NBAGManager.model.User;
import com.group.NBAGManager.repository.TeamRepository;
import com.group.NBAGManager.repository.UserRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Filter {

    private JLabel postionLabel;
    private JLabel weightLabel;
    private JLabel heightLabel;
    private JLabel pointsLabel;
    private JLabel blocksLabel;
    private JLabel stealsLabel;
    private JLabel assistsLabel;
    private JLabel reboundsLabel;
    private JPanel panelMain;
    private JButton searchButton;
    private JComboBox positionChoice;
    private JTextField pointsField;
    private JTextField heightField1;
    private JTextField weightField1;
    private JTextField blocksField;
    private JTextField stealsField;
    private JTextField assistsField;
    private JTextField reboundsField;
    private JTextField heightField2;
    private JTextField weightField2;
    private JLabel heightGreaterThanLabel;
    private JLabel weightGreaterThanLabel;
    private JLabel heightLesserThanLabel;
    private JLabel weightLesserThanLabel;
    private JFrame frame;


    public Filter() {
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TeamRepository teamRepository = RepositoryHandler.getInstance().getTeamRepository();
                List<Player> players = teamRepository.findAll();

                String height1 = heightField1.getText();
                String height2 = heightField2.getText();
                String weight1 = weightField1.getText();
                String weight2 = weightField2.getText();
                String pointsText = pointsField.getText();
                String reboundsText = reboundsField.getText();
                String assistsText = assistsField.getText();
                String stealsText = stealsField.getText();
                String blocksText = blocksField.getText();

                if (!isNumericOrIsNull(height1) || !isNumericOrIsNull(height2)) {
                    System.out.println("Invalid height input!!");
                    return;
                }
                if (!isNumericOrIsNull(weight1) || !isNumericOrIsNull(weight2)) {
                    System.out.println("Invalid weight input!!");
                    return;
                }
                if (!isNumericOrIsNull(pointsText)) {
                    System.out.println("Invalid points input!!");
                    return;
                }
                if (!isNumericOrIsNull(reboundsText)) {
                    System.out.println("Invalid rebounds input!!");
                    return;
                }
                if (!isNumericOrIsNull(assistsText)) {
                    System.out.println("Invalid assists input!!");
                    return;
                }
                if (!isNumericOrIsNull(stealsText)) {
                    System.out.println("Invalid steals input!!");
                    return;
                }
                if (!isNumericOrIsNull(blocksText)) {
                    System.out.println("Invalid blocks input!!");
                    return;
                }

                Double heightGreater = translateTextToDouble(height1);
                Double heightLesser = translateTextToDouble(height2);
                Double weightGreater = translateTextToDouble(weight1);
                Double weightLesser = translateTextToDouble(weight2);
                String position = (String) positionChoice.getSelectedItem();
                Double points = translateTextToDouble(pointsText);
                Double rebounds = translateTextToDouble(reboundsText);
                Double assists = translateTextToDouble(assistsText);
                Double steals = translateTextToDouble(stealsText);
                Double blocks = translateTextToDouble(blocksText);

                ArrayList<Player> selectedPlayers = new ArrayList<>();
                for (Player player: players) {
                    boolean check = true;
                    if (heightGreater != null && player.getHeight() < heightGreater) {
                        check = false;
                    }
                    if (heightLesser != null && player.getHeight() > heightLesser) {
                        check = false;
                    }
                    if (weightGreater != null && player.getWeight() < weightGreater) {
                        check = false;
                    }
                    if (weightLesser != null && player.getWeight() > weightLesser) {
                        check = false;
                    }
                    if (!position.equals("Any") && !position.equals(player.getPosition())) {
                        check = false;
                    }
                    if (points != null && player.getPoints() < points) {
                        check = false;
                    }
                    if (rebounds != null && player.getRebounds() < rebounds) {
                        check = false;
                    }
                    if (assists != null && player.getAssists() < assists) {
                        check = false;
                    }
                    if (steals != null && player.getSteals() < steals) {
                        check = false;
                    }
                    if (blocks != null && player.getBlocks() < blocks) {
                        check = false;
                    }

                    if (check) {
                        selectedPlayers.add(player);
                    }
                }

                System.out.println(selectedPlayers);
                frame.dispose();
                new App().displayForm(selectedPlayers);
            }
        });
    }

    private static Double translateTextToDouble(String text) {
        return (isNumeric(text)) ? Double.parseDouble(text) : null;
    }

    private static boolean isNull(String text) {
        return text.isBlank();
    }

    private static boolean isNumeric(String text) {
        try {
            Double.parseDouble(text);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isNumericOrIsNull(String text) {
        return isNumeric(text) || isNull(text);
    }

    public void displayForm(){
        frame = new JFrame("Filter");
        frame.setContentPane(panelMain);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Filter().displayForm();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        JButton customSearchButton = GuiCreator.createButton("Search", new Font("Roboto Mono", Font.BOLD, 15), Color.decode("#646669"), Color.decode("#323437"), false, 1, 4);
        searchButton = customSearchButton;
    }
}
