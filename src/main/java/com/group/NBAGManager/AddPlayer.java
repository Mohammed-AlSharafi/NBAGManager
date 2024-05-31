package com.group.NBAGManager;

import com.group.NBAGManager.model.CurrentSession;
import com.group.NBAGManager.model.Player;
import com.group.NBAGManager.model.RepositoryHandler;
import com.group.NBAGManager.model.User;
import com.group.NBAGManager.repository.PlayerRepository;
import com.group.NBAGManager.repository.TeamRepository;
import com.group.NBAGManager.repository.UserRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.List;

public class AddPlayer {
    private JPanel panelMain;
    PlayerRepository playerRepository = RepositoryHandler.getInstance().getPlayerRepository();
    private JTable playersTable;
    private JScrollPane scroll;
    List<Player> team;
    TeamRepository repo;


    public AddPlayer() {
        UserRepository userRepository=RepositoryHandler.getInstance().getUserRepository();
        User currentUser = userRepository.findUserByUsername("testUser");
        CurrentSession.getInstance().setLoggedInUser(currentUser);
        repo = new TeamRepository();
        team = repo.findAll();
        playersTable.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                System.out.println(playersTable.getSelectedRow());
            }
        });
        setPlayersTable();
        tableFormatting();
        addMouseFunction();
    }

    private void setPlayersTable(){
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
        playersTable.setRowHeight(100);
    }

    public void tableFormatting(){
        playersTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
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
        playersTable.getColumnModel().getColumn(1).setCellRenderer(textRenderer);
        playersTable.getColumnModel().getColumn(2).setCellRenderer(textRenderer);
        playersTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        playersTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        playersTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        scroll.setViewportView(playersTable);
    }

    private void addMouseFunction(){
        playersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = playersTable.rowAtPoint(e.getPoint());
                int column = playersTable.columnAtPoint(e.getPoint());
                if(column==0&&!(row<0)){
                    Player player = (Player) playersTable.getValueAt(row,column);
                    handleRowClick(player);
                }
            }
        });
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
                    String salary_check = "Salary is under minimum. Players with more than 20.0 points per game have minimum salary.";
                    salary_check+="Players with less points have minimum salary of 1000.";
                    salary_check+="Your current player has "+player.getPoints()+" points.";
                    if(checkPlayerSalary(player,salary))JOptionPane.showMessageDialog(null,salary_check);
                    else if(!checkTeamSalary(salary)) JOptionPane.showMessageDialog(null,"This player's salary will exceed the team salary cap.");
                    else{
                        player.setSalary(salary);
                        repo.save(player);
                        JOptionPane.showMessageDialog(null,"Player has been added to team.");
                    }
                }
            }
        }
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
        Player check = repo.findById(player.getPlayerId());
        return check!=null;
    }

    private boolean checkTeamSalary(double Salary){
        double salary = 0;
        for(Player player : team){
            salary += player.getSalary();
        }
        salary+=Salary;
        return salary <= 20000;
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

