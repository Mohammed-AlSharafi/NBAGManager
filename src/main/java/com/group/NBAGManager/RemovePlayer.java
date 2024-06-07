package com.group.NBAGManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.group.NBAGManager.model.CurrentSession;
import com.group.NBAGManager.model.Player;
import com.group.NBAGManager.model.User;
import com.group.NBAGManager.repository.TeamRepository;
import com.group.NBAGManager.model.RepositoryHandler;
import com.group.NBAGManager.repository.UserRepository;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class RemovePlayer {
    private JPanel panelMain;
    private JScrollPane scrollPane;
    private JTable table1;
    TeamRepository repo;
    private List<Player> list;
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
        setTable();
        tableFormatting();
        addMouseListenerToTable();
        panelMain.add(scrollPane, BorderLayout.CENTER);
    }

    public void setTable(){
            list = repo.findAll();
            String[] columnNames = {"Name", "Profile", "Average Statistics Per Game"};
            Object[][] data = new Object[list.size()][3];
            for (int i = 0; i < list.size(); i++) {
                Player player = list.get(i);
                data[i][0] = player; // Store the player object directly
                data[i][1] = formatHtml(displayProfile(player));
                data[i][2] = formatHtml(displayStats(player));
            }

            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                // Gets column class for each column
                public Class<?> getColumnClass(int column) {
                    return column == 0 ? Player.class : String.class;
                }

                @Override
                // Cells are not editable
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            table1.setModel(model);
            table1.setRowHeight(100);
        }

        private String formatHtml(String content) {
            return "<html>" + content.replaceAll(", ", "<br>") + "</html>";
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

    public void tableFormatting(){
        table1.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            //formats first column to fit in name and image of player
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Player player) {
                    String name = player.getFirstName() + " " + player.getLastName();
                    ImageIcon imageIcon = new ImageIcon("C:/Users/User/Documents/Uni/Semester 2/WIA1002/Lebron.jpg"); // Adjust path to image
                    Image image = imageIcon.getImage();
                    Image resizedImage = image.getScaledInstance(50, 70, Image.SCALE_SMOOTH);
                    ImageIcon resizedIcon = new ImageIcon(resizedImage);
                    JLabel label = new JLabel("<html>" + name + "<br></html>", resizedIcon, JLabel.CENTER);
                    label.setHorizontalTextPosition(JLabel.CENTER);
                    label.setVerticalTextPosition(JLabel.BOTTOM);
                    return label;
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });

        // Custom renderer for text columns to support HTML formatting
        //breaks into new line for each stat
        DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    label.setText((String) value);
                }
                return label;
            }
        };
        table1.getColumnModel().getColumn(1).setCellRenderer(textRenderer);
        table1.getColumnModel().getColumn(2).setCellRenderer(textRenderer);
        table1.getColumnModel().getColumn(0).setPreferredWidth(100);
        table1.getColumnModel().getColumn(1).setPreferredWidth(150);
        table1.getColumnModel().getColumn(2).setPreferredWidth(200);
        scrollPane.setViewportView(table1);
    }

    private void addMouseListenerToTable(){
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    int row = table1.rowAtPoint(e.getPoint());
                    int column = table1.columnAtPoint(e.getPoint());
                    if(column==0&&!(row<0)){
                        Player player = (Player) table1.getValueAt(row,column);
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
                JOptionPane.showConfirmDialog(null, player.getFirstName()+" "+player.getLastName()+" has been removed.");
                DefaultTableModel model = (DefaultTableModel) table1.getModel();
                model.removeRow(row);
            }
            if(list.size()==10)JOptionPane.showMessageDialog(null,"Team size is less than minimum.");
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

    private String displayProfile(Player player) {
        String[] profiles = {"Age", "Height", "Weight", "Position", "Salary"};
        String[] data = {"" + player.getAge(), "" + player.getHeight(), "" + player.getWeight(), player.getPosition(), "" + player.getSalary()};
        StringBuilder profile = new StringBuilder();
        for (int i = 0; i < profiles.length; i++) {
            profile.append(profiles[i]).append(": ").append(data[i]);
            if (i != profiles.length - 1) profile.append(", ");
        }
        return profile.toString();
    }

    private String displayStats(Player player) {
        String[] stats = {"Points", "Rebounds", "Assists", "Steals", "Blocks"};
        String[] data = {"" + player.getPoints(), "" + player.getRebounds(), "" + player.getAssists(), "" + player.getSteals(), "" + player.getBlocks()};
        StringBuilder stat = new StringBuilder();
        for (int i = 0; i < stats.length; i++) {
            stat.append(stats[i]).append(": ").append(data[i]);
            if (i != stats.length - 1) stat.append(", ");
        }
        return stat.toString();
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

    public void displayFull(){
        displayForm();
    }
}
