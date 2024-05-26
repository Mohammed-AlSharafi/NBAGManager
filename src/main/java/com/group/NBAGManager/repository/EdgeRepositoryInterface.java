package com.group.NBAGManager.repository;

import com.group.NBAGManager.model.Graph.Edge;
import com.group.NBAGManager.model.Graph.WeightedGraph;

public interface EdgeRepositoryInterface extends RepositoryInterface<Edge<String,Integer>>{
    public void addAllEdgesToGraph(WeightedGraph<String,Integer> graph);
}
