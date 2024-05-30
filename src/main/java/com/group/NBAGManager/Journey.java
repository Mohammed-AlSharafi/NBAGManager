package com.group.NBAGManager;

import com.group.NBAGManager.components.RoundedButton;
import com.group.NBAGManager.model.CurrentSession;
import com.group.NBAGManager.model.Graph.JourneyGraphHelper;
import com.group.NBAGManager.model.Graph.WeightedGraph;
import com.group.NBAGManager.model.RepositoryHandler;
import com.group.NBAGManager.model.User;
import com.group.NBAGManager.repository.UserRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class Journey {
    User currentUser = CurrentSession.getInstance().getLoggedInUser();
    UserRepository userRepository = RepositoryHandler.getInstance().getUserRepository();
    WeightedGraph<String, Integer> journeyGraph = new WeightedGraph<>();
    List<String> shortestPath;
    private JPanel panelMain;
    private JButton getNextLocationBtn;
    private JLabel nextLocation;
    private JComboBox<String> currentLocation;
    private JTextArea bestRoute;
    private JLabel totalDistanceLbl;

    public Journey(){
        //populating the graph
        JourneyGraphHelper.populateGraph(journeyGraph);

        //finding the shortest path
        shortestPath = journeyGraph.findShortestPathVisitingAllVertices("San Antonio");

        //adding vertices currentLocation to the combo box
        journeyGraph.getAllVertexObjects().forEach(vertex->{
            currentLocation.addItem(vertex);
        });

        //initializing currentLocation (if exists)
        if(CurrentSession.getInstance().getLoggedInUser().getLocation()!=null) {
            currentLocation.setSelectedItem(CurrentSession.getInstance().getLoggedInUser().getLocation());
        }

        //initializing the next location
        nextLocation.setFont(nextLocation.getFont().deriveFont(30.0f));
        setNextLocationField();

        //setting the best route in the bestRoute text area
        bestRoute.setFont(bestRoute.getFont().deriveFont(Font.BOLD));
        StringBuilder shortestPathSB = new StringBuilder();
        for(int i = 0; i < shortestPath.size(); i++){
            shortestPathSB.append((i + 1))
                    .append(". ").append(shortestPath.get(i))
                    .append(" (")
                    .append(journeyGraph.getVertex(shortestPath.get(i)).getExtraInfo())
                    .append(")\n");
        }
        bestRoute.setText(shortestPathSB.toString());

        //setting the total distance
        totalDistanceLbl.setText("Total Distance: " + journeyGraph.getTotalDistance(shortestPath) + " KMs");

        //getRouteBtn action listener
        getNextLocationBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                int currentIndex = shortestPath.indexOf(currentLocation.getSelectedItem());
                if (currentIndex < shortestPath.size() - 1) {
                    currentLocation.setSelectedItem(shortestPath.get(currentIndex + 1));
                }
            }
        });

        //currentLocation combo box action listener
        currentLocation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                currentUser.setLocation(currentLocation.getSelectedItem().toString());
                setNextLocationField();
            }
        });
    }
    public void displayForm(){

        //setting initial buttons state
        getNextLocationBtn.setEnabled(false);
        currentLocation.setEnabled(false);

        JFrame frame = new JFrame("Journey");
        frame.setContentPane(new Journey().panelMain);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1250,700);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if(userRepository != null) {
                    userRepository.update(currentUser);
                }
            }
        });


        //        //creating a graph
//        try {
//            //adding vertices from VertexRepository
//            VertexRepository vertexRepository = new VertexRepository();
//            vertexRepository.findAll().forEach(vertex->{
//                journeyGraph.addVertex(vertex.getVertexInfo(), vertex.getExtraInfo());
//            });
//
//            vertexRepository.close();
//
//            //adding edges from EdgeRepository
//            EdgeRepository edgeRepository = new EdgeRepository(journeyGraph);
//            edgeRepository.addAllEdgesToGraph(journeyGraph);
//            edgeRepository.close();
//
//            System.out.println(journeyGraph.getAllVertexObjects());
//        }catch (RuntimeException e) {
//            JOptionPane.showMessageDialog(null, "Error while communicating with the database.", "Error", JOptionPane.ERROR_MESSAGE);
//        }

    }
    public void setNextLocationField(){
        int nextLocationIndex = shortestPath.indexOf(currentLocation.getSelectedItem()) + 1;
        nextLocation.setText(shortestPath.get(nextLocationIndex));
        nextLocation.revalidate();
        nextLocation.repaint();
    }

    private void createUIComponents() {
        // Create a new RoundedButton
        RoundedButton roundedButton = new RoundedButton("Next Location");

        // Set the button's properties
        roundedButton.setPreferredSize(new Dimension(150, 30));

        // Set the custom button to the getNextLocationBtn field
        getNextLocationBtn = roundedButton;
    }
}

