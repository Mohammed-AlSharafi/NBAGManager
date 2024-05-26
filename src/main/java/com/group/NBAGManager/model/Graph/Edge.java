package com.group.NBAGManager.model.Graph;

public class Edge<T extends Comparable<T>, N extends Comparable<N>> {
    Vertex<T,N> toVertex;
    N weight;
    Edge<T,N> nextEdge;

    public Edge(Vertex<T, N> toVertex, N weight, Edge<T, N> nextEdge) {
        this.toVertex = toVertex;
        this.weight = weight;
        this.nextEdge = nextEdge;
    }

    public Edge() {
        toVertex = null;
        weight = null;
        nextEdge = null;
    }

    public Vertex<T, N> getToVertex() {
        return toVertex;
    }

    public N getWeight() {
        return weight;
    }
}
