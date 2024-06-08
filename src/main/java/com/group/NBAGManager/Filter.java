package com.group.NBAGManager;

import com.group.NBAGManager.model.GuiCreator;
import com.group.NBAGManager.model.Player;
import com.group.NBAGManager.model.RepositoryHandler;
import com.group.NBAGManager.repository.TeamRepository;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
    private JComboBox<String> positionChoice;
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

    static class CustomDocument extends PlainDocument {
        public CustomDocument() {
            super();
            setDocumentFilter(new DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                    StringBuilder sb = new StringBuilder();
                    sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
                    sb.insert(offset, string);
                    if (Filter.isNumericOrIsNull(sb.toString())) {
                        super.insertString(fb, offset, string, attr);
                    }
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    StringBuilder sb = new StringBuilder();
                    sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
                    int end = offset + length;
                    sb.replace(offset, end, text);
                    if (Filter.isNumericOrIsNull(sb.toString())) {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }

                @Override
                public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                    StringBuilder sb = new StringBuilder();
                    sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
                    int end = offset + length;
                    sb.delete(offset, end);
                    if (Filter.isNumericOrIsNull(sb.toString())) {
                        super.remove(fb, offset, length);
                    }
                }
            });
        }
    }

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

        heightField1.setDocument(new CustomDocument());
        heightField2.setDocument(new CustomDocument());
        weightField1.setDocument(new CustomDocument());
        weightField2.setDocument(new CustomDocument());
        pointsField.setDocument(new CustomDocument());
        reboundsField.setDocument(new CustomDocument());
        assistsField.setDocument(new CustomDocument());
        stealsField.setDocument(new CustomDocument());
        blocksField.setDocument(new CustomDocument());
    }

    private static Double translateTextToDouble(String text) {
        return (isNumeric(text)) ? Double.parseDouble(text) : null;
    }

    private static boolean isNull(String text) {
        return text.isBlank();
    }

    private static boolean isNumeric(String text) {
        for (char c: text.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                return false;
            }
        }
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

    public void displayForm() {
        frame = new JFrame("Filter");
        frame.setContentPane(panelMain);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                new App().displayForm();
            }
        });
    }

    private void createUIComponents() {
        searchButton = GuiCreator.createButton("Search", new Font("Roboto Mono", Font.BOLD, 15), Color.decode("#646669"), Color.decode("#323437"), false, 1, 4);
    }
}
