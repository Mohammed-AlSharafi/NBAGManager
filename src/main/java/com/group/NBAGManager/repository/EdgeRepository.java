package com.group.NBAGManager.repository;

import com.group.NBAGManager.model.Graph.Edge;
import com.group.NBAGManager.model.Graph.Vertex;
import com.group.NBAGManager.model.Graph.WeightedGraph;
import com.group.NBAGManager.model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EdgeRepository extends Repository<Edge<String,Integer>> implements EdgeRepositoryInterface{
    private WeightedGraph<String,Integer> graph;
    public EdgeRepository() {
        super();
    }
    public EdgeRepository(WeightedGraph<String,Integer> graph) {
        super();
        this.graph = graph;
    }
    //helper method to set the PreparedStatement parameters
    private void setEdgeParameters(PreparedStatement pStatement, Vertex<String,Integer> obj) throws SQLException {
        pStatement.setString(1, obj.getVertexInfo());
        pStatement.setString(2, obj.getFirstEdge().getToVertex().getVertexInfo());
        pStatement.setInt(3, obj.getFirstEdge().getWeight());
    }

    public void save(Edge<String,Integer> obj) {

    }

    public void save(Vertex<String,Integer> vertex) {
        try{
            String query = "INSERT INTO paths (fromCity, toCity, distance) VALUES (?, ?, ?)";
            PreparedStatement pStatement = con.prepareStatement(query);
            setEdgeParameters(pStatement, vertex);
            pStatement.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void addAllEdgesToGraph(WeightedGraph<String,Integer> graph){
        try{
            String query = "SELECT * FROM paths";
            PreparedStatement pStatement = con.prepareStatement(query);
            pStatement.executeQuery();
            while (pStatement.getResultSet().next()){
                String fromVertex = pStatement.getResultSet().getString("fromCity");
                String toVertex = pStatement.getResultSet().getString("toCity");
                int weight = pStatement.getResultSet().getInt("distance");
                graph.addEdge(fromVertex, toVertex, weight);
            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Edge<String, Integer> findById(int id) {
        return null;
    }

    public List<Edge<String, Integer>> findAll() {
        return List.of();
    }

    public void update(Edge<String, Integer> obj) {

    }

    public void deleteById(int id) {

    }

    public void delete(Edge<String, Integer> obj) {

    }
}
