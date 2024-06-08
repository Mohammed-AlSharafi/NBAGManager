package com.group.NBAGManager;

import com.group.NBAGManager.model.*;
import com.group.NBAGManager.repository.PlayerRepository;
import com.group.NBAGManager.repository.TeamRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class AddPlayer {
    private JPanel panelMain;
    private JButton SearchId;
    private JButton SearchName;
    private PlayerRepository playerRepository;
    private JTable playersTable;
    private JScrollPane scroll;
    private JTextField searchField;
    List<Player> team;
    public TeamRepository teamRepository;
    private Map<String, Player> playerMap = new HashMap<>();

    public AddPlayer() {
        teamRepository = RepositoryHandler.getInstance().getTeamRepository();
        playerRepository = RepositoryHandler.getInstance().getPlayerRepository();
        team = teamRepository.findAll();
        playersTable.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                System.out.println(playersTable.getSelectedRow());
            }
        });
        setPlayersTable();
        addMouseFunction();
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchText = searchField.getText();
                if (!searchText.isEmpty()) {
                    scrollToPlayerByName(searchText);
                }
            }
        });
    }

    private void setPlayersTable(){
        List<Player> teamPlayers = teamRepository.findAll();
        List<Player> marketPlayers = playerRepository.findAll();
        ArrayList<Player> unOwnedPlayers = new ArrayList<>(marketPlayers);
        unOwnedPlayers.removeAll(teamPlayers);
        unOwnedPlayers.forEach(player->{
            System.out.println(player.getPlayerId());
        });

        String[] columnNames = {
                "Name", "Age", "Height", "Weight", "Position", "Salary",
                "Points", "Rebounds", "Assists", "Steals", "Blocks"
        };
        Object[][] data = new Object[unOwnedPlayers.size()][columnNames.length];
        playerMap.clear();
        for (int i = 0; i < unOwnedPlayers.size(); i++) {
            Player player = unOwnedPlayers.get(i);
            String playerName = player.getFirstName()+" "+player.getLastName();
            data[i][0] = playerName;
            data[i][1] = player.getAge();
            data[i][2] = player.getHeight();
            data[i][3] = player.getWeight();
            data[i][4] = player.getPosition();
            data[i][5] = player.getSalary();
            data[i][6] = player.getPoints();
            data[i][7] = player.getRebounds();
            data[i][8] = player.getAssists();
            data[i][9] = player.getSteals();
            data[i][10] = player.getBlocks();
            playerMap.put(playerName,player);
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int column){
                return switch (column) {
                    case 0, 4 -> String.class;
                    case 1 -> Integer.class;
                    case 2, 3, 5, 6, 7, 8, 9, 10 -> Double.class;
                    default -> Object.class;
                };
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        playersTable.setModel(model);

    // Adjust column widths
        playersTable.getColumnModel().getColumn(0).setPreferredWidth(170); // Name
        playersTable.getColumnModel().getColumn(1).setPreferredWidth(60);  // Age
        playersTable.getColumnModel().getColumn(2).setPreferredWidth(60);  // Height
        playersTable.getColumnModel().getColumn(3).setPreferredWidth(60);  // Weight
        playersTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Position
        playersTable.getColumnModel().getColumn(5).setPreferredWidth(70); // Salary
        playersTable.getColumnModel().getColumn(6).setPreferredWidth(70); // Points
        playersTable.getColumnModel().getColumn(7).setPreferredWidth(70); // Rebounds
        playersTable.getColumnModel().getColumn(8).setPreferredWidth(70); // Assists
        playersTable.getColumnModel().getColumn(9).setPreferredWidth(70); // Steals
        playersTable.getColumnModel().getColumn(10).setPreferredWidth(70); // Blocks

    // Set custom renderer for all columns
        for (int i = 0; i < playersTable.getColumnCount(); i++) {
        playersTable.getColumnModel().getColumn(i).setCellRenderer(new CustomCellRenderer());
    }
        scroll.setViewportView(playersTable);
}

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

    private void addMouseFunction(){
        playersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    int row = playersTable.rowAtPoint(e.getPoint());
                    int column = playersTable.columnAtPoint(e.getPoint());
                    if(!(row<0)){
                        Player player = getPlayerFromRow(row);
                        handleRowClick(player);
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        CurrentSession.getInstance().setLoggedInUser(RepositoryHandler.getInstance().getUserRepository().findUserByUsername("testUser"));
        AddPlayer addPlayer = new AddPlayer();
        addPlayer.displayForm();
    }

    private void handleRowClick(Player player){
        if(checkPlayer(player)){
            JOptionPane.showMessageDialog(null, "Player already in team.");
            return;
        }
        String message = "Add Player?";
        String[] options = {"Yes","No"};
        int response = JOptionPane.showOptionDialog(null, message, "Player addition", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,null,options,null);
        if(response==0){
            if(!checkSize()) JOptionPane.showMessageDialog(null, "Team size will exceed cap. \nPlayer addition cancelled.");
            else{
                JFormattedTextField salaryfield = setTextField();
                int input = JOptionPane.showConfirmDialog(null, salaryfield, "Please enter player's salary. ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(input == JOptionPane.OK_OPTION){
                    double salary = ((Number)salaryfield.getValue()).doubleValue();
                    String salary_check = "Salary is under minimum. Players with more than 20.0 points per game have minimum salary of 3000.";
                    salary_check+="Players with less points have minimum salary of 1000.";
                    salary_check+="Your current player has "+player.getPoints()+" points.";
                    if(checkPlayerSalary(player,salary))JOptionPane.showMessageDialog(null,salary_check);
                    else{
                        if(!checkTeamSalary(salary)) JOptionPane.showMessageDialog(null,"This player's salary will exceed the team salary cap.");
                        else{
                            player.setSalary(salary);
                            teamRepository.save(player);
                            JOptionPane.showMessageDialog(null,"Player has been added to team.");
                            team = teamRepository.findAll();
                        }
                    }
                }
            }
        }
    }

    private Player getPlayerFromRow(int row) {
        DefaultTableModel model = (DefaultTableModel) playersTable.getModel();
        String playerName = (String) model.getValueAt(row, 0);
        return playerMap.get(playerName); // Retrieve player from the map
    }

    private void scrollToPlayerByName(String playerName) {
        DefaultTableModel model = (DefaultTableModel) playersTable.getModel();
        for (int row = 0; row < model.getRowCount(); row++) {
            Player player = getPlayerFromRow(row);
            if ((player.getFullName()).toLowerCase().contains(playerName.toLowerCase())){
                playersTable.setRowSelectionInterval(row, row);
                return;
            }
        }
//        JOptionPane.showMessageDialog(panelMain, "Player with name " + playerName + " not found.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private JFormattedTextField setTextField(){
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

    private boolean checkPlayer(Player player){
        Player check = teamRepository.findById(player.getPlayerId());
        return check!=null;
    }

    private boolean checkTeamSalary(double Salary){
        System.out.println(Salary);
        double totalsalary = 0;
        for(Player player : team){
            totalsalary += player.getSalary();
        }
        totalsalary+=Salary;
        System.out.println(totalsalary);
        return totalsalary <= 20000.0;
    }

    private boolean checkPlayerSalary(Player player, double Salary){
        double points = player.getPoints();
        if(points>20.0){
            return Salary<3000.0;
        }
        else return Salary<1000.0;
    }

    private boolean checkSize(){
        return team.size()<15;
    }

    public void displayForm(){
        JFrame frame = new JFrame("AddPlayer");
        frame.setContentPane(panelMain);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1250,600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}