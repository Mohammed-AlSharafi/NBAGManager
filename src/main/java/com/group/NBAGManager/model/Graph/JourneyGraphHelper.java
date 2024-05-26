package com.group.NBAGManager.model.Graph;

import com.group.NBAGManager.Journey;

public class JourneyGraphHelper {
    public static void populateGraph(WeightedGraph<String, Integer> journeyGraph){
        journeyGraph.addVertex("San Antonio", "Spurs");
        journeyGraph.addVertex("Golden State", "Warriors");
        journeyGraph.addVertex("Boston", "Celtics");
        journeyGraph.addVertex("Miami", "Heat");
        journeyGraph.addVertex("Los Angeles", "Lakers");
        journeyGraph.addVertex("Phoenix", "Suns");
        journeyGraph.addVertex("Orlando", "Magic");
        journeyGraph.addVertex("Denver", "Nuggets");
        journeyGraph.addVertex("Oklahoma City", "Thunder");
        journeyGraph.addVertex("Houston", "Rockets");

        journeyGraph.addUndirectedEdge("San Antonio", "Phoenix", 500);
        journeyGraph.addUndirectedEdge("San Antonio", "Oklahoma City", 678);
        journeyGraph.addUndirectedEdge("San Antonio", "Houston", 983);
        journeyGraph.addUndirectedEdge("San Antonio", "Orlando", 1137);
        journeyGraph.addUndirectedEdge("Phoenix", "Los Angeles", 577);
        journeyGraph.addUndirectedEdge("Los Angeles", "Golden State", 554);
        journeyGraph.addUndirectedEdge("Los Angeles", "Oklahoma City", 1901);
        journeyGraph.addUndirectedEdge("Golden State", "Denver", 1507);
        journeyGraph.addUndirectedEdge("Golden State", "Oklahoma City", 2214);
        journeyGraph.addUndirectedEdge("Denver", "Oklahoma City", 942);
        journeyGraph.addUndirectedEdge("Denver", "Boston", 2845);
        journeyGraph.addUndirectedEdge("Boston", "Houston", 2584);
        journeyGraph.addUndirectedEdge("Boston", "Miami", 3045);
        journeyGraph.addUndirectedEdge("Miami", "Orlando", 268);
        journeyGraph.addUndirectedEdge("Orlando", "Houston", 458);
        journeyGraph.addUndirectedEdge("Houston", "Oklahoma City", 778);
    }
}
