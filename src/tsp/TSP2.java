package tsp;

import algorithms.Graph;

import java.util.Collection;
import java.util.Iterator;

public class TSP2 extends TSP1 {

    @Override
    protected int bound(Integer currentVertex, Collection<Integer> unvisited){
        //calcul de l
        double l = Double.MAX_VALUE;
        double sommeLi = 0;

        for(Integer j : unvisited){
            if(g.getCost(currentVertex,j) < l){
                l = g.getCost(currentVertex,j);
            }

            double li = g.getCost(j,0);
            for(Integer k : unvisited){
                if(j != k && g.getCost(j,k) < li){
                    li = g.getCost(j,k);
                }
            }
            sommeLi += li;

        }

        return (int) (l + sommeLi);
    }
}
