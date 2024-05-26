package com.group.NBAGManager.repository;

import com.group.NBAGManager.model.Graph.Vertex;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VertexRepository extends Repository<Vertex<String,Integer>> implements VertexRepositoryInterface{
    private void setVertexParameters(PreparedStatement pStatement, Vertex<String,Integer> obj) throws SQLException {
        pStatement.setString(1, obj.getVertexInfo());
        pStatement.setString(2, obj.getExtraInfo());
    }
    public void save(Vertex<String, Integer> obj) {
        try{
            String query = "INSERT INTO cities (cityName, teamName) VALUES (?, ?)";
            PreparedStatement pStatement = con.prepareStatement(query);
            setVertexParameters(pStatement, obj);
            pStatement.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Vertex<String, Integer> findById(int id) {
        Vertex<String, Integer> vertex = null;
        try{
            String query = "SELECT * FROM cities WHERE cityId = ?";
            PreparedStatement pStatement = con.prepareStatement(query);
            pStatement.setInt(1, id);
            pStatement.executeQuery();
        if (pStatement.getResultSet().next()){
                vertex = new Vertex<>(
                        pStatement.getResultSet().getString("cityName"),
                        pStatement.getResultSet().getString("teamName"),
                        null
                );
            }

    } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vertex;
    }

        public List<Vertex<String, Integer>> findAll() {
        List<Vertex<String,Integer>> vertexList = new ArrayList<>();
        try{
            String query = "SELECT * FROM cities";
            PreparedStatement pStatement = con.prepareStatement(query);
            pStatement.executeQuery();
            while (pStatement.getResultSet().next()){
                Vertex<String,Integer> vertex = new Vertex<>(
                        pStatement.getResultSet().getString("cityName"),
                        pStatement.getResultSet().getString("teamName"),
                        null
                );
                vertexList.add(vertex);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vertexList;
    }

    public void update(Vertex<String, Integer> obj) {

    }

    public void deleteById(int id) {

    }

    public void delete(Vertex<String, Integer> obj) {

    }
}
