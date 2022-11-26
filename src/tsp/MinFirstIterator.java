package tsp;

import algorithms.Graph;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

public class MinFirstIterator implements Iterator<Integer> {
    private Integer[] candidates;
    private int nbCandidates;
    private TreeMap <Double,Integer> costs;

    /**
     * Create an iterator to traverse the set of vertices in <code>unvisited</code>
     * which are successors of <code>currentVertex</code> in <code>g</code>
     * Vertices are traversed in the ascending order of their distance to <code>currentVertex</code>
     * @param currentVertex
     * @param g
     */
    public MinFirstIterator(Collection<Integer> unvisited, int currentVertex, Graph g){
        this.candidates = new Integer[unvisited.size()];
        //System.out.println(candidates.length);
        for (Integer s : unvisited){
            if (g.isArc(currentVertex, s))
                candidates[nbCandidates++] = s;
        }
        Arrays.sort(candidates, (o1, o2) -> {
            if (o1!=null&&o2!=null) {
                double distDiff = g.getCost(currentVertex, o1) - g.getCost(currentVertex, o2);
                if (distDiff < 0) return 1;
                if (distDiff == 0) return 0;
                return -1;
            }
            else return 0;
        });
    }

    @Override
    public boolean hasNext() {
        return nbCandidates > 0;
    }

    @Override
    public Integer next() {
        nbCandidates--;
        return candidates[nbCandidates];
    }

    @Override
    public void remove() {}

}
