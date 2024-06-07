package com.group.NBAGManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.group.NBAGManager.model.Player;
import com.group.NBAGManager.repository.TeamRepository;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Map;

public class RemovePlayer {
    private JPanel panelMain;
    private JScrollPane scrollPane;
    private JTable table1;
    TeamRepository repo;
    private List<Player> list;
    private Map<String, Player> playerMap2 = new HashMap<>();

    public RemovePlayer(){
        panelMain.setLayout(new BorderLayout());
        repo = new TeamRepository();
        //returns the value of component being resized
        table1.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                System.out.println(table1.getSelectedRow());
            }
        });
        displayPlayers();
        addMouseListenerToTable();
        panelMain.add(scrollPane, BorderLayout.CENTER);
    }

    private void displayPlayers() {
        list = repo.findAll();
        String[] columnNames = {"Name", "Age", "Height", "Weight", "Position", "Salary", "Points", "Rebounds", "Assists", "Steals", "Blocks"};
        Object[][] data = new Object[list.size()][columnNames.length];

        for (int i = 0; i < list.size(); i++) {
            Player player = list.get(i);
            String playername = player.getFirstName() + " " + player.getLastName();
            data[i][0] = playername;
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
            playerMap2.put(playername,player);
        }

        TableModel dataModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0, 4 -> String.class;
                    case 1 -> Integer.class;
                    case 2, 3, 5, 6, 7, 8, 9, 10 -> Double.class;
                    default -> Object.class;
                };
            }
        };

        table1.setModel(dataModel);
        table1.setRowSorter(new TableRowSorter<>(dataModel));

        // Adjust column widths
        table1.getColumnModel().getColumn(0).setPreferredWidth(170); // Name
        table1.getColumnModel().getColumn(1).setPreferredWidth(60);  // Age
        table1.getColumnModel().getColumn(2).setPreferredWidth(60);  // Height
        table1.getColumnModel().getColumn(3).setPreferredWidth(60);  // Weight
        table1.getColumnModel().getColumn(4).setPreferredWidth(100); // Position
        table1.getColumnModel().getColumn(5).setPreferredWidth(70); // Salary
        table1.getColumnModel().getColumn(6).setPreferredWidth(70); // Points
        table1.getColumnModel().getColumn(7).setPreferredWidth(70); // Rebounds
        table1.getColumnModel().getColumn(8).setPreferredWidth(70); // Assists
        table1.getColumnModel().getColumn(9).setPreferredWidth(70); // Steals
        table1.getColumnModel().getColumn(10).setPreferredWidth(70); // Blocks

        // Set custom renderer for all columns
        for (int i = 0; i < table1.getColumnCount(); i++) {
            table1.getColumnModel().getColumn(i).setCellRenderer(new CustomCellRenderer());
        }
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

    public void displayForm(){
        JFrame frame = new JFrame("RemovePlayer");
        frame.setContentPane(new RemovePlayer().panelMain);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1250,600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void addMouseListenerToTable(){
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    int row = table1.rowAtPoint(e.getPoint());
                    int column = table1.columnAtPoint(e.getPoint());
                    if(column==0&&!(row<0)){
                        String playername = (String) table1.getValueAt(row,column);
                        Player player = playerMap2.get(playername);
                        handleRowClick(row, player);
                    }
                }
                }
        });
    }

    private void handleRowClick(int row, Player player){
        String message = "Remove Player?";
        String[] options = {"Yes","No"};
        int response = JOptionPane.showOptionDialog(null,message,"Player Removal",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE, null, options, null);
        if(response == 0){
            if(list.size()<10){
                repo.deleteById(player.getPlayerId());
                JOptionPane.showMessageDialog(null, player.getFirstName()+" "+player.getLastName()+" has been removed.");
                DefaultTableModel model = (DefaultTableModel) table1.getModel();
                model.removeRow(row);
            }
            else if(list.size()==10)JOptionPane.showMessageDialog(null,"Team size is less than minimum.");
            else if(checkTeamPositions(player)<2){
                String position = player.getPosition();
                if(position.equals("Center"))JOptionPane.showMessageDialog(null,"The number of Centers in your team is less than the minimum(2).");
                else if(position.equals("Guard"))JOptionPane.showMessageDialog(null,"The number of Guards in your team is less than the minimum(2).");
                else JOptionPane.showMessageDialog(null,"The number of Forwards in your team is less than the minimum(2).");
            }
            else{
                repo.deleteById(player.getPlayerId());
                JOptionPane.showMessageDialog(null, player.getFirstName()+" "+player.getLastName()+" has been removed.");
                DefaultTableModel model = (DefaultTableModel) table1.getModel();
                model.removeRow(row);
            }
        }
    }

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
