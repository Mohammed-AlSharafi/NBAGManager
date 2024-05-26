package com.group.NBAGManager.model.Graph;

import com.group.NBAGManager.repository.EdgeRepository;

import java.util.*;

public class WeightedGraph<T extends Comparable<T>, N extends Comparable<N>>{
    private Vertex<T,N> head;
    private int size;

    public WeightedGraph() {
        head = null;
        size = 0;
    }
    public int getSize(){
        return this.size;
    }
    public boolean hasVertex(T val){
        Vertex<T,N> temp = head;
        while(temp != null){
            if(temp.vertexInfo.compareTo(val) == 0)
                return true;
            temp = temp.nextVertex;
        }
        return false;
    }
    public int getInDeg(T v){
        Vertex<T, N> temp = head;
        while (temp != null){
            if(temp.vertexInfo.compareTo(v) == 0){
                return temp.inDeg;
            }
            temp = temp.nextVertex;
        }
        return -1;
    }
    public int getOutDeg(T v){
        Vertex<T, N> temp = head;
        while (temp != null){
            if(temp.vertexInfo.compareTo(v) == 0){
                return temp.outDeg;
            }
            temp = temp.nextVertex;
        }
        return -1;
    }
    public boolean addVertex(T v, T e){
        Vertex<T,N> vertex = new Vertex<>(v, e, null);
        if(head == null){
            head = vertex;
            size++;
            return true;
        }
        Vertex<T,N> temp = head;
        while(temp.nextVertex != null){
            if(temp.vertexInfo.compareTo(v) == 0)
                return false;
            temp = temp.nextVertex;
        }
        temp.nextVertex = vertex;
        size++;
        return true;
    }
    public int getIndex(T v){
        Vertex<T,N> temp = head;
        int index = 0;
        while(temp != null){
            if(temp.vertexInfo.compareTo(v) == 0){
                return index;
            }
            temp = temp.nextVertex;
            index++;
        }
        return -1;
    }
    public ArrayList<T> getAllVertexObjects(){
        ArrayList<T> vertices = new ArrayList<>();
        Vertex<T,N> temp = head;
        while(temp != null){
            vertices.add(temp.vertexInfo);
            temp = temp.nextVertex;
        }
        return vertices;
    }
    public T getVertex(int position){
        Vertex<T,N> temp = head;
        for (int i = 0; i < position; i++){
            temp = temp.nextVertex;
        }
        return (temp == null)? null : temp.vertexInfo;
    }
    public Vertex<T,N> getVertex(T value){
        Vertex<T,N> temp = head;
        while(temp != null){
            if(temp.getVertexInfo().compareTo(value) == 0){
                return temp;
            }
            temp = temp.nextVertex;
        }
        return null;
    }
    public boolean hasEdge(T source, T destination){
        Vertex<T,N> tempVertex = head;
        while(tempVertex != null){
            if(tempVertex.vertexInfo.compareTo(source) == 0){
                Edge<T,N> tempEdge = tempVertex.firstEdge;
                while(tempEdge != null){
                    if(tempEdge.toVertex.vertexInfo.compareTo(destination) == 0){
                        return true;
                    }
                    tempEdge = tempEdge.nextEdge;
                }
                return false;
            }
            tempVertex = tempVertex.nextVertex;
        }
        return false;
    }
    public boolean addEdge(T source, T destination, N weight){
        Vertex<T,N> tempSource = head;
        while(tempSource!=null){
            if(tempSource.vertexInfo.compareTo(source) == 0){
                Vertex<T,N> tempDestination = head;
                while (tempDestination != null){
                    if(tempDestination.vertexInfo.compareTo(destination) == 0){
                        tempSource.firstEdge = new Edge<>(tempDestination, weight, tempSource.firstEdge);
                        return true;
                    }
                    tempDestination = tempDestination.nextVertex;
                }
                return false;
            }
            tempSource = tempSource.nextVertex;
        }
        return false;
    }

    public boolean addUndirectedEdge(T source, T destination, N weight){
        Vertex<T,N> tempSource = head;
        while(tempSource!=null){
            if(tempSource.vertexInfo.compareTo(source) == 0){
                Vertex<T,N> tempDestination = head;
                while (tempDestination != null){
                    if(tempDestination.vertexInfo.compareTo(destination) == 0){
                        tempSource.firstEdge = new Edge<>(tempDestination, weight, tempSource.firstEdge);
                        tempDestination.firstEdge = new Edge<>(tempSource, weight, tempDestination.firstEdge);
                        return true;
                    }
                    tempDestination = tempDestination.nextVertex;
                }
                return false;
            }
            tempSource = tempSource.nextVertex;
        }
        return false;
    }

    public N getEdgeWeight(T source, T destination){
        Vertex<T,N> tempSource = head;
        while(tempSource != null){
            if(tempSource.vertexInfo.compareTo(source) == 0){
                Edge<T,N> tempEdge = tempSource.firstEdge;
                while(tempEdge!=null){
                    if(tempEdge.toVertex.vertexInfo.compareTo(destination) == 0){
                        return tempEdge.weight;
                    }
                    tempEdge = tempEdge.nextEdge;
                }
                return null;
            }
            tempSource = tempSource.nextVertex;
        }
        return null;
    }
    public void removeEdge(T source, T destination){
        Vertex<T,N> tempSource = head;
        while(tempSource != null){
            if (tempSource.vertexInfo.compareTo(source) == 0) {
                if(tempSource.firstEdge.toVertex.vertexInfo.compareTo(destination)==0){
                    tempSource.firstEdge.toVertex.inDeg--;
                    tempSource.outDeg--;
                    tempSource.firstEdge = tempSource.firstEdge.nextEdge;
                    return;
                }
                Edge<T,N> previous = tempSource.firstEdge;
                Edge<T,N> current = tempSource.firstEdge.nextEdge;
                while(current!=null){
                    if(current.toVertex.vertexInfo.compareTo(destination) == 0){
                        current.toVertex.inDeg--;
                        tempSource.outDeg--;
                        previous.nextEdge = current.nextEdge;
                        return;
                    }
                    previous = current;
                    current = current.nextEdge;
                }
            }
            tempSource = tempSource.nextVertex;
        }
    }

    public ArrayList<T> getNeighbours(T v){
        ArrayList<T> neighbors = new ArrayList<>();
        Vertex<T, N> tempVertex = head;
        while(tempVertex != null){
            if(tempVertex.vertexInfo.compareTo(v) == 0){
                Edge<T,N> tempEdge = tempVertex.firstEdge;
                while (tempEdge != null){
                    neighbors.add(tempEdge.toVertex.vertexInfo);
                    tempEdge = tempEdge.nextEdge;
                }
            }
            tempVertex = tempVertex.nextVertex;
        }
        return neighbors;
    }
    public void printGraph(){
        Vertex<T,N> tempVertex = head;
        while(tempVertex != null){
            System.out.print("#"+tempVertex.vertexInfo+": ");
            Edge<T,N> tempEdge = tempVertex.firstEdge;
            while(tempEdge != null){
                System.out.print("-"+tempEdge.toVertex.vertexInfo);
                tempEdge = tempEdge.nextEdge;
            }
            System.out.println();
            tempVertex = tempVertex.nextVertex;
        }
    }
    public List<T> findShortestPathVisitingAllVertices(T start) {
        List<T> path = new ArrayList<>();
        Set<T> visited = new HashSet<>();
        T current = start;
        path.add(current);
        visited.add(current);

        while (visited.size() < this.getSize()) {
            T next = null;
            N minDist = null;

            Vertex<T, N> currentVertex = getVertex(current);
            if (currentVertex == null) {
                throw new IllegalStateException("Vertex not found in the graph");
            }

            Edge<T, N> tempEdge = currentVertex.firstEdge;
            while (tempEdge != null) {
                T neighbor = tempEdge.toVertex.vertexInfo;
                if (!visited.contains(neighbor) && (minDist == null || tempEdge.weight.compareTo(minDist) < 0)) {
                    next = neighbor;
                    minDist = tempEdge.weight;
                }
                tempEdge = tempEdge.nextEdge;
            }

            if (next == null) {
                throw new IllegalStateException("Graph is not fully connected");
            }

            path.add(next);
            visited.add(next);
            current = next;
        }

        // Return to the starting point to complete the cycle
        path.add(start);
        return path;
    }
    public int getTotalDistance(List<T> path){
        int distance = 0;
        for (int i = 0; i < path.size()-2; i++) {
            System.out.println(path.get(i));
            System.out.println(path.get(i+1));
            distance += (Integer) getEdgeWeight(path.get(i), path.get(i+1));
        }
        return distance;
    }
}
