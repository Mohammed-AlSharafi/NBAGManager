package com.group.NBAGManager.model.Graph;

public class Vertex <T extends Comparable<T>, N extends Comparable<N>>{
    T vertexInfo;
    T extraInfo;
    int inDeg;
    int outDeg;
    Vertex<T,N> nextVertex;
    Edge<T,N> firstEdge;

    public Vertex()
    {
        vertexInfo = null;
        inDeg = 0;
        outDeg = 0;
        nextVertex = null;
        firstEdge = null;
    }

    public Vertex(T vertexInfo, T extraInfo, Vertex<T, N> nextVertex) {
        this.vertexInfo = vertexInfo;
        this.extraInfo = extraInfo;
        this.inDeg = 0;
        this.outDeg = 0;
        this.nextVertex = nextVertex;
        this.firstEdge = null;
    }

    public T getVertexInfo() {
        return vertexInfo;
    }

    public T getExtraInfo() {
        return extraInfo;
    }

    public Edge<T, N> getFirstEdge() {
        return firstEdge;
    }
}
