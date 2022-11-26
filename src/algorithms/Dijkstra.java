package algorithms;

import controller.Controller;
import models.Intersection;
import models.Itinerary;
import models.Map;
import models.Segment;

import java.util.*;

public class Dijkstra {
    // contains parent node of vertex with given id
    private final HashMap<Long, Intersection> pi;
    // contains min distance of vertex with given id
    private final HashMap<Long, Double> dist;
    // L'objet Plan de notre cas d'etude
    private final Map map;
    // Intersection d'ou l'on part
    private final Intersection start;
    //priority queue for use in dijkstra algorithm
    private final PriorityQueue<pqElem> pq;

    public static class pqElem implements Comparator<pqElem> {
        public Intersection intersection;
        public double cost;

        public pqElem() {
        }

        public pqElem(Intersection intersection, double cost) {
            this.intersection = intersection;
            this.cost = cost;
        }

        @Override
        public int compare(pqElem o1, pqElem o2) {
            return Double.compare(o1.cost, o2.cost);
        }
    }

    /**
     * Create a Dijkstra object to initialize the algorithm
     * @param map the map
     * @param start the starting Intersection of the algorithm
     */
    public Dijkstra(Map map, Intersection start) {
        this.map = map;
        this.start = start;
        this.pi = new HashMap<Long, Intersection>();
        this.dist = new HashMap<Long, Double>();
        this.pq = new PriorityQueue<>(map.numberOfNodes(), new pqElem());

        Collection<Intersection> listOfIntersections = map.getlistIntersections().values();
        // Initialize pi and dist arrays
        for (Intersection intersection : listOfIntersections) {
            pi.put(intersection.getId(), null);
            dist.put(intersection.getId(), Double.MAX_VALUE);
        }
    }

    /**
     * Run the algorithm
     */

    public void run() {
        //Priority queue init
        pq.add(new pqElem(start, 0));

        //Pi table init
        pi.put(start.getId(), null);

        //set distance to 0 for starting point
        dist.put(start.getId(), 0.0);

        HashSet<Intersection> visited = new HashSet<>();
        //Algorithm
        //while we have intersections in the frontier
        while (!pq.isEmpty()) {
            Intersection current = pq.poll().intersection;  //Grey node
            // if (visited.contains(current)) continue;
            if (map.getEdges(current.getId()) != null) {
                //For all successors of current node
                for (Segment next : map.getEdges(current.getId())) {
                    Intersection succ = next.getEnd();
                    double new_cost = dist.get(current.getId()) + next.getLength();
                    if (!dist.containsKey(next.getEnd().getId()) || new_cost < dist.get(next.getEnd().getId())) {
                        //The cost must be minimal
                        if(!visited.contains(succ)){
                            //If the node is not visited, release the arc
                            dist.put(succ.getId(), new_cost);     //release the arc
                            pi.put(succ.getId(), current);
                            pq.add(new pqElem(succ, new_cost));
                        }
                    }
                }
                //Color the node in black
                visited.add(current);
            }
        }

    }

    /**
     * Extract a shortest Itinerary
     * To be used after run()
     * @param idDest id of the destination intersection
     * @return
     */
    public Itinerary getItinerary(Long idDest) {
        Itinerary itinerary = null;
        if (map.getlistIntersections().containsKey(idDest)) {
            Intersection dest = map.getlistIntersections().get(idDest);
            Intersection current = map.getlistIntersections().get(idDest);
            LinkedList<Segment> path = new LinkedList<Segment>();
            while (pi.get(current.getId()) != null) {
                Intersection prec = current;
                current = pi.get(current.getId());

                int sizeOfPath = path.size();

                List<Segment> listEdgesOut = map.getEdges(current.getId());
                if (listEdgesOut != null) {
                    for (Segment s : listEdgesOut) {
                        if (s.getEnd().getId() == prec.getId()) {
                            path.addFirst(s);
                            break;
                        }
                    }
                }

                if (sizeOfPath == path.size()) {
                    return null;
                }
            }

            if (path.size() == 0) {
                return null;
            }

            itinerary = new Itinerary(path, start, dest);
        }
        return itinerary;
    }
}