package com.group.NBAGManager;

import com.group.NBAGManager.model.Player;
import com.group.NBAGManager.model.RepositoryHandler;
import com.group.NBAGManager.repository.TeamRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class App {
    private JButton addPlayerBtn;
    private JPanel panelMain;
    private JButton removeButton;
    private JButton injuriesButton;
    private JButton contractButton;
    private JButton rankingButton;
    private JButton journeyButton;
    private JButton filteredSearchButton;
    private JButton resetButton;
    private JLabel heading;
    private JScrollPane scroll;
    private JTable displayTable;
    private JFrame frame;
    private TeamRepository teamRepository;

    public App() {
        teamRepository = RepositoryHandler.getInstance().getTeamRepository();

        addPlayerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AddPlayer addPlayer = new AddPlayer();
                addPlayer.displayForm();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RemovePlayer removePlayer = new RemovePlayer();
                removePlayer.displayForm();
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
                frame.dispose();
                Filter filter = new Filter();
                filter.displayForm();
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new App().displayForm();
            }
        });
    }

    public void displayForm() {
        resetButton.setVisible(false);
        displayForm(teamRepository.findAll());
    }

    public void displayForm(List<Player> players) {
        frame = new JFrame("App");
        frame.setContentPane(panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        displayPlayers(players);
    }

    private void displayPlayers(List<Player> players) {
        String[] columnNames = {"Name", "Age", "Height", "Weight", "Position", "Salary", "Points", "Rebounds", "Assists", "Steals", "Blocks"};
        Object[][] data = new Object[players.size()][columnNames.length];

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            data[i][0] = player.getFirstName() + " " + player.getLastName();
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
        }

        TableModel dataModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                    case 4:
                        return String.class;
                    case 1:
                        return Integer.class;
                    case 2:
                    case 3:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        return Double.class;
                    default:
                        return Object.class;
                }
            }
        };

        displayTable.setModel(dataModel);
        displayTable.setRowSorter(new TableRowSorter<>(dataModel));

        // Adjust column widths
        displayTable.getColumnModel().getColumn(0).setPreferredWidth(170); // Name
        displayTable.getColumnModel().getColumn(1).setPreferredWidth(60);  // Age
        displayTable.getColumnModel().getColumn(2).setPreferredWidth(60);  // Height
        displayTable.getColumnModel().getColumn(3).setPreferredWidth(60);  // Weight
        displayTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Position
        displayTable.getColumnModel().getColumn(5).setPreferredWidth(70); // Salary
        displayTable.getColumnModel().getColumn(6).setPreferredWidth(70); // Points
        displayTable.getColumnModel().getColumn(7).setPreferredWidth(70); // Rebounds
        displayTable.getColumnModel().getColumn(8).setPreferredWidth(70); // Assists
        displayTable.getColumnModel().getColumn(9).setPreferredWidth(70); // Steals
        displayTable.getColumnModel().getColumn(10).setPreferredWidth(70); // Blocks

        // Set custom renderer for all columns
        for (int i = 0; i < displayTable.getColumnCount(); i++) {
            displayTable.getColumnModel().getColumn(i).setCellRenderer(new CustomCellRenderer());
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

    public static void main(String[] args) {
        App app = new App();
        app.displayForm();
    }
}
