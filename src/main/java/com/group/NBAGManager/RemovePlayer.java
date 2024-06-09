package com.group.NBAGManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.group.NBAGManager.components.RoundedButton;
import com.group.NBAGManager.model.Player;
import com.group.NBAGManager.model.RepositoryHandler;
import com.group.NBAGManager.repository.TeamRepository;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

public class RemovePlayer {
    private JFrame frame;
    private JPanel panelMain;
    private JButton backButton;
    private JScrollPane scrollPanel;
    private JTable playersTable;
    private JTextField searchField;
    TeamRepository repo;
    private List<Player> list;
    private Map<String, Player> playerMap = new HashMap<>();

    public RemovePlayer(){
        //get instance of team repository for team pool
        repo = RepositoryHandler.getInstance().getTeamRepository();

        displayPlayers();/*creates the table for the Free Agent Market*/
        addMouseListenerToTable();/*adds a mouse listener to the table*/

        //handles search bar function
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

    //method for highlighting searched player
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

    //return row number of searched player
    private Player getPlayerFromRow(int row) {
        DefaultTableModel model = (DefaultTableModel) playersTable.getModel();
        String playerName = (String) model.getValueAt(row, 0);
        return playerMap.get(playerName); // Retrieve player from the map
    }

    //method for displaying Free Agent Market
    private void displayPlayers() {
        list = repo.findAll();
        String[] columnNames = {"Name", "Height", "Weight", "Position", "Salary", "Points", "Rebounds", "Assists", "Steals", "Blocks"};
        Object[][] data = new Object[list.size()][columnNames.length];

        for (int i = 0; i < list.size(); i++) {
            Player player = list.get(i);
            String playername = player.getFirstName() + " " + player.getLastName();
            data[i][0] = playername;
            data[i][1] = player.getHeight();
            data[i][2] = player.getWeight();
            data[i][3] = player.getPosition();
            data[i][4] = player.getSalary();
            data[i][5] = player.getPoints();
            data[i][6] = player.getRebounds();
            data[i][7] = player.getAssists();
            data[i][8] = player.getSteals();
            data[i][9] = player.getBlocks();
            playerMap.put(playername,player);
        }

        //format for table
        TableModel dataModel = new DefaultTableModel(data, columnNames) {
            //cells are not editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            //method to modify returned value for certain columns
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0, 4 -> String.class;
                    case 1 -> Integer.class;
                    case 2, 3, 5, 6, 7, 8, 9 -> Double.class;
                    default -> Object.class;
                };
            }
        };

        //sets the model of the table as dataModel
        playersTable.setModel(dataModel);
        playersTable.setRowSorter(new TableRowSorter<>(dataModel));

        // Adjust column widths
        playersTable.getColumnModel().getColumn(0).setPreferredWidth(170); // Name
        playersTable.getColumnModel().getColumn(1).setPreferredWidth(60);  // Height
        playersTable.getColumnModel().getColumn(2).setPreferredWidth(60);  // Weight
        playersTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Position
        playersTable.getColumnModel().getColumn(4).setPreferredWidth(70); // Salary
        playersTable.getColumnModel().getColumn(5).setPreferredWidth(70); // Points
        playersTable.getColumnModel().getColumn(6).setPreferredWidth(70); // Rebounds
        playersTable.getColumnModel().getColumn(7).setPreferredWidth(70); // Assists
        playersTable.getColumnModel().getColumn(8).setPreferredWidth(70); // Steals
        playersTable.getColumnModel().getColumn(9).setPreferredWidth(70); // Blocks

        // Set custom renderer for all columns
        for (int i = 0; i < playersTable.getColumnCount(); i++) {
            playersTable.getColumnModel().getColumn(i).setCellRenderer(new CustomCellRenderer());
        }
    }

    private void createUIComponents() {
        backButton = new RoundedButton("Back");
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

    //sets the frame to be displayable
    public void displayForm(){
        frame = new JFrame("RemovePlayer");
        frame.setContentPane(panelMain);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000,500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    //creates mouse listener for the table
    private void addMouseListenerToTable(){
        playersTable.addMouseListener(new MouseAdapter() {
            //method for handling row clicks
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    int row = playersTable.rowAtPoint(e.getPoint());
                    if(!(row<0)){
                        String playerName = (String) playersTable.getValueAt(row,0);
                        Player player = playerMap.get(playerName);
                        handleRowClick(row, player);
                    }
                }
                }
        });
    }

    //Handles logic for removing players
    private void handleRowClick(int row, Player player){
        list = repo.findAll();
        String message = "Remove Player?";
        String[] options = {"Yes","No"};
        int response = JOptionPane.showOptionDialog(null,message,"Player Removal",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE, null, options, null);
        if(response == 0){
            if(list.size()<10){
                repo.deleteById(player.getPlayerId());
                JOptionPane.showMessageDialog(null, player.getFirstName()+" "+player.getLastName()+" has been removed.");
                DefaultTableModel model = (DefaultTableModel) playersTable.getModel();
                model.removeRow(row);
            }
            else if(list.size()==10)JOptionPane.showMessageDialog(null,"Team size is less than minimum.");
            else if(checkTeamPositions(player)<=2){
                String position = player.getPosition();
                if(position.equals("Center"))JOptionPane.showMessageDialog(null,"The number of Centers in your team will be less than the minimum(2).");
                else if(position.equals("Guard"))JOptionPane.showMessageDialog(null,"The number of Guards in your team will be less than the minimum(2).");
                else JOptionPane.showMessageDialog(null,"The number of Forwards in your team will be less than the minimum(2).");
            }
            else{
                repo.deleteById(player.getPlayerId());
                JOptionPane.showMessageDialog(null, player.getFirstName()+" "+player.getLastName()+" has been removed.");
                DefaultTableModel model = (DefaultTableModel) playersTable.getModel();
                model.removeRow(row);
            }
        }
    }

    //checks the number of certain player position in a team
    private int checkTeamPositions(Player player){
        String role = player.getPosition();
        int count = 0;
        for(Player players:list){
            if(players.getPosition().equals(role)){
                count+=1;
            }
        }
        return count;
    }
}
