package algorithms;

import models.Intersection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private double[][] costs;   //Negative cost => link doesn't exist
    private final int nbVertices;

    public Graph(){
        nbVertices = 0;
    }

    /**
     * Initialize a graph with no edges
     * @param nbVertices
     */
    public Graph(int nbVertices){
        this.nbVertices = nbVertices;
        costs = new double[nbVertices][nbVertices];
        for(int i=0; i<nbVertices; i++){
            for(int j=0; j<nbVertices; j++){
                costs[i][j] = -1;
            }
        }
    }

    /**
     * Add an edge
     * @param i start
     * @param j end
     * @param cost
     */
    public void addEdge(int i, int j, double cost){
        costs[i][j] = cost;
    }

    public int getNbVertices() {
        return nbVertices;
    }

    public double getCost(int i, int j) {
        if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
            return -1;
        return costs[i][j];
    }

    public boolean isArc(int i, int j) {
        if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
            return false;
        return getCost(i,j) >= 0;
    }

}