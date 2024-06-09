package com.group.NBAGManager;

import com.group.NBAGManager.InjuryReserveManagement.InjuryReserveManagement;
import com.group.NBAGManager.components.RoundedButton;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
    List<Player> allPlayers;

    public App() {
        teamRepository = RepositoryHandler.getInstance().getTeamRepository();

        addPlayerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AddPlayer addPlayer = new AddPlayer();
                addPlayer.displayForm();

                // Add a window listener to update the table when the add player window is closed
                addPlayer.getFrame().addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        displayPlayers(teamRepository.findAll());
                    }
                });
            }
        });

        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RemovePlayer removePlayer = new RemovePlayer();
                removePlayer.displayForm();

                // Add a window listener to update the table when the remove player window is closed
                removePlayer.getFrame().addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        displayPlayers(teamRepository.findAll());
                    }
                });
            }
        });
        injuriesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InjuryReserveManagement injury = new InjuryReserveManagement();
                injury.displayForm();
            }
        });
        contractButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ContractExtensionQueue contract = new ContractExtensionQueue();
                contract.displayForm();
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
                Filter filter = new Filter();
                filter.displayForm();

                filter.getFrame().addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        List<Player> filteredPlayers = filter.getFilteredPlayers();
                        if(filteredPlayers != allPlayers) {
                            resetButton.setVisible(true);
                            displayPlayers(filteredPlayers);
                        }else {
                            resetButton.setVisible(false);
                        }
                    }
                });
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayPlayers(allPlayers);
                resetButton.setVisible(false);
            }
        });
    }

    public void displayForm() {
        resetButton.setVisible(false);
        allPlayers = teamRepository.findAll();
        displayForm(allPlayers);
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
        String[] columnNames = {"Name", "Height", "Weight", "Position", "Salary", "Points", "Rebounds", "Assists", "Steals", "Blocks"};
        Object[][] data = new Object[players.size()][columnNames.length];

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            data[i][0] = player.getFirstName() + " " + player.getLastName();
            data[i][1] = player.getHeight();
            data[i][2] = player.getWeight();
            data[i][3] = player.getPosition();
            data[i][4] = player.getSalary();
            data[i][5] = player.getPoints();
            data[i][6] = player.getRebounds();
            data[i][7] = player.getAssists();
            data[i][8] = player.getSteals();
            data[i][9] = player.getBlocks();
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
        displayTable.getColumnModel().getColumn(1).setPreferredWidth(60);  // Height
        displayTable.getColumnModel().getColumn(2).setPreferredWidth(60);  // Weight
        displayTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Position
        displayTable.getColumnModel().getColumn(4).setPreferredWidth(70); // Salary
        displayTable.getColumnModel().getColumn(5).setPreferredWidth(70); // Points
        displayTable.getColumnModel().getColumn(6).setPreferredWidth(70); // Rebounds
        displayTable.getColumnModel().getColumn(7).setPreferredWidth(70); // Assists
        displayTable.getColumnModel().getColumn(8).setPreferredWidth(70); // Steals
        displayTable.getColumnModel().getColumn(9).setPreferredWidth(70); // Blocks

        // Set custom renderer for all columns
        for (int i = 0; i < displayTable.getColumnCount(); i++) {
            displayTable.getColumnModel().getColumn(i).setCellRenderer(new CustomCellRenderer());
        }
    }

    private void createUIComponents() {
        addPlayerBtn = new RoundedButton("Add Player");
        removeButton = new RoundedButton("Remove Player");
        injuriesButton = new RoundedButton("Injuries");
        contractButton = new RoundedButton("Contract");
        rankingButton = new RoundedButton("Ranking");
        journeyButton = new RoundedButton("Journey");
        filteredSearchButton = new RoundedButton("Filtered Search");
        resetButton = new RoundedButton("Reset");
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
}
