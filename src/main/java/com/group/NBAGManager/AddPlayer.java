package com.group.NBAGManager;

import com.group.NBAGManager.model.Player;
import com.group.NBAGManager.model.RepositoryHandler;
import com.group.NBAGManager.repository.PlayerRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class AddPlayer {
    private JPanel panelMain;
    PlayerRepository playerRepository = RepositoryHandler.getInstance().getPlayerRepository();
    private JTable playersTable;

    public AddPlayer() {
        playersTable.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                System.out.println(playersTable.getSelectedRow());
            }
        });
        List<Player> list = playerRepository.findAll();
        String[] columnNames = {"Name", "Profile", "Average Statistics Per Game"};
        Object[][] data = new Object[list.size()][3];

        for (int i = 0; i < list.size(); i++) {
            Player player = list.get(i);
            data[i][0] = player; // Store the player object directly
            data[i][1] = "<html>" + displayProfile(player).replaceAll(", ", "<br>") + "</html>";
            data[i][2] = "<html>" + displayStats(player).replaceAll(", ", "<br>") + "</html>";
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Player.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        playersTable.setModel(model);
    }

    public void displayForm(){
        JFrame frame = new JFrame("AddPlayer");
        frame.setContentPane(new AddPlayer().panelMain);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1250,700);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if(playerRepository != null) {
                    playerRepository.close();
                }
            }
        });
    }
    public String displayProfile(Player player){
        String [] profiles = {"Age","Height","Weight","Position","Salary"};
        String [] data = {""+player.getAge(),""+player.getHeight(),""+player.getWeight(),player.getPosition(),""+player.getSalary()};
        String profile = "";
        for (int i = 0;i<profiles.length;i++) {
            profile += profiles[i]+": "+data[i];
            if(i!=profiles.length-1) profile+=", ";
        }
        return profile;
    }

    public String displayStats(Player player){
        String [] stats = {"Points","Rebounds","Assists","Steals","Blocks"};
        String [] data = {""+player.getPoints(),""+player.getRebounds(),""+player.getAssists(),""+player.getSteals(),""+player.getBlocks()};
        String stat = "";
        for (int i = 0;i<stats.length;i++) {
            stat += stats[i]+": "+data[i];
            if(i!=stats.length-1) stat+=", ";
        }
        return stat;
    }

    public static void main(String[] args) {
        AddPlayer addPlayer = new AddPlayer();
        addPlayer.displayForm();
    }
}
