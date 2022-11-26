package algorithms;

import models.*;
import tsp.TSP;
import tsp.TSP3;

import java.util.HashMap;
import java.util.LinkedList;


public class TourComputer {
    private final HashMap<Long,HashMap<Long,Itinerary>> shortestPaths;
    private final Map map;
    private final Graph reducedGraph;
    private final Intersection warehouse;
    private final Courier courier;
    private final HashMap<Integer,Request> requests;
    private final LinkedList<Intersection> stops;
    private final HashMap<Long, Integer> mapIdsToInt;
    private final HashMap<Integer, Long> mapIntToIds;
    private final HashMap<Long,Integer> mapIdsToStartTime;
    private final Integer HOUR_START_AT_WAREHOUSE = 8;
    private final Integer DELIVERY_TIME = 5;
    private final int EXECUTION_TIME_LIMIT =20000;

    /**
     * Create a TourComputer object to run algorithms to calculate a Tour
     * @param map
     * @param idCourier
     */
    public TourComputer(Map map,int idCourier) {
        this.map=map;
        courier = map.getCouriers().get(idCourier);
        stops = new LinkedList<>();
        requests = new HashMap<>();
        shortestPaths = new HashMap<>();
        reducedGraph = new Graph(courier.getRequests().size()+1);
        warehouse= map.getWarehouse();
        mapIdsToInt = new HashMap<>();
        mapIntToIds = new HashMap<>();
        mapIdsToStartTime=new HashMap<>();

    }

    /**
     * Numerate Intersections Id in order to access easily to cost
     * key: IntersectionId, value: index of associated cost
     */
    public void encodingStopsId () {
        // Adding warehouse -> idWarehouse should be paired with 0 since it is always the starting point
        mapIdsToInt.put(warehouse.getId(), 0);
        mapIntToIds.put(0, warehouse.getId());
        mapIdsToStartTime.put(warehouse.getId(),HOUR_START_AT_WAREHOUSE);
        int count = 1;
        stops.add(warehouse);
        for (Request request : courier.getRequests()) {
            Intersection stop = request.getDestination();
            stops.add(stop);
            mapIdsToInt.put(stop.getId(), count);
            mapIntToIds.put(count,stop.getId());
            mapIdsToStartTime.put(stop.getId(),request.getTimeWindow().getStart());
            requests.put(count,request);
            count++;

        }
    }
    /**
     * Create a reduced graph in order to resolve TSP
     * It is possible that there is no way to reach a particular destination
     * return destionationId in that case, otherwise return 0
     */
    public Long createReducedGraph() {
        for (Intersection stop : stops) {
            Dijkstra dij = new Dijkstra(map, stop);
            long startId = stop.getId();    //defining a starting point
            //Step 1 : Call Dijkstra to calculate the shortest path from current point to all other delivery points
            dij.run();
            //Step 2 : Store all of them in the map itineraries
            HashMap<Long,Itinerary> itineraries = new HashMap<>();
            for (Intersection otherStop  : stops) {
                long endId = otherStop.getId();        //defining an arrival point
                if (endId != startId) {
                    //Constraints:
                    //The starting point and arrival point must be different
                    //The time window of arrival point must be after the one of starting point
                    //The arrival point can be warehouse
                    if (dij.getItinerary(endId)==null) return endId;
                    else if (calculateTimeWindowDifference(startId,endId)>=0||endId==warehouse.getId()){
                        itineraries.put(endId,dij.getItinerary(endId));
                    }
                }
            }
            //Step 3: Stock all itineraries from the starting point to all other arrival points in shortestPaths
            shortestPaths.put(startId, itineraries);

            //Step 4 : Add this information to the reduced graph
            for (Intersection s : stops) {
                int start = mapIdsToInt.get(startId);
                long endId = s.getId();
                if (endId != startId) {
                    double cost;
                    int end = mapIdsToInt.get(endId);
                    Itinerary itineraryFromStartToEnd = shortestPaths.get(startId).get(endId);
                    int timeDifference = calculateTimeWindowDifference(startId,endId);
                    //TODO: 92s to compute Tour of 20 delivery points, to be improved
                    if (timeDifference>=0){
                        //Adding an arc only when the time window of the destination is not before the one of the departure
                        cost = itineraryFromStartToEnd.getLength()+timeDifference*3600;
                        reducedGraph.addEdge(start, end, cost);
                    } else if (endId == warehouse.getId()) {
                        //Adding an arc to come back to the warehouse
                        cost = itineraryFromStartToEnd.getLength();
                        reducedGraph.addEdge(start, end, cost);
                    }
                }
            }
        }
        return 0L;
    }

    private int calculateTimeWindowDifference (long startId, long endId){
        return mapIdsToStartTime.get(endId)-mapIdsToStartTime.get(startId);
    }

    /**
     * Instantiate a tour after obtaining a solution to TSP
     */

    public Tour getBestTour(){
        int nextPointIndex,startingPointIndex,warehouseIndex;
        long nextPointId,startingPointId;
        TSP tsp = new TSP3();
        long startTime = System.currentTimeMillis();
        tsp.searchSolution(EXECUTION_TIME_LIMIT, reducedGraph);
        LinkedList<Itinerary> orderedItineraries = new LinkedList<>();
        LinkedList<Request> orderedRequests = new LinkedList<>();
        System.out.print("Solution of cost "+tsp.getSolutionCost()+" found in "
                +(System.currentTimeMillis() - startTime)+"ms : ");
        startingPointIndex = tsp.getSolution(0);
        warehouseIndex = startingPointIndex;
        TimeStamp currentTime = new TimeStamp(HOUR_START_AT_WAREHOUSE,0);
        System.out.print(startingPointIndex + " ");
        for (int i=1; i<stops.size(); i++){
            nextPointIndex = tsp.getSolution(i);
            System.out.print(nextPointIndex+ " ");
            nextPointId = mapIntToIds.get(nextPointIndex);
            startingPointId = mapIntToIds.get(startingPointIndex);
            Itinerary pathToNextPoint = shortestPaths.get(startingPointId).get(nextPointId);
            TimeStamp departureTime = new TimeStamp(currentTime);
            pathToNextPoint.setDepartureTime(departureTime);
            currentTime.add(pathToNextPoint.getDeplacementTimeInMinutes());
            TimeStamp arrivalTime = new TimeStamp(currentTime);
            pathToNextPoint.setArrivalTime(arrivalTime);
            orderedItineraries.add(pathToNextPoint);
            Request associatedRequest = requests.get(nextPointIndex);
            if(associatedRequest.getTimeWindow().getStart()>arrivalTime.getHour()){
                //If the courier arrives before the time window, set current time to the time window of request
                currentTime = new TimeStamp(associatedRequest.getTimeWindow().getStart(),0);
                int differenceHour = associatedRequest.getTimeWindow().getStart()-arrivalTime.getHour();
                associatedRequest.setWaitingTime((differenceHour)*60-arrivalTime.getMinute());
            }
            currentTime.add(DELIVERY_TIME);
            TimeStamp deliveryTime = new TimeStamp(currentTime);
            associatedRequest.setDeliveryTime(deliveryTime);
            orderedRequests.add(requests.get(nextPointIndex));
            startingPointIndex=nextPointIndex;
        }
        //Path to complete the tour, i.e, the warehouse
        orderedItineraries.add(shortestPaths.get(mapIntToIds.get(startingPointIndex)).get(mapIntToIds.get(warehouseIndex)));
        System.out.println(0);
        Tour bestTour = new Tour(orderedItineraries,orderedRequests);
        bestTour.setCourier(courier);
        return bestTour;
    }

    /**
     * Delete a Request from an already calculated Tour and return the new Tour
     * @param tour
     * @param request
     * @return
     */
    public Tour deleteRequest(Tour tour, Request request){
        Tour newTour;
        LinkedList<Request> newRequests = tour.getOrderedRequests();
        newRequests.remove(request);
        LinkedList<Itinerary> newItineraries = new LinkedList<>();
        LinkedList<Itinerary> oldItineraries = tour.getOrderedItineraries();

        if(oldItineraries.size() > 2){
            //Find index of first itinerary to delete
            int index = -1;
            for(int i = 0; i < oldItineraries.size(); i++){
                if(oldItineraries.get(i).getDestination().getId() == request.getDestination().getId()){
                    index = i;
                    break;
                }
            }
            //Add all itineraries before to new list
            for(int i=0; index != -1 && i<index; i++){
                newItineraries.add(oldItineraries.get(i));
            }
            //Add shortest path from before to after deleted delivery
            Dijkstra dijBefore = new Dijkstra(map, oldItineraries.get(index).getStart());
            dijBefore.run();
            newItineraries.add(dijBefore.getItinerary(oldItineraries.get(index+1).getDestination().getId()));
            //Add every itineraries after if they exist
            for(int i=index+2; i<oldItineraries.size(); i++){
                newItineraries.add(oldItineraries.get(i));
            }
        }

        newTour = new Tour(newItineraries,newRequests);
        newTour.calculateDeliveryTime();
        return newTour;
    }

    /**
     * Add a Request to an already calculated Tour and return the new Tour
     * @param tour
     * @param request
     * @param before
     * @return
     */
    public Tour addRequest(Tour tour, Request request, Request before){
        Tour newTour;
        LinkedList<Request> newRequests = tour.getOrderedRequests();
        LinkedList<Itinerary> newItineraries = new LinkedList<>();
        LinkedList<Itinerary> oldItineraries = tour.getOrderedItineraries();

        //Calculate shortest path
        Dijkstra dijBefore = new Dijkstra(map, before.getDestination());
        Dijkstra dijRequest = new Dijkstra(map, request.getDestination());
        dijBefore.run();
        dijRequest.run();

        //Find index of the itinerary to delete
        int index = -1;
        for(int i = 0; i < oldItineraries.size(); i++){
            if(oldItineraries.get(i).getStart().getId() == before.getDestination().getId()){
                index = i;
                break;
            }
        }

        //Add all itineraries before to new list
        for(int i=0; index != -1 && i<index; i++){
            newItineraries.add(oldItineraries.get(i));
        }

        //Add shortest path from Before to New then from New to after
        newItineraries.add(dijBefore.getItinerary(request.getDestination().getId()));
        newItineraries.add(dijRequest.getItinerary(oldItineraries.get(index).getDestination().getId()));

        //Add every itineraries after if they exist
        for(int i=index+1; i<oldItineraries.size(); i++){
            newItineraries.add(oldItineraries.get(i));
        }

        //Add request to the list
        newRequests.add(index,request);

        newTour = new Tour(newItineraries,newRequests);
        newTour.calculateDeliveryTime();
        return newTour;
    }

}
