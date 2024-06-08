package com.group.NBAGManager.repository;

import com.group.NBAGManager.model.CurrentSession;
import com.group.NBAGManager.model.Player;
import com.group.NBAGManager.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeamRepository extends Repository<Player> implements TeamRepositoryInterface {

    //helper method to set the PreparedStatement parameters
    private void setTeamParameters(PreparedStatement pStatement, Player obj, User currentUser) throws SQLException {
        pStatement.setInt(1, currentUser.getUserId());
        pStatement.setInt(2, obj.getPlayerId());
        pStatement.setDouble(3, obj.getSalary());
    }

    public void save(Player obj) {
        User currentUser = CurrentSession.getInstance().getLoggedInUser();
        try{
            String query = "INSERT INTO teams (userId, playerId, salary) VALUES (?, ?, ?)";
            PreparedStatement pStatement = con.prepareStatement(query);
            setTeamParameters(pStatement, obj, currentUser);
            pStatement.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Player findById(int id) {
        User currentUser = CurrentSession.getInstance().getLoggedInUser();
        Player player = null;
        try{
            String query = "SELECT players.*, teams.salary, teams.isInjured, teams.injuryDateTime," +
                    " teams.injuryDescription, teams.isContractRenewQueued FROM players INNER JOIN teams" +
                    " ON players.playerId = teams.playerId INNER JOIN users ON teams.userId = users.userId" +
                    " WHERE players.playerId = ? AND users.userId = ?";
            PreparedStatement pStatement = con.prepareStatement(query);
            pStatement.setInt(1, id);
            pStatement.setInt(2, currentUser.getUserId());
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
                        rs.getDouble("salary"),
                        rs.getDouble("points"),
                        rs.getDouble("rebounds"),
                        rs.getDouble("assists"),
                        rs.getDouble("steals"),
                        rs.getDouble("blocks"),
                        rs.getDouble("compositeScore"),
                        rs.getBoolean("isInjured"),
                        rs.getTimestamp("injuryDateTime") == null? null : rs.getTimestamp("injuryDateTime").toLocalDateTime(),
                        rs.getString("injuryDescription"),
                        rs.getBoolean("isContractRenewQueued")
                );
            }
            return player;
        }catch (SQLException e){
            System.out.println(e);
            throw new RuntimeException();
        }
    }

    public List<Player> findAll() {
        User currentUser = CurrentSession.getInstance().getLoggedInUser();
        List<Player> players = new ArrayList<>();
        try{
            String query = "SELECT players.*, teams.salary, teams.isInjured, teams.injuryDateTime," +
                    " teams.injuryDescription, teams.isContractRenewQueued FROM players INNER JOIN teams" +
                    " ON players.playerId = teams.playerId INNER JOIN users ON teams.userId = users.userId" +
                    " WHERE users.userId = ?";
            PreparedStatement pStatement = con.prepareStatement(query);
            pStatement.setInt(1,currentUser.getUserId());
            ResultSet rs = pStatement.executeQuery();
            while (rs.next()){
                Player player = new Player(
                        rs.getInt("playerId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getInt("age"),
                        rs.getDouble("height"),
                        rs.getDouble("weight"),
                        rs.getString("position"),
                        rs.getDouble("salary"),
                        rs.getDouble("points"),
                        rs.getDouble("rebounds"),
                        rs.getDouble("assists"),
                        rs.getDouble("steals"),
                        rs.getDouble("blocks"),
                        rs.getDouble("compositeScore"),
                        rs.getBoolean("isInjured"),
                        rs.getTimestamp("injuryDateTime") == null? null : rs.getTimestamp("injuryDateTime").toLocalDateTime(),
                        rs.getString("injuryDescription"),
                        rs.getBoolean("isContractRenewQueued")
                );
                players.add(player);
            }
            return players;
        }catch (SQLException e){
            System.out.println(e);
            throw new RuntimeException();
        }
    }

    public List<Player> findIsInjured(boolean injured) {
        User currentUser = CurrentSession.getInstance().getLoggedInUser();
        List<Player> players = new ArrayList<>();
        try{
            String query = "SELECT players.*, teams.salary, teams.isInjured, teams.injuryDateTime," +
                    " teams.injuryDescription, teams.isContractRenewQueued FROM players INNER JOIN teams" +
                    " ON players.playerId = teams.playerId INNER JOIN users ON teams.userId = users.userId" +
                    " WHERE users.userId = ? AND teams.isInjured = ?";
            PreparedStatement pStatement = con.prepareStatement(query);
            pStatement.setInt(1,currentUser.getUserId());
            pStatement.setBoolean(2, injured);

            ResultSet rs = pStatement.executeQuery();
            while (rs.next()){
                Player player = new Player(
                        rs.getInt("playerId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getInt("age"),
                        rs.getDouble("height"),
                        rs.getDouble("weight"),
                        rs.getString("position"),
                        rs.getDouble("salary"),
                        rs.getDouble("points"),
                        rs.getDouble("rebounds"),
                        rs.getDouble("assists"),
                        rs.getDouble("steals"),
                        rs.getDouble("blocks"),
                        rs.getDouble("compositeScore"),
                        rs.getBoolean("isInjured"),
                        rs.getTimestamp("injuryDateTime") == null? null : rs.getTimestamp("injuryDateTime").toLocalDateTime(),
                        rs.getString("injuryDescription"),
                        rs.getBoolean("isContractRenewQueued")
                );
                players.add(player);
            }
            return players;
        }catch (SQLException e){
            System.out.println(e);
            throw new RuntimeException();
        }

    }

    public void update(Player obj) {
        User currentUser = CurrentSession.getInstance().getLoggedInUser();
        try{
            String query = "UPDATE teams SET salary = ?, isInjured = ?, injuryDateTime = ?, injuryDescription = ?," +
                    " isContractRenewQueued = ? WHERE playerId = ? AND userId = ?";
            PreparedStatement pStatement = con.prepareStatement(query);
            pStatement.setDouble(1, obj.getSalary());
            pStatement.setBoolean(2, obj.isInjured());
            pStatement.setTimestamp(3, (obj.getInjuryDateTime() == null) ? null : java.sql.Timestamp.valueOf(obj.getInjuryDateTime()));
            pStatement.setString(4, obj.getInjuryDescription());
            pStatement.setBoolean(5, obj.isContractRenewQueued());
            pStatement.setInt(6, obj.getPlayerId());
            pStatement.setInt(7, currentUser.getUserId());
            pStatement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e);
            throw new RuntimeException();
        }
    }

    public List<Player> findIsContractExtensionQueued(boolean isContractRenewQueued) {
        User currentUser = CurrentSession.getInstance().getLoggedInUser();
        List<Player> players = new ArrayList<>();
        try{
            String query = "SELECT players.*, teams.salary, teams.isInjured, teams.injuryDateTime," +
                    " teams.injuryDescription, teams.isContractRenewQueued FROM players INNER JOIN teams" +
                    " ON players.playerId = teams.playerId INNER JOIN users ON teams.userId = users.userId" +
                    " WHERE users.userId = ? AND teams.isContractRenewQueued = ?";
            PreparedStatement pStatement = con.prepareStatement(query);
            pStatement.setInt(1,currentUser.getUserId());
            pStatement.setBoolean(2, isContractRenewQueued);
            ResultSet rs = pStatement.executeQuery();
            while (rs.next()){
                Player player = new Player(
                        rs.getInt("playerId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getInt("age"),
                        rs.getDouble("height"),
                        rs.getDouble("weight"),
                        rs.getString("position"),
                        rs.getDouble("salary"),
                        rs.getDouble("points"),
                        rs.getDouble("rebounds"),
                        rs.getDouble("assists"),
                        rs.getDouble("steals"),
                        rs.getDouble("blocks"),
                        rs.getDouble("compositeScore"),
                        rs.getBoolean("isInjured"),
                        rs.getTimestamp("injuryDateTime") == null? null : rs.getTimestamp("injuryDateTime").toLocalDateTime(),
                        rs.getString("injuryDescription"),
                        rs.getBoolean("isContractRenewQueued")
                );
                players.add(player);
            }
            return players;
        }catch (SQLException e){
            System.out.println(e);
            throw new RuntimeException();
        }
    }

    public void deleteById(int id) {
        User currentUser = CurrentSession.getInstance().getLoggedInUser();
        try{
            String query = "DELETE FROM teams WHERE playerId = ? AND userId = ?";
            PreparedStatement pStatement = con.prepareStatement(query);
            pStatement.setInt(1, id);
            pStatement.setInt(2, currentUser.getUserId());
            pStatement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e);
            throw new RuntimeException();
        }
    }

    public void delete(Player obj) {
        deleteById(obj.getPlayerId());
    }
}
