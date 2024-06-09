package com.group.NBAGManager.repository;

import com.group.NBAGManager.model.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerRepository extends Repository<Player> implements PlayerRepositoryInterface {
    List<Player> playerList = new ArrayList<>();

//    public PlayerRepository() {
////        //dummy players
////        Player[] players = {
////                new Player("Lebron", "James", 39, 2.01, 113, "Forward", 25.6, 8.0, 7.2, 1.2, 0.2),
////        };
////        playerList.addAll(List.of(players));
//    }

    //helper method to set the PreparedStatement parameters
    private void setPlayerParameters(PreparedStatement pStatement, Player obj, boolean id) throws SQLException {
        int index = 1;
        if (id) {
            pStatement.setInt(index++, obj.getPlayerId());
        }
        pStatement.setString(index++, obj.getFirstName());
        pStatement.setString(index++, obj.getLastName());
        pStatement.setInt(index++, obj.getAge());
        pStatement.setDouble(index++, obj.getHeight());
        pStatement.setDouble(index++, obj.getWeight());
        pStatement.setString(index++, obj.getPosition());
        pStatement.setDouble(index++, obj.getPoints());
        pStatement.setDouble(index++, obj.getRebounds());
        pStatement.setDouble(index++, obj.getAssists());
        pStatement.setDouble(index++, obj.getSteals());
        pStatement.setDouble(index++, obj.getBlocks());
        pStatement.setDouble(index, obj.getCompositeScore());
    }

    //add player to database
    public void save(Player obj) {
        try{
            String query = "INSERT INTO players (playerId, firstName, lastName, age, height, weight, position, points, rebounds, assists, steals, blocks, compositeScore) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pStatement = con.prepareStatement(query);
            setPlayerParameters(pStatement, obj, true);
            pStatement.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    //get player by id
    public Player findById(int id) {
        Player player = null;
        try{
            String query = "SELECT * FROM players WHERE playerId = ?";
            PreparedStatement pStatement = con.prepareStatement(query);
            pStatement.setInt(1, id);
            ResultSet rs = pStatement.executeQuery();
            if (rs.next()){
                player = new Player(
                        rs.getInt("playerId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getInt("age"),
                        rs.getDouble("height"),
                        rs.getDouble("weight"),
                        rs.getString("position"),
                        rs.getDouble("points"),
                        rs.getDouble("rebounds"),
                        rs.getDouble("assists"),
                        rs.getDouble("steals"),
                        rs.getDouble("blocks"),
                        rs.getDouble("compositeScore")
                );
            }
            return player;
        }catch (SQLException e){
            System.out.println(e);
            throw new RuntimeException();
        }
    }

    //get all players from database
    public List<Player> findAll() {
        List<Player> players = new ArrayList<>();
        try{
            String query = "SELECT * FROM players";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                Player player = new Player(
                        rs.getInt("playerId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getInt("age"),
                        rs.getDouble("height"),
                        rs.getDouble("weight"),
                        rs.getString("position"),
                        rs.getDouble("points"),
                        rs.getDouble("rebounds"),
                        rs.getDouble("assists"),
                        rs.getDouble("steals"),
                        rs.getDouble("blocks"),
                        rs.getDouble("compositeScore")
                );
                players.add(player);
            }
            return players;
        }catch (SQLException e){
            System.out.println(e);
            throw new RuntimeException();
        }
    }

    //update player using player object
    public void update(Player obj) {
        try{
            String query = "UPDATE players SET firstName = ?, lastName = ?, age = ?, height = ?, weight = ?, position = ?, points = ?, rebounds = ?, assists = ?, steals = ?, blocks = ?, compositeScore = ? WHERE playerId = ?";
            PreparedStatement pStatement = con.prepareStatement(query);
            setPlayerParameters(pStatement, obj,false);
            pStatement.setInt(13, obj.getPlayerId());
            pStatement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e);
            throw new RuntimeException();
        }
    }

    //delete player by id
    public void deleteById(int id) {
        try{
            String query = "DELETE FROM players WHERE playerId = ?";
            PreparedStatement pStatement = con.prepareStatement(query);
            pStatement.setInt(1, id);
            pStatement.executeUpdate();

        }catch (SQLException e){
            System.out.println(e);
            throw new RuntimeException();
        }
    }

    //delete player by object
    public void delete(Player obj) {
        deleteById(obj.getPlayerId());
    }
}
