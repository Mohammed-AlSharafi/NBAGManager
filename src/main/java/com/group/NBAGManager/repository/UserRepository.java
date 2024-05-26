package com.group.NBAGManager.repository;

import com.group.NBAGManager.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRepository extends Repository<User> implements UserRepositoryInterface{

    //helper method to set the PreparedStatement parameters
    private void setUserParameters(PreparedStatement pStatement, User obj) throws SQLException {
        pStatement.setString(1, obj.getUsername());
        pStatement.setString(2, obj.getPassword());
        pStatement.setString(3, obj.getSalt());
        pStatement.setBoolean(4, obj.isFirstLogin());
    }

    public void save(User obj) {
        try{
            String query = "INSERT INTO users (username, password, salt, isFirstLogin) VALUES (?, ?, ?, ?)";
            PreparedStatement pStatement = con.prepareStatement(query);
            setUserParameters(pStatement, obj);
            pStatement.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public User findById(int id) {
        User user = null;
        try{
            String query = "SELECT * FROM users WHERE userId = ?";
            PreparedStatement pStatement = con.prepareStatement(query);
            pStatement.setInt(1, id);
            ResultSet rs = pStatement.executeQuery();
            if (rs.next()){
                user = new User(
                        rs.getInt("userId"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("salt"),
                        rs.getBoolean("isFirstLogin"),
                        rs.getString("location")
                );
            }
            return user;
        }catch (SQLException e){
            System.out.println(e);
            throw new RuntimeException();
        }
    }

    public User findUserByUsername(String username) {
        User user = null;
        try{
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement pStatement = con.prepareStatement(query);
            pStatement.setString(1, username);
            ResultSet rs = pStatement.executeQuery();
            if (rs.next()){
                user = new User(
                        rs.getInt("userId"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("salt"),
                        rs.getBoolean("isFirstLogin"),
                        rs.getString("location")
                );
            }
            return user;
        }catch (SQLException e){
            System.out.println(e);
            throw new RuntimeException();
        }
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try{
            String query = "SELECT * FROM users";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                User user = new User(
                        rs.getInt("userId"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("salt"),
                        rs.getBoolean("isFirstLogin"),
                        rs.getString("location")
                );
                users.add(user);
            }
            return users;
        }catch (SQLException e){
            System.out.println(e);
            throw new RuntimeException();
        }
    }

    public void update(User obj) {
        try{
            String query = "UPDATE users SET username = ?, password = ?, salt = ?, isFirstLogin = ?, location = ? WHERE userId = ?";
            PreparedStatement pStatement = con.prepareStatement(query);
            setUserParameters(pStatement, obj);
            pStatement.setString(5, obj.getLocation());
            pStatement.setInt(6, obj.getUserId());
            pStatement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e);
            throw new RuntimeException();
        }
    }

    public void deleteById(int id) {
        try{
            String query = "DELETE FROM users WHERE userId = ?";
            PreparedStatement pStatement = con.prepareStatement(query);
            pStatement.setInt(1, id);
            pStatement.executeUpdate();

        }catch (SQLException e){
            System.out.println(e);
            throw new RuntimeException();
        }
    }

    public void delete(User obj) {
        deleteById(obj.getUserId());
    }
}
