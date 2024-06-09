package com.group.NBAGManager;

import com.group.NBAGManager.model.CurrentSession;
import com.group.NBAGManager.model.Player;
import com.group.NBAGManager.model.RepositoryHandler;
import com.group.NBAGManager.repository.PlayerRepository;
import com.group.NBAGManager.repository.TeamRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableModel;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPlayer {
    private JFrame frame;
    private JPanel panelMain;
    private JButton SearchId;
    private JButton SearchName;
    private PlayerRepository playerRepository;
    private JTable playersTable;
    private JScrollPane scroll;
    private JTextField searchField;
    private JButton backButton;
    public TeamRepository teamRepository;
    private Map<String, Player> playerMap = new HashMap<>();
    List<Player> teamPlayers;
    List<Player> marketPlayers;

    public AddPlayer() {
        //creates team repository for checking team size
        teamRepository = RepositoryHandler.getInstance().getTeamRepository();
        //creates player repository for Free Agent Market
        playerRepository = RepositoryHandler.getInstance().getPlayerRepository();

        teamPlayers = teamRepository.findAll();
        marketPlayers = playerRepository.findAll();

        //creates the player table
        setPlayersTable();
        //adds mouse listener to the table
        addMouseFunction();
        //handles searchbar function
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchText = searchField.getText();
                if (!searchText.isEmpty()) {
                    scrollToPlayerByName(searchText);
                }
            }
        });
        //method for going back to previous panel
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    public JFrame getFrame() {
        return frame;
    }

    //creates player table for Free Agent Market
    private void setPlayersTable() {
        //load the table
        ArrayList<Player> unOwnedPlayers = new ArrayList<>(marketPlayers);
        unOwnedPlayers.removeAll(teamPlayers);

        String[] columnNames = {
                "Name", "Height", "Weight", "Position",
                "Points", "Rebounds", "Assists", "Steals", "Blocks"
        };
        Object[][] data = new Object[unOwnedPlayers.size()][columnNames.length];
        playerMap.clear();

        for (int i = 0; i < unOwnedPlayers.size(); i++) {
            Player player = unOwnedPlayers.get(i);
            String playerName = player.getFirstName() + " " + player.getLastName();
            data[i][0] = playerName;
            data[i][1] = player.getHeight();
            data[i][2] = player.getWeight();
            data[i][3] = player.getPosition();
            data[i][4] = player.getPoints();
            data[i][5] = player.getRebounds();
            data[i][6] = player.getAssists();
            data[i][7] = player.getSteals();
            data[i][8] = player.getBlocks();
            playerMap.put(playerName, player);
        }

        //format for table
        TableModel model = new DefaultTableModel(data, columnNames) {
            //method to modify returned value for certain columns
            @Override
            public Class<?> getColumnClass(int column) {
                return switch (column) {
                    case 0, 3 -> String.class;
                    case 1, 2, 4, 5, 6, 7 -> Double.class;
                    default -> Object.class;
                };
            }

            //cells are not editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        //sets the model of the table as dataModel
        playersTable.setModel(model);
        playersTable.setRowSorter(new TableRowSorter<>(model));

        // Adjust column widths
        playersTable.getColumnModel().getColumn(0).setPreferredWidth(170); // Name
        playersTable.getColumnModel().getColumn(1).setPreferredWidth(60);  // Height
        playersTable.getColumnModel().getColumn(2).setPreferredWidth(60);  // Weight
        playersTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Position
        playersTable.getColumnModel().getColumn(4).setPreferredWidth(70); // Points
        playersTable.getColumnModel().getColumn(5).setPreferredWidth(70); // Rebounds
        playersTable.getColumnModel().getColumn(6).setPreferredWidth(70); // Assists
        playersTable.getColumnModel().getColumn(7).setPreferredWidth(70); // Steals
        playersTable.getColumnModel().getColumn(8).setPreferredWidth(70); // Blocks

        // Set custom renderer for all columns
        for (int i = 0; i < playersTable.getColumnCount(); i++) {
            playersTable.getColumnModel().getColumn(i).setCellRenderer(new CustomCellRenderer());
        }
        scroll.setViewportView(playersTable);
    }

    //customizes the appearance of JTable cells, adjusting font and colors based on selection
    static class CustomCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel(" " + value.toString());
            label.setFont(new Font("Serif", Font.PLAIN, getFont().getSize()));
            label.setOpaque(true);
            if (isSelected) {
                label.setBackground(table.getSelectionBackground());
                label.setForeground(table.getSelectionForeground());
            } else {
                label.setBackground(table.getBackground());
                label.setForeground(table.getForeground());
            }
            return label;
        }
    }

    //adds mouse listener to table
    private void addMouseFunction() {
        playersTable.addMouseListener(new MouseAdapter() {
            //method for handling row clicks
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = playersTable.convertRowIndexToModel(playersTable.getSelectedRow());
                    if (!(row < 0)) {
                        handleRowClick(row);
                    }
                }
            }
        });
    }

    //handles logic for adding players
    private void handleRowClick(int row) {
        Player player = getPlayerFromRow(row);

        if (checkPlayer(player)) {
            JOptionPane.showMessageDialog(null, "Player already in team.");
            return;
        }

        String message = "Add Player?";
        String[] options = {"Yes", "No"};
        int response = JOptionPane.showOptionDialog(null, message, "Player addition", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
        if (response == 0) {
            if (!checkSize())
                JOptionPane.showMessageDialog(null, "Team size will exceed cap. \nPlayer addition cancelled.");
            else {
                JFormattedTextField salaryField = setTextField();
                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Please enter player's salary: "));
                panel.add(salaryField);

                int input = JOptionPane.showConfirmDialog(null, panel, "Player Salary", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (input == JOptionPane.OK_OPTION) {
                    double salary = ((Number) salaryField.getValue()).doubleValue();
                    String salary_check = "Salary is under minimum. Players with more than 20.0 points per game have minimum salary of 3000.";
                    salary_check += "Players with less points have minimum salary of 1000.";
                    salary_check += "Your current player has " + player.getPoints() + " points.";

                    if (checkPlayerSalary(player, salary))
                        JOptionPane.showMessageDialog(null, salary_check);
                    else {
                        if (!checkTeamSalary(salary))
                            JOptionPane.showMessageDialog(null, "This player's salary will exceed the team salary cap.");
                        else {
                            player.setSalary(salary);
                            teamRepository.save(player);

                            teamPlayers.add(player); //to update current cached team players
                            marketPlayers.remove(player); //to update current cached market players
                            DefaultTableModel model = (DefaultTableModel) playersTable.getModel();
                            model.removeRow(row);

                            JOptionPane.showMessageDialog(null, "Player has been added to team.");
                            teamPlayers = teamRepository.findAll();
                        }
                    }
                }
            }
        }
    }

    //return row number of searched player
    private Player getPlayerFromRow(int row) {
        DefaultTableModel model = (DefaultTableModel) playersTable.getModel();
        String playerName = (String) model.getValueAt(row, 0);
        return playerMap.get(playerName); // Retrieve player from the map
    }

    //method for highlighting searched player
    private void scrollToPlayerByName(String playerName) {
        DefaultTableModel model = (DefaultTableModel) playersTable.getModel();
        for (int row = 0; row < model.getRowCount(); row++) {
            Player player = getPlayerFromRow(row);
            if (player.getFullName().toLowerCase().contains(playerName.toLowerCase())) {
                playersTable.setRowSelectionInterval(row, row);
                return;
            }
        }
//        JOptionPane.showMessageDialog(panelMain, "Player with name " + playerName + " not found.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    //creates an input field that only accepts numbers
    private JFormattedTextField setTextField() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setMinimum(0);
        numberFormatter.setMaximum(20000);
        numberFormatter.setAllowsInvalid(true);
        numberFormatter.setCommitsOnValidEdit(true);
        JFormattedTextField input = new JFormattedTextField(numberFormatter);
        input.setColumns(5);
        return input;
    }

    //checks if player already exists in team pool
    private boolean checkPlayer(Player player) {
        Player check = teamRepository.findById(player.getPlayerId());
        return check != null;
    }

    //checks total salary of team plus selected player
    private boolean checkTeamSalary(double Salary) {
        System.out.println(Salary);
        double totalsalary = 0;
        for (Player player : teamPlayers) {
            totalsalary += player.getSalary();
        }
        totalsalary += Salary;
        System.out.println(totalsalary);
        return totalsalary <= 20000.0;
    }

    //checks player salary to see if it fits the requirement
    private boolean checkPlayerSalary(Player player, double Salary) {
        double points = player.getPoints();
        if (points > 20.0) {
            return Salary < 3000.0;
        } else return Salary < 1000.0;
    }

    private boolean checkSize() {
        return teamPlayers.size() < 15;
    }

    //sets the frame to be displayable
    public void displayForm() {
        frame = new JFrame("AddPlayer");
        frame.setContentPane(panelMain);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}