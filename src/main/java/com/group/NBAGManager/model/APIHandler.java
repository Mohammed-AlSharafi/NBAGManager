package com.group.NBAGManager.model;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class APIHandler {
    private HashMap<Integer, Player> playerIdHashMap = new HashMap<>();
    private Properties properties = new Properties();

    public APIHandler() {
        String propertiesLocation = "nba.properties";
        try {
            properties.load(new FileInputStream(propertiesLocation));
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Player> getMarketPlayers(){
//        JPanel panel = new JPanel();
//        panel.setLayout(new BorderLayout());

//        JLabel progressLabel = new JLabel("Syncing with database...");
//        JProgressBar progressBar = new JProgressBar();
//        progressBar.setValue(0);
//        progressBar.setStringPainted(true);
//        panel.add(progressLabel,BorderLayout.NORTH);
//        panel.add(progressBar, BorderLayout.SOUTH);
//
        List<Player> allActivePlayers = getActivePlayers();
//
//        progressBar.setValue(50);
//        progressBar.setStringPainted(true);
//
//        JOptionPane optionPane = new JOptionPane(panel, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
//        JDialog dialog = new JDialog(optionPane.createDialog("Progress..."));
//
//        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//        dialog.add(progressLabel);
//        dialog.add(progressBar);
//        dialog.setSize(250, 100);
//        dialog.setModal(true);
//        dialog.pack();
//        dialog.setVisible(true);
//        dialog.setLocationRelativeTo(null);
        List<Player> statusList = getOtherStats(allActivePlayers);
//        progressBar.setValue(100);
//        progressBar.setStringPainted(true);
        return statusList;
    }

    private List<Player> getActivePlayers() {
        int num = 0;
        List<Player> activePlayers = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            String apiUrl = "https://api.balldontlie.io/v1/players/active?per_page=100&cursor=" + num;
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", properties.getProperty("apiKey"));

                int responseCode = connection.getResponseCode();
                System.out.println("Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // Parse JSON response
                    String jsonResponse = response.toString();
                    JSONObject jsonObject = new JSONObject(jsonResponse);

                    // If the response contains an array
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    for (int j = 0; j < dataArray.length(); j++) {
                        JSONObject item = dataArray.getJSONObject(j);
                        Player player = new Player(
                                item.getInt("id"),
                                item.getString("first_name"),
                                item.getString("last_name"),
                                0,
                                convertHeightToFeet(item.getString("height")),
                                Double.parseDouble(item.getString("weight")),
                                item.getString("position").substring(0, 1)
                        );

                        playerIdHashMap.put(player.getPlayerId(), player);
                        activePlayers.add(player);
                    }

                    // find next cursor
                    JSONObject meta = jsonObject.getJSONObject("meta");
                    num = meta.getInt("next_cursor");
//                    System.out.println(num);
//                    System.out.println(i + 1);
                }
                else {
                    System.out.println("GET request failed");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("end");
                break;
            }
        }

        return activePlayers;
    }

    private List<Player> getOtherStats(List<Player> players) {
        List<Player> usablePlayers = new ArrayList<>();

        StringBuilder sb = new StringBuilder("https://api.balldontlie.io/v1/season_averages?");
        sb.append("season=").append(LocalDate.now().getYear()-1).append("&");

        players.forEach(player->{
            sb.append("player_ids[]=").append(player.getPlayerId()).append("&");
        });
        sb.append("per_page=100&");
        sb.append("cursor=");

        System.out.println("Total players: " + players.size());
        System.out.println(sb);

        int next = 0;
        for (int i = 0; i < 100; i++) {
            try {
                URL url = new URL(sb.toString() + next);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", properties.getProperty("apiKey"));

                int responseCode = connection.getResponseCode();
                System.out.println("Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // Parse JSON response
                    String jsonResponse = response.toString();
                    JSONObject jsonObject = new JSONObject(jsonResponse);

                    // If the response contains an array
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    for (int j = 0; j < dataArray.length(); j++) {
                        JSONObject playerObj = dataArray.getJSONObject(j);

                        Player player = playerIdHashMap.get(playerObj.getInt("player_id"));
                        player.setPoints(playerObj.getDouble("pts"));
                        player.setRebounds(playerObj.getDouble("reb"));
                        player.setAssists(playerObj.getDouble("ast"));
                        player.setSteals(playerObj.getDouble("stl"));
                        player.setBlocks(playerObj.getDouble("blk"));
                        player.setCompositeScore(player.calculatePerformanceScore());

                        usablePlayers.add(player);
//                        System.out.println((j+1) + " " + player);
                    }
                    JSONObject meta = jsonObject.getJSONObject("meta");
                    next = meta.getInt("next_cursor");
                }
                else {
                    System.out.println("GET request failed");
                }
            }
            catch (Exception e) {
//                e.printStackTrace();
                System.out.println(e.getMessage());
                System.out.println("end");
                break;
            }
        }

        return usablePlayers;
    }

    private double convertHeightToFeet(String height) throws NumberFormatException {
        // Split the input string into feet and inches parts
        String[] parts = height.split("-");
        if (parts.length != 2) {
            throw new NumberFormatException("Height must be in the format 'X-Y' where X is feet and Y is inches.");
        }

        // Parse the feet and inches parts
        int feet = Integer.parseInt(parts[0]);
        int inches = Integer.parseInt(parts[1]);

        // Convert the total height to feet
        double totalHeightInFeet = feet + inches / 12.0;
        return Math.round(totalHeightInFeet * 100.0) / 100.0;
    }
}
